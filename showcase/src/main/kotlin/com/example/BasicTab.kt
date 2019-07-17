package com.example

import kotlinx.serialization.Serializable
import pl.treksoft.kvision.core.Col
import pl.treksoft.kvision.core.FontStyle
import pl.treksoft.kvision.core.FontVariant
import pl.treksoft.kvision.core.FontWeight
import pl.treksoft.kvision.core.PopoverOptions
import pl.treksoft.kvision.core.TextDecoration
import pl.treksoft.kvision.core.TextDecorationLine
import pl.treksoft.kvision.core.TextDecorationStyle
import pl.treksoft.kvision.core.TooltipOptions
import pl.treksoft.kvision.html.Div.Companion.div
import pl.treksoft.kvision.html.Iframe.Companion.iframe
import pl.treksoft.kvision.html.Image.Companion.image
import pl.treksoft.kvision.html.ImageShape
import pl.treksoft.kvision.html.Link.Companion.link
import pl.treksoft.kvision.html.ListTag.Companion.listTag
import pl.treksoft.kvision.html.ListType
import pl.treksoft.kvision.html.Span.Companion.span
import pl.treksoft.kvision.html.TAG
import pl.treksoft.kvision.html.Tag.Companion.tag
import pl.treksoft.kvision.html.setData
import pl.treksoft.kvision.i18n.I18n.tr
import pl.treksoft.kvision.panel.SimplePanel
import pl.treksoft.kvision.panel.VPanel.Companion.vPanel
import pl.treksoft.kvision.require
import pl.treksoft.kvision.table.Cell.Companion.cell
import pl.treksoft.kvision.table.Row.Companion.row
import pl.treksoft.kvision.table.Table.Companion.table
import pl.treksoft.kvision.table.TableType
import pl.treksoft.kvision.utils.px

@Serializable
data class HbsKid(val name: String, val age: Int)

@Serializable
data class HbsPerson(val name: String, val hometown: String, val kids: List<HbsKid>)

class BasicTab : SimplePanel() {
    init {
        this.marginTop = 10.px
        this.minHeight = 400.px
        vPanel(spacing = 3) {
            span {
                +tr("A simple label")
            }
            span {
                fontFamily = "Times New Roman"
                fontSize = 32.px
                fontStyle = FontStyle.OBLIQUE
                fontWeight = FontWeight.BOLDER
                fontVariant = FontVariant.SMALLCAPS
                textDecoration = TextDecoration(TextDecorationLine.UNDERLINE, TextDecorationStyle.DOTTED, Col.RED)
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
                +tr("(hover to view a tooltip and click to view a popover)")
            }
            image(require("img/dog.jpg"), shape = ImageShape.CIRCLE) {
                enableTooltip(TooltipOptions(title = tr("This is a tooltip")))
                enablePopover(
                    PopoverOptions(
                        title = tr("This is a popover"),
                        content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce nec fringilla turpis."
                    )
                )
            }
            tag(TAG.CODE) {
                +tr("Some text written in <code></code> HTML tag.")
            }
            div(rich = true) {
                +tr("Rich <b>text</b> <i>written</i> with <span style=\"font-family: Verdana; font-size: 14pt\">any <strong>forma</strong>tting</span>.")
            }
            link(tr("A link to Google"), "http://www.google.com")
            span {
                +tr("A responsive table:")
            }
            table(
                listOf(tr("Column 1"), tr("Column 2"), tr("Column 3")),
                setOf(TableType.BORDERED, TableType.CONDENSED, TableType.STRIPED, TableType.HOVER), responsive = true
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

            iframe(src = "https://rjaros.github.io/kvision/api/") {
                iframeWidth = 600
                iframeHeight = 300
            }

        }
    }
}