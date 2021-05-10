package com.example

import io.kvision.core.Container
import io.kvision.core.PosFloat
import io.kvision.core.onClickLaunch
import io.kvision.html.TAG
import io.kvision.html.div
import io.kvision.html.i
import io.kvision.html.image
import io.kvision.html.link
import io.kvision.html.tag
import io.kvision.utils.px

fun Container.menuItem(
    label: String,
    checked: Boolean,
    className: String = "check",
    handler: suspend () -> Unit
) {
    div(label, className = "item") {
        if (checked) {
            i(className = "$className icon") {
                float = PosFloat.RIGHT
                marginRight = 0.px
            }
        }
        onClickLaunch {
            handler()
        }
    }
}

fun Container.natImage(user: User) {
    image(
        "https://lipis.github.io/flag-icon-css/flags/4x3/${user.nat.lowercase()}.svg",
        className = "ui mini middle aligned image"
    ) {
        title = user.nat
    }
}

fun Container.photo(user: User) {
    image(src = user.picture.medium, className = "ui image tiny circular")
}

fun Container.address(user: User) {
    tag(TAG.ADDRESS) {
        +"${user.location.street.name} ${user.location.street.number}"
        tag(TAG.BR)
        +"${user.location.postcode} ${user.location.city}"
        tag(TAG.BR)
        +"${user.location.state} ${user.nat}"
        link(
            "",
            "https://www.google.com/maps/search/?api=1&query=${user.location.coordinates.latitude},${user.location.coordinates.longitude}",
            icon = "ui map marked icon",
            target = "map",
            className = "maplink"
        )
    }
}
