package com.example

import pl.treksoft.kvision.dropdown.contextMenu
import pl.treksoft.kvision.dropdown.DD
import pl.treksoft.kvision.dropdown.Direction
import pl.treksoft.kvision.dropdown.cmLink
import pl.treksoft.kvision.dropdown.ddLink
import pl.treksoft.kvision.dropdown.dropDown
import pl.treksoft.kvision.dropdown.header
import pl.treksoft.kvision.dropdown.separator
import pl.treksoft.kvision.form.check.checkBox
import pl.treksoft.kvision.form.text.text
import pl.treksoft.kvision.html.button
import pl.treksoft.kvision.html.ButtonStyle
import pl.treksoft.kvision.html.image
import pl.treksoft.kvision.html.span
import pl.treksoft.kvision.i18n.I18n.tr
import pl.treksoft.kvision.navbar.nav
import pl.treksoft.kvision.navbar.navLink
import pl.treksoft.kvision.navbar.navForm
import pl.treksoft.kvision.navbar.navbar
import pl.treksoft.kvision.panel.hPanel
import pl.treksoft.kvision.panel.SimplePanel
import pl.treksoft.kvision.panel.vPanel
import pl.treksoft.kvision.require
import pl.treksoft.kvision.utils.px

class DropDownTab : SimplePanel() {
    init {
        this.marginTop = 10.px
        this.minHeight = 400.px
        vPanel(spacing = 30) {
            navbar("NavBar") {
                nav {
                    navLink(tr("File"), icon = "fas fa-file")
                    navLink(tr("Edit"), icon = "fas fa-bars")
                    dropDown(
                        tr("Favourites"),
                        listOf(tr("HTML") to "#!/basic", tr("Forms") to "#!/forms"),
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
                    tr("HTML") to "#!/basic",
                    tr("Forms") to "#!/forms",
                    tr("Buttons") to "#!/buttons",
                    tr("Dropdowns") to "#!/dropdowns",
                    tr("Containers") to "#!/containers"
                ), "fas fa-arrow-right", style = ButtonStyle.SUCCESS
            ).apply {
                minWidth = 250.px
            }
            dropDown(tr("Dropdown with custom list"), icon = "far fa-image", style = ButtonStyle.WARNING) {
                minWidth = 250.px
                image(require("img/cat.jpg")) { margin = 10.px; title = "Cat" }
                separator()
                image(require("img/dog.jpg")) { margin = 10.px; title = "Dog" }
            }
            hPanel(spacing = 5) {
                val fdd = dropDown(
                    tr("Dropdown with special options"), listOf(
                        tr("Header") to DD.HEADER.option,
                        tr("HTML") to "#!/basic",
                        tr("Forms") to "#!/forms",
                        tr("Buttons") to "#!/buttons",
                        tr("Separator") to DD.SEPARATOR.option,
                        tr("Dropdowns (disabled)") to DD.DISABLED.option,
                        tr("Separator") to DD.SEPARATOR.option,
                        tr("Containers") to "#!/containers"
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
                cmLink(tr("HTML"), "#!/basic")
                cmLink(tr("Forms"), "#!/forms")
                cmLink(tr("Buttons"), "#!/buttons")
                cmLink(tr("Dropdowns"), "#!/dropdowns")
                separator()
                dropDown(tr("Dropdown"), forDropDown = true) {
                    ddLink(tr("Containers"), "#!/containers")
                    ddLink(tr("Layout"), "#!/layout")
                }
            }
        }
    }
}
