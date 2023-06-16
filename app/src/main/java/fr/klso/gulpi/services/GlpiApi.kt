package fr.klso.gulpi.services

import fr.klso.gulpi.models.ApiSession
import fr.klso.gulpi.models.Computer
import fr.klso.gulpi.models.search.PaginableSearchComputers
import fr.klso.gulpi.models.search.PaginableSearchItems
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface GlpiApi {
    @GET("apirest.php/initSession")
    suspend fun initSession(
        @Header("App-Token") appToken: String,
        @Header("Authorization") authorization: String
    ): ApiSession

    @GET("apirest.php/computer/{computerId}")
    suspend fun getComputer(
        @Header("App-Token") appToken: String,
        @Header("Session-Token") sessionToken: String,
        @Path("computerId") computerId: String
    ): Computer

    @GET("apirest.php/search/computer/")
    suspend fun searchComputers(
        @Header("App-Token") appToken: String,
        @Header("Session-Token") sessionToken: String,
        @QueryMap query: Map<String, String>,
    ): PaginableSearchComputers

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
    ): PaginableSearchItems

    @GET("apirest.php/getGlpiConfig")
    suspend fun config(
        @Header("App-Token") appToken: String,
        @Header("Session-Token") sessionToken: String,
    ): Unit
}