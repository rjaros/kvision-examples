package com.example

import io.kvision.core.AlignItems
import io.kvision.core.WhiteSpace
import io.kvision.html.icon
import io.kvision.html.span
import io.kvision.panel.VPanel
import io.kvision.utils.px

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
