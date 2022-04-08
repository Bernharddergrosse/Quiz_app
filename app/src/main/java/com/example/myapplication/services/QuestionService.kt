package com.example.myapplication.services

import com.example.myapplication.model.Game.Game
import com.example.myapplication.model.question.Question
import retrofit2.http.*

interface QuestionService {
    @GET("questions/")          suspend fun listAll(): List<Question>
    @GET("questions/{id}")      suspend fun get(@Path("id") id: Long): Question
    @PUT("questions/{id}")      suspend fun update(@Path("id") id: Long, @Body g: Game)
    @DELETE("questions/{id}")   suspend fun delete(@Path("id") id: Long)
}