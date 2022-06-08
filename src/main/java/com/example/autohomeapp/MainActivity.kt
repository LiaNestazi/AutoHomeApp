package com.example.autohomeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.autohomeapp.models.Car
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private var database = FirebaseDatabase.getInstance()
    private lateinit var cars: ArrayList<Car>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        var car = Car("", "https://avatars.mds.yandex.net/get-autoru-vos/1552469/dd1c0be791ded75643591f2e8e3c2378/1200x900n",
//        "Audi A5 I", "Серия компактных представительских авто, выпускаемых немецким автопроизводителем Audi","1 950 000", "Audi", "Кабриолет")
//        database.getReference("Cars").push().setValue(car)

        Thread.sleep(1500)
        startActivity(Intent(this, BottomNavigationActivity::class.java))
    }
}