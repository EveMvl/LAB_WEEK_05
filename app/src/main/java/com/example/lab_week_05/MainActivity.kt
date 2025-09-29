package com.example.lab_week_05

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// Data class untuk JSON response
data class CatImage(
    val id: String,
    val url: String,
    val width: Int,
    val height: Int
)

// Retrofit interface
interface CatApiService {
    @GET("images/search")
    fun searchImages(
        @Query("limit") limit: Int,
        @Query("size") format: String
    ): Call<List<CatImage>>
}

class MainActivity : AppCompatActivity() {

    // Retrofit instance
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // API service
    private val catApiService by lazy {
        retrofit.create(CatApiService::class.java)
    }

    // TextView reference
    private val apiResponseView: TextView by lazy {
        findViewById(R.id.api_response)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Padding untuk system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Panggil API
        getCatImageResponse()
    }

    // Function untuk memanggil API
    private fun getCatImageResponse() {
        val call = catApiService.searchImages(1, "full")
        call.enqueue(object : Callback<List<CatImage>> {
            override fun onFailure(call: Call<List<CatImage>>, t: Throwable) {
                Log.e(MAIN_ACTIVITY, "Failed to get response", t)
            }

            override fun onResponse(call: Call<List<CatImage>>, response: Response<List<CatImage>>) {
                if (response.isSuccessful) {
                    val catImage = response.body()?.firstOrNull()
                    apiResponseView.text = catImage?.url ?: "No image found"
                } else {
                    Log.e(
                        MAIN_ACTIVITY, "Failed to get response\n" +
                                response.errorBody()?.string().orEmpty()
                    )
                }
            }
        })
    }

    companion object {
        const val MAIN_ACTIVITY = "MAIN_ACTIVITY"
    }
}
