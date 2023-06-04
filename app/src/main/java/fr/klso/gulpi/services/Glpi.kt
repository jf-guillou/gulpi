package fr.klso.gulpi.services

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import fr.klso.gulpi.models.ApiSession
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class Glpi {
    private val api: GlpiApi by lazy {
        createGlpiApi()
    }

    suspend fun initSession(): ApiSession {
        return api.initSession(
            "",
            ""
        )
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun createGlpiApi(): GlpiApi {

        val contentType = "application/json".toMediaType()
        val retrofit = Retrofit.Builder()
            .baseUrl("")
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()

        return retrofit.create(GlpiApi::class.java)
    }
}