package fr.klso.gulpi.utilities.exceptions

import retrofit2.HttpException

class ApiError(e: HttpException?) : ApiHttpException(e) {
    companion object {
        const val apiMsg: String = "ERROR"
    }
}