package com.matejjukic.ferit.drugsyreminder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction

const val KEY = "com.matejjukic.ferit.drugsyreminder.KEY"

class DrugActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drug)
        val bundle = intent.extras
        if (bundle?.getString(IDENTIFIER)=="button"){
            val fragmentTransaction: FragmentTransaction?=supportFragmentManager.beginTransaction()
            fragmentTransaction?.replace(R.id.fragment_placeholder, DrugInsertFragment())
            fragmentTransaction?.commit()
        }
        else{
            val infoFragment = DrugInfoFragment()
            val fragmentBundle = Bundle()
            fragmentBundle.putString(KEY, bundle?.getString(IDENTIFIER))
            infoFragment.arguments = fragmentBundle
            val fragmentTransaction: FragmentTransaction?=supportFragmentManager.beginTransaction()
            fragmentTransaction?.replace(R.id.fragment_placeholder, infoFragment)
            fragmentTransaction?.commit()
        }
    }

}