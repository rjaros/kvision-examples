package com.example

import kotlinx.browser.document
import io.kvision.core.JustifyContent
import io.kvision.data.dataContainer
import io.kvision.html.Button
import io.kvision.html.ButtonStyle
import io.kvision.html.Span
import io.kvision.html.button
import io.kvision.i18n.I18n.tr
import io.kvision.panel.HPanel

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
