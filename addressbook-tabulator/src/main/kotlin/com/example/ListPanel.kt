package com.example

import pl.treksoft.kvision.core.AlignItems
import pl.treksoft.kvision.core.Container
import pl.treksoft.kvision.core.FlexWrap
import pl.treksoft.kvision.core.JustifyContent
import pl.treksoft.kvision.core.onEvent
import pl.treksoft.kvision.form.check.radioGroup
import pl.treksoft.kvision.form.text.TextInputType
import pl.treksoft.kvision.form.text.text
import pl.treksoft.kvision.i18n.I18n.tr
import pl.treksoft.kvision.modal.Confirm
import pl.treksoft.kvision.panel.hPanel
import pl.treksoft.kvision.panel.simplePanel
import pl.treksoft.kvision.tabulator.Align
import pl.treksoft.kvision.tabulator.ColumnDefinition
import pl.treksoft.kvision.tabulator.Formatter
import pl.treksoft.kvision.tabulator.Layout
import pl.treksoft.kvision.tabulator.Tabulator
import pl.treksoft.kvision.tabulator.TabulatorOptions
import pl.treksoft.kvision.tabulator.tabulator
import pl.treksoft.kvision.utils.obj
import pl.treksoft.kvision.utils.px

fun Container.listPanel() {
    simplePanel {
        lateinit var tabulator: Tabulator<Address>
        hPanel(justify = JustifyContent.SPACEAROUND, wrap = FlexWrap.WRAP, alignItems = AlignItems.CENTER) {
            text(TextInputType.SEARCH) {
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
                height = "calc(100vh - 75px)", layout = Layout.FITCOLUMNS, columns = listOf(
                    ColumnDefinition(tr("First name"), "firstName"),
                    ColumnDefinition(tr("Last name"), "lastName"),
                    ColumnDefinition(tr("E-mail"), "email", formatterFunction = { cell, _, _ ->
                        cell.getValue().let { "<a href='mailto:$it'>$it</a>" }
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
                        cellClick = { evt, cell ->
                            evt.stopPropagation()
                            Confirm.show(tr("Are you sure?"), tr("Do you want to delete this address?")) {
                                Model.delete(cell.getRow().getIndex() as Int)
                            }
                        }
                    )
                ), persistenceMode = false
            )
        ) {
            marginBottom = 0.px
            setEventListener<Tabulator<Address>> {
                tabulatorRowClick = { e ->
                    @Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
                    Model.edit((e.detail as pl.treksoft.kvision.tabulator.js.Tabulator.RowComponent).getIndex() as Int)
                }
            }
            setFilter { address ->
                val state = Model.addressBook.value
                address.match(state.search) && (state.filter == Filter.ALL || address.favourite ?: false)
            }
        }
    }
}
