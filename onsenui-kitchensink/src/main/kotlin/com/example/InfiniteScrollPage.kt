package com.example

import kotlinx.browser.window
import pl.treksoft.kvision.html.button
import pl.treksoft.kvision.html.div
import pl.treksoft.kvision.html.p
import pl.treksoft.kvision.onsenui.OnsenUi
import pl.treksoft.kvision.onsenui.control.Segment
import pl.treksoft.kvision.onsenui.control.segment
import pl.treksoft.kvision.onsenui.core.Navigator
import pl.treksoft.kvision.onsenui.core.backButton
import pl.treksoft.kvision.onsenui.core.page
import pl.treksoft.kvision.onsenui.list.item
import pl.treksoft.kvision.onsenui.list.onsLazyRepeat
import pl.treksoft.kvision.onsenui.list.onsList
import pl.treksoft.kvision.onsenui.tabbar.TabsPosition
import pl.treksoft.kvision.onsenui.tabbar.tab
import pl.treksoft.kvision.onsenui.tabbar.tabbar
import pl.treksoft.kvision.onsenui.toolbar.toolbar
import pl.treksoft.kvision.onsenui.visual.icon
import pl.treksoft.kvision.state.bind
import pl.treksoft.kvision.state.observableListOf
import pl.treksoft.kvision.utils.px

object InfiniteScrollModel {
    val items = observableListOf<Int>().apply {
        addAll(0 until 30)
    }

    fun loadMore() {
        items.addAll(items.size until items.size + 10)
    }
}

fun Navigator.infiniteScrollPage(app: App) {
    lateinit var segment: Segment
    page("infinite-scroll") {
        toolbar(if (app.isMD) "Infinite Scroll" else null) {
            left {
                backButton("Home")
            }
            if (!app.isMD) {
                center {
                    segment = segment {
                        width = 200.px
                        button("Load More")
                        button("Lazy Repeat")
                    }
                }
            }
        }
        tabbar(TabsPosition.AUTO) {
            if (!app.isMD) segment.tabbar = this
            tab("Load More") {
                page {
                    p(
                        "Useful for loading more items when the scroll reaches the bottom of the page, typically after an asynchronous API call.",
                        className = "intro"
                    )
                    onsList().bind(InfiniteScrollModel.items) { items ->
                        items.forEach {
                            item("Item #$it")
                        }
                    }
                    div(className = "after-list") {
                        icon("fa-spinner", size = "26px", spin = true)
                    }
                    onInfiniteScroll { done ->
                        window.setTimeout({
                            InfiniteScrollModel.loadMore()
                            done()
                        }, 600)
                    }
                }
            }
            tab("Lazy Repeat") {
                page {
                    p(
                        "Automatically unloads items that are not visible and loads new ones. Useful when the list contains thousands of items.",
                        className = "intro"
                    )
                    onsList {
                        onsLazyRepeat(3000) { index ->
                            OnsenUi.createElement("<ons-list-item>Item #$index</ons-list-item>")
                        }
                    }
                }
            }
        }
    }
}
