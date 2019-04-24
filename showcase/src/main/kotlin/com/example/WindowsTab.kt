package com.example

import pl.treksoft.kvision.form.check.CheckBox.Companion.checkBox
import pl.treksoft.kvision.form.text.TextInput
import pl.treksoft.kvision.form.text.TextInput.Companion.textInput
import pl.treksoft.kvision.form.text.TextInputType
import pl.treksoft.kvision.html.Button.Companion.button
import pl.treksoft.kvision.html.ButtonStyle
import pl.treksoft.kvision.i18n.I18n.gettext
import pl.treksoft.kvision.i18n.I18n.tr
import pl.treksoft.kvision.panel.SimplePanel
import pl.treksoft.kvision.panel.VPanel.Companion.vPanel
import pl.treksoft.kvision.utils.px
import pl.treksoft.kvision.window.Window
import kotlin.random.Random

class WindowsTab : SimplePanel() {
    init {
        this.marginTop = 10.px
        this.minHeight = 400.px
    }
}
