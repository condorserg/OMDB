package com.condorserg.omdb.ui.searchMovies

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.condorserg.omdb.*

import com.condorserg.omdb.repositories.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MoviesViewModel : ViewModel() {

    private val repository = MovieRepository()
    private val movieListLiveData = MutableLiveData<List<RemoteMovie>>()
    private val isLoadingLiveData = MutableLiveData<Boolean>(false)
    private var currentJob: Job? = null
    private val context = App.appContext

    val moviesList: LiveData<List<RemoteMovie>>
        get() = movieListLiveData

    private val showToastLiveData = SingleLiveEvent<String>()
    val showToast: LiveData<String>
        get() = showToastLiveData

    val isLoading: LiveData<Boolean>
        get() = isLoadingLiveData


    private fun searchMovies(movieType: MovieType, searchText: String) {
        viewModelScope.launch {
            isLoadingLiveData.postValue(true)
            repository.searchMovies(movieType, searchText, onComplete = { moviesList ->
                isLoadingLiveData.postValue(false)
                movieListLiveData.postValue(moviesList)
                saveMovies(moviesList)
            }, onError = {
                movieListLiveData.postValue(emptyList())
                isLoadingLiveData.postValue(false)
                showToastLiveData.postValue("Фильм не найден или ошибка поиска")
            })
        }
    }

    //комбинированный Flow
    fun movieTypeQueryFlow(movieTypeFlow: Flow<MovieType>, queryFlow: Flow<String>) {
        //пример https://betterprogramming.pub/learn-how-to-combine-kotlin-flows-317849a71d3e
        //https://developer.android.com/kotlin/flow
        currentJob?.cancel()
        currentJob = movieTypeFlow.combine(queryFlow)
        { movieType, text -> movieType to text }
            .debounce(1000)
            .distinctUntilChanged()
//            .flowOn(Dispatchers.IO)
            .mapLatest { (movieType, text) ->
                if (text.length >= 3) {
                    //здесь нужно проверить есть ли интернет
                    isLoadingLiveData.postValue(true)
                    if (repository.checkInternet(context)) {
                        searchMovies(movieType, text)
                    } else {
                        //в БД ищем, только если нет Интернета
                        showToastLiveData.postValue("Нет интернета. Поиск выполнен в БД")
                        movieListLiveData.postValue(searchDatabase(text, movieType))
                    }
                    isLoadingLiveData.postValue(false)
                }
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

    private fun saveMovies(movies: List<RemoteMovie>) {
        viewModelScope.launch {
            try {
                repository.saveMovies(movies)
            } catch (t: Throwable) {
                Log.e("Save Movies", "Saving to db error", t)
                showToastLiveData.postValue("Ошибка сохранения в БД")
            }
        }
    }

    private fun searchDatabase(searchQuery: String, movieType: MovieType): List<RemoteMovie> {
        return repository.searchDatabase("%$searchQuery%", movieType)
    }

    fun currentJobCanceller() {
        currentJob?.cancel()
    }
}