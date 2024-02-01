package com.example.asteroidradar.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.asteroidradar.database.getDatabase
import com.example.asteroidradar.domain.Asteroid
import com.example.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

class HomeViewModel(application: Application): AndroidViewModel(application) {

    private val  database = getDatabase(application)
    private val repository = AsteroidRepository(database, context = application.applicationContext)

    private val _filteredAsteroids: MutableLiveData<List<Asteroid>> = MutableLiveData()
    val filteredAsteroids: LiveData<List<Asteroid>> = _filteredAsteroids

    init {
        viewModelScope.launch {
            repository.getAsteroids()
            repository.getPictureOfDay()
        }
    }
    val asteroidDataList = repository.asteroids
    val picturesByDay = repository.pictureOfTheDay

    fun getHazardousAsteroids() {
        viewModelScope.launch {
            _filteredAsteroids.postValue(repository.getHazardousAsteroids())
        }
    }

    fun getNotHazardousAsteroids() {
        viewModelScope.launch {
            _filteredAsteroids.postValue(repository.getNotHazardousAsteroids())
        }
    }

    fun getTodayAsteroids() {
        viewModelScope.launch {
            _filteredAsteroids.postValue(repository.getTodayAsteroids())
        }
    }

    fun getAllAsteroids() {
        viewModelScope.launch {
            _filteredAsteroids.postValue(repository.getAllAsteroids())
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}