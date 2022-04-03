package com.matejjukic.ferit.drugsyreminder

import android.app.Activity
import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

class NotificationSender: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val notificationId: Int = Random.nextInt()
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val database = DrugsDatabase.getInstance(DrugsyReminder.context)!!
        val dao = database.getDrugsDao()
        val drugNames = ArrayList<String>()
        var drugs: List<Drugs>
        runBlocking{
            drugs = dao.getAll()
            for(i in drugs) {
                if(i.current == true) {
                    dao.updateHour(i.id)
                    if (i.name == null)
                        drugNames.add("")
                    else
                        drugNames.add(i.name)
                    dao.updateState(false, i.id)
                }
            }
        }


        val drugsToDrink = drugNames.joinToString(", ")

        val builder= context?.let {
            NotificationCompat.Builder(it, "alarm")
                .setSmallIcon(R.drawable.pill)
                .setContentTitle("Vrijeme je za lijek")
                .setContentText("Vrijeme je za lijekove: $drugsToDrink")
                .setAutoCancel(true)
                .setSound(soundUri)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(Notification.FLAG_INSISTENT)

        }


        drugs = MainActivity.getNextAlarm(dao)
        runBlocking{
            for(i in dao.getAll())
                if(drugs.contains(i))
                    dao.updateState(true, i.id)
                else
                    dao.updateState(false, i.id)
        }

        MainActivity.updateTime(drugs)
        MainActivity.setAlarm()
        val notificationManagerCompat: NotificationManagerCompat? =
            context?.let { NotificationManagerCompat.from(it) }
        if (builder != null) {
            notificationManagerCompat?.notify(notificationId, builder.build())
        }
        Handler(Looper.getMainLooper()).postDelayed({
            if (builder != null) {
                notificationManagerCompat?.cancel(notificationId)
            }
        }, 30000)
    }
}