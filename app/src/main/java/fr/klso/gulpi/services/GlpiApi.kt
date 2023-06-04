package fr.klso.gulpi.services

import fr.klso.gulpi.models.ApiSession
import fr.klso.gulpi.models.Item
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface GlpiApi {
    @GET("apirest.php/initSession")
    suspend fun initSession(
        @Header("App-Token") appToken: String,
        @Header("Authorization") authorization: String
    ): ApiSession

    @GET("apirest.php/{itemType}/{itemId}")
    suspend fun getItem(
        @Header("App-Token") appToken: String,
        @Header("Session-Token") sessionToken: String,
        @Path("itemType") itemType: String,
        @Path("itemId") itemId: String
    ): Item

    @GET("apirest.php/{itemType}/")
    suspend fun getItems(
        @Header("App-Token") appToken: String,
        @Header("Session-Token") sessionToken: String,
        @Path("itemType") itemType: String
    ): Unit

    @GET("apirest.php/search/{itemType}/")
    suspend fun search(
        @Header("App-Token") appToken: String,
        @Header("Session-Token") sessionToken: String,
        @Path("itemType") itemType: String
    ): Unit

    @GET("apirest.php/getGlpiConfig")
    suspend fun config(
        @Header("App-Token") appToken: String,
        @Header("Session-Token") sessionToken: String,
    ): Unit
}