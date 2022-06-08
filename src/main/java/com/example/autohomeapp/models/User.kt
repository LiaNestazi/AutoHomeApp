package com.example.autohomeapp.models

class User {
    var email: String
    var login: String
    var favs: ArrayList<String>

    constructor(email: String, login: String){
        this.email = email
        this.login = login
        this.favs = ArrayList()
    }
    constructor(){
        email = ""
        login = ""
        favs = ArrayList()
    }
}