package com.example

import pl.treksoft.kvision.core.Col
import pl.treksoft.kvision.core.FontStyle
import pl.treksoft.kvision.core.FontVariant
import pl.treksoft.kvision.core.FontWeight
import pl.treksoft.kvision.core.TextDecoration
import pl.treksoft.kvision.core.TextDecorationLine
import pl.treksoft.kvision.core.TextDecorationStyle
import pl.treksoft.kvision.html.Div.Companion.div
import pl.treksoft.kvision.html.Iframe.Companion.iframe
import pl.treksoft.kvision.html.Image.Companion.image
import pl.treksoft.kvision.html.ImageShape
import pl.treksoft.kvision.html.Label.Companion.label
import pl.treksoft.kvision.html.Link.Companion.link
import pl.treksoft.kvision.html.ListTag.Companion.listTag
import pl.treksoft.kvision.html.ListType
import pl.treksoft.kvision.html.TAG
import pl.treksoft.kvision.html.Tag.Companion.tag
import pl.treksoft.kvision.panel.SimplePanel
import pl.treksoft.kvision.panel.VPanel.Companion.vPanel
import pl.treksoft.kvision.require
import pl.treksoft.kvision.table.Cell.Companion.cell
import pl.treksoft.kvision.table.Row.Companion.row
import pl.treksoft.kvision.table.Table.Companion.table
import pl.treksoft.kvision.table.TableType
import pl.treksoft.kvision.utils.obj
import pl.treksoft.kvision.utils.px

class BasicTab : SimplePanel() {
    init {
        this.marginTop = 10.px
        this.minHeight = 400.px
        vPanel(spacing = 3) {
            label("A simple label")
            label("A label with custom CSS styling") {
                fontFamily = "Times New Roman"
                fontSize = 32.px
                fontStyle = FontStyle.OBLIQUE
                fontWeight = FontWeight.BOLDER
                fontVariant = FontVariant.SMALLCAPS
                textDecoration = TextDecoration(TextDecorationLine.UNDERLINE, TextDecorationStyle.DOTTED, Col.RED)
            }
            label("A list:")
            listTag(ListType.UL, listOf("First list element", "Second list element", "Third list element"))
            label("An image:")
            image(require("./img/dog.jpg"), shape = ImageShape.CIRCLE)
            tag(TAG.CODE, "Some text written in <code></code> HTML tag.")
            tag(
                TAG.DIV,
                "Rich <b>text</b> <i>written</i> with <span style=\"font-family: Verdana; font-size: 14pt\">" +
                        "any <strong>forma</strong>tting</span>.",
                rich = true
            )
            link("A link to Google", "http://www.google.com")
            label("A responsive table:")
            table(
                listOf("Column 1", "Column 2", "Column 3"),
                setOf(TableType.BORDERED, TableType.CONDENSED, TableType.STRIPED, TableType.HOVER), responsive = true
            ) {
                row {
                    cell("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce nec fringilla turpis.")
                    cell("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce nec fringilla turpis.")
                    cell("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce nec fringilla turpis.")
                }
                row {
                    cell("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce nec fringilla turpis.")
                    cell("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce nec fringilla turpis.")
                    cell("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce nec fringilla turpis.")
                }
            }
            label("A Handlebars.js template:")

            val data = obj {
                name = "Alan"
                hometown = "Somewhere, TX"
                kids = arrayOf(obj {
                    name = "Jimmy"
                    age = "12"
                }, obj {
                    name = "Sally"
                    age = "4"
                })
            }

            div {
                template = require("./hbs/template1.hbs")
                templateData = data
            }

            label("An iframe:")

            iframe(src = "https://rjaros.github.io/kvision/") {
                iframeWidth = 600
                iframeHeight = 300
            }

        }
    }
}