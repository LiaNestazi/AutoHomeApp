package com.example.autohomeapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.autohomeapp.adapters.CarListAdapter
import com.example.autohomeapp.models.Car
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FavoritesActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var database_cars: DatabaseReference
    private lateinit var database_fav: DatabaseReference
    private lateinit var adapter: CarListAdapter
    private lateinit var textForEmpty: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var back: ImageButton
    private lateinit var name: String
    private lateinit var ref: String
    private val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        supportActionBar?.hide()
        recyclerView = findViewById(R.id.recycler_view)
        database_cars = FirebaseDatabase.getInstance().getReference("Cars")
        database_fav = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.currentUser?.uid.toString()).child("favs")
        textForEmpty = findViewById(R.id.text_for_empty)
        progressBar = findViewById(R.id.progress_bar)
        back = findViewById(R.id.back_button)
        recyclerView.layoutManager = LinearLayoutManager(this)



        back.setOnClickListener(View.OnClickListener {
            finish()
        })
        var favs: ArrayList<String> = ArrayList()
        var cars: ArrayList<Car> = ArrayList()
        database_fav.addValueEventListener(object: ValueEventListener
        {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChildren()) {
                    favs.clear()
                    for (data in snapshot.children) {
                        var id = data.getValue(String::class.java)
                        if (id != null) {
                            favs.add(id)
                        }
                    }

                    database_cars.addValueEventListener(object: ValueEventListener{
                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                        @SuppressLint("NotifyDataSetChanged")
                        override fun onDataChange(snapshot: DataSnapshot) {
                            cars.clear()
                            if (snapshot.hasChildren()) {
                                for (data in snapshot.children){
                                    var car = data.getValue(Car::class.java)
                                    if (car != null){
                                        if (favs.contains(car.uId)){
                                            cars.add(car)
                                        }
                                    }
                                }
                                if (cars.isEmpty()){
                                    try {
                                        progressBar.visibility = ProgressBar.INVISIBLE
                                        textForEmpty.visibility = View.VISIBLE
                                        textForEmpty.text = "Здесь пока что ничего нет"
                                    } catch (ignored: NullPointerException) {
                                    }
                                } else{
                                    try {
                                        textForEmpty.visibility = View.INVISIBLE
                                        progressBar.visibility = ProgressBar.INVISIBLE
                                    } catch (ignored: java.lang.NullPointerException) { }
                                }
                                adapter = CarListAdapter(this@FavoritesActivity, cars)
                                recyclerView.adapter = adapter
                                adapter.notifyDataSetChanged()
                            }
                            else {
                                cars.clear()
                                try {
                                    progressBar.visibility = ProgressBar.INVISIBLE
                                    textForEmpty.visibility = View.VISIBLE
                                    textForEmpty.text = "Здесь пока что ничего нет"
                                } catch (ignored: NullPointerException) {
                                }
                            }
                        }

                    })

                } else{
                    favs.clear()
                    try {
                        progressBar.visibility = ProgressBar.INVISIBLE
                        textForEmpty.visibility = View.VISIBLE
                        textForEmpty.text = "Здесь пока что ничего нет"
                    } catch (ignored: NullPointerException) {
                    }
                }


            }
        })

    }
}