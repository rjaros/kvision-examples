package com.example

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pl.treksoft.kvision.core.onEvent
import pl.treksoft.kvision.form.FormPanel
import pl.treksoft.kvision.form.formPanel
import pl.treksoft.kvision.form.text.Password
import pl.treksoft.kvision.form.text.Text
import pl.treksoft.kvision.html.Button
import pl.treksoft.kvision.html.ButtonStyle
import pl.treksoft.kvision.i18n.I18n.tr
import pl.treksoft.kvision.modal.Alert
import pl.treksoft.kvision.modal.Dialog
import pl.treksoft.kvision.remote.Credentials
import pl.treksoft.kvision.remote.LoginService
import pl.treksoft.kvision.remote.SecurityMgr
import pl.treksoft.kvision.utils.ENTER_KEY

class LoginWindow : Dialog<Credentials>(closeButton = false, escape = false, animation = false) {

    private val loginPanel: FormPanel<Credentials>
    private val loginButton: Button
    private val userButton: Button
    private val registerPanel: FormPanel<Profile>
    private val registerButton: Button
    private val cancelButton: Button

    init {
        loginPanel = formPanel {
            add(Credentials::username, Text(label = "${tr("Login")}:"), required = true)
            add(Credentials::password, Password(label = "${tr("Password")}:"), required = true)
            onEvent {
                keydown = {
                    if (it.keyCode == ENTER_KEY) {
                        processCredentials()
                    }
                }
            }
        }
        registerPanel = formPanel {
            add(Profile::name, Text(label = "${tr("Your name")}:"), required = true)
            add(Profile::username, Text(label = "Login:"), required = true)
            add(
                Profile::password, Password(label = "${tr("Password")}:"), required = true,
                validatorMessage = { "Password too short" }) {
                (it.getValue()?.length ?: 0) >= 8
            }
            add(Profile::password2, Password(label = "${tr("Confirm password")}:"), required = true,
                validatorMessage = { tr("Password too short") }) {
                (it.getValue()?.length ?: 0) >= 8
            }
            validator = {
                val result = it[Profile::password] == it[Profile::password2]
                if (!result) {
                    it.getControl(Profile::password)?.validatorError = tr("Passwords are not the same")
                    it.getControl(Profile::password2)?.validatorError = tr("Passwords are not the same")
                }
                result
            }
            validatorMessage = { tr("Passwords are not the same") }

        }
        cancelButton = Button(tr("Cancel"), "fas fa-times").onClick {
            hideRegisterForm()
        }
        registerButton = Button(tr("Register"), "fas fa-check", ButtonStyle.PRIMARY).onClick {
            processRegister()
        }
        loginButton = Button(tr("Login"), "fas fa-check", ButtonStyle.PRIMARY).onClick {
            processCredentials()
        }
        userButton = Button(tr("Register user"), "fas fa-user").onClick {
            showRegisterForm()
        }
        addButton(userButton)
        addButton(loginButton)
        addButton(cancelButton)
        addButton(registerButton)
        hideRegisterForm()
    }

    private fun showRegisterForm() {
        loginPanel.hide()
        registerPanel.show()
        registerPanel.clearData()
        loginButton.hide()
        userButton.hide()
        cancelButton.show()
        registerButton.show()
    }

    private fun hideRegisterForm() {
        loginPanel.show()
        registerPanel.hide()
        loginButton.show()
        userButton.show()
        cancelButton.hide()
        registerButton.hide()
    }

    private fun processCredentials() {
        if (loginPanel.validate()) {
            setResult(loginPanel.getData())
            loginPanel.clearData()
        }
    }

    private fun processRegister() {
        if (registerPanel.validate()) {
            val userData = registerPanel.getData()
            GlobalScope.launch {
                if (Model.registerProfile(userData, userData.password!!)
                ) {
                    Alert.show(text = tr("User registered. You can now log in.")) {
                        hideRegisterForm()
                    }
                } else {
                    Alert.show(text = tr("This login is not available. Please try again."))
                }
            }
        }
    }
}

object Security : SecurityMgr() {

    private val loginService = LoginService("login")
    private val loginWindow = LoginWindow()

    override suspend fun login(): Boolean {
        return loginService.login(loginWindow.getResult())
    }

    override suspend fun afterLogin() {
        Model.readProfile()
    }
}
