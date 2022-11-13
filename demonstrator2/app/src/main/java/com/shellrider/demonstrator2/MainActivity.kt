package com.shellrider.demonstrator2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import com.shellrider.demonstrator2.adapter.EncounterAdapter
import com.shellrider.demonstrator2.datasource.EncounterTableHandler

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private var encounterTableHandler: EncounterTableHandler = EncounterTableHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment: NavHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

    }

    public fun getEncounterTableHandler() : EncounterTableHandler {
        return encounterTableHandler
    }
}