package com.example

import pl.treksoft.kvision.core.onClick
import pl.treksoft.kvision.html.Align
import pl.treksoft.kvision.html.button
import pl.treksoft.kvision.html.p
import pl.treksoft.kvision.onsenui.FloatDirection
import pl.treksoft.kvision.onsenui.FloatPosition
import pl.treksoft.kvision.onsenui.control.fab
import pl.treksoft.kvision.onsenui.core.Navigator
import pl.treksoft.kvision.onsenui.core.backButton
import pl.treksoft.kvision.onsenui.core.page
import pl.treksoft.kvision.onsenui.dialog.*
import pl.treksoft.kvision.onsenui.form.OnsButtonType
import pl.treksoft.kvision.onsenui.form.onsButton
import pl.treksoft.kvision.onsenui.list.DividerType
import pl.treksoft.kvision.onsenui.list.item
import pl.treksoft.kvision.onsenui.list.onsList
import pl.treksoft.kvision.onsenui.list.onsListTitle
import pl.treksoft.kvision.onsenui.toolbar.ToolbarButton
import pl.treksoft.kvision.onsenui.toolbar.toolbar
import pl.treksoft.kvision.onsenui.toolbar.toolbarButton
import pl.treksoft.kvision.onsenui.visual.icon
import pl.treksoft.kvision.utils.obj
import kotlin.browser.window

fun Navigator.dialogsPage(app: App) {
    lateinit var infoButton: ToolbarButton
    lateinit var popover: Popover
    page("dialogs", className = "dialogs") {
        toolbar("Dialogs") {
            left {
                backButton("Home")
            }
            right {
                infoButton = toolbarButton(
                    if (app.isMD) null else "More",
                    icon = if (app.isMD) "md-more-vert" else null
                ).onClick {
                    popover.showOnsPopover(this)
                }
            }
        }
        if (app.isMD) {
            fab("md-face", FloatPosition.BOTTOM_RIGHT)
        }
        onsListTitle("Notifications")
        onsList(inset = true) {
            item("Alert", tappable = true, divider = DividerType.LONG).onClick {
                showAlert("Hello, world!")
            }
            item("Simple Confirmation", tappable = true, divider = DividerType.LONG).onClick {
                showConfirm("Are you ready?")
            }
            item("Prompt", tappable = true, divider = DividerType.LONG).onClick {
                showPrompt("What is your name?")
            }
            item("Toast", tappable = true, divider = DividerType.LONG).onClick {
                showToast("Hi there!", "Dismiss", timeout = 1500)
            }
            item("Action/Bottom Sheet", tappable = true, divider = DividerType.LONG).onClick {
                showActionSheet(buttons = listOf("Label 1", "Label 2", obj {
                    this.label = "Label 3"
                    this.modifier = "destructive"
                }, "Cancel"), cancelable = true)
            }
        }
        val dialog = dialog(cancelable = true, className = "lorem-dialog") {
            page {
                toolbar("Simple Dialog")
                p("Lorem ipsum dolor", align = Align.CENTER)
                p(align = Align.CENTER) {
                    onsButton("Close", buttonType = OnsButtonType.LIGHT).onClick {
                        this@dialog.hideDialog()
                    }
                }
            }
        }
        val alertDialog = alertDialog("Hey!!", cancelable = true, rowfooter = !app.isMD) {
            +"Lorem ipsum "
            icon("fa-comment-dots", className = "far")
            alertDialogButton("Wat").onClick { this@alertDialog.hideDialog() }
            alertDialogButton("Hmm").onClick { this@alertDialog.hideDialog() }
            alertDialogButton("Sure").onClick { this@alertDialog.hideDialog() }
        }
        val toast = toast(animation = ToastAnimation.FALL) {
            +"Supercalifragilisticexpialidocious"
            button("No way").onClick {
                this@toast.hideToast()
            }
        }
        val modal = modal {
            p(align = Align.CENTER, rich = true) {
                +"Loading "
                icon("fa-spinner", spin = true)
                +"<br><br>"
                +"Click or wait"
            }
            onClick { this@modal.hideModal() }
        }
        var timeoutId: Int? = null
        popover = popover(FloatDirection.DOWN, cancelable = true) {
            coverTarget = true
            onsList {
                listOf("Call history", "Import/export", "New contact", "Settings").forEach {
                    item(it, tappable = true, divider = if (app.isMD) DividerType.NONE else DividerType.LONG).onClick {
                        this@popover.hideOnsPopover()
                    }
                }
            }
        }
        val actionSheet = actionSheet(cancelable = true) {
            actionSheetButton("Action 1", icon = "md-square-o").onClick { this@actionSheet.hideActionSheet() }
            actionSheetButton("Action 2", icon = "md-square-o").onClick { this@actionSheet.hideActionSheet() }
            actionSheetButton("Action 3", icon = "md-square-o") {
                modifier = "destructive"
                onClick { this@actionSheet.hideActionSheet() }
            }
            actionSheetButton("Cancel", icon = "md-square-o").onClick { this@actionSheet.hideActionSheet() }
        }
        onsListTitle("Components")
        onsList(inset = true) {
            item("Simple Dialog", tappable = true, divider = DividerType.LONG).onClick {
                dialog.showDialog()
            }
            item("Alert Dialog", tappable = true, divider = DividerType.LONG).onClick {
                alertDialog.showDialog()
            }
            item("Toast (top)", tappable = true, divider = DividerType.LONG).onClick {
                toast.showToast()
            }
            item("Modal", tappable = true, divider = DividerType.LONG).onClick {
                modal.showModal()
                timeoutId?.let { window.clearTimeout(it) }
                timeoutId = window.setTimeout({ modal.hideModal() }, 2000)
            }
            item("Popover - MD Menu", tappable = true, divider = DividerType.LONG).onClick {
                popover.showOnsPopover(infoButton)
            }
            item("Action/Bottom Sheet", tappable = true, divider = DividerType.LONG).onClick {
                actionSheet.showActionSheet()
            }
        }
    }
}
