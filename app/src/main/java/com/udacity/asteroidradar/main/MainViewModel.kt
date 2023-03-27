package com.udacity.asteroidradar.main

import androidx.lifecycle.*
import com.udacity.asteroidradar.AsteroidRadarApplication
import com.udacity.asteroidradar.api.ApodApiStatus
import com.udacity.asteroidradar.database.NasaDatabase
import com.udacity.asteroidradar.repository.Asteroid
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

class MainViewModel(application: AsteroidRadarApplication) : ViewModel() {

    // Initialize the repository from which to retrieve cached data
    private val repository = AsteroidRepository(NasaDatabase.getDatabase(application))

    /*
        UI variables for displaying the image of the day
     */
    private val _imageStatus = MutableLiveData<ApodApiStatus>()
    val imageStatus: LiveData<ApodApiStatus>
        get() = _imageStatus

    private val _imageUrl = MutableLiveData<String?>()
    val imageUrl: LiveData<String?>
        get() = _imageUrl

    init {
        viewModelScope.launch {
            displayImage()
            repository.refreshAsteroids()
        }
    }

    val asteroidList: LiveData<List<Asteroid>?> = repository.asteroids

    private suspend fun displayImage() {
        _imageStatus.value = ApodApiStatus.LOADING
        try {
            val image = repository.getImageOfTheDay()
            _imageUrl.value = if (image.mediaType != "image") null else image.url
            _imageStatus.value = ApodApiStatus.SUCCESS
        } catch (e: Exception) {
            _imageStatus.value = ApodApiStatus.ERROR
        }
    }

    // ViewModelFactory class
    class MainViewModelFactory(private val application: AsteroidRadarApplication) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(application) as T
            }
            throw IllegalArgumentException("ViewModel class not found: unable to create ViewModel")
        }

    }
}