package om.condorserg.materialdesign.db

import androidx.room.*
import com.condorserg.omdb.MovieType

import com.condorserg.omdb.RemoteMovie
import com.condorserg.omdb.db.MoviesContract
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<RemoteMovie>)
//
    @Query("SELECT * FROM ${MoviesContract.TABLE_NAME}")
    fun getAllMovies(): Flow<List<RemoteMovie>>

    @Query("SELECT * FROM ${MoviesContract.TABLE_NAME} WHERE title LIKE :searchQuery AND type = :movieType")
    fun searchDatabase(searchQuery: String, movieType: MovieType): List<RemoteMovie>

}