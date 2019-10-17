package com.example

import pl.treksoft.kvision.core.Border
import pl.treksoft.kvision.core.BorderStyle
import pl.treksoft.kvision.core.Col
import pl.treksoft.kvision.core.FontWeight
import pl.treksoft.kvision.core.Overflow
import pl.treksoft.kvision.data.DataContainer
import pl.treksoft.kvision.data.SorterType
import pl.treksoft.kvision.html.link
import pl.treksoft.kvision.html.span
import pl.treksoft.kvision.panel.FlexJustify
import pl.treksoft.kvision.panel.FlexWrap
import pl.treksoft.kvision.panel.hPanel
import pl.treksoft.kvision.panel.SimplePanel
import pl.treksoft.kvision.panel.VPanel
import pl.treksoft.kvision.types.toStringF
import pl.treksoft.kvision.utils.perc
import pl.treksoft.kvision.utils.px

class TweetPanel : SimplePanel() {
    init {
        border = Border(1.px, BorderStyle.SOLID, Col.SILVER)
        width = 500.px
        height = 402.px

        val dataContainer = DataContainer(Model.tweets, { tweet, _, _ ->
            Post(tweet)
        }, sorter = {
            it.id
        }, sorterType = {
            SorterType.DESC
        }, container = VPanel(spacing = 1).apply {
            height = 400.px
            overflow = Overflow.AUTO
        })
        add(dataContainer)
    }
}

class Post(tweet: Tweet) : VPanel(spacing = 2) {
    init {
        marginRight = 2.px
        marginLeft = 2.px
        padding = 5.px
        borderBottom = Border(1.px, BorderStyle.INSET, Col.SILVER)
        hPanel(justify = FlexJustify.SPACEBETWEEN) {
            span(tweet.nickname) {
                fontWeight = FontWeight.BOLD
            }
            span(tweet.date.toStringF()) {
                fontSize = 90.perc
            }
        }
        span(tweet.message)
        hPanel(spacing = 3, wrap = FlexWrap.WRAP) {
            tweet.tags.forEach {
                link("#$it") {
                    fontSize = 90.perc
                }
            }
        }
    }
}
