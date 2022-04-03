package com.matejjukic.ferit.drugsyreminder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import kotlinx.coroutines.runBlocking
import org.w3c.dom.Text

const val FRAGMENT_INDENTIFIER = "com.matejjukic.ferit.drugsyreminder.FRAGMENT_IDENTIFIER"
class DrugInfoFragment : Fragment() {

        private lateinit var name: TextView
        private lateinit var intakeTime: TextView
        private lateinit var timeOffset: TextView
        private lateinit var drug: Drugs

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val drugInsertFragment = DrugInsertFragment()
        val view = inflater.inflate(R.layout.fragment_drug_info, container, false)
        var flag = false
        name = view.findViewById(R.id.Name)
        intakeTime = view.findViewById(R.id.intakeTime)
        timeOffset = view.findViewById(R.id.timeOffset)
        val bundle = Bundle()
        runBlocking {
            drug = arguments?.getString(KEY)?.let { MainActivity.getDao().getDrug(it) }!!
            name.text=drug?.name
            if((drug?.hour!!<10)&&(drug?.minute!! < 10))
                intakeTime.text = "0" + drug?.hour.toString() + ":0" + drug?.minute.toString()
            else if(drug.minute!! < 10)
                intakeTime.text = drug?.hour.toString() + ":0" + drug?.minute.toString()
            else if(drug.hour!! < 10)
                intakeTime.text = "0" + drug?.hour.toString() + ":" + drug?.minute.toString()
            else
                intakeTime.text = drug?.hour.toString() + ":" + drug?.minute.toString()

            timeOffset.text=drug?.step.toString() + " sati"
        }
        view.findViewById<Button>(R.id.delete).setOnClickListener {
            runBlocking {
                MainActivity.getDao().delete(drug)
                Toast.makeText(activity?.applicationContext, "Izbrisano", Toast.LENGTH_LONG).show()
                activity?.finish()
            }
        }
        bundle.putString(FRAGMENT_INDENTIFIER, arguments?.getString(KEY))
        drugInsertFragment.arguments = bundle
        view.findViewById<Button>(R.id.edit).setOnClickListener{
                val fragmentTransaction: FragmentTransaction?=activity?.supportFragmentManager?.beginTransaction()
                fragmentTransaction?.replace(R.id.fragment_placeholder, drugInsertFragment)
                fragmentTransaction?.commit()




        }
        return view
    }



}