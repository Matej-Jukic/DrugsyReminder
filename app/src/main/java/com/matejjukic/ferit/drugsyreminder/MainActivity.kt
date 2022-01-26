package com.matejjukic.ferit.drugsyreminder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.runBlocking

const val IDENTIFIER = "com.matejjukic.ferit.drugsyreminder.IDENTIFIER"

private lateinit var database: DrugsDatabase
private lateinit var drugsDao: DrugsDao
private lateinit var drugsView: RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<FloatingActionButton>(R.id.add_drug).setOnClickListener{
            val intent = Intent(this, DrugActivity::class.java)
            intent.putExtra(IDENTIFIER, "button")
            startActivity(intent)
        }
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
        fun getDao(): DrugsDao{
            return drugsDao
        }
    }
}