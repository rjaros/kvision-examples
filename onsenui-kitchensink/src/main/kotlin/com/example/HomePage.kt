package com.example

import io.kvision.core.onClick
import io.kvision.html.p
import io.kvision.onsenui.core.page
import io.kvision.onsenui.tabbar.Tab
import io.kvision.onsenui.visual.card
import io.kvision.utils.px

data class CardItem(val title: String, val content: String, val pageId: String)

val cards = listOf(
    CardItem("Pull Hook", "Simple \"pull to refresh\" functionality to update data.", "pull-hook"),
    CardItem("Dialogs", "Components and utility methods to display many types of dialogs.", "dialogs"),
    CardItem("Buttons", "Different styles for buttons, floating action buttons and speed dials.", "buttons"),
    CardItem("Carousel", "Customizable carousel that can be optionally fullscreen.", "carousel"),
    CardItem("Infinite Scroll", "Two types of infinite lists: \"Load More\" and \"Lazy Repeat\".", "infinite-scroll"),
    CardItem("Progress", "Linear progress, circular progress and spinners.", "progress")
)

fun Tab.homePage(app: App) {
    page {
        p("This is a kitchen sink example that shows off the KVision bindings for Onsen UI.", className = "intro")
        cards.forEach { c ->
            card {
                title(c.title) {
                    fontSize = 20.px
                }
                content(c.content)
                onClick {
                    app.navigator.pushPage(c.pageId)
                }
            }
        }
    }
}
