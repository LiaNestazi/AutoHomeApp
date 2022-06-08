package com.example.autohomeapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.autohomeapp.adapters.CategoriesAdapter
import com.google.firebase.database.*

class ModelsActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var textForEmpty: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var backButton: ImageButton
    private lateinit var adapter: CategoriesAdapter
    private lateinit var ref: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_models)

        if (intent.hasExtra("ref")) {
            ref = intent.getStringExtra("ref")!!
        }

        supportActionBar?.hide()
        database = FirebaseDatabase.getInstance().getReference(ref)
        recyclerView = findViewById(R.id.recycler_view)
        textForEmpty = findViewById(R.id.text_for_empty)
        progressBar = findViewById(R.id.progress_bar)
        backButton = findViewById(R.id.back_button)
        backButton.setOnClickListener(View.OnClickListener {
            finish()
        })
        recyclerView.layoutManager = LinearLayoutManager(this)
        database.addValueEventListener(object: ValueEventListener
        {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                var models = ArrayList<String>()
                if (snapshot.hasChildren()) {
                    for (data in snapshot.children) {
                        var model = data.getValue(String::class.java)
                        if (model != null) {
                            models.add(model as String)
                        }
                    }
                    adapter = CategoriesAdapter(this@ModelsActivity, ref,models)
                    recyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()
                    try {
                        textForEmpty.visibility = View.INVISIBLE
                        progressBar.visibility = ProgressBar.INVISIBLE
                    } catch (ignored: java.lang.NullPointerException) { }
                } else{
                    models.clear()
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