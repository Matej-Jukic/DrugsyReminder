package com.matejjukic.ferit.drugsyreminder

import android.app.AlarmManager
import android.app.Application
import android.content.Context

class DrugsyReminder: Application() {

    companion object{
        lateinit var context: Context
            private set
        lateinit var manager: AlarmManager
            private set
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        manager = getSystemService(ALARM_SERVICE) as AlarmManager
    }
}