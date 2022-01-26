package com.matejjukic.ferit.drugsyreminder

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Drugs::class], version = 1)
abstract class DrugsDatabase : RoomDatabase() {

    companion object{
        private var instance: DrugsDatabase? = null
        fun getInstance(context: Context): DrugsDatabase?{
            if(instance == null)
                synchronized(this) {
                    instance = buildDatabase(context)
                }
            return instance
        }
        fun destroyInstance(){
            instance = null
        }
        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            DrugsDatabase::class.java, "Drugs Database"
        ).build()
    }
    abstract fun getDrugsDao(): DrugsDao
}