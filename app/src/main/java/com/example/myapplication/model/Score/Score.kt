package com.example.myapplication.model.Score

import com.example.myapplication.model.User

data class Score(var id: Long, var points: Int, var user: User?);

/*
class Score {
    var _id:        Long = -1;
    var points:     Int = -1;
    lateinit var user:       User;

    constructor(points: Int, user: User) {
        this.points = points
        this.user = user
    }

    constructor(_id: Long, points: Int, user: User) {
        this._id = _id
        this.points = points
        this.user = user
    }

    public fun setId(id: Long) {
        this._id = id;
    }

    override fun toString(): String {
        return "Score(_id=$_id, points=$points, user=$user)"
    }
}
*/