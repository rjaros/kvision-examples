package com.example

import io.kvision.core.onClick
import io.kvision.onsenui.core.page
import io.kvision.onsenui.list.header
import io.kvision.onsenui.list.item
import io.kvision.onsenui.list.onsList
import io.kvision.onsenui.tabbar.Tab
import io.kvision.utils.obj

object AnimModel {
    val animations = listOf("none", "default", "slide-ios", "slide-md", "lift-ios", "lift-md", "fade-ios", "fade-md")
}

fun Tab.animPage(app: App) {
    page {
        onsList {
            header("Transitions")
            AnimModel.animations.forEach { animation ->
                item(animation) {
                    modifier = "chevron"
                    onClick {
                        app.navigator.pushPage("presentation", obj {
                            this.animation = animation
                            this.data = obj {
                                this.title = animation
                            }
                        })
                    }
                }
            }
        }
    }
}
