package com.example

import pl.treksoft.kvision.form.check.RadioGroup
import pl.treksoft.kvision.form.check.RadioGroup.Companion.radioGroup
import pl.treksoft.kvision.form.text.TextInput
import pl.treksoft.kvision.form.text.TextInput.Companion.textInput
import pl.treksoft.kvision.form.text.TextInputType
import pl.treksoft.kvision.modal.Confirm
import pl.treksoft.kvision.panel.FlexAlignItems
import pl.treksoft.kvision.panel.FlexWrap
import pl.treksoft.kvision.panel.HPanel.Companion.hPanel
import pl.treksoft.kvision.panel.VPanel
import pl.treksoft.kvision.tabulator.Align
import pl.treksoft.kvision.tabulator.ColumnDefinition
import pl.treksoft.kvision.tabulator.Formatter
import pl.treksoft.kvision.tabulator.Layout
import pl.treksoft.kvision.tabulator.Options
import pl.treksoft.kvision.tabulator.Tabulator
import pl.treksoft.kvision.tabulator.Tabulator.Companion.tabulator
import pl.treksoft.kvision.utils.obj
import pl.treksoft.kvision.utils.px
import pl.treksoft.kvision.utils.vh
import pl.treksoft.kvision.tabulator.js.Tabulator as JsTabulator

object ListPanel : VPanel() {

    private lateinit var search: TextInput
    private lateinit var searchTypes: RadioGroup

    init {
        height = 100.vh
        minWidth = 430.px

        hPanel(FlexWrap.NOWRAP, alignItems = FlexAlignItems.CENTER, spacing = 20) {
            padding = 10.px
            search = textInput(TextInputType.SEARCH) {
                placeholder = "Search ..."
            }
            searchTypes = radioGroup(listOf("all" to "All", "fav" to "Favourites"), "all", inline = true) {
                marginBottom = 0.px
            }
        }
        val tabulator = tabulator(
            Model.addresses, Options(
                height = "calc(100vh - 60px)", layout = Layout.FITCOLUMNS, columns = listOf(
                    ColumnDefinition("First name", "firstName"),
                    ColumnDefinition("Last name", "lastName"),
                    ColumnDefinition("E-mail", "email", formatterFunction = { cell, _, _ ->
                        cell.getValue()?.let { "<a href='mailto:$it'>$it</a>" } ?: ""
                    }),
                    ColumnDefinition(
                        "",
                        "favourite",
                        align = Align.CENTER,
                        width = "40",
                        formatter = Formatter.TICKCROSS,
                        formatterParams = obj {
                            crossElement = false
                        }
                    ),
                    ColumnDefinition(
                        "", align = Align.CENTER,
                        width = "40",
                        formatter = Formatter.BUTTONCROSS,
                        headerSort = false,
                        cellClick = { evt, cell ->
                            evt.stopPropagation()
                            Confirm.show("Are you sure?", "Do you want to delete this address?") {
                                EditPanel.delete(cell.getRow().getIndex() as Int)
                            }
                        }
                    )
                ), persistenceMode = false
            )
        ) {
            marginBottom = 0.px
            setEventListener<Tabulator<Address>> {
                tabulatorRowClick = { e ->
                    EditPanel.edit((e.detail as JsTabulator.RowComponent).getIndex() as Int)
                }
            }
            setFilter { address ->
                address.match(search.value) && (searchTypes.value == "all" || address.favourite ?: false)
            }
        }

        search.setEventListener {
            input = {
                tabulator.applyFilter()
            }
        }
        searchTypes.setEventListener {
            change = {
                tabulator.applyFilter()
            }
        }
    }
}
