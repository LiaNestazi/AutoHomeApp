package com.example.autohomeapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.autohomeapp.adapters.CarListAdapter
import com.example.autohomeapp.models.Car
import com.google.firebase.database.*

class SortedActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var database: DatabaseReference
    private lateinit var adapter: CarListAdapter
    private lateinit var textForEmpty:TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var back: ImageButton
    private lateinit var name: String
    private lateinit var ref: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sorted)

        supportActionBar?.hide()
        recyclerView = findViewById(R.id.recycler_view)
        database = FirebaseDatabase.getInstance().getReference("Cars")
        textForEmpty = findViewById(R.id.text_for_empty)
        progressBar = findViewById(R.id.progress_bar)
        back = findViewById(R.id.back_button)
        recyclerView.layoutManager = LinearLayoutManager(this)

        if (intent.hasExtra("name")) {
            name = intent.getStringExtra("name")!!
        }

        if (intent.hasExtra("ref")) {
             ref = intent.getStringExtra("ref")!!
        }

        back.setOnClickListener(View.OnClickListener {
            finish()
        })
        database.addValueEventListener(object: ValueEventListener
        {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                var cars = ArrayList<Car>()
                if (snapshot.hasChildren()) {
                    for (data in snapshot.children) {
                        var car = data.getValue(Car::class.java)
                        if (car != null) {
                            if (ref.equals("Models")){
                                if (car.model.equals(name)){
                                    cars.add(car)
                                }
                            }
                            if (ref.equals("Categories")){
                                if (car.category.equals(name)){
                                    cars.add(car)
                                }
                            }
                        }
                    }
                    adapter = CarListAdapter(this@SortedActivity, cars)
                    recyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()
                    try {
                        textForEmpty.visibility = View.INVISIBLE
                        progressBar.visibility = ProgressBar.INVISIBLE
                    } catch (ignored: java.lang.NullPointerException) { }
                } else{
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

    }
}