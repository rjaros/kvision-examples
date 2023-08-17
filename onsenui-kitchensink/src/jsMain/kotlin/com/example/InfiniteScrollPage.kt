package com.example

import kotlinx.browser.window
import io.kvision.html.button
import io.kvision.html.div
import io.kvision.html.p
import io.kvision.onsenui.OnsenUi
import io.kvision.onsenui.control.Segment
import io.kvision.onsenui.control.segment
import io.kvision.onsenui.core.Navigator
import io.kvision.onsenui.core.backButton
import io.kvision.onsenui.core.page
import io.kvision.onsenui.list.item
import io.kvision.onsenui.list.onsLazyRepeat
import io.kvision.onsenui.list.onsList
import io.kvision.onsenui.tabbar.TabsPosition
import io.kvision.onsenui.tabbar.tab
import io.kvision.onsenui.tabbar.tabbar
import io.kvision.onsenui.toolbar.toolbar
import io.kvision.onsenui.visual.icon
import io.kvision.state.bind
import io.kvision.state.observableListOf
import io.kvision.utils.px

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
