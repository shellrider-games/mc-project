package com.shellrider.demonstrator2

import android.os.Bundle
import android.os.Debug
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.shellrider.demonstrator2.adapter.EncounterAdapter

class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        view.findViewById<FloatingActionButton>(R.id.addFAB).setOnClickListener {
            findNavController().navigate(R.id.createEncounterForm)
        }
        val mainActivity = activity as MainActivity
        Log.d("DATABASE", mainActivity.getEncounterTableHandler().listOfEncounters().toString())
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = EncounterAdapter(mainActivity, mainActivity.getEncounterTableHandler().listOfEncounters())
        return view
    }
}