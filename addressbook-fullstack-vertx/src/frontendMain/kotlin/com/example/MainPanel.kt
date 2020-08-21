package com.example

import kotlinx.browser.document
import pl.treksoft.kvision.core.JustifyContent
import pl.treksoft.kvision.data.dataContainer
import pl.treksoft.kvision.html.Button
import pl.treksoft.kvision.html.ButtonStyle
import pl.treksoft.kvision.html.Span
import pl.treksoft.kvision.html.button
import pl.treksoft.kvision.i18n.I18n.tr
import pl.treksoft.kvision.panel.HPanel

object MainPanel : HPanel(justify = JustifyContent.SPACEBETWEEN) {
    init {
        button(tr("Add new address"), "fas fa-plus", style = ButtonStyle.PRIMARY).onClick {
            EditPanel.add()
        }
        dataContainer(Model.profile, { profile, _, _ ->
            if (profile.name != null) {
                Button("Logout: ${profile.name}", "fas fa-sign-out-alt", style = ButtonStyle.WARNING).onClick {
                    document.location?.href = "/logout"
                }
            } else {
                Span("")
            }
        })
    }
}
