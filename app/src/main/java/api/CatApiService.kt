package com.example.LAB_WEEK_05

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

data class CatImage(
    val id: String,
    val url: String,
    val width: Int,
    val height: Int
)

interface CatApiService {
    @GET("images/search")
    fun searchImages(
        @Query("limit") limit: Int,
        @Query("size") size: String
    ): Call<List<CatImage>>
}
