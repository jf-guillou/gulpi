package fr.klso.gulpi.utilities

import android.util.Log
import fr.klso.gulpi.utilities.exceptions.ApiAppTokenMissingException
import fr.klso.gulpi.utilities.exceptions.ApiError
import fr.klso.gulpi.utilities.exceptions.ApiHttpException
import fr.klso.gulpi.utilities.exceptions.ApiSessionTokenInvalidException
import fr.klso.gulpi.utilities.exceptions.ApiUnexpectedResponseException
import fr.klso.gulpi.utilities.exceptions.ApiWrongAppTokenException
import org.json.JSONArray
import retrofit2.HttpException

private const val TAG = "ApiExceptionParser"

class ApiExceptionParser {
    companion object {
        fun run(e: HttpException): ApiHttpException {
            val code = e.code()
            val body = e.response()?.errorBody()?.string()
                ?: return ApiUnexpectedResponseException(e)
            Log.d(TAG, "Got HTTP $code")

            val err = JSONArray(body)
            val errCode = err.getString(0)
            val errMsg = err.getString(1)
            Log.d(TAG, errCode)
            Log.d(TAG, errMsg)

            when (code) {
                400 -> {
                    if (errCode.equals(ApiWrongAppTokenException.apiMsg)) {
                        return ApiWrongAppTokenException(e)
                    }
                    if (errCode.equals(ApiAppTokenMissingException.apiMsg)) {
                        return ApiAppTokenMissingException(e)
                    }
                    if (errCode.equals(ApiError.apiMsg)) {
                        return ApiError(e)
                    }
                }

                401 -> {
                    if (errCode.equals(ApiSessionTokenInvalidException.apiMsg)) {
                        return ApiSessionTokenInvalidException(e)
                    }
                }
            }

            return ApiUnexpectedResponseException(e)
        }
    }
}