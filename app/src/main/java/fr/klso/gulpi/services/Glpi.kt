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
import fr.klso.gulpi.utilities.exceptions.ApiUnexpectedResponseException
import fr.klso.gulpi.utilities.toQueryMap
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit

private const val TAG = "API"

object Glpi {
    private var api: GlpiApi? = null
    var appToken: String = ""
    var sessionToken: String = ""
    val hasApi: Boolean
        get() = api != null
    val usable: Boolean
        get() = hasApi && appToken.isNotEmpty() && sessionToken.isNotEmpty()

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

    suspend fun checkEndpoint(): Boolean {
        if (api == null) {
            throw ApiNotInitializedException()
        }

        try {
            api!!.initSession("", "")
        } catch (e: HttpException) {
            if (e.code() == 400) {
                val body = e.response()?.errorBody()
                if (body != null && body.string().contains(ApiMissingAppTokenException.apiMsg)) {
                    return true
                }
            }
        } catch (e: Exception) {
            return false
        }

        return false
    }

    suspend fun initSession(userToken: String): ApiSession {
        if (api == null) {
            throw ApiNotInitializedException()
        }
        if (appToken.isEmpty()) {
            throw ApiMissingAppTokenException()
        }

        try {
            return api!!.initSession(
                appToken,
                "user_token $userToken"
            )
        } catch (e: HttpException) {
            if (e.code() == 400) {
                val body = e.response()?.errorBody()
                if (body != null) {
                    throw ApiAuthFailedException()
                }
            }
        }

        throw ApiUnexpectedResponseException()
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
