package com.example.equiposargentinos.api

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface EqApiService {
    @GET("search_all_teams.php?l=Argentinian%20Primera%20Division")
    suspend fun getTeams(): FbJsonResponse
}

private var retrofit = Retrofit.Builder()
    .baseUrl("https://www.thesportsdb.com/api/v1/json/2/")
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

var service: EqApiService = retrofit.create(EqApiService::class.java)