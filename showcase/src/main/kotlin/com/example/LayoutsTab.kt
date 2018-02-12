package com.example

import pl.treksoft.kvision.core.Background
import pl.treksoft.kvision.core.COLOR
import pl.treksoft.kvision.core.Container
import pl.treksoft.kvision.html.ALIGN
import pl.treksoft.kvision.html.TAG
import pl.treksoft.kvision.html.Tag
import pl.treksoft.kvision.html.Tag.Companion.tag
import pl.treksoft.kvision.panel.DockPanel.Companion.dockPanel
import pl.treksoft.kvision.panel.FLEXALIGNITEMS
import pl.treksoft.kvision.panel.FLEXDIR
import pl.treksoft.kvision.panel.FLEXJUSTIFY
import pl.treksoft.kvision.panel.FLEXWRAP
import pl.treksoft.kvision.panel.FlexPanel.Companion.flexPanel
import pl.treksoft.kvision.panel.GRIDJUSTIFY
import pl.treksoft.kvision.panel.GridPanel.Companion.gridPanel
import pl.treksoft.kvision.panel.HPanel.Companion.hPanel
import pl.treksoft.kvision.panel.ResponsiveGridPanel.Companion.responsiveGridPanel
import pl.treksoft.kvision.panel.SIDE
import pl.treksoft.kvision.panel.SimplePanel
import pl.treksoft.kvision.panel.VPanel.Companion.vPanel
import pl.treksoft.kvision.utils.px

class LayoutsTab : SimplePanel() {
    init {
        this.marginTop = 10.px()
        this.minHeight = 400.px()
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
        tag(TAG.H4, "Horizontal layout")
        hPanel(spacing = 5) {
            add(getDiv("1", 100))
            add(getDiv("2", 150))
            add(getDiv("3", 200))
        }
    }

    private fun Container.addVPanel() {
        tag(TAG.H4, "Vertical layout")
        vPanel(spacing = 5) {
            add(getDiv("1", 100))
            add(getDiv("2", 150))
            add(getDiv("3", 200))
        }
    }

    private fun Container.addFlexPanel1() {
        tag(TAG.H4, "CSS flexbox layouts")
        flexPanel(
            FLEXDIR.ROW, FLEXWRAP.WRAP, FLEXJUSTIFY.FLEXEND, FLEXALIGNITEMS.CENTER,
            spacing = 5
        ) {
            add(getDiv("1", 100))
            add(getDiv("2", 150))
            add(getDiv("3", 200))
        }
    }

    private fun Container.addFlexPanel2() {
        flexPanel(
            FLEXDIR.ROW, FLEXWRAP.WRAP, FLEXJUSTIFY.SPACEBETWEEN, FLEXALIGNITEMS.CENTER,
            spacing = 5
        ) {
            add(getDiv("1", 100))
            add(getDiv("2", 150))
            add(getDiv("3", 200))
        }
    }

    private fun Container.addFlexPanel3() {
        flexPanel(
            FLEXDIR.ROW, FLEXWRAP.WRAP, FLEXJUSTIFY.CENTER, FLEXALIGNITEMS.CENTER,
            spacing = 5
        ) {
            add(getDiv("1", 100))
            add(getDiv("2", 150))
            add(getDiv("3", 200))
        }
    }

    private fun Container.addFlexPanel4() {
        flexPanel(
            FLEXDIR.ROW, FLEXWRAP.WRAP, FLEXJUSTIFY.FLEXSTART, FLEXALIGNITEMS.CENTER,
            spacing = 5
        ) {
            add(getDiv("1", 100), order = 3)
            add(getDiv("2", 150), order = 1)
            add(getDiv("3", 200), order = 2)
        }
    }

    private fun Container.addFlexPanel5() {
        flexPanel(
            FLEXDIR.COLUMN, FLEXWRAP.WRAP, FLEXJUSTIFY.FLEXSTART, FLEXALIGNITEMS.FLEXEND,
            spacing = 5
        ) {
            add(getDiv("1", 100), order = 3)
            add(getDiv("2", 150), order = 1)
            add(getDiv("3", 200), order = 2)
        }
    }

    private fun Container.addGridPanel1() {
        tag(TAG.H4, "CSS grid layouts")
        gridPanel(columnGap = 5, rowGap = 5, justifyItems = GRIDJUSTIFY.CENTER) {
            background = Background(COLOR.KHAKI)
            add(getDiv("1,1", 100), 1, 1)
            add(getDiv("1,2", 100), 1, 2)
            add(getDiv("2,1", 100), 2, 1)
            add(getDiv("2,2", 100), 2, 2)
        }
    }

    private fun Container.addGridPanel2() {
        gridPanel(columnGap = 5, rowGap = 5, justifyItems = GRIDJUSTIFY.CENTER) {
            background = Background(COLOR.CORNFLOWERBLUE)
            add(getDiv("1,1", 150), 1, 1)
            add(getDiv("2,2", 150), 2, 2)
            add(getDiv("3,3", 150), 3, 3)
        }
    }

    private fun Container.addRespGridPanel() {
        tag(TAG.H4, "Responsive grid layout")
        responsiveGridPanel {
            background = Background(COLOR.SILVER)
            add(getDiv("1,1", 150), 1, 1)
            add(getDiv("3,1", 150), 3, 1)
            add(getDiv("2,2", 150), 2, 2)
            add(getDiv("3,3", 150), 3, 3)
        }
    }

    private fun Container.addDockPanel() {
        tag(TAG.H4, "Dock layout")
        dockPanel {
            background = Background(COLOR.YELLOW)
            add(getDiv("CENTER", 150), SIDE.CENTER)
            add(getDiv("LEFT", 150), SIDE.LEFT)
            add(getDiv("RIGHT", 150), SIDE.RIGHT)
            add(getDiv("UP", 150).apply { marginBottom = 10.px() }, SIDE.UP)
            add(getDiv("DOWN", 150).apply { marginTop = 10.px() }, SIDE.DOWN)
        }
    }


    private fun getDiv(value: String, size: Int): Tag {
        return Tag(TAG.DIV, value).apply {
            paddingTop = ((size / 2) - 10).px()
            align = ALIGN.CENTER
            background = Background(COLOR.GREEN)
            width = size.px()
            height = size.px()
        }
    }
}
