package com.example

import pl.treksoft.kvision.data.DataContainer.Companion.dataContainer
import pl.treksoft.kvision.html.Button
import pl.treksoft.kvision.html.Button.Companion.button
import pl.treksoft.kvision.html.ButtonStyle
import pl.treksoft.kvision.html.Label
import pl.treksoft.kvision.panel.FlexJustify
import pl.treksoft.kvision.panel.HPanel
import kotlin.browser.document

object MainPanel : HPanel(justify = FlexJustify.SPACEBETWEEN) {
    init {
        button("Add new address", "fa-plus", style = ButtonStyle.PRIMARY).onClick {
            EditPanel.add()
        }
        dataContainer(Model.profile, { profile, _, _ ->
            if (profile.id != null) {
                Button("Logout: ${profile.displayName}", "fa-sign-out", style = ButtonStyle.WARNING).onClick {
                    document.location?.href = "/logout"
                }
            } else {
                Label("")
            }
        })
    }
}
