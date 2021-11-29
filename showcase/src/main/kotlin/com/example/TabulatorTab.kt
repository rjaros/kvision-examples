@file:UseContextualSerialization(Date::class)

package com.example

import io.kvision.core.onEvent
import io.kvision.form.InputSize
import io.kvision.form.check.checkBoxInput
import io.kvision.form.select.SimpleSelectInput
import io.kvision.form.spinner.SpinnerInput
import io.kvision.form.text.TextInput
import io.kvision.form.time.DateTimeInput
import io.kvision.html.Icon
import io.kvision.html.Span
import io.kvision.html.button
import io.kvision.i18n.I18n.tr
import io.kvision.modal.Alert
import io.kvision.modal.Confirm
import io.kvision.panel.SimplePanel
import io.kvision.panel.hPanel
import io.kvision.state.observableListOf
import io.kvision.tabulator.Align
import io.kvision.tabulator.ColumnDefinition
import io.kvision.tabulator.Editor
import io.kvision.tabulator.Formatter
import io.kvision.tabulator.Layout
import io.kvision.tabulator.PaginationMode
import io.kvision.tabulator.TableType
import io.kvision.tabulator.TabulatorOptions
import io.kvision.tabulator.tabulator
import io.kvision.types.toDateF
import io.kvision.types.toStringF
import io.kvision.utils.auto
import io.kvision.utils.obj
import io.kvision.utils.px
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseContextualSerialization
import kotlinx.serialization.serializer
import kotlin.js.Date

@Serializable
data class Employee(
    val name: String?,
    val position: String?,
    val office: String?,
    val active: Boolean = false,
    val startDate: Date?,
    val salary: Int?,
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
            320800
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
                                onEvent {
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
                                onEvent {
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
                            onEvent {
                                change = {
                                    success(self.value)
                                }
                            }
                        }
                    }),
                    ColumnDefinition(
                        tr("Active"),
                        "active",
                        hozAlign = Align.CENTER,
                        editorComponentFunction = { _, _, success, _, data ->
                            checkBoxInput(value = data.active).apply {
                                size = InputSize.SMALL
                                margin = auto
                                padding = 0.px
                                marginTop = 10.px
                                minHeight = 13.px
                                onEvent {
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
                                showClear = false
                                onEvent {
                                    change = {
                                        success(self.value?.toStringF())
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
                            onEvent {
                                blur = {
                                    success(self.value)
                                }
                            }
                        }
                    }),
                    ColumnDefinition(
                        "",
                        headerSort = false,
                        hozAlign = Align.CENTER,
                        width = "50",
                        formatterComponentFunction = { _, _, d ->
                            Icon("fas fa-times").apply {
                                onEvent {
                                    click = {
                                        Confirm.show(tr("Are you sure?"), tr("Delete row?")) {
                                            val row = this@TabulatorTab.data.find { it.id == d.id }
                                            this@TabulatorTab.data.remove(row)
                                        }
                                    }
                                }
                            }
                        })
                ), pagination = true, paginationSize = 10
            ), types = setOf(TableType.BORDERED, TableType.HOVER, TableType.STRIPED), serializer = serializer()
        ) {
            height = 430.px
        }
        hPanel(spacing = 5) {
            button(tr("Add new employee"), "fas fa-plus").onClick {
                this@TabulatorTab.data.add(Employee(null, null, null, false, null, null))
            }

            button(tr("Show current data model"), "fas fa-search").onClick {
                console.log(this@TabulatorTab.data.toList())
                Alert.show(tr("Current data model"), this@TabulatorTab.data.toList().toString())
            }
        }
    }
}
