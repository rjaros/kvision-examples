package com.example

import io.kvision.core.JustifyContent
import io.kvision.html.ButtonStyle
import io.kvision.html.button
import io.kvision.html.div
import io.kvision.i18n.I18n.tr
import io.kvision.panel.HPanel
import io.kvision.state.bind
import kotlinx.browser.document

object MainPanel : HPanel(justify = JustifyContent.SPACEBETWEEN) {
    init {
        button(tr("Add new address"), "fas fa-plus", style = ButtonStyle.PRIMARY).onClick {
            EditPanel.add()
        }
        div().bind(Model.profile) { profile ->
            if (profile.name != null) {
                button("Logout: ${profile.name}", "fas fa-sign-out-alt", style = ButtonStyle.WARNING).onClick {
                    document.location?.href = "/logout"
                }
            }
        }
    }
}
