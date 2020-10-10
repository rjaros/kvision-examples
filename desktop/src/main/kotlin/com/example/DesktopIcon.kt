package com.example

import pl.treksoft.kvision.core.AlignItems
import pl.treksoft.kvision.core.WhiteSpace
import pl.treksoft.kvision.html.icon
import pl.treksoft.kvision.html.span
import pl.treksoft.kvision.panel.VPanel
import pl.treksoft.kvision.utils.px

class DesktopIcon(icon: String, content: String) : VPanel(alignItems = AlignItems.CENTER) {
    init {
        width = 64.px
        height = 64.px
        icon(icon) {
            addCssClass("fa-3x")
        }
        span(content) {
            whiteSpace = WhiteSpace.NOWRAP
        }
    }
}
