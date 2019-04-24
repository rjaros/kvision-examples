package com.example

import pl.treksoft.kvision.i18n.I18n.tr
import pl.treksoft.kvision.panel.SimplePanel
import pl.treksoft.kvision.table.TableType
import pl.treksoft.kvision.tabulator.ColumnDefinition
import pl.treksoft.kvision.tabulator.Editor
import pl.treksoft.kvision.tabulator.Formatter
import pl.treksoft.kvision.tabulator.Layout
import pl.treksoft.kvision.tabulator.Options
import pl.treksoft.kvision.tabulator.PaginationMode
import pl.treksoft.kvision.tabulator.Tabulator.Companion.tabulator
import pl.treksoft.kvision.utils.obj
import pl.treksoft.kvision.utils.px

class TabulatorTab : SimplePanel() {

    init {
        this.marginTop = 10.px

        tabulator<Any>(
            options = Options(
                ajaxURL = "data.json",
                ajaxResponse = { _, _, resp ->
                    resp.data
                },
                layout = Layout.FITCOLUMNS,
                columns = listOf(
                    ColumnDefinition(
                        tr("Name"), "0", headerFilter = Editor.INPUT
                    ),
                    ColumnDefinition(tr("Position"), "1"),
                    ColumnDefinition(tr("Office"), "2"),
                    ColumnDefinition(tr("Start date"), "4", formatter = Formatter.DATETIME, formatterParams = obj {
                        outputFormat = "DD/MM/YYYY"
                    }),
                    ColumnDefinition(tr("Salary"), "5", formatter = Formatter.MONEY, formatterParams = obj {
                        decimal = "."
                        thousand = " "
                        symbol = "$ "
                        precision = false
                    })
                ), pagination = PaginationMode.LOCAL, paginationSize = 10
            ), types = setOf(TableType.BORDERED, TableType.CONDENSED, TableType.STRIPED, TableType.HOVER)
        ) {
            height = 430.px

        }
    }
}
