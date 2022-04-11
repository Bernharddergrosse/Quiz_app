package com.example.myapplication.model.Game

import com.example.myapplication.model.User
import com.example.myapplication.model.question.Question

data class GameToCreate(var date: String, var time: String, var user1: User, var user2: User?, var questionsAsked: Array<Question>)
