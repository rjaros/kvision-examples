package com.example

import io.kvision.dropdown.DD
import io.kvision.dropdown.Direction
import io.kvision.dropdown.cmLink
import io.kvision.dropdown.contextMenu
import io.kvision.dropdown.ddLink
import io.kvision.dropdown.dropDown
import io.kvision.dropdown.header
import io.kvision.dropdown.separator
import io.kvision.form.check.checkBox
import io.kvision.form.text.text
import io.kvision.html.ButtonStyle
import io.kvision.html.button
import io.kvision.html.image
import io.kvision.html.span
import io.kvision.i18n.I18n.tr
import io.kvision.navbar.nav
import io.kvision.navbar.navForm
import io.kvision.navbar.navLink
import io.kvision.navbar.navbar
import io.kvision.panel.SimplePanel
import io.kvision.panel.hPanel
import io.kvision.panel.vPanel
import io.kvision.require
import io.kvision.utils.px

class DropDownTab : SimplePanel() {
    init {
        this.marginTop = 10.px
        this.minHeight = 600.px
        vPanel(spacing = 30) {
            navbar("NavBar", collapseOnClick = true) {
                nav {
                    navLink(tr("File"), icon = "fas fa-file")
                    navLink(tr("Edit"), icon = "fas fa-bars")
                    dropDown(
                        tr("Favourites"),
                        listOf(tr("HTML") to "#/basic", tr("Forms") to "#/forms"),
                        icon = "fas fa-star",
                        forNavbar = true
                    )
                }
                navForm {
                    text(label = tr("Search:"))
                    checkBox(label = tr("Search")) {
                        inline = true
                    }
                }
                nav(rightAlign = true) {
                    navLink(tr("System"), icon = "fab fa-windows")
                }
            }
            dropDown(
                tr("Dropdown with navigation menu"), listOf(
                    tr("HTML") to "#/basic",
                    tr("Forms") to "#/forms",
                    tr("Buttons") to "#/buttons",
                    tr("Dropdowns") to "#/dropdowns",
                    tr("Containers") to "#/containers"
                ), "fas fa-arrow-right", style = ButtonStyle.SUCCESS
            ).apply {
                minWidth = 250.px
            }
            dropDown(tr("Dropdown with custom list"), icon = "far fa-image", style = ButtonStyle.WARNING) {
                minWidth = 250.px
                image(require("img/cat.jpg")) { height = 170.px; margin = 10.px; title = "Cat" }
                separator()
                image(require("img/dog.jpg")) { height = 170.px; margin = 10.px; title = "Dog" }
            }
            hPanel(spacing = 5) {
                val fdd = dropDown(
                    tr("Dropdown with special options"), listOf(
                        tr("Header") to DD.HEADER.option,
                        tr("HTML") to "#/basic",
                        tr("Forms") to "#/forms",
                        tr("Buttons") to "#/buttons",
                        tr("Separator") to DD.SEPARATOR.option,
                        tr("Dropdowns (disabled)") to DD.DISABLED.option,
                        tr("Separator") to DD.SEPARATOR.option,
                        tr("Containers") to "#/containers"
                    ), "fas fa-asterisk", style = ButtonStyle.PRIMARY
                ) {
                    direction = Direction.DROPUP
                    minWidth = 250.px
                }
                button(tr("Toggle dropdown"), style = ButtonStyle.INFO).onClick { e ->
                    fdd.toggle()
                    e.stopPropagation()
                }
            }
            span(tr("Open the context menu with right mouse click."))
            contextMenu {
                header(tr("Menu header"))
                cmLink(tr("HTML"), "#/basic")
                cmLink(tr("Forms"), "#/forms")
                cmLink(tr("Buttons"), "#/buttons")
                cmLink(tr("Dropdowns"), "#/dropdowns")
                separator()
                dropDown(tr("Dropdown"), forDropDown = true) {
                    ddLink(tr("Containers"), "#/containers")
                    ddLink(tr("Layouts"), "#/layouts")
                }
            }
        }
    }
}
