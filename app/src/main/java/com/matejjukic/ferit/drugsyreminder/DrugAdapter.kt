package com.matejjukic.ferit.drugsyreminder

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity

class DrugAdapter(private val items: List<Drugs>, private val context: Context):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            RecyclerView.ViewHolder {
        return DrugsViewHolder(

            LayoutInflater.from(parent.context).inflate(R.layout.drugs_adapter, parent,
                false)
        )
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder,
                                  position: Int) {
        when(holder) {
            is DrugsViewHolder -> {
                holder.bind(items[position], context)
            }
        }
    }
    override fun getItemCount(): Int {
        return items.size
    }
    class DrugsViewHolder constructor(
        itemView: View
    ): RecyclerView.ViewHolder(itemView) {
        private val drugName = itemView.findViewById<TextView>(R.id.drug_name)
        private val nextUse = itemView.findViewById<TextView>(R.id.next_use)
        private val layout = itemView.findViewById<ConstraintLayout>(R.id.item_holder)
        fun bind(drugs: Drugs, context: Context) {
            drugName.text = drugs.name
            if((drugs.hour!!<10)&&(drugs.minute!! < 10))
                nextUse.text = "0" + drugs.hour.toString() + ":0" + drugs.minute.toString()
            else if(drugs.minute!! < 10)
                nextUse.text = drugs.hour.toString() + ":0" + drugs.minute.toString()
            else if(drugs.hour!! < 10)
                nextUse.text = "0" + drugs.hour.toString() + ":" + drugs.minute.toString()
            else
                nextUse.text = drugs.hour.toString() + ":" + drugs.minute.toString()
            layout.setOnClickListener{
                val intent = Intent(context, DrugActivity::class.java)
                intent.putExtra(IDENTIFIER, drugs.id)
                context.startActivity(intent)
            }
        }
    }
}