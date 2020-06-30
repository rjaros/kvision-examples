package com.example

import pl.treksoft.kvision.core.onClick
import pl.treksoft.kvision.onsenui.core.page
import pl.treksoft.kvision.onsenui.list.header
import pl.treksoft.kvision.onsenui.list.item
import pl.treksoft.kvision.onsenui.list.onsList
import pl.treksoft.kvision.onsenui.tabbar.Tab
import pl.treksoft.kvision.utils.obj

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
