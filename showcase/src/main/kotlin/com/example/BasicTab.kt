package com.example

import pl.treksoft.kvision.html.IMAGESHAPE
import pl.treksoft.kvision.html.Image
import pl.treksoft.kvision.html.LISTTYPE
import pl.treksoft.kvision.html.Label
import pl.treksoft.kvision.html.Link
import pl.treksoft.kvision.html.ListTag
import pl.treksoft.kvision.html.TAG
import pl.treksoft.kvision.html.Tag
import pl.treksoft.kvision.panel.SimplePanel
import pl.treksoft.kvision.panel.VPanel
import pl.treksoft.kvision.utils.px

class BasicTab : SimplePanel() {
    init {
        this.marginTop = 10.px()
        this.minHeight = 400.px()
        this.add(VPanel(spacing = 3).apply {
            add(Label("A simple label"))
            add(Label("A list:"))
            add(ListTag(LISTTYPE.UL, listOf("First list element", "Second list element", "Third list element")))
            add(Label("An image:"))
            add(Image(require("./img/dog.jpg"), shape = IMAGESHAPE.CIRCLE))
            add(Tag(TAG.CODE, "Some text written in <code></code> HTML tag."))
            add(
                Tag(
                    TAG.DIV,
                    "Rich <b>text</b> <i>written</i> with <span style=\"font-family: Verdana; font-size: 14pt\">" +
                            "any <strong>forma</strong>tting</span>.",
                    rich = true
                )
            )
            add(Link("A link to Google", "http://www.google.com"))
        })
    }
}