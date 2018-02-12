package com.example

import pl.treksoft.kvision.dropdown.DD
import pl.treksoft.kvision.dropdown.DropDown.Companion.dropDown
import pl.treksoft.kvision.html.BUTTONSTYLE
import pl.treksoft.kvision.html.Button.Companion.button
import pl.treksoft.kvision.html.Image
import pl.treksoft.kvision.panel.HPanel.Companion.hPanel
import pl.treksoft.kvision.panel.SimplePanel
import pl.treksoft.kvision.panel.VPanel.Companion.vPanel
import pl.treksoft.kvision.utils.px

class DropDownTab : SimplePanel() {
    init {
        this.marginTop = 10.px()
        this.minHeight = 400.px()
        vPanel(spacing = 30) {
            dropDown(
                "Dropdown with navigation menu", listOf(
                    "Basic formatting" to "#!/basic",
                    "Forms" to "#!/forms",
                    "Buttons" to "#!/buttons",
                    "Dropdowns" to "#!/dropdowns",
                    "Containers" to "#!/containers"
                ), "fa-arrow-right", style = BUTTONSTYLE.SUCCESS
            ).apply {
                width = 250.px()
            }
            dropDown("Dropdown with custom list", icon = "fa-picture-o", style = BUTTONSTYLE.WARNING).apply {
                width = 250.px()
                add(Image(require("./img/cat.jpg")).apply { margin = 10.px(); title = "Cat" })
                add(Image(require("./img/dog.jpg")).apply { margin = 10.px(); title = "Dog" })
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
                    ), "fa-asterisk", style = BUTTONSTYLE.PRIMARY
                ).apply {
                    dropup = true
                    width = 250.px()
                }
                button("Toggle dropdown", style = BUTTONSTYLE.INFO).onClick { e ->
                    fdd.toggle()
                    e.stopPropagation()
                }
            }
        }
    }
}
