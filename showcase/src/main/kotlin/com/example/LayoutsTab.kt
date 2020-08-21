package com.example

import pl.treksoft.kvision.core.AlignItems
import pl.treksoft.kvision.core.Background
import pl.treksoft.kvision.core.Col
import pl.treksoft.kvision.core.Color
import pl.treksoft.kvision.core.Container
import pl.treksoft.kvision.core.FlexDirection
import pl.treksoft.kvision.core.FlexWrap
import pl.treksoft.kvision.core.JustifyContent
import pl.treksoft.kvision.core.JustifyItems
import pl.treksoft.kvision.html.Align
import pl.treksoft.kvision.html.Div
import pl.treksoft.kvision.html.Tag
import pl.treksoft.kvision.html.h4
import pl.treksoft.kvision.i18n.I18n.tr
import pl.treksoft.kvision.panel.Side
import pl.treksoft.kvision.panel.SimplePanel
import pl.treksoft.kvision.panel.dockPanel
import pl.treksoft.kvision.panel.flexPanel
import pl.treksoft.kvision.panel.gridPanel
import pl.treksoft.kvision.panel.hPanel
import pl.treksoft.kvision.panel.responsiveGridPanel
import pl.treksoft.kvision.panel.vPanel
import pl.treksoft.kvision.utils.auto
import pl.treksoft.kvision.utils.px

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
            add(getDiv("1", 100))
            add(getDiv("2", 150))
            add(getDiv("3", 200))
        }
    }

    private fun Container.addVPanel() {
        h4(tr("Vertical layout"))
        vPanel(spacing = 5) {
            add(getDiv("1", 100))
            add(getDiv("2", 150))
            add(getDiv("3", 200))
        }
    }

    private fun Container.addFlexPanel1() {
        h4(tr("CSS flexbox layouts"))
        flexPanel(
            FlexDirection.ROW, FlexWrap.WRAP, JustifyContent.FLEXEND, AlignItems.CENTER,
            spacing = 5
        ) {
            add(getDiv("1", 100))
            add(getDiv("2", 150))
            add(getDiv("3", 200))
        }
    }

    private fun Container.addFlexPanel2() {
        flexPanel(
            FlexDirection.ROW, FlexWrap.WRAP, JustifyContent.SPACEBETWEEN, AlignItems.CENTER,
            spacing = 5
        ) {
            add(getDiv("1", 100))
            add(getDiv("2", 150))
            add(getDiv("3", 200))
        }
    }

    private fun Container.addFlexPanel3() {
        flexPanel(
            FlexDirection.ROW, FlexWrap.WRAP, JustifyContent.CENTER, AlignItems.CENTER,
            spacing = 5
        ) {
            add(getDiv("1", 100))
            add(getDiv("2", 150))
            add(getDiv("3", 200))
        }
    }

    private fun Container.addFlexPanel4() {
        flexPanel(
            FlexDirection.ROW, FlexWrap.WRAP, JustifyContent.FLEXSTART, AlignItems.CENTER,
            spacing = 5
        ) {
            add(getDiv("1", 100), order = 3)
            add(getDiv("2", 150), order = 1)
            add(getDiv("3", 200), order = 2)
        }
    }

    private fun Container.addFlexPanel5() {
        flexPanel(
            FlexDirection.COLUMN, FlexWrap.WRAP, JustifyContent.FLEXSTART, AlignItems.FLEXEND,
            spacing = 5
        ) {
            add(getDiv("1", 100), order = 3)
            add(getDiv("2", 150), order = 1)
            add(getDiv("3", 200), order = 2)
        }
    }

    private fun Container.addGridPanel1() {
        h4(tr("CSS grid layouts"))
        gridPanel(columnGap = 5, rowGap = 5, justifyItems = JustifyItems.CENTER) {
            background = Background(Color.name(Col.KHAKI))
            add(getDiv("1,1", 100), 1, 1)
            add(getDiv("1,2", 100), 1, 2)
            add(getDiv("2,1", 100), 2, 1)
            add(getDiv("2,2", 100), 2, 2)
        }
    }

    private fun Container.addGridPanel2() {
        gridPanel(columnGap = 5, rowGap = 5, justifyItems = JustifyItems.CENTER) {
            background = Background(Color.name(Col.CORNFLOWERBLUE))
            add(getDiv("1,1", 150), 1, 1)
            add(getDiv("2,2", 150), 2, 2)
            add(getDiv("3,3", 150), 3, 3)
        }
    }

    private fun Container.addRespGridPanel() {
        h4(tr("Responsive grid layout"))
        responsiveGridPanel {
            background = Background(Color.name(Col.SILVER))
            add(getDiv("1,1", 150), 1, 1)
            add(getDiv("3,1", 150), 3, 1)
            add(getDiv("2,2", 150), 2, 2)
            add(getDiv("3,3", 150), 3, 3)
        }
    }

    private fun Container.addDockPanel() {
        h4(tr("Dock layout"))
        dockPanel {
            background = Background(Color.name(Col.YELLOW))
            add(getDiv(tr("CENTER"), 150).apply { margin = auto }, Side.CENTER)
            add(getDiv(tr("LEFT"), 150), Side.LEFT)
            add(getDiv(tr("RIGHT"), 150), Side.RIGHT)
            add(getDiv(tr("UP"), 150).apply { margin = auto; marginBottom = 10.px }, Side.UP)
            add(getDiv(tr("DOWN"), 150).apply { margin = auto; marginTop = 10.px }, Side.DOWN)
        }
    }

    private fun getDiv(value: String, size: Int): Tag {
        return Div(value).apply {
            paddingTop = ((size / 2) - 10).px
            align = Align.CENTER
            background = Background(Color.name(Col.GREEN))
            width = size.px
            height = size.px
        }
    }
}
