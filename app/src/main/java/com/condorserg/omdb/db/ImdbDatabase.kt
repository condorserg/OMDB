package com.condorserg.omdb.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.condorserg.omdb.RemoteMovie
import om.condorserg.materialdesign.db.MoviesDao


@Database(
    entities = [
        RemoteMovie::class
    ],
    version = ImdbDatabase.DB_VERSION
)
abstract class ImdbDatabase : RoomDatabase() {

    abstract fun moviesDao(): MoviesDao

    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "imdb-database"
    }
}