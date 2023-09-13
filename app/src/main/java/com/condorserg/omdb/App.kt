package com.condorserg.omdb

import android.app.Application
import android.content.Context
import com.condorserg.omdb.db.Database


class App: Application() {
    override fun onCreate() {
        super.onCreate()
        Database.init(this)
        appContext = applicationContext
    }

    companion object{
        lateinit var appContext: Context
    }
}