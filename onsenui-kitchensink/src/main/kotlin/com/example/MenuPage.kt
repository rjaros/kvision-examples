package com.example

import pl.treksoft.kvision.core.onClick
import pl.treksoft.kvision.html.div
import pl.treksoft.kvision.html.image
import pl.treksoft.kvision.onsenui.core.page
import pl.treksoft.kvision.onsenui.list.DividerType
import pl.treksoft.kvision.onsenui.list.item
import pl.treksoft.kvision.onsenui.list.onsList
import pl.treksoft.kvision.onsenui.list.onsListTitle
import pl.treksoft.kvision.onsenui.splitter.SplitterSide
import pl.treksoft.kvision.onsenui.visual.icon
import pl.treksoft.kvision.require
import kotlin.browser.window

data class AccessItem(val title: String, val icon: String)

data class LinkItem(val title: String, val icon: String, val url: String)

val accessList = listOf(
    AccessItem("Home", "ion-ios-home, material:md-home"),
    AccessItem("Forms", "ion-ios-create, material:md-edit"),
    AccessItem("Animations", "ion-ios-film, material:md-movie-alt")
)

val linksList = listOf(
    LinkItem("KVision", "fa-github", "https://github.com/rjaros/kvision"),
    LinkItem("Onsen UI", "fa-mobile-alt", "https://onsen.io"),
    LinkItem("Code", "fa-code", "https://github.com/rjaros/kvision-examples/onsenui-kitchensink")
)

fun SplitterSide.menuPage(app: App) {
    page {
        modifier = "white"
        div(className = "profile-pic") {
            image(require("img/kvision-onsenui.png"))
        }
        onsListTitle("Access")
        onsList {
            accessList.forEachIndexed { index, accessItem ->
                item(accessItem.title, divider = if (app.isMD) DividerType.NONE else null) {
                    left {
                        icon(accessItem.icon, fixedWidth = true, className = "list-item__icon")
                    }
                    right {
                        icon("fa-link")
                    }
                    onClick {
                        this@menuPage.close()
                        app.selectTab(index + 1)
                    }
                }
            }
        }
        onsListTitle("Links")
        onsList {
            linksList.forEach { item ->
                item(item.title, divider = if (app.isMD) DividerType.NONE else null) {
                    left {
                        icon(item.icon, fixedWidth = true, className = "list-item__icon")
                    }
                    right {
                        icon("fa-external-link-alt")
                    }
                    onClick {
                        window.open(item.url, "_blank")
                    }
                }
            }
        }
    }
}