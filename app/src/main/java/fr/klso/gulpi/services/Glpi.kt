package fr.klso.gulpi.services

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import fr.klso.gulpi.models.ApiSession
import fr.klso.gulpi.models.Computer
import fr.klso.gulpi.models.search.PaginableSearchItems
import fr.klso.gulpi.models.search.SearchComputer
import fr.klso.gulpi.models.search.SearchCriteria
import fr.klso.gulpi.utilities.exceptions.ApiAuthFailedException
import fr.klso.gulpi.utilities.exceptions.ApiMissingAppTokenException
import fr.klso.gulpi.utilities.exceptions.ApiNotInitializedException
import fr.klso.gulpi.utilities.toQueryMap
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit


object Glpi {
    private var api: GlpiApi? = null
    var appToken: String = ""
    var sessionToken: String = ""
    val usable: Boolean
        get() = api != null

    fun init(url: String) {
        api = createApi(url)
    }

    private fun assertUsable() {
        if (api == null) {
            throw ApiNotInitializedException()
        }
        if (appToken.isEmpty()) {
            throw ApiMissingAppTokenException()
        }
        if (sessionToken.isEmpty()) {
            throw ApiAuthFailedException()
        }
    }

    suspend fun initSession(userToken: String): ApiSession {
        if (api == null) {
            throw ApiNotInitializedException()
        }
        if (appToken.isEmpty()) {
            throw ApiMissingAppTokenException()
        }

        return api!!.initSession(
            appToken,
            "user_token $userToken"
        )
    }

    suspend fun getComputer(id: String): Computer? {
        assertUsable()

        try {
            return api!!.getComputer(
                appToken,
                sessionToken,
                id
            )
        } catch (e: HttpException) {
            if (e.code() == 404) {
                return null
            }
            throw e
        }
    }

    suspend fun searchComputers(
        criteria: List<SearchCriteria>
    ): PaginableSearchItems {
        assertUsable()

        val query = criteria.toQueryMap()
        for ((k, v) in SearchComputer.columns.withIndex()) {
            query["forcedisplay[$k]"] = v.toString()
        }

        try {
            return api!!.searchComputers(
                appToken,
                sessionToken,
                query
            )
        } catch (e: HttpException) {
//            if (e.code() == 404) {
//                return PaginableSearchResults()
//            }
            throw e
        }
    }

    private fun createApi(url: String): GlpiApi {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        val contentType = "application/json".toMediaType()
        val json = Json { ignoreUnknownKeys = true }
        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(url)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()

        return retrofit.create(GlpiApi::class.java)
    }
}
