package com.matejjukic.ferit.drugsyreminder

import android.app.*
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.runBlocking
import java.util.*
import java.util.Calendar.HOUR_OF_DAY
import kotlin.collections.ArrayList

const val IDENTIFIER = "com.matejjukic.ferit.drugsyreminder.IDENTIFIER"

private lateinit var database: DrugsDatabase
private lateinit var drugsDao: DrugsDao
private lateinit var drugsView: RecyclerView
private val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)


class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()
        findViewById<FloatingActionButton>(R.id.add_drug).setOnClickListener{
            val intent = Intent(this, DrugActivity::class.java)
            intent.putExtra(IDENTIFIER, "button")
            startActivity(intent)
        }
    }

    override fun onResume(){
        super.onResume()
        fillRecyclerView()
        drugs = getNextAlarm(drugsDao)
        runBlocking{
            for(i in getDao().getAll())
                if(drugs.contains(i))
                    getDao().updateState(true, i.id)
                else
                    getDao().updateState(false, i.id)
        }

        updateTime(drugs)
        setAlarm()
    }

    private fun fillRecyclerView(){
        drugsView = findViewById(R.id.drug_list)
        runBlocking {
            database = DrugsDatabase.getInstance(applicationContext)!!
            drugsDao = database.getDrugsDao()
            drugsView.apply{
                layoutManager =
                    LinearLayoutManager(this@MainActivity)
                adapter =
                    DrugAdapter(drugsDao.getAll().toMutableList(), context)

            }
        }
    }

    companion object{

        lateinit var drugs: ArrayList<Drugs>
        private lateinit var calendar: Calendar
        private lateinit var alarmManager: AlarmManager
        private lateinit var pendingIntent: PendingIntent

        fun getDao(): DrugsDao{
            return drugsDao
        }
        fun updateTime(currentDrugs: List<Drugs>){

            calendar = Calendar.getInstance().apply{
                timeInMillis = System.currentTimeMillis()
            }
            try{
                    calendar.set(Calendar.HOUR_OF_DAY, currentDrugs[0].hour!!)
                    calendar.set(Calendar.MINUTE, currentDrugs[0].minute!!)
            }
            catch(e:IndexOutOfBoundsException){

                calendar.set(Calendar.YEAR, 3000)

            }

        }

        private fun getCurrentHour(): Int{
            return Calendar.getInstance().get(HOUR_OF_DAY)
        }
        private fun getCurrentMinute(): Int{
            return Calendar.getInstance().get(Calendar.MINUTE)
        }

        fun getNextAlarm(dao: DrugsDao): ArrayList<Drugs> {

            val drugs: ArrayList<Drugs>
            var currentHour: Int=getCurrentHour()
            var currentMinute: Int=getCurrentMinute()
            var nearestHour: Int = 100
            var nearestMinute: Int = 100
            var hourDiff: Int = 100
            var minuteDiff: Int = 100
            var nextAlarmNotFound : Boolean = true

            runBlocking {
                drugs = dao.getAll() as ArrayList<Drugs>
            }
            for (i in drugs){
                if ( i.hour!! >= currentHour )
                {
                    if ( i.hour!! < nearestHour )
                        {
                            nearestHour = i.hour!!
                            nearestMinute = i.minute!!
                            nextAlarmNotFound = false
                    }
                    if (( i.hour!! == nearestHour ) && ( i.minute!! < nearestMinute )){
                        nearestHour = i.hour!!
                        nearestMinute = i.minute!!
                        nextAlarmNotFound = false
                    }
                }
            }

            if(nextAlarmNotFound){
                for (i in drugs){
                    if(( i.hour!! < nearestHour ) || (( nearestHour == i.hour!! ) && ( i.minute!! < nearestMinute ))){
                        nearestHour = i.hour
                        nearestMinute = i.minute!!
                    }
                }
            }

            drugs.retainAll{it.hour == nearestHour && it.minute == nearestMinute}
            return drugs
        }

        fun setAlarm(){

            alarmManager = DrugsyReminder.manager
            val intent = Intent(DrugsyReminder.context, NotificationSender::class.java)
            pendingIntent =
                PendingIntent.getBroadcast(DrugsyReminder.context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(){
        val name : CharSequence = "alarm"

        val importance : Int = NotificationManager.IMPORTANCE_HIGH
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
            .build()
        val channel : NotificationChannel = NotificationChannel("alarm", name, importance)
        channel.setSound(soundUri, audioAttributes)
        val notificationManager : NotificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }



    }

