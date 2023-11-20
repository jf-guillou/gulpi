package fr.klso.gulpi.utilities

import fr.klso.gulpi.utilities.exceptions.ApiAppTokenMissingException
import fr.klso.gulpi.utilities.exceptions.ApiHttpException
import fr.klso.gulpi.utilities.exceptions.ApiSessionTokenInvalidException
import fr.klso.gulpi.utilities.exceptions.ApiUnexpectedResponseException
import fr.klso.gulpi.utilities.exceptions.ApiWrongAppTokenException
import retrofit2.HttpException

class ApiExceptionParser {
    companion object {
        fun run(e: HttpException): ApiHttpException {
            val code = e.code()
            val body = e.response()?.errorBody() ?: return ApiUnexpectedResponseException(e)

            when (code) {
                400 -> {
                    if (body.string().contains(ApiWrongAppTokenException.apiMsg)) {
                        throw ApiWrongAppTokenException(e)
                    }
                    if (body.string().contains(ApiAppTokenMissingException.apiMsg)) {
                        throw ApiAppTokenMissingException(e)
                    }
                }

                401 -> {
                    if (body.string().contains(ApiSessionTokenInvalidException.apiMsg)) {
                        throw ApiSessionTokenInvalidException(e)
                    }
                }
            }

            return ApiUnexpectedResponseException(e)
        }
    }
}