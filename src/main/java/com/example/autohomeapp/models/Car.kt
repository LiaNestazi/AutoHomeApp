package com.example.autohomeapp.models

class Car {
    var uId: String
    var imageUrl: String
    var name: String
    var desc: String
    var price: String
    var model: String
    var category: String
    var rating: String
    var color: String
    var drive: String
    var fuel: String
    var power: String
    var volume: String


    constructor(uId: String, imageUrl: String, name: String, desc: String, price: String, model: String, category: String, rating: String,
    color: String, drive: String, fuel: String, power: String, volume: String){
        this.uId = uId
        this.imageUrl = imageUrl
        this.name = name
        this.desc = desc
        this.price = price
        this.model = model
        this.category = category
        this.rating = rating
        this.color = color
        this.drive = drive
        this.fuel = fuel
        this.power = power
        this.volume = volume

    }
    constructor(){
        uId = ""
        imageUrl = ""
        name = ""
        desc = ""
        price = ""
        model = ""
        category = ""
        rating = ""
        color = ""
        drive = ""
        fuel = ""
        power = ""
        volume = ""
    }
}