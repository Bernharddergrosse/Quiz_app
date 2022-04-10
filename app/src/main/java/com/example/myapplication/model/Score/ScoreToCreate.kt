package com.example.myapplication.model.Score

import com.example.myapplication.model.User

data class ScoreToCreate(var points: Int, var date: String, var time: String, var user: User?);
