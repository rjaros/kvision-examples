package com.example

import pl.treksoft.kvision.core.Background
import pl.treksoft.kvision.core.Col
import pl.treksoft.kvision.core.Color
import pl.treksoft.kvision.core.Container
import pl.treksoft.kvision.dropdown.dropDown
import pl.treksoft.kvision.html.div
import pl.treksoft.kvision.html.h4
import pl.treksoft.kvision.i18n.I18n.tr
import pl.treksoft.kvision.panel.Direction
import pl.treksoft.kvision.panel.SideTabSize
import pl.treksoft.kvision.panel.SimplePanel
import pl.treksoft.kvision.panel.TabPosition
import pl.treksoft.kvision.panel.splitPanel
import pl.treksoft.kvision.panel.stackPanel
import pl.treksoft.kvision.panel.tab
import pl.treksoft.kvision.panel.tabPanel
import pl.treksoft.kvision.panel.vPanel
import pl.treksoft.kvision.utils.px

class ContainersTab : SimplePanel() {
    init {
        this.marginTop = 10.px
        vPanel(spacing = 5) {
            addStackPanel()
            addTabPanel()
            addTabPanelLeft()
            addTabPanelRight()
            addVerticalSplitPanel()
            addHorizontalSplitPanel()
        }
    }

    private fun Container.addStackPanel() {
        h4(tr("Stack panel"))
        stackPanel {
            route("/containers/blue") {
                div("&nbsp;", rich = true) {
                    background = Background(Color.name(Col.BLUE))
                    height = 40.px
                }
            }
            route("/containers/green") {
                div("&nbsp;", rich = true) {
                    background = Background(Color.name(Col.GREEN))
                    height = 40.px
                }
            }
        }
        dropDown(
            tr("Activate panel from the stack"), listOf(
                tr("Blue panel") to "#!/containers/blue",
                tr("Green panel") to "#!/containers/green"
            )
        )
    }

    private fun Container.addTabPanel() {
        h4(tr("Tab panel with draggable tabs"))
        tabPanel(draggableTabs = true) {
            tab(tr("Blue panel")) {
                div("&nbsp;", rich = true) {
                    background = Background(Color.name(Col.BLUE))
                    height = 40.px
                }
            }
            tab(tr("Green panel")) {
                div("&nbsp;", rich = true) {
                    background = Background(Color.name(Col.GREEN))
                    height = 40.px
                }
            }
            tab(tr("Red panel (closable)"), closable = true) {
                div("&nbsp;", rich = true) {
                    background = Background(Color.name(Col.RED))
                    height = 40.px
                }
            }
        }
    }

    private fun Container.addTabPanelLeft() {
        h4(tr("Tab panel with tabs on the left"))
        tabPanel(TabPosition.LEFT, SideTabSize.SIZE_1) {
            tab(tr("Blue panel")) {
                div("&nbsp;", rich = true) {
                    background = Background(Color.name(Col.BLUE))
                    height = 140.px
                }
            }
            tab(tr("Green panel")) {
                div("&nbsp;", rich = true) {
                    background = Background(Color.name(Col.GREEN))
                    height = 140.px
                }
            }
        }
    }

    private fun Container.addTabPanelRight() {
        h4(tr("Tab panel with tabs on the right"))
        tabPanel(TabPosition.RIGHT, SideTabSize.SIZE_1) {
            tab(tr("Blue panel")) {
                div("&nbsp;", rich = true) {
                    background = Background(Color.name(Col.BLUE))
                    height = 140.px
                }
            }
            tab(tr("Green panel")) {
                div("&nbsp;", rich = true) {
                    background = Background(Color.name(Col.GREEN))
                    height = 140.px
                }
            }
        }
    }

    private fun Container.addVerticalSplitPanel() {
        h4(tr("Vertical split panel"))
        splitPanel {
            div("&nbsp;", rich = true) {
                background = Background(Color.name(Col.BLUE))
                height = 200.px
            }
            div("&nbsp;", rich = true) {
                background = Background(Color.name(Col.GREEN))
                height = 200.px
            }
        }
    }

    private fun Container.addHorizontalSplitPanel() {
        h4(tr("Horizontal split panel"))
        splitPanel(direction = Direction.HORIZONTAL) {
            height = 220.px
            div("&nbsp;", rich = true) {
                background = Background(Color.name(Col.BLUE))
                height = 100.px
            }
            div("&nbsp;", rich = true) {
                background = Background(Color.name(Col.GREEN))
                height = 100.px
            }
        }
    }
}
