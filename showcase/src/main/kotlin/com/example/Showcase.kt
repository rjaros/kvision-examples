package com.example

import pl.treksoft.kvision.Application
import pl.treksoft.kvision.core.Border
import pl.treksoft.kvision.core.BorderStyle
import pl.treksoft.kvision.core.Col
import pl.treksoft.kvision.core.Color
import pl.treksoft.kvision.core.onEvent
import pl.treksoft.kvision.form.select.select
import pl.treksoft.kvision.i18n.DefaultI18nManager
import pl.treksoft.kvision.i18n.I18n
import pl.treksoft.kvision.i18n.I18n.tr
import pl.treksoft.kvision.module
import pl.treksoft.kvision.pace.Pace
import pl.treksoft.kvision.panel.root
import pl.treksoft.kvision.panel.tab
import pl.treksoft.kvision.panel.tabPanel
import pl.treksoft.kvision.panel.vPanel
import pl.treksoft.kvision.require
import pl.treksoft.kvision.startApplication
import pl.treksoft.kvision.utils.auto
import pl.treksoft.kvision.utils.perc
import pl.treksoft.kvision.utils.px

class Showcase : Application() {
    init {
        require("css/showcase.css")
        require("react-awesome-button/dist/themes/theme-blue.css")
    }

    override fun start() {

        I18n.manager =
            DefaultI18nManager(
                mapOf(
                    "pl" to require("i18n/messages-pl.json"),
                    "en" to require("i18n/messages-en.json")
                )
            )
        Pace.init()
        root("showcase") {
            vPanel {
                width = 100.perc
                tabPanel(scrollableTabs = true) {
                    width = 80.perc
                    margin = 20.px
                    marginLeft = auto
                    marginRight = auto
                    padding = 20.px
                    border = Border(2.px, BorderStyle.SOLID, Color.name(Col.SILVER))
                    tab(tr("HTML"), "fas fa-bars", route = "/basic") {
                        add(BasicTab())
                    }
                    tab(tr("Forms"), "fas fa-edit", route = "/forms") {
                        add(FormTab())
                    }
                    tab(tr("Buttons"), "far fa-check-square", route = "/buttons") {
                        add(ButtonsTab())
                    }
                    tab(tr("Dropdowns"), "fas fa-arrow-down", route = "/dropdowns") {
                        add(DropDownTab())
                    }
                    tab(tr("Containers"), "fas fa-database", route = "/containers") {
                        add(ContainersTab())
                    }
                    tab(tr("Layouts"), "fas fa-th-list", route = "/layouts") {
                        add(LayoutsTab())
                    }
                    tab(tr("Windows"), "fas fa-window-maximize", route = "/windows") {
                        add(ModalsTab())
                    }
                    tab(tr("Data binding"), "fas fa-retweet", route = "/data") {
                        add(DataTab())
                    }
                    tab(tr("Drag & Drop"), "fas fa-arrows-alt", route = "/dragdrop") {
                        add(DragDropTab())
                    }
                    tab(tr("Charts"), "far fa-chart-bar", route = "/charts") {
                        add(ChartTab())
                    }
                    tab(tr("Tables"), "fas fa-table", route = "/tabulator") {
                        add(TabulatorTab())
                    }
                    tab(tr("RESTful"), "fas fa-plug", route = "/restful") {
                        add(RestTab())
                    }
                }
                select(listOf("en" to tr("English"), "pl" to tr("Polish")), I18n.language) {
                    width = 300.px
                    marginLeft = auto
                    marginRight = auto
                    onEvent {
                        change = {
                            I18n.language = self.value ?: "en"
                        }
                    }
                }
            }
        }
    }
}

fun main() {
    startApplication(::Showcase, module.hot)
}
