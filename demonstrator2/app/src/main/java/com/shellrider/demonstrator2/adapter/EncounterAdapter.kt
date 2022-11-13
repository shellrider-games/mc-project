package com.shellrider.demonstrator2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shellrider.demonstrator2.R
import com.shellrider.demonstrator2.model.Encounter

class EncounterAdapter(private val context: Context, private val encounterData: List<Encounter>) : RecyclerView.Adapter<EncounterAdapter.EncounterViewHolder>() {
    class EncounterViewHolder(private val view:View) : RecyclerView.ViewHolder(view) {
        val nameView: TextView = view.findViewById(R.id.encounterName)
        val descriptionView: TextView = view.findViewById(R.id.encounterDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EncounterViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent,false)
        return EncounterViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: EncounterViewHolder, position: Int) {
        val encounter = encounterData[position]
        holder.nameView.text = encounter.name
        holder.descriptionView.text = encounter.description
    }

    override fun getItemCount() = encounterData.size
}