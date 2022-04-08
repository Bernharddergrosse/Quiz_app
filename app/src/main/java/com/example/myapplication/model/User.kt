package com.example.myapplication.model

data class User(var id: Long, var username: String, var password: String);
/*
class User {
    var _id:        Long = -1;
    var username:   String = "unknown"
    var password:   String = "unknown"

    constructor(username: String, password: String) {
        this.username = username
        this.password = password
    }

    constructor(_id: Long, username: String, password: String) {
        this._id = _id
        this.username = username
        this.password = password
    }

    public fun setId(id: Long) {
        this._id = id;
    }

    override fun toString(): String {
        return "$_id $username $password"
    }
}*/