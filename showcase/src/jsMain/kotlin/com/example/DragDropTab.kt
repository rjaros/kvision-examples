package com.example

import io.kvision.core.AlignItems
import io.kvision.core.Border
import io.kvision.core.BorderStyle
import io.kvision.core.Col
import io.kvision.core.Color
import io.kvision.core.JustifyContent
import io.kvision.html.Align
import io.kvision.html.div
import io.kvision.i18n.I18n.tr
import io.kvision.panel.SimplePanel
import io.kvision.panel.hPanel
import io.kvision.panel.vPanel
import io.kvision.state.bindEach
import io.kvision.state.observableListOf
import io.kvision.utils.px

class DragDropTab : SimplePanel() {
    init {
        this.marginTop = 10.px
        this.minHeight = 400.px

        val listGreen = observableListOf(
            tr("January"),
            tr("February"),
            tr("March"),
            tr("April"),
            tr("May"),
            tr("June"),
            tr("July"),
            tr("August"),
            tr("September"),
            tr("October"),
            tr("November")
        )

        val listBlue = observableListOf(
            tr("December")
        )

        hPanel(justify = JustifyContent.CENTER, alignItems = AlignItems.FLEXSTART, useWrappers = true, spacing = 50) {
            vPanel(spacing = 10) {
                width = 200.px
                padding = 10.px
                border = Border(2.px, BorderStyle.SOLID, Color.name(Col.GREEN))
                setDropTargetData("text/xml") { data ->
                    if (data != null) {
                        listBlue.remove(data)
                        listGreen.add(data)
                    }
                }
            }.bindEach(listGreen) {
                div(it, align = Align.CENTER) {
                    padding = 3.px
                    border = Border(1.px, BorderStyle.DASHED)
                    setDragDropData("text/plain", it)
                }
            }

            vPanel(spacing = 10) {
                width = 200.px
                padding = 10.px
                border = Border(2.px, BorderStyle.SOLID, Color.name(Col.BLUE))
                setDropTargetData("text/plain") { data ->
                    if (data != null) {
                        listGreen.remove(data)
                        listBlue.add(data)
                    }
                }
            }.bindEach(listBlue) {
                div(it, align = Align.CENTER) {
                    padding = 3.px
                    border = Border(1.px, BorderStyle.DASHED)
                    setDragDropData("text/xml", it)
                }
            }

        }
    }
}
