package fr.klso.gulpi.services

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import fr.klso.gulpi.models.ApiSession
import fr.klso.gulpi.models.Item
import fr.klso.gulpi.models.ItemType
import fr.klso.gulpi.utilities.ApiAuthFailedException
import fr.klso.gulpi.utilities.ApiMissingAppTokenException
import fr.klso.gulpi.utilities.ApiNotInitializedException
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
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

    suspend fun getItem(type: ItemType = ItemType.COMPUTER, itemId: String): Item {
        if (api == null) {
            throw ApiNotInitializedException()
        }
        if (appToken.isEmpty()) {
            throw ApiMissingAppTokenException()
        }
        if (sessionToken.isEmpty()) {
            throw ApiAuthFailedException()
        }

        return api!!.getItem(
            appToken,
            sessionToken,
            type.str,
            itemId
        )
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun createApi(url: String): GlpiApi {
        val contentType = "application/json".toMediaType()
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()

        return retrofit.create(GlpiApi::class.java)
    }
}