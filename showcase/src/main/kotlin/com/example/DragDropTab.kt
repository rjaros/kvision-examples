package com.example

import com.lightningkite.kotlin.observable.list.observableListOf
import pl.treksoft.kvision.core.Border
import pl.treksoft.kvision.core.BorderStyle
import pl.treksoft.kvision.core.Col
import pl.treksoft.kvision.data.BaseDataComponent
import pl.treksoft.kvision.data.DataContainer
import pl.treksoft.kvision.html.Align
import pl.treksoft.kvision.html.Div
import pl.treksoft.kvision.panel.FlexAlignItems
import pl.treksoft.kvision.panel.FlexJustify
import pl.treksoft.kvision.panel.HPanel.Companion.hPanel
import pl.treksoft.kvision.panel.SimplePanel
import pl.treksoft.kvision.panel.VPanel
import pl.treksoft.kvision.utils.px

class DragDropTab : SimplePanel() {
    init {
        this.marginTop = 10.px
        this.minHeight = 400.px

        class DataModel(text: String) : BaseDataComponent() {
            var text: String by obs(text)
        }

        val listGreen = observableListOf(
            DataModel("January"),
            DataModel("February"),
            DataModel("March"),
            DataModel("April"),
            DataModel("May"),
            DataModel("June"),
            DataModel("July"),
            DataModel("August"),
            DataModel("September"),
            DataModel("October"),
            DataModel("November")
        )

        val listBlue = observableListOf(
            DataModel("December")
        )

        val dataContainer1 = DataContainer(listGreen, { index ->
            Div(listGreen[index].text, align = Align.CENTER) {
                padding = 3.px
                border = Border(1.px, BorderStyle.DASHED)
                setDragDropData("text/plain", "$index")
            }
        }, child = VPanel(spacing = 10) {
            width = 200.px
            padding = 10.px
            border = Border(2.px, BorderStyle.SOLID, Col.GREEN)
            setDropTargetData("text/xml") { data ->
                if (data != null) {
                    val element = listBlue[data.toInt()].text
                    listBlue.removeAt(data.toInt())
                    listGreen.add(DataModel(element))
                }
            }
        })

        val dataContainer2 = DataContainer(listBlue, { index ->
            Div(listBlue[index].text, align = Align.CENTER) {
                padding = 3.px
                border = Border(1.px, BorderStyle.DASHED)
                setDragDropData("text/xml", "$index")
            }
        }, child = VPanel(spacing = 10) {
            width = 200.px
            padding = 10.px
            border = Border(2.px, BorderStyle.SOLID, Col.BLUE)
            setDropTargetData("text/plain") { data ->
                if (data != null) {
                    val element = listGreen[data.toInt()].text
                    listGreen.removeAt(data.toInt())
                    listBlue.add(DataModel(element))
                }
            }
        })

        val panel = hPanel(justify = FlexJustify.CENTER, alignItems = FlexAlignItems.FLEXSTART, spacing = 50)
        panel.add(dataContainer1)
        panel.add(dataContainer2)
    }
}
