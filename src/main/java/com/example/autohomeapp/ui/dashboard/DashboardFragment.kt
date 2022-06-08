package com.example.autohomeapp.ui.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.autohomeapp.R
import com.example.autohomeapp.adapters.CarListAdapter
import com.example.autohomeapp.databinding.FragmentDashboardBinding
import com.example.autohomeapp.models.Car
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CarListAdapter

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        setHasOptionsMenu(true)
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        database = FirebaseDatabase.getInstance().getReference("Cars")
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)

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
                            cars.add(car as Car)
                        }
                    }
                    dashboardViewModel.setCars(cars)
                    Log.d("mytag", dashboardViewModel.getCars().get(0).desc)
                    adapter = CarListAdapter(context, cars)
                    recyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()
                    try {
                        binding.textForEmpty.visibility = View.INVISIBLE
                        binding.progressBar.visibility = ProgressBar.INVISIBLE
                    } catch (ignored: java.lang.NullPointerException) { }
                } else{
                    cars.clear()
                    try {
                        binding.progressBar.visibility = ProgressBar.INVISIBLE
                        binding.textForEmpty.visibility = View.VISIBLE
                        binding.textForEmpty.text = "Здесь пока что ничего нет"
                    } catch (ignored: NullPointerException) {
                    }
                }
            }
        })
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val item = menu?.findItem(R.id.action_search)
        val searchView = item?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {

                val searchText = newText!!.lowercase(Locale.getDefault())
                if (searchText.isNotEmpty()){
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
                                        if (car.name.lowercase(Locale.getDefault()).contains(searchText)||
                                                car.desc.lowercase(Locale.getDefault()).contains(searchText)||
                                                car.price.lowercase(Locale.getDefault()).contains(searchText)){
                                            cars.add(car as Car)
                                        }
                                    }
                                }
                                adapter = CarListAdapter(context, cars)
                                recyclerView.adapter = adapter
                                adapter.notifyDataSetChanged()
                                try {
                                    binding.textForEmpty.visibility = View.INVISIBLE
                                    binding.progressBar.visibility = ProgressBar.INVISIBLE
                                } catch (ignored: java.lang.NullPointerException) { }
                            } else{
                                cars.clear()
                                try {
                                    binding.progressBar.visibility = ProgressBar.INVISIBLE
                                    binding.textForEmpty.visibility = View.VISIBLE
                                    binding.textForEmpty.text = "Ничего не найдено"
                                } catch (ignored: NullPointerException) {
                                }
                            }
                        }
                    })
                }
                return false

            }

        })

        item.setOnActionExpandListener(object : MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
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
                                    cars.add(car as Car)
                                }
                            }
                            adapter = CarListAdapter(context, cars)
                            recyclerView.adapter = adapter
                            adapter.notifyDataSetChanged()
                            try {
                                binding.textForEmpty.visibility = View.INVISIBLE
                                binding.progressBar.visibility = ProgressBar.INVISIBLE
                            } catch (ignored: java.lang.NullPointerException) { }
                        } else{
                            cars.clear()
                            try {
                                binding.progressBar.visibility = ProgressBar.INVISIBLE
                                binding.textForEmpty.visibility = View.VISIBLE
                                binding.textForEmpty.text = "Здесь пока что ничего нет"
                            } catch (ignored: NullPointerException) {
                            }
                        }
                    }
                })
                return true
            }

        })
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}