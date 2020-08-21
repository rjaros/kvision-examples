package com.example

import kotlinx.browser.window
import pl.treksoft.kvision.html.div
import pl.treksoft.kvision.html.image
import pl.treksoft.kvision.onsenui.core.Navigator
import pl.treksoft.kvision.onsenui.core.backButton
import pl.treksoft.kvision.onsenui.core.page
import pl.treksoft.kvision.onsenui.core.pullHook
import pl.treksoft.kvision.onsenui.list.header
import pl.treksoft.kvision.onsenui.list.item
import pl.treksoft.kvision.onsenui.list.onsList
import pl.treksoft.kvision.onsenui.toolbar.toolbar
import pl.treksoft.kvision.onsenui.visual.icon
import pl.treksoft.kvision.onsenui.visual.progressCircular
import pl.treksoft.kvision.state.bind
import pl.treksoft.kvision.state.observableListOf
import pl.treksoft.kvision.utils.px
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
