package com.example

import kotlinx.browser.document
import io.kvision.Application
import io.kvision.core.Border
import io.kvision.core.BorderStyle
import io.kvision.core.Component
import io.kvision.core.FlexDirection
import io.kvision.core.FlexWrap
import io.kvision.core.onEvent
import io.kvision.dropdown.ddLink
import io.kvision.dropdown.dropDown
import io.kvision.dropdown.separator
import io.kvision.html.Link
import io.kvision.modal.Alert
import io.kvision.navbar.Nav
import io.kvision.navbar.NavbarType
import io.kvision.navbar.nav
import io.kvision.navbar.navbar
import io.kvision.panel.flexPanel
import io.kvision.module
import io.kvision.panel.root
import io.kvision.require
import io.kvision.startApplication
import io.kvision.utils.px
import io.kvision.utils.vh

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
            flexPanel(FlexDirection.COLUMN, FlexWrap.WRAP, spacing = 20) {
                padding = 20.px
                paddingTop = 70.px
                height = 100.vh
                add(DesktopIcon("fas fa-calculator", "Calculator").apply {
                    onEvent {
                        dblclick = {
                            Calculator.run(this@root)
                        }
                    }
                })
                add(DesktopIcon("fas fa-edit", "Text Editor").apply {
                    onEvent {
                        dblclick = {
                            TextEditor.run(this@root)
                        }
                    }
                })
                add(DesktopIcon("fas fa-paint-brush", "Paint").apply {
                    onEvent {
                        dblclick = {
                            Paint.run(this@root)
                        }
                    }
                })
                add(DesktopIcon("fab fa-firefox", "Web Browser").apply {
                    onEvent {
                        dblclick = {
                            WebBrowser.run(this@root)
                        }
                    }
                })
            }
        }
    }

    companion object {

        lateinit var taskBar: Nav

        fun addTask(window: DesktopWindow): Component {
            val task =
                Link(window.caption ?: "Window", icon = window.icon, classes = setOf("nav-item", "nav-link")).apply {
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
    startApplication(::App, module.hot)
}
