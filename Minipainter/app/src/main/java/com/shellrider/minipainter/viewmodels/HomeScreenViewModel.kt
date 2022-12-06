package com.shellrider.minipainter.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.shellrider.minipainter.datamodel.Miniature
import com.shellrider.minipainter.datamodel.MiniatureRepository
import com.shellrider.minipainter.datamodel.MiniatureRoomDatabase

class HomeScreenViewModel(application: Application) : AndroidViewModel(application) {
    val miniatures: LiveData<List<Miniature>>

    init {
        val db = MiniatureRoomDatabase.getDatabase(application)
        val miniatureDao = db.miniatureDao()
        val repository = MiniatureRepository(miniatureDao)

        miniatures = repository.miniatures
    }
}