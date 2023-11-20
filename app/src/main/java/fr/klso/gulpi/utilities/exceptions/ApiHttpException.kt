package fr.klso.gulpi.utilities.exceptions

import retrofit2.HttpException

open class ApiHttpException(val e: HttpException?) : Throwable()