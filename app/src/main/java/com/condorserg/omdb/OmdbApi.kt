package om.condorserg.materialdesign

import com.condorserg.omdb.MovieType
import com.condorserg.omdb.Search
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val OMDB_API_KEY = "d50cee67"

interface OmdbApi {

        @GET("/")
        fun searchMovies(@Query("s") searchText: String,
                         @Query("type") type: MovieType,
                         @Query("apikey") ombd_api_key: String = OMDB_API_KEY)
        : Call<Search>
}