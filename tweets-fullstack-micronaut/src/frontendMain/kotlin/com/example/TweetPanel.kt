package com.example

import io.kvision.core.Border
import io.kvision.core.BorderStyle
import io.kvision.core.Col
import io.kvision.core.Color
import io.kvision.core.FlexWrap
import io.kvision.core.FontWeight
import io.kvision.core.JustifyContent
import io.kvision.core.Overflow
import io.kvision.html.link
import io.kvision.html.span
import io.kvision.panel.SimplePanel
import io.kvision.panel.VPanel
import io.kvision.panel.hPanel
import io.kvision.panel.vPanel
import io.kvision.state.bind
import io.kvision.types.toStringF
import io.kvision.utils.perc
import io.kvision.utils.px

class TweetPanel : SimplePanel() {
    init {
        border = Border(1.px, BorderStyle.SOLID, Color.name(Col.SILVER))
        width = 500.px
        height = 402.px

        vPanel(spacing = 1) {
            height = 400.px
            overflow = Overflow.AUTO
        }.bind(Model.tweets) { tweets ->
            tweets.sortedByDescending { it.date?.getTime() }.forEach {
                add(Post(it))
            }
        }
    }
}

class Post(tweet: Tweet) : VPanel(spacing = 2) {
    init {
        marginRight = 2.px
        marginLeft = 2.px
        padding = 5.px
        borderBottom = Border(1.px, BorderStyle.INSET, Color.name(Col.SILVER))
        hPanel(justify = JustifyContent.SPACEBETWEEN) {
            span(tweet.nickname) {
                fontWeight = FontWeight.BOLD
            }
            span(tweet.date?.toStringF()) {
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
