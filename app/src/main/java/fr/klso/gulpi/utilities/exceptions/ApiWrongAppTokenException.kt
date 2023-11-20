package fr.klso.gulpi.utilities.exceptions

import retrofit2.HttpException

class ApiWrongAppTokenException(e: HttpException) : ApiHttpException(e) {
    companion object {
        const val apiMsg: String = "ERROR_WRONG_APP_TOKEN_PARAMETER"
    }
}