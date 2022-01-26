package com.matejjukic.ferit.drugsyreminder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.get
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.random.Random


class DrugInsertFragment : Fragment() {

    private lateinit var name:EditText
    private var ID: Int = 0;
    private val random: Random = Random
    private lateinit var timePicker:TimePicker
    private lateinit var spinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_drug_insert, container, false)
        spinner=view.findViewById(R.id.spinner)
        name=view.findViewById(R.id.input_drug_name)
        val items = (1..24).toList()
        val adapter = activity?.let {
            ArrayAdapter(it.applicationContext, android.R.layout.simple_spinner_dropdown_item, items)
        }
        spinner.adapter = adapter
        timePicker = view.findViewById(R.id.time_Picker)
        timePicker.setIs24HourView(true)
        if(arguments?.getString(FRAGMENT_INDENTIFIER) != null){
            runBlocking {
                val drug = MainActivity.getDao().getDrug(arguments?.getString(FRAGMENT_INDENTIFIER)!!)
                name.setText(drug.name)
                timePicker.hour= drug.hour!!
                timePicker.minute= drug.minute!!
            }
        }
        view.findViewById<Button>(R.id.save).setOnClickListener{
            if(arguments?.getString(FRAGMENT_INDENTIFIER) == null){
                runBlocking{
                    do{
                        ID = random.nextInt(100000)
                    }
                        while(!checkID())
                    MainActivity.getDao().insert(Drugs(ID.toString(),name.text.toString(),timePicker.hour, timePicker.minute,spinner.selectedItem.toString().toInt()))
                    Toast.makeText(activity?.applicationContext, "Stavka dodana", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                runBlocking{
                    MainActivity.getDao().updateDrug(arguments?.getString(FRAGMENT_INDENTIFIER)!!, name.text.toString(),timePicker.hour, timePicker.minute,spinner.selectedItem.toString().toInt())
                    Toast.makeText(activity?.applicationContext, "Stavka izmijenjena", Toast.LENGTH_SHORT).show()
                }
            }
        }



        return view
    }
    fun checkID():Boolean{
        var checker=true
        runBlocking {
            for (i in MainActivity.getDao().getAllIds())
                if(i == ID.toString())
                    checker=false


        }
        return checker
    }
}