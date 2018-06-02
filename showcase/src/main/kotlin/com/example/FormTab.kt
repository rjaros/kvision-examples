package com.example

import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.serialization.Serializable
import pl.treksoft.kvision.form.FormPanel.Companion.formPanel
import pl.treksoft.kvision.form.check.CheckBox
import pl.treksoft.kvision.form.check.Radio
import pl.treksoft.kvision.form.check.RadioGroup
import pl.treksoft.kvision.form.select.AjaxOptions
import pl.treksoft.kvision.form.select.Select
import pl.treksoft.kvision.form.spinner.Spinner
import pl.treksoft.kvision.form.text.Password
import pl.treksoft.kvision.form.text.RichText
import pl.treksoft.kvision.form.text.Text
import pl.treksoft.kvision.form.text.TextArea
import pl.treksoft.kvision.form.time.DateTime
import pl.treksoft.kvision.form.upload.Upload
import pl.treksoft.kvision.html.Button.Companion.button
import pl.treksoft.kvision.html.ButtonStyle
import pl.treksoft.kvision.modal.Alert
import pl.treksoft.kvision.modal.Confirm
import pl.treksoft.kvision.panel.FlexAlignItems
import pl.treksoft.kvision.panel.FlexWrap
import pl.treksoft.kvision.panel.HPanel
import pl.treksoft.kvision.panel.SimplePanel
import pl.treksoft.kvision.progress.ProgressBar
import pl.treksoft.kvision.types.KDate
import pl.treksoft.kvision.types.KFile
import pl.treksoft.kvision.utils.obj
import pl.treksoft.kvision.utils.px

@Serializable
data class Form(
    val text: String? = null,
    val password: String? = null,
    val password2: String? = null,
    val textarea: String? = null,
    val richtext: String? = null,
    val date: KDate? = null,
    val time: KDate? = null,
    val checkbox: Boolean = false,
    val radio: Boolean = false,
    val select: String? = null,
    val ajaxselect: String? = null,
    val spinner: Double? = null,
    val radiogroup: String? = null,
    val upload: List<KFile>? = null
)

class FormTab : SimplePanel() {
    init {
        this.marginTop = 10.px
        val formPanel = formPanel<Form> {
            add(
                Form::text,
                Text(label = "Required text field with regexp [0-9] validator").apply {
                    placeholder = "Enter your age"
                },
                required = true,
                validatorMessage = { "Only numbers are allowed" }) {
                it.getValue()?.matches("^[0-9]+$")
            }
            add(Form::password, Password(label = "Password field with minimum length validator"),
                validatorMessage = { "Password too short" }) {
                (it.getValue()?.length ?: 0) >= 8
            }
            add(Form::password2, Password(label = "Password confirmation"),
                validatorMessage = { "Password too short" }) {
                (it.getValue()?.length ?: 0) >= 8
            }
            add(Form::textarea, TextArea(label = "Text area field"))
            add(
                Form::richtext,
                RichText(label = "Rich text field with a placeholder").apply { placeholder = "Add some info" })
            add(
                Form::date,
                DateTime(format = "YYYY-MM-DD", label = "Date field with a placeholder").apply {
                    placeholder = "Enter date"
                })
            add(
                Form::time,
                DateTime(format = "HH:mm", label = "Time field")
            )
            add(Form::checkbox, CheckBox(label = "Required checkbox")) { it.getValue() }
            add(Form::radio, Radio(label = "Radio button"))
            add(
                Form::select, Select(
                    options = listOf("first" to "First option", "second" to "Second option"),
                    label = "Simple select"
                )
            )

            add(Form::ajaxselect, Select(label = "Select with remote data source").apply {
                emptyOption = true
                ajaxOptions = AjaxOptions("https://api.github.com/search/repositories", preprocessData = {
                    it.items.map { item ->
                        obj {
                            this.value = item.id
                            this.text = item.name
                            this.data = obj {
                                this.subtext = item.owner.login
                            }
                        }
                    }
                }, data = obj {
                    q = "{{{q}}}"
                }, minLength = 3, requestDelay = 1000)
            })
            add(Form::spinner, Spinner(label = "Spinner field 10 - 20", min = 10, max = 20))
            add(
                Form::radiogroup, RadioGroup(
                    listOf("option1" to "First option", "option2" to "Second option"),
                    inline = true, label = "Radio button group"
                )
            )
            add(Form::upload, Upload("/", multiple = true, label = "Upload files (images only)").apply {
                explorerTheme = true
                dropZoneEnabled = false
                allowedFileTypes = setOf("image")
            })
            validator = {
                val result = it[Form::password] == it[Form::password2]
                if (!result) {
                    it.getControl(Form::password)?.validatorError = "Passwords are not the same"
                    it.getControl(Form::password2)?.validatorError = "Passwords are not the same"
                }
                result
            }
            validatorMessage = { "The passwords are not the same." }
        }
        formPanel.add(HPanel(spacing = 10, alignItems = FlexAlignItems.CENTER, wrap = FlexWrap.WRAP) {
            val p = ProgressBar(0, striped = true) {
                marginBottom = 0.px
                width = 300.px
            }
            button("Show data", "fa-info", ButtonStyle.SUCCESS).onClick {
                console.log(formPanel.getDataJson())
                Alert.show("Form data in plain JSON", JSON.stringify(formPanel.getDataJson(), space = 1))
            }
            button("Clear data", "fa-times", ButtonStyle.DANGER).onClick {
                Confirm.show("Are you sure?", "Do you want to clear your data?") {
                    formPanel.clearData()
                    p.progress = 0
                }
            }
            button("Validate", "fa-check", ButtonStyle.INFO).onClick {
                launch {
                    p.progress = 100
                    delay(500)
                    formPanel.validate()
                }
            }
            add(p)
        })
    }
}