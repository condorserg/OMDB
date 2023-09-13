package com.condorserg.omdb.db

import android.content.Context
import androidx.room.Room


object Database {

    lateinit var instance: ImdbDatabase
        private set

    fun init(context: Context) {
        instance = Room.databaseBuilder(
            context,
            ImdbDatabase::class.java,
            ImdbDatabase.DB_NAME
        )
            .build()
    }
}