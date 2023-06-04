package fr.klso.gulpi.services

import fr.klso.gulpi.models.ApiSession
import retrofit2.http.GET
import retrofit2.http.Header

interface GlpiApi {
    @GET("apirest.php/initSession")
    suspend fun initSession(
        @Header("Authorization") authorization: String,
        @Header("App-Token") appToken: String
    ): ApiSession
}