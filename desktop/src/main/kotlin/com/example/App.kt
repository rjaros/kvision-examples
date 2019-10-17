package com.example

import pl.treksoft.kvision.Application
import pl.treksoft.kvision.core.Border
import pl.treksoft.kvision.core.BorderStyle
import pl.treksoft.kvision.core.Component
import pl.treksoft.kvision.dropdown.ddLink
import pl.treksoft.kvision.dropdown.dropDown
import pl.treksoft.kvision.dropdown.separator
import pl.treksoft.kvision.html.Link
import pl.treksoft.kvision.modal.Alert
import pl.treksoft.kvision.navbar.Nav
import pl.treksoft.kvision.navbar.nav
import pl.treksoft.kvision.navbar.navbar
import pl.treksoft.kvision.navbar.NavbarType
import pl.treksoft.kvision.panel.FlexDir
import pl.treksoft.kvision.panel.flexPanel
import pl.treksoft.kvision.panel.FlexWrap
import pl.treksoft.kvision.panel.root
import pl.treksoft.kvision.require
import pl.treksoft.kvision.startApplication
import pl.treksoft.kvision.utils.px
import pl.treksoft.kvision.utils.vh
import kotlin.browser.document

class App : Application() {
    init {
        require("css/kvapp.css")
    }

    override fun start() {
        root("kvapp") {
            navbar(type = NavbarType.FIXEDTOP) {
                nav {
                    dropDown("Menu", icon = "fab fa-windows", forNavbar = true) {
                        ddLink("Calculator", icon = "fas fa-calculator").onClick {
                            Calculator.run(this@root)
                        }
                        ddLink("Text Editor", icon = "fas fa-edit").onClick {
                            TextEditor.run(this@root)
                        }
                        ddLink("Paint", icon = "fas fa-paint-brush").onClick {
                            Paint.run(this@root)
                        }
                        ddLink("Web Browser", icon = "fab fa-firefox").onClick {
                            WebBrowser.run(this@root)
                        }
                        separator()
                        ddLink("About", icon = "fas fa-info-circle").onClick {
                            Alert.show("KVision Desktop", "KVision example application.")
                        }
                        ddLink("Shutdown", icon = "fas fa-power-off").onClick {
                            document.location?.reload()
                        }
                    }
                }
                taskBar = nav()
            }
            flexPanel(FlexDir.COLUMN, FlexWrap.WRAP, spacing = 20) {
                padding = 20.px
                paddingTop = 70.px
                height = 100.vh
                add(DesktopIcon("fas fa-calculator", "Calculator").setEventListener<DesktopIcon> {
                    dblclick = {
                        Calculator.run(this@root)
                    }
                })
                add(DesktopIcon("fas fa-edit", "Text Editor").setEventListener<DesktopIcon> {
                    dblclick = {
                        TextEditor.run(this@root)
                    }
                })
                add(DesktopIcon("fas fa-paint-brush", "Paint").setEventListener<DesktopIcon> {
                    dblclick = {
                        Paint.run(this@root)
                    }
                })
                add(DesktopIcon("fab fa-firefox", "Web Browser").setEventListener<DesktopIcon> {
                    dblclick = {
                        WebBrowser.run(this@root)
                    }
                })
            }
        }
    }

    companion object {
        
        lateinit var taskBar: Nav

        fun addTask(window: DesktopWindow): Component {
            val task = Link(window.caption ?: "Window", icon = window.icon, classes = setOf("nav-item", "nav-link")).apply {
                border = Border(1.px, BorderStyle.SOLID)
                marginLeft = 5.px
            }.onClick {
                if (window.minimized) window.toggleMinimize()
                window.toFront()
                window.focus()
            }
            taskBar.add(task)
            return task
        }

        fun removeTask(task: Component) {
            taskBar.remove(task)
            task.dispose()
        }
    }
}

fun main() {
    startApplication(::App)
}
