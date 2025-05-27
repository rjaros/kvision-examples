package com.example

import dev.kilua.rpc.SecurityException
import io.kvision.html.Button
import io.kvision.html.ButtonStyle
import io.kvision.i18n.I18n.tr
import io.kvision.modal.Dialog
import io.kvision.remote.SecurityMgr
import io.kvision.rest.HttpMethod
import io.kvision.rest.ResponseBodyType
import io.kvision.rest.RestClient
import io.kvision.rest.requestDynamic
import io.kvision.utils.obj
import kotlinx.browser.document
import kotlinx.coroutines.asDeferred
import kotlinx.serialization.Serializable

@Serializable
actual data class Profile(
    val name: String? = null,
    val username: String? = null
)

@Serializable
data class Credentials(val username: String? = null, val password: String? = null)

/**
 * Form login dispatcher.
 */
class LoginService(val loginEndpoint: String) {
    val loginAgent = RestClient()

    /**
     * Login with a form.
     * @param credentials username and password credentials
     */
    suspend fun login(credentials: Credentials?): Boolean =
        if (credentials?.username != null) {
            loginAgent.requestDynamic(loginEndpoint) {
                data = obj {
                    this.username = credentials.username
                    this.password = credentials.password
                }
                method = HttpMethod.POST
                contentType = "application/x-www-form-urlencoded"
                responseBodyType = ResponseBodyType.READABLE_STREAM
            }.then { _: dynamic -> true }.asDeferred().await()
        } else {
            throw SecurityException("Credentials cannot be empty")
        }
}

class LoginWindow : Dialog<Credentials>(closeButton = false, escape = false, animation = false) {

    private val loginButton: Button

    init {
        loginButton = Button(tr("Login"), "fas fa-check", ButtonStyle.PRIMARY) {
            onClick {
                this@LoginWindow.processCredentials()
            }
        }
        addButton(loginButton)
    }

    private fun processCredentials() {
        document.location?.href = "/oauth2/authorization/google"
    }

}

object Security : SecurityMgr() {

    private val loginService = LoginService("/oauth2/authorization/google")
    private val loginWindow = LoginWindow()

    override suspend fun login(): Boolean {
        return loginService.login(loginWindow.getResult())
    }

    override suspend fun afterLogin() {
        Model.readProfile()
    }
}
