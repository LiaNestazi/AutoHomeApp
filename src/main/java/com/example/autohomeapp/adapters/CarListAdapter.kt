package com.example.autohomeapp.adapters

import android.annotation.SuppressLint
import android.app.Dialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.autohomeapp.R
import com.example.autohomeapp.models.Car
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class CarListAdapter(private val context: android.content.Context?, private val cars: ArrayList<Car>): RecyclerView.Adapter<CarListAdapter.ViewHolder>() {
    private val mAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private val sharedPref =  context?.getSharedPreferences("user", android.content.Context.MODE_PRIVATE)
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val image: ImageView = itemView.findViewById(R.id.image_view)
        val name: TextView = itemView.findViewById(R.id.name)
        val desc: TextView = itemView.findViewById(R.id.desc)
        val price: TextView = itemView.findViewById(R.id.price)
        val fav: CheckBox = itemView.findViewById(R.id.fav_check)
        val rating: RatingBar = itemView.findViewById(R.id.rating_bar)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_auto, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.name.text = cars.get(position).name
        Log.d("mytag", cars.get(position).price)
        holder.desc.text = cars.get(position).desc
        val price = cars.get(position).price+" руб."
        holder.price.text = price
        if (!cars.get(position).imageUrl.equals("")) {
            Picasso.get().load(cars.get(position).imageUrl).into(holder.image)
        }

        holder.rating.rating = cars.get(position).rating.toFloat()

        database.getReference("Users").child(mAuth.currentUser?.uid.toString())
            .child("favs").addValueEventListener(object: ValueEventListener
        {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChildren()) {
                    for (data in snapshot.children) {
                        var id = data.getValue(String::class.java)
                        if (id != null) {
                            if (id.equals(cars.get(position).uId)){
                                checkFav(holder.fav)
                            }
                        }
                    }

                }
            }
        })

        holder.fav.setOnClickListener(View.OnClickListener {
            if (holder.fav.isChecked){
                val email = sharedPref?.getString("email", "null")
                if (email != null && email != "null") {
                    holder.fav.setButtonDrawable(R.drawable.ic_baseline_favorite_24)
                    database.getReference("Users").child(mAuth.currentUser?.uid.toString())
                        .child("favs").child(cars.get(position).uId).setValue(cars.get(position).uId)
                } else{
                    Toast.makeText(context, "Авторизуйтесь, чтобы добавлять в избранное", Toast.LENGTH_SHORT).show()
                }
            } else{
                holder.fav.setButtonDrawable(R.drawable.ic_baseline_favorite_border_24)
                database.getReference("Users").child(mAuth.currentUser?.uid.toString())
                    .child("favs").child(cars.get(position).uId).removeValue()
            }
        })
        holder.itemView.setOnClickListener {
            openDescribeWindow(holder.itemView, position, cars.get(position).uId)

        }
    }

    fun openDescribeWindow(itemView: View, position: Int, getuId: String){
        val dialog = Dialog(context!!)
        dialog.setContentView(R.layout.describe_dialog)
        val image: ImageView = dialog.findViewById(R.id.image_view)
        val name: TextView = dialog.findViewById(R.id.name)
        val desc: TextView = dialog.findViewById(R.id.desc)
        val price: TextView = dialog.findViewById(R.id.price)
        val fav: CheckBox = dialog.findViewById(R.id.fav_check)
        val rating: RatingBar = dialog.findViewById(R.id.rating_bar)
        val category: TextView = dialog.findViewById(R.id.category)
        val color: TextView = dialog.findViewById(R.id.color)
        val drive: TextView = dialog.findViewById(R.id.drive)
        val fuel: TextView = dialog.findViewById(R.id.fuel)
        val power: TextView = dialog.findViewById(R.id.power)
        val volume: TextView = dialog.findViewById(R.id.volume)
        dialog.window!!.setBackgroundDrawableResource(R.drawable.layout_bg)

        var car = cars.get(position)
        if (!car.imageUrl.equals("")) {
            Picasso.get().load(cars.get(position).imageUrl).into(image)
        }
        name.text = car.name
        desc.text = car.desc
        val price_text = car.price+" руб."
        price.text = price_text
        rating.rating = car.rating.toFloat()
        category.text = car.category
        color.text = car.color
        drive.text = car.drive
        fuel.text = car.fuel
        power.text = car.power
        volume.text = car.volume
        database.getReference("Users").child(mAuth.currentUser?.uid.toString())
            .child("favs").addValueEventListener(object: ValueEventListener
            {
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChildren()) {
                        for (data in snapshot.children) {
                            var id = data.getValue(String::class.java)
                            if (id != null) {
                                if (id.equals(cars.get(position).uId)){
                                    checkFav(fav)
                                    notifyDataSetChanged()
                                }
                            }
                        }

                    }
                }
            })
        fav.isClickable = false

        dialog.show()
    }
    fun checkFav(fav: CheckBox){
        fav.setChecked(true)
        fav.setButtonDrawable(R.drawable.ic_baseline_favorite_24)
    }
    override fun getItemCount(): Int {
        return cars.size
    }
}