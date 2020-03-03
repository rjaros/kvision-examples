package com.example

import pl.treksoft.kvision.core.Background
import pl.treksoft.kvision.core.Border
import pl.treksoft.kvision.core.BorderStyle
import pl.treksoft.kvision.core.Col
import pl.treksoft.kvision.core.Color
import pl.treksoft.kvision.core.Style
import pl.treksoft.kvision.core.style
import pl.treksoft.kvision.core.TextAlign
import pl.treksoft.kvision.html.Div
import pl.treksoft.kvision.html.image
import pl.treksoft.kvision.panel.DockPanel
import pl.treksoft.kvision.panel.Side
import pl.treksoft.kvision.utils.perc
import pl.treksoft.kvision.utils.px

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
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${pokemon.url.substring(
                34,
                pokemon.url.length - 1
            )}.png",
            centered = true
        )
        add(Div(pokemon.name.capitalize(), classes = setOf("caption")), Side.DOWN)
    }
}
