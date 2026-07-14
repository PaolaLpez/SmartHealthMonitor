package mx.utng.smarthealthmonitor.data.remote

import mx.utng.smarthealthmonitor.wear.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NeonClient {
    private val BASE_URL: String
        get() {
            val host = BuildConfig.NEON_HOST
            val apiHost = if (host.startsWith("ep-")) {
                "api." + host.substringAfter(".")
            } else {
                host
            }
            return "https://$apiHost/"
        }
 
    val AUTH_HEADER: String? = null
    val CONN_STRING  = "postgresql://neondb_owner:npg_3DiXsvakN4TG@${BuildConfig.NEON_HOST}/neondb?sslmode=require"
 
    val api: NeonApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }).build())
            .build()
            .create(NeonApiService::class.java)
    }
}
