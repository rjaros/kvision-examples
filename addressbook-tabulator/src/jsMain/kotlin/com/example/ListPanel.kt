package com.example

import io.kvision.core.AlignItems
import io.kvision.core.Container
import io.kvision.core.JustifyContent
import io.kvision.core.onEvent
import io.kvision.form.check.radioGroup
import io.kvision.form.text.text
import io.kvision.html.InputType
import io.kvision.i18n.I18n.tr
import io.kvision.modal.Confirm
import io.kvision.panel.hPanel
import io.kvision.panel.simplePanel
import io.kvision.tabulator.Align
import io.kvision.tabulator.ColumnDefinition
import io.kvision.tabulator.Formatter
import io.kvision.tabulator.Layout
import io.kvision.tabulator.RenderType
import io.kvision.tabulator.Tabulator
import io.kvision.tabulator.TabulatorOptions
import io.kvision.tabulator.tabulator
import io.kvision.utils.obj
import io.kvision.utils.px
import kotlinx.serialization.serializer
import org.w3c.dom.events.Event

fun Container.listPanel() {
    simplePanel {
        lateinit var tabulator: Tabulator<Address>
        hPanel(justify = JustifyContent.SPACEAROUND, alignItems = AlignItems.CENTER) {
            width = 410.px
            text(InputType.SEARCH) {
                placeholder = tr("Search ...")
                onEvent {
                    input = {
                        Model.setSearch(self.value)
                        tabulator.applyFilter()
                    }
                }
            }
            radioGroup(
                listOf(Filter.ALL.name to tr("All"), Filter.FAVOURITE.name to tr("Favourites")),
                Filter.ALL.name,
                inline = true
            ).onEvent {
                change = {
                    Model.setFilter(Filter.valueOf(self.value!!))
                    tabulator.applyFilter()
                }
            }
        }
        tabulator = tabulator(
            Model.addressBook, { it.addresses }, options = TabulatorOptions(
                renderVertical = RenderType.VIRTUAL,
                height = "calc(100vh - 90px)", layout = Layout.FITCOLUMNS, columns = listOf(
                    ColumnDefinition(tr("First name"), "firstName"),
                    ColumnDefinition(tr("Last name"), "lastName"),
                    ColumnDefinition(tr("E-mail"), "email", formatterFunction = { cell, _, _ ->
                        cell.getValue()?.let { "<a href='mailto:$it'>$it</a>" }
                    }),
                    ColumnDefinition(
                        "",
                        "favourite",
                        hozAlign = Align.CENTER,
                        width = "40",
                        formatter = Formatter.TICKCROSS,
                        formatterParams = obj {
                            crossElement = false
                        }
                    ),
                    ColumnDefinition(
                        "", hozAlign = Align.CENTER,
                        width = "40",
                        formatter = Formatter.BUTTONCROSS,
                        headerSort = false,
                        cellClick = { evt: Event, cell ->
                            evt.preventDefault()
                            Confirm.show(tr("Are you sure?"), tr("Do you want to delete this address?")) {
                                Model.delete(cell.getRow().getIndex() as Int)
                            }
                        }
                    )
                ), persistenceMode = false
            ), serializer = serializer()
        ) {
            marginBottom = 0.px
            setEventListener<Tabulator<Address>> {
                rowClickTabulator = { e ->
                    @Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
                    Model.edit((e.detail as io.kvision.tabulator.js.Tabulator.RowComponent).getIndex() as Int)
                }
            }
            setFilter { address ->
                val state = Model.addressBook.value
                address.match(state.search) && (state.filter == Filter.ALL || address.favourite ?: false)
            }
        }
    }
}
