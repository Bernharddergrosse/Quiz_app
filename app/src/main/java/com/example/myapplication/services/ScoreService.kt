package com.example.myapplication.services

import com.example.myapplication.model.Score.Score
import com.example.myapplication.model.Score.ScoreToCreate
import retrofit2.http.*

interface ScoreService {
    @GET("scores/")          suspend fun listAll(): List<Score>
    @GET("scores/{id}")      suspend fun get(@Path("id") id: Long): Score
    @GET("scores/user/{userId}") suspend fun getForUser(@Path("userId") userId: Long): List<Score>
    @POST("scores")          suspend fun create(@Body s: ScoreToCreate): Score
    @PUT("scores/{id}")      suspend fun update(@Path("id") id: Int, @Body s: Score)
    @DELETE("scores/{id}")   suspend fun delete(@Path("id") id: Int)
}