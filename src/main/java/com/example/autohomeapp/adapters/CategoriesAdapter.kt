package com.example.autohomeapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.autohomeapp.R
import com.example.autohomeapp.SortedActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CategoriesAdapter(private val context: android.content.Context?, private val ref: String, private val categories: ArrayList<String>): RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {
    private val mAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val name: TextView = itemView.findViewById(R.id.name)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_category, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = categories.get(position)
        holder.itemView.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, SortedActivity::class.java)
            intent.putExtra("ref", ref)
            intent.putExtra("name", categories.get(position))
            context?.startActivity(intent)
        })
    }

    override fun getItemCount(): Int {
        return categories.size
    }
}