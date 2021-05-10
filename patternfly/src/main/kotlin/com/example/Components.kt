package com.example

import io.kvision.core.Container
import io.kvision.html.*

fun Container.menuItem(
    label: String,
    checked: Boolean,
    className: String = "pf-c-options-menu__menu-item",
    handler: () -> Unit
) {
    li {
        role = "menuitem"
        button(label, className = className) {
            if (checked) {
                span(className = "$className-icon") {
                    i(className = "fas fa-check")
                }
            }
            onClick {
                handler()
            }
        }
    }
}

fun Container.natImage(user: User) {
    image(
        "https://lipis.github.io/flag-icon-css/flags/4x3/${user.nat.lowercase()}.svg",
        className = "sc-user-nat"
    ) {
        title = user.nat
    }
}

fun Container.photo(user: User) {
    div(className = "sc-user-photo-75") {
        image(src = user.picture.medium)
    }
}

fun Container.address(user: User) {
    address {
        +"${user.location.street.name} ${user.location.street.number}"
        br()
        +"${user.location.postcode} ${user.location.city}"
        br()
        +"${user.location.state} ${user.nat}"
        link(
            "",
            "https://www.google.com/maps/search/?api=1&query=${user.location.coordinates.latitude},${user.location.coordinates.longitude}",
            icon = "fas fa-map-marked-alt",
            target = "map",
            className = "pf-u-ml-sm"
        )
    }
}

fun Container.userInfo(user: User) {
    photo(user)
    address(user)
    ul {
        li {
            link(user.email, "mailto:${user.email}", icon = "fas fa-envelope pf-u-mr-sm")
        }
        li {
            link(user.phone, "tel:${user.phone}", icon = "fas fa-phone pf-u-mr-sm")
        }
        li {
            link(user.cell, "tel:${user.cell}", icon = "fas fa-mobile-alt pf-u-mr-sm")
        }
    }
}
