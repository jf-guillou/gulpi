package fr.klso.gulpi.utilities.exceptions

import retrofit2.HttpException

class ApiUnexpectedResponseException(e: HttpException) : ApiHttpException(e) {
}