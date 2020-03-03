package com.example

import kotlinx.serialization.Serializable
import pl.treksoft.kvision.core.*
import pl.treksoft.kvision.html.*
import pl.treksoft.kvision.i18n.I18n.tr
import pl.treksoft.kvision.panel.SimplePanel
import pl.treksoft.kvision.panel.fieldsetPanel
import pl.treksoft.kvision.panel.vPanel
import pl.treksoft.kvision.require
import pl.treksoft.kvision.table.ResponsiveType
import pl.treksoft.kvision.table.TableType
import pl.treksoft.kvision.table.cell
import pl.treksoft.kvision.table.row
import pl.treksoft.kvision.table.table
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
                textDecoration = TextDecoration(TextDecorationLine.UNDERLINE, TextDecorationStyle.DOTTED, Color.name(Col.RED))
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
            fieldsetPanel(tr("A fieldset")) {
                paddingTop = 10.px
                paddingBottom = 10.px
                tag(TAG.DIV, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce nec fringilla turpis.")
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

            iframe(src = "https://rjaros.github.io/kvision/api/") {
                iframeWidth = 600
                iframeHeight = 300
            }

        }
    }
}