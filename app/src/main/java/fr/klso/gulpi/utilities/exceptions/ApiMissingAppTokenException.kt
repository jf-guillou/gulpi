package fr.klso.gulpi.utilities.exceptions

class ApiMissingAppTokenException : Throwable() {
    companion object {
        val apiMsg: String = "ERROR_APP_TOKEN_PARAMETERS_MISSING"
    }
}