package com.example

import kotlinx.browser.window
import io.kvision.core.onClick
import io.kvision.html.div
import io.kvision.html.image
import io.kvision.onsenui.core.page
import io.kvision.onsenui.list.DividerType
import io.kvision.onsenui.list.item
import io.kvision.onsenui.list.onsList
import io.kvision.onsenui.list.onsListTitle
import io.kvision.onsenui.splitter.SplitterSide
import io.kvision.onsenui.visual.icon
import io.kvision.require

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
    LinkItem("Code", "fa-code", "https://github.com/rjaros/kvision-examples/tree/master/onsenui-kitchensink")
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