package com.example.myapplication.model.Game

import com.example.myapplication.model.User
import com.example.myapplication.model.question.Question

data class Game(var id: Long, var date: String, var time: String, var user1: User, var user2: User?, var questionsAsked: Array<Question>)
/*
class Game {
    var id:        Long = -1;
    var date:       String = "unknown"
    var time:       String = "unknown"
    lateinit var user1:    User;
    lateinit var user2:    User;



    constructor(date: String, time: String, user1: User, user2: User) {
        this.date = date
        this.time = time
        this.user1 = user1
        this.user2 = user2
    }

    constructor(_id: Long, date: String, time: String, user1: User, user2: User) {
        this._id = _id
        this.date = date
        this.time = time
        this.user1 = user1
        this.user2 = user2
    }


    public fun setId(id: Long) {
        this._id = id;
    }

    override fun toString(): String {
        return "Game(_id=$_id, date='$date', time='$time', user1=$user1, user2=$user2)"
    }
}
*/
