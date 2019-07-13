package com.example

import pl.treksoft.kvision.form.InputSize
import pl.treksoft.kvision.form.check.CheckBoxInput
import pl.treksoft.kvision.form.check.CheckBoxInput.Companion.checkBoxInput
import pl.treksoft.kvision.form.select.SimpleSelectInput
import pl.treksoft.kvision.form.spinner.SpinnerInput
import pl.treksoft.kvision.form.text.TextInput
import pl.treksoft.kvision.form.time.DateTimeInput
import pl.treksoft.kvision.html.Button.Companion.button
import pl.treksoft.kvision.html.Icon
import pl.treksoft.kvision.html.Span
import pl.treksoft.kvision.i18n.I18n.tr
import pl.treksoft.kvision.modal.Alert
import pl.treksoft.kvision.modal.Confirm
import pl.treksoft.kvision.panel.HPanel.Companion.hPanel
import pl.treksoft.kvision.panel.SimplePanel
import pl.treksoft.kvision.table.TableType
import pl.treksoft.kvision.tabulator.Align
import pl.treksoft.kvision.tabulator.ColumnDefinition
import pl.treksoft.kvision.tabulator.Editor
import pl.treksoft.kvision.tabulator.Formatter
import pl.treksoft.kvision.tabulator.Layout
import pl.treksoft.kvision.tabulator.PaginationMode
import pl.treksoft.kvision.tabulator.Tabulator.Companion.tabulator
import pl.treksoft.kvision.tabulator.TabulatorOptions
import pl.treksoft.kvision.types.toDateF
import pl.treksoft.kvision.types.toStringF
import pl.treksoft.kvision.utils.obj
import pl.treksoft.kvision.utils.observableListOf
import pl.treksoft.kvision.utils.px
import kotlin.js.Date

data class Employee(
    val name: String?,
    val position: String?,
    val office: String?,
    val active: Boolean = false,
    val startDate: Date?,
    val salary: Int?,
    @Suppress("ArrayInDataClass") val _children: Array<Employee>? = null,
    val id: Int = counter++
) {
    companion object {
        internal var counter = 0
    }
}

class TabulatorTab : SimplePanel() {

    val data = observableListOf(
        Employee(
            "Tiger Nixon",
            "System Architect",
            "Edinburgh",
            false,
            "2011-04-25".toDateF("YYYY-MM-DD"),
            320800,
            arrayOf(
                Employee("John Snow", "Programmer", "Edinburgh", true, "2012-04-23".toDateF("YYY-MM-DD"), 100000),
                Employee("Mark Lee", "Junior programmer", "Edinburgh", true, "2016-06-02".toDateF("YYY-MM-DD"), 80000)
            )
        ),
        Employee(
            "Garrett Winters",
            "Accountant",
            "Tokyo",
            true,
            "2011-07-25".toDateF("YYYY-MM-DD"),
            170750
        ),
        Employee(
            "Ashton Cox",
            "Junior Technical Author",
            "San Francisco",
            true,
            "2009-01-12".toDateF("YYYY-MM-DD"),
            86000
        )
    )

    init {
        this.marginTop = 10.px

        tabulator(data,
            options = TabulatorOptions(
                layout = Layout.FITCOLUMNS,
                columns = listOf(
                    ColumnDefinition(
                        tr("Name"), "name", headerFilter = Editor.INPUT,
                        editorComponentFunction = { _, _, success, _, data ->
                            TextInput(value = data.name).apply {
                                size = InputSize.SMALL
                                setEventListener<TextInput> {
                                    change = {
                                        success(self.value)
                                    }
                                }
                            }
                        }, editable = { false }, cellDblClick = { _, cell -> cell.edit(true) }
                    ),
                    ColumnDefinition(
                        tr("Position"), "position",
                        editorComponentFunction = { _, _, success, _, data ->
                            TextInput(value = data.position).apply {
                                size = InputSize.SMALL
                                setEventListener<TextInput> {
                                    change = {
                                        success(self.value)
                                    }
                                }
                            }
                        }),
                    ColumnDefinition(tr("Office"), "office", editorComponentFunction = { _, _, success, _, data ->
                        SimpleSelectInput(
                            listOf(
                                "London" to "London",
                                "Edinburgh" to "Edinburgh",
                                "Tokyo" to "Tokyo",
                                "San Francisco" to "San Francisco"
                            ),
                            value = data.office,
                            emptyOption = true
                        ).apply {
                            size = InputSize.SMALL
                            setEventListener<SimpleSelectInput> {
                                change = {
                                    success(self.value)
                                }
                            }
                        }
                    }),
                    ColumnDefinition(
                        tr("Active"),
                        "active",
                        align = Align.CENTER,
                        editorComponentFunction = { _, _, success, _, data ->
                            checkBoxInput(value = data.active).apply {
                                size = InputSize.SMALL
                                height = 20.px
                                setEventListener<CheckBoxInput> {
                                    click = {
                                        success(self.value)
                                    }
                                }
                            }
                        }, formatter = Formatter.TICKCROSS
                    ),
                    ColumnDefinition(
                        tr("Start date"),
                        "startDate", formatterComponentFunction = { _, _, data ->
                            Span(data.startDate?.toStringF("YYYY-MM-DD"))
                        },
                        editorComponentFunction = { _, _, success, _, data ->
                            DateTimeInput(value = data.startDate, format = "YYYY-MM-DD").apply {
                                size = InputSize.SMALL
                                clearBtn = false
                                setEventListener<DateTimeInput> {
                                    change = {
                                        success(self.value)
                                    }
                                }
                            }
                        }),
                    ColumnDefinition(tr("Salary"), "salary", formatter = Formatter.MONEY, formatterParams = obj {
                        decimal = "."
                        thousand = " "
                        symbol = "$ "
                        precision = false
                    }, editorComponentFunction = { _, _, success, _, data ->
                        SpinnerInput(data.salary).apply {
                            size = InputSize.SMALL
                            setEventListener<SpinnerInput> {
                                blur = {
                                    success(self.value)
                                }
                            }
                        }
                    }),
                    ColumnDefinition(
                        "",
                        headerSort = false,
                        align = Align.CENTER,
                        width = "50",
                        formatterComponentFunction = { _, _, d ->
                            Icon("fa-times").apply {
                                setEventListener<Icon> {
                                    click = {
                                        Confirm.show(tr("Are you sure?"), tr("Delete row?")) {
                                            val row = data.find { it.id == d.id }
                                            data.remove(row)
                                        }
                                    }
                                }
                            }
                        })
                ), pagination = PaginationMode.LOCAL, paginationSize = 10, dataTree = true
            ), types = setOf(TableType.BORDERED, TableType.STRIPED, TableType.HOVER)
        ) {
            height = 430.px
        }
        hPanel(spacing = 5) {
            button(tr("Add new employee"), "fa-plus").onClick {
                data.add(Employee(null, null, null, false, null, null))
            }

            button(tr("Show current data model"), "fa-search").onClick {
                console.log(data.toList())
                Alert.show(tr("Current data model"), data.toList().toString())
            }
        }
    }
}
