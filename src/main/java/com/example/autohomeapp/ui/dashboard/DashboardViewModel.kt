package com.example.autohomeapp.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.autohomeapp.models.Car

class DashboardViewModel : ViewModel() {

    private var cars = arrayListOf<Car>()
    fun getCars(): ArrayList<Car>{
        return cars
    }
    fun setCars(cars: ArrayList<Car>){
        this.cars = cars
    }
}