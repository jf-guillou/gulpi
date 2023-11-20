package fr.klso.gulpi.utilities.exceptions

import retrofit2.HttpException

class ApiSessionTokenInvalidException(e: HttpException) : ApiHttpException(e) {
    companion object {
        const val apiMsg: String = "ERROR_SESSION_TOKEN_INVALID"
    }
}