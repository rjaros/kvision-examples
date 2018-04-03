package com.example

import pl.treksoft.kvision.dropdown.ContextMenu
import pl.treksoft.kvision.dropdown.DD
import pl.treksoft.kvision.dropdown.DropDown.Companion.dropDown
import pl.treksoft.kvision.dropdown.Header.Companion.header
import pl.treksoft.kvision.dropdown.Separator.Companion.separator
import pl.treksoft.kvision.form.check.CheckBox.Companion.checkBox
import pl.treksoft.kvision.form.text.Text.Companion.text
import pl.treksoft.kvision.html.Button.Companion.button
import pl.treksoft.kvision.html.ButtonStyle
import pl.treksoft.kvision.html.Image
import pl.treksoft.kvision.html.Label.Companion.label
import pl.treksoft.kvision.html.Link.Companion.link
import pl.treksoft.kvision.html.TAG
import pl.treksoft.kvision.html.Tag.Companion.tag
import pl.treksoft.kvision.navbar.Nav.Companion.nav
import pl.treksoft.kvision.navbar.NavForm.Companion.navForm
import pl.treksoft.kvision.navbar.Navbar.Companion.navbar
import pl.treksoft.kvision.panel.HPanel.Companion.hPanel
import pl.treksoft.kvision.panel.SimplePanel
import pl.treksoft.kvision.panel.VPanel.Companion.vPanel
import pl.treksoft.kvision.require
import pl.treksoft.kvision.utils.px

class DropDownTab : SimplePanel() {
    init {
        this.marginTop = 10.px
        this.minHeight = 400.px
        vPanel(spacing = 30) {
            navbar("NavBar") {
                nav {
                    tag(TAG.LI) {
                        link("File", icon = "fa-file")
                    }
                    tag(TAG.LI) {
                        link("Edit", icon = "fa-bars")
                    }
                    dropDown(
                        "Favourites",
                        listOf("Basic formatting" to "#!/basic", "Forms" to "#!/forms"),
                        icon = "fa-star",
                        forNavbar = true
                    )
                }
                navForm {
                    text(label = "Search:")
                    checkBox()
                }
                nav(rightAlign = true) {
                    tag(TAG.LI) {
                        link("System", icon = "fa-windows")
                    }
                }
            }
            dropDown(
                "Dropdown with navigation menu", listOf(
                    "Basic formatting" to "#!/basic",
                    "Forms" to "#!/forms",
                    "Buttons" to "#!/buttons",
                    "Dropdowns & Menus" to "#!/dropdowns",
                    "Containers" to "#!/containers"
                ), "fa-arrow-right", style = ButtonStyle.SUCCESS
            ).apply {
                minWidth = 250.px
            }
            dropDown("Dropdown with custom list", icon = "fa-picture-o", style = ButtonStyle.WARNING).apply {
                minWidth = 250.px
                add(Image(require("./img/cat.jpg")).apply { margin = 10.px; title = "Cat" })
                add(Image(require("./img/dog.jpg")).apply { margin = 10.px; title = "Dog" })
            }
            hPanel(spacing = 5) {
                val fdd = dropDown(
                    "Dropdown with special options", listOf(
                        "Header" to DD.HEADER.option,
                        "Basic formatting" to "#!/basic",
                        "Forms" to "#!/forms",
                        "Buttons" to "#!/buttons",
                        "Separator" to DD.SEPARATOR.option,
                        "Dropdowns (disabled)" to DD.DISABLED.option,
                        "Separator" to DD.SEPARATOR.option,
                        "Containers" to "#!/containers"
                    ), "fa-asterisk", style = ButtonStyle.PRIMARY
                ).apply {
                    dropup = true
                    minWidth = 250.px
                }
                button("Toggle dropdown", style = ButtonStyle.INFO).onClick { e ->
                    fdd.toggle()
                    e.stopPropagation()
                }
            }
            label("Open the context menu with right mouse click.")
            val contextMenu = ContextMenu {
                header("Menu header")
                link("Basic formatting", "#!/basic")
                link("Forms", "#!/forms")
                link("Buttons", "#!/buttons")
                link("Dropdown & Menus", "#!/dropdowns")
                separator()
                dropDown("Dropdown", forNavbar = true) {
                    link("Containers", "#!/containers")
                    link("Layout", "#!/layout")
                }
            }
            setContextMenu(contextMenu)
        }
    }
}
