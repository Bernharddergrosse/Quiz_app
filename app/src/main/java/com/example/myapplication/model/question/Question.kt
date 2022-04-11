package com.example.myapplication.model.question

import com.example.myapplication.model.Game.Game

data class Question(var id: Long, var answer1: String,var answer2: String,var answer3: String,var answer4: String, var questionText: String, var correctAnswer: String)
