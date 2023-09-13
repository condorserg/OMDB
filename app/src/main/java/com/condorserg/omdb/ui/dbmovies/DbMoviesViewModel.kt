package com.condorserg.omdb.ui.dbmovies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.condorserg.omdb.RemoteMovie
import com.condorserg.omdb.repositories.MovieRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DbMoviesViewModel : ViewModel() {
    private val movieRepository = MovieRepository()
    private val movieDBListLiveData = MutableLiveData<List<RemoteMovie>>()

    val moviesDBList: LiveData<List<RemoteMovie>>
        get() = movieDBListLiveData

    init {
        //пример 34.7 Flow в Android
        movieRepository.observeMovies()
            .onEach { movieDBListLiveData.postValue(it) }
            .launchIn(viewModelScope)
    }
}