package com.example

import kotlinx.browser.window
import io.kvision.html.div
import io.kvision.html.image
import io.kvision.onsenui.core.Navigator
import io.kvision.onsenui.core.backButton
import io.kvision.onsenui.core.page
import io.kvision.onsenui.core.pullHook
import io.kvision.onsenui.list.header
import io.kvision.onsenui.list.item
import io.kvision.onsenui.list.onsList
import io.kvision.onsenui.toolbar.toolbar
import io.kvision.onsenui.visual.icon
import io.kvision.onsenui.visual.progressCircular
import io.kvision.state.bind
import io.kvision.state.observableListOf
import io.kvision.utils.px
import kotlin.random.Random

data class Kitten(val name: String, val url: String)

object PullHookModel {
    private val names = listOf("Oscar", "Max", "Tiger", "Sam", "Misty", "Simba", "Coco", "Chloe", "Lucy", "Missy")
    val kittens = observableListOf<Kitten>()

    private fun getRandomKitten(): Kitten {
        val name = names[Random.nextInt(names.size)]
        val width = Random.nextInt(40, 60)
        val height = Random.nextInt(40, 60)
        val url = "https://placekitten.com/g/$width/$height"
        return Kitten(name, url)
    }

    fun addRandmomKitten() {
        kittens.add(getRandomKitten())
    }

    init {
        for (index in 1..8) {
            addRandmomKitten()
        }
    }
}

fun Navigator.pullHookPage(app: App) {
    page("pull-hook") {
        toolbar("Pull Hook") {
            left {
                backButton("Home")
            }
        }
        pullHook {
            if (app.isMD) {
                fixedContent = true
                pullHeight = 84.px
                div(className = "pull-hook-progress") {
                    progressCircular(indeterminate = true)
                }
            } else {
                fixedContent = false
                pullHeight = 64.px
                icon("fa-spinner", "22px", spin = true, className = "pull-hook-spinner")
            }
            onAction {
                window.setTimeout({
                    PullHookModel.addRandmomKitten()
                    it()
                }, 1500)
            }
        }
        onsList().bind(PullHookModel.kittens) { kittens ->
            header("Pull to refresh")
            kittens.forEach {
                item(it.name) {
                    left {
                        image(it.url, className = "list-item__thumbnail")
                    }
                }
            }
        }
    }
}
