package com.example

import io.kvision.core.Background
import io.kvision.core.Border
import io.kvision.core.BorderStyle
import io.kvision.core.Col
import io.kvision.core.Color
import io.kvision.core.Style
import io.kvision.core.TextAlign
import io.kvision.core.style
import io.kvision.html.div
import io.kvision.html.image
import io.kvision.panel.DockPanel
import io.kvision.utils.perc
import io.kvision.utils.px

val pokeBoxStyle = Style {
    border = Border(1.px, BorderStyle.SOLID, Color.name(Col.GRAY))
    width = 200.px
    height = 200.px
    margin = 10.px

    style("img") {
        marginTop = 30.px
    }

    style("div.caption") {
        textAlign = TextAlign.CENTER
        background = Background(Color.name(Col.SILVER))
        width = 100.perc
    }
}

class PokeBox(pokemon: Pokemon) : DockPanel() {
    init {
        addCssStyle(pokeBoxStyle)
        image(
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${
                pokemon.url.substring(
                    34,
                    pokemon.url.length - 1
                )
            }.png",
            centered = true
        )
        down {
            div(
                pokemon.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                classes = setOf("caption")
            )
        }
    }
}
