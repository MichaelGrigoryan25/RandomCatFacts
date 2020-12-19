package com.example.myapplication

import com.example.myapplication.api.Fact
import retrofit2.Call
import retrofit2.http.GET

interface ApiRequests {
    @GET("/facts/random")
    fun getFacts(): Call<Fact>
}