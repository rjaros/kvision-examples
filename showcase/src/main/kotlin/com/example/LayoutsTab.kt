package com.example

import pl.treksoft.kvision.core.Background
import pl.treksoft.kvision.core.COLOR
import pl.treksoft.kvision.core.Container
import pl.treksoft.kvision.html.ALIGN
import pl.treksoft.kvision.html.TAG
import pl.treksoft.kvision.html.Tag
import pl.treksoft.kvision.panel.*
import pl.treksoft.kvision.utils.px

class LayoutsTab : SimplePanel() {
    init {
        this.marginTop = 10.px()
        this.minHeight = 400.px()
        val panel = VPanel(spacing = 5)
        addHPanel(panel)
        addVPanel(panel)
        addFlexPanel1(panel)
        addFlexPanel2(panel)
        addFlexPanel3(panel)
        addFlexPanel4(panel)
        addFlexPanel5(panel)
        addGridPanel1(panel)
        addGridPanel2(panel)
        addRespGridPanel(panel)
        addDockPanel(panel)
        this.add(panel)
    }

    private fun addHPanel(panel: Container) {
        panel.add(Tag(TAG.H4, "Horizontal layout"))
        panel.add(HPanel(spacing = 5).apply {
            add(getDiv("1", 100))
            add(getDiv("2", 150))
            add(getDiv("3", 200))
        })
    }

    private fun addVPanel(panel: Container) {
        panel.add(Tag(TAG.H4, "Vertical layout"))
        panel.add(VPanel(spacing = 5).apply {
            add(getDiv("1", 100))
            add(getDiv("2", 150))
            add(getDiv("3", 200))
        })
    }

    private fun addFlexPanel1(panel: Container) {
        panel.add(Tag(TAG.H4, "CSS flexbox layouts"))
        panel.add(FlexPanel(
            FLEXDIR.ROW, FLEXWRAP.WRAP, FLEXJUSTIFY.FLEXEND, FLEXALIGNITEMS.CENTER,
            spacing = 5
        ).apply {
            add(getDiv("1", 100))
            add(getDiv("2", 150))
            add(getDiv("3", 200))
        })
    }

    private fun addFlexPanel2(panel: Container) {
        panel.add(FlexPanel(
            FLEXDIR.ROW, FLEXWRAP.WRAP, FLEXJUSTIFY.SPACEBETWEEN, FLEXALIGNITEMS.CENTER,
            spacing = 5
        ).apply {
            add(getDiv("1", 100))
            add(getDiv("2", 150))
            add(getDiv("3", 200))
        })
    }

    private fun addFlexPanel3(panel: Container) {
        panel.add(FlexPanel(
            FLEXDIR.ROW, FLEXWRAP.WRAP, FLEXJUSTIFY.CENTER, FLEXALIGNITEMS.CENTER,
            spacing = 5
        ).apply {
            add(getDiv("1", 100))
            add(getDiv("2", 150))
            add(getDiv("3", 200))
        })
    }

    private fun addFlexPanel4(panel: Container) {
        panel.add(FlexPanel(
            FLEXDIR.ROW, FLEXWRAP.WRAP, FLEXJUSTIFY.FLEXSTART, FLEXALIGNITEMS.CENTER,
            spacing = 5
        ).apply {
            add(getDiv("1", 100), order = 3)
            add(getDiv("2", 150), order = 1)
            add(getDiv("3", 200), order = 2)
        })
    }

    private fun addFlexPanel5(panel: Container) {
        panel.add(FlexPanel(
            FLEXDIR.COLUMN, FLEXWRAP.WRAP, FLEXJUSTIFY.FLEXSTART, FLEXALIGNITEMS.FLEXEND,
            spacing = 5
        ).apply {
            add(getDiv("1", 100), order = 3)
            add(getDiv("2", 150), order = 1)
            add(getDiv("3", 200), order = 2)
        })
    }

    private fun addGridPanel1(panel: Container) {
        panel.add(Tag(TAG.H4, "CSS grid layouts"))
        panel.add(GridPanel(columnGap = 5, rowGap = 5, justifyItems = GRIDJUSTIFY.CENTER).apply {
            background = Background(COLOR.KHAKI)
            add(getDiv("1,1", 100), 1, 1)
            add(getDiv("1,2", 100), 1, 2)
            add(getDiv("2,1", 100), 2, 1)
            add(getDiv("2,2", 100), 2, 2)
        })
    }

    private fun addGridPanel2(panel: Container) {
        panel.add(GridPanel(columnGap = 5, rowGap = 5, justifyItems = GRIDJUSTIFY.CENTER).apply {
            background = Background(COLOR.CORNFLOWERBLUE)
            add(getDiv("1,1", 150), 1, 1)
            add(getDiv("2,2", 150), 2, 2)
            add(getDiv("3,3", 150), 3, 3)
        })
    }

    private fun addRespGridPanel(panel: Container) {
        panel.add(Tag(TAG.H4, "Responsive grid layout"))
        panel.add(ResponsiveGridPanel().apply {
            background = Background(COLOR.SILVER)
            add(getDiv("1,1", 150), 1, 1)
            add(getDiv("3,1", 150), 3, 1)
            add(getDiv("2,2", 150), 2, 2)
            add(getDiv("3,3", 150), 3, 3)
        })
    }

    private fun addDockPanel(panel: Container) {
        panel.add(Tag(TAG.H4, "Dock layout"))
        panel.add(DockPanel().apply {
            background = Background(COLOR.YELLOW)
            add(getDiv("CENTER", 150), SIDE.CENTER)
            add(getDiv("LEFT", 150), SIDE.LEFT)
            add(getDiv("RIGHT", 150), SIDE.RIGHT)
            add(getDiv("UP", 150).apply { marginBottom = 10.px() }, SIDE.UP)
            add(getDiv("DOWN", 150).apply { marginTop = 10.px() }, SIDE.DOWN)
        })
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
