package com.example

import io.kvision.core.*
import io.kvision.html.ImageShape
import io.kvision.html.ListType
import io.kvision.html.code
import io.kvision.html.div
import io.kvision.html.iframe
import io.kvision.html.image
import io.kvision.html.link
import io.kvision.html.listTag
import io.kvision.html.setData
import io.kvision.html.span
import io.kvision.i18n.tr
import io.kvision.panel.SimplePanel
import io.kvision.panel.fieldsetPanel
import io.kvision.panel.vPanel
import io.kvision.require
import io.kvision.table.ResponsiveType
import io.kvision.table.TableType
import io.kvision.table.cell
import io.kvision.table.row
import io.kvision.table.table
import io.kvision.utils.perc
import io.kvision.utils.px
import kotlinx.serialization.Serializable

@Serializable
data class HbsKid(val name: String, val age: Int)

@Serializable
data class HbsPerson(val name: String, val hometown: String, val kids: List<HbsKid>)

class BasicTab : SimplePanel() {
    init {
        this.marginTop = 10.px
        this.minHeight = 400.px
        vPanel(spacing = 3, useWrappers = true) {
            span {
                +tr("A simple label")
            }
            span {
                fontFamily = "Times New Roman"
                fontSize = 32.px
                fontStyle = FontStyle.OBLIQUE
                fontWeight = FontWeight.BOLDER
                fontVariant = FontVariant.SMALLCAPS
                textDecoration =
                    TextDecoration(TextDecorationLine.UNDERLINE, TextDecorationStyle.DOTTED, Color.name(Col.RED))
                +tr("A label with custom CSS styling")
            }
            span {
                +tr("A list:")
            }
            listTag(ListType.UL, listOf(tr("First list element"), tr("Second list element"), tr("Third list element")))
            span {
                +tr("An image:")
            }
            span {
                +tr("(click to view a popover)")
            }
            image(require("img/dog.jpg"), shape = ImageShape.CIRCLE) {
                maxWidth = 100.perc
                enablePopover(
                    PopoverOptions(
                        title = tr("This is a popover"),
                        content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce nec fringilla turpis."
                    )
                )
            }
            code {
                +tr("Some text written in <code></code> HTML tag.")
            }
            div(rich = true) {
                +tr("Rich <b>text</b> <i>written</i> with <span style=\"font-family: Verdana; font-size: 14pt\">any <strong>forma</strong>tting</span>.")
            }
            link(tr("A link to Google"), "http://www.google.com")
            fieldsetPanel(tr("A fieldset")) {
                paddingTop = 10.px
                paddingBottom = 10.px
                div("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce nec fringilla turpis.")
            }
            span {
                +tr("A responsive table:")
            }
            table(
                listOf(tr("Column 1"), tr("Column 2"), tr("Column 3")),
                setOf(TableType.BORDERED, TableType.SMALL, TableType.STRIPED, TableType.HOVER),
                responsiveType = ResponsiveType.RESPONSIVE
            ) {
                row {
                    cell {
                        +"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce nec fringilla turpis."
                    }
                    cell {
                        +"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce nec fringilla turpis."
                    }
                    cell {
                        +"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce nec fringilla turpis."
                    }
                }
                row {
                    cell {
                        +"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce nec fringilla turpis."
                    }
                    cell {
                        +"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce nec fringilla turpis."
                    }
                    cell {
                        +"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce nec fringilla turpis."
                    }
                }
            }
            span {
                +tr("A Handlebars.js template:")
            }

            val data = HbsPerson("Alan", "Somewhere, TX", listOf(HbsKid("Jimmy", 12), HbsKid("Sally", 5)))

            div {
                templates = mapOf(
                    "en" to require("hbs/template1.en.hbs"),
                    "pl" to require("hbs/template1.pl.hbs")
                )
                setData(data)
            }

            span {
                +tr("An iframe:")
            }

            iframe(src = "https://rjaros.github.io/kvision/index.html") {
                width = 100.perc
                iframeWidth = 800
                iframeHeight = 400
            }

        }
    }
}