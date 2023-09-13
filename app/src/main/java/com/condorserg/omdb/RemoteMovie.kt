package com.condorserg.omdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.condorserg.omdb.db.MoviesContract

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(
    tableName = MoviesContract.TABLE_NAME,
    indices = [Index(MoviesContract.Columns.TITLE)]
)

@JsonClass(generateAdapter = true)
data class RemoteMovie(

    @Json(name = "Title")
    @ColumnInfo(name = MoviesContract.Columns.TITLE)
    val title: String,
    @Json(name = "Year")
    @ColumnInfo(name = MoviesContract.Columns.YEAR)
    val year: String,
    @PrimaryKey
    @Json(name = "imdbID")
    @ColumnInfo(name = MoviesContract.Columns.IMDB_ID)
    val imdbID: String,
    @Json(name = "Type")
    @ColumnInfo(name = MoviesContract.Columns.TYPE)
    val type: String,
    @Json(name = "Poster")
    @ColumnInfo(name = MoviesContract.Columns.POSTER)
    val poster: String
)