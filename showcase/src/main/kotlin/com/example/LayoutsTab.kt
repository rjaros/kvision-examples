package com.example

import io.kvision.core.AlignItems
import io.kvision.core.Background
import io.kvision.core.Col
import io.kvision.core.Color
import io.kvision.core.Container
import io.kvision.core.FlexDirection
import io.kvision.core.FlexWrap
import io.kvision.core.JustifyContent
import io.kvision.core.JustifyItems
import io.kvision.html.Align
import io.kvision.html.Tag
import io.kvision.html.div
import io.kvision.html.h4
import io.kvision.i18n.I18n.tr
import io.kvision.panel.SimplePanel
import io.kvision.panel.dockPanel
import io.kvision.panel.flexPanel
import io.kvision.panel.gridPanel
import io.kvision.panel.hPanel
import io.kvision.panel.responsiveGridPanel
import io.kvision.panel.vPanel
import io.kvision.utils.auto
import io.kvision.utils.px

class LayoutsTab : SimplePanel() {
    init {
        this.marginTop = 10.px
        this.minHeight = 400.px
        vPanel(spacing = 5) {
            addHPanel()
            addVPanel()
            addFlexPanel1()
            addFlexPanel2()
            addFlexPanel3()
            addFlexPanel4()
            addFlexPanel5()
            addGridPanel1()
            addGridPanel2()
            addRespGridPanel()
            addDockPanel()
        }
    }

    private fun Container.addHPanel() {
        h4(tr("Horizontal layout"))
        hPanel(spacing = 5) {
            customDiv("1", 100)
            customDiv("2", 150)
            customDiv("3", 200)
        }
    }

    private fun Container.addVPanel() {
        h4(tr("Vertical layout"))
        vPanel(spacing = 5) {
            customDiv("1", 100)
            customDiv("2", 150)
            customDiv("3", 200)
        }
    }

    private fun Container.addFlexPanel1() {
        h4(tr("CSS flexbox layouts"))
        flexPanel(
            FlexDirection.ROW, FlexWrap.WRAP, JustifyContent.FLEXEND, AlignItems.CENTER,
            spacing = 5
        ) {
            customDiv("1", 100)
            customDiv("2", 150)
            customDiv("3", 200)
        }
    }

    private fun Container.addFlexPanel2() {
        flexPanel(
            FlexDirection.ROW, FlexWrap.WRAP, JustifyContent.SPACEBETWEEN, AlignItems.CENTER,
            spacing = 5
        ) {
            customDiv("1", 100)
            customDiv("2", 150)
            customDiv("3", 200)
        }
    }

    private fun Container.addFlexPanel3() {
        flexPanel(
            FlexDirection.ROW, FlexWrap.WRAP, JustifyContent.CENTER, AlignItems.CENTER,
            spacing = 5
        ) {
            customDiv("1", 100)
            customDiv("2", 150)
            customDiv("3", 200)
        }
    }

    private fun Container.addFlexPanel4() {
        flexPanel(
            FlexDirection.ROW, FlexWrap.WRAP, JustifyContent.FLEXSTART, AlignItems.CENTER,
            spacing = 5
        ) {
            options(order = 3) {
                customDiv("1", 100)
            }
            options(order = 1) {
                customDiv("2", 150)
            }
            options(order = 2) {
                customDiv("3", 200)
            }
        }
    }

    private fun Container.addFlexPanel5() {
        flexPanel(
            FlexDirection.COLUMN, FlexWrap.WRAP, JustifyContent.FLEXSTART, AlignItems.FLEXEND,
            spacing = 5
        ) {
            options(order = 3) {
                customDiv("1", 100)
            }
            options(order = 1) {
                customDiv("2", 150)
            }
            options(order = 2) {
                customDiv("3", 200)
            }
        }
    }

    private fun Container.addGridPanel1() {
        h4(tr("CSS grid layouts"))
        gridPanel(columnGap = 5, rowGap = 5, justifyItems = JustifyItems.CENTER) {
            background = Background(Color.name(Col.KHAKI))
            options(1, 1) {
                customDiv("1,1", 100)
            }
            options(1, 2) {
                customDiv("1,2", 100)
            }
            options(2, 1) {
                customDiv("2,1", 100)
            }
            options(2, 2) {
                customDiv("2,2", 100)
            }
        }
    }

    private fun Container.addGridPanel2() {
        gridPanel(columnGap = 5, rowGap = 5, justifyItems = JustifyItems.CENTER) {
            background = Background(Color.name(Col.CORNFLOWERBLUE))
            options(1, 1) {
                customDiv("1,1", 150)
            }
            options(2, 2) {
                customDiv("2,2", 150)
            }
            options(3, 3) {
                customDiv("3,3", 150)
            }
        }
    }

    private fun Container.addRespGridPanel() {
        h4(tr("Responsive grid layout"))
        responsiveGridPanel {
            background = Background(Color.name(Col.SILVER))
            options(1, 1) {
                customDiv("1,1", 150)
            }
            options(3, 1) {
                customDiv("3,1", 150)
            }
            options(2, 2) {
                customDiv("2,2", 150)
            }
            options(3, 3) {
                customDiv("3,3", 150)
            }
        }
    }

    private fun Container.addDockPanel() {
        h4(tr("Dock layout"))
        dockPanel {
            background = Background(Color.name(Col.YELLOW))
            center {
                customDiv(tr("CENTER"), 150).apply { margin = auto }
            }
            left {
                customDiv(tr("LEFT"), 150)
            }
            right {
                customDiv(tr("RIGHT"), 150)
            }
            up {
                customDiv(tr("UP"), 150).apply { margin = auto; marginBottom = 10.px }
            }
            down {
                customDiv(tr("DOWN"), 150).apply { margin = auto; marginTop = 10.px }
            }
        }
    }

    private fun Container.customDiv(value: String, size: Int): Tag {
        return div(value).apply {
            paddingTop = ((size / 2) - 10).px
            align = Align.CENTER
            background = Background(Color.name(Col.GREEN))
            width = size.px
            height = size.px
        }
    }
}
