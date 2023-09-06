package fr.klso.gulpi.utilities.exceptions

class ApiWrongAppTokenException : Throwable() {
    companion object {
        val apiMsg: String = "ERROR_WRONG_APP_TOKEN_PARAMETER"
    }
}