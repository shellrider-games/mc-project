package com.shellrider.demonstrator2

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CreateEncounterForm : Fragment() {
    private var mandatoryFields = mutableListOf<TextView>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val encounterTableHandler = (activity as MainActivity).getEncounterTableHandler()
        val view = inflater.inflate(R.layout.fragment_create_encounter_form, container, false)
        val fab = view.findViewById<FloatingActionButton>(R.id.doneFAB)
        mandatoryFields.add(view.findViewById(R.id.editEncountername))
        mandatoryFields.add(view.findViewById(R.id.editEncounterdescription))
        for(field in mandatoryFields){
            field.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {
                    fab.isEnabled = allMandatoryFilled()
                }
            })
        }
        fab.setOnClickListener {
            if(allMandatoryFilled()) {
                encounterTableHandler.addEncounter(
                    view.findViewById<TextView>(R.id.editEncountername).text.toString(),
                    view.findViewById<TextView>(R.id.editEncounterdescription).text.toString()
                )
                findNavController().navigate(R.id.homeFragment)
            }
        }
        return view
    }

    private fun allMandatoryFilled(): Boolean {
        var retVal = true
        for (field in mandatoryFields) {
            if (field.text.isEmpty()) retVal = false
        }
        return retVal
    }
}