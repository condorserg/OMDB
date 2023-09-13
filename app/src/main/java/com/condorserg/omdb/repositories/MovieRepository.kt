package com.condorserg.omdb.repositories

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.condorserg.omdb.MovieType
import com.condorserg.omdb.RemoteMovie
import com.condorserg.omdb.Search
import com.condorserg.omdb.db.Database
import kotlinx.coroutines.flow.Flow
import om.condorserg.materialdesign.Networking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException

class MovieRepository {
    private val moviesDao = Database.instance.moviesDao()

    fun searchMovies(
        movieType: MovieType,
        searchText: String,
        onComplete: (List<RemoteMovie>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        Networking.omdbApi.searchMovies(searchText, movieType).enqueue(
            object : Callback<Search> {
                override fun onResponse(call: Call<Search>, response: Response<Search>) {
                    if (response.isSuccessful) {
                        response.body()?.movies.let {
                            if (it != null) {
                                onComplete(it)
                            }
                        }
                        Log.d("Server", "movies = ${response.body()}")
                    } else {
                        Log.d("Server", "Incorrect Status Code")
                        onError(RuntimeException("Incorrect Status Code"))

                    }
                }

                override fun onFailure(call: Call<Search>, t: Throwable) {
                    Log.e("Server", "Response parsing error = ${t.message}", t)
                    onError(t)

                }

            }
        )
    }

    suspend fun saveMovies(movies: List<RemoteMovie>) {
        moviesDao.insertMovies(movies)
    }

    fun searchDatabase(searchQuery: String, movieType: MovieType): List<RemoteMovie> {
        return moviesDao.searchDatabase(searchQuery, movieType)
    }

    fun observeMovies(): Flow<List<RemoteMovie>> {
        return moviesDao.getAllMovies()
    }

    fun checkInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }
}
