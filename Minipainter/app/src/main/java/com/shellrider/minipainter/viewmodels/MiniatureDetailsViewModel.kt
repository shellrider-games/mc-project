package com.shellrider.minipainter.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.shellrider.minipainter.datamodel.MiniatureRepository
import com.shellrider.minipainter.datamodel.MiniatureRoomDatabase
import com.shellrider.minipainter.datamodel.MiniatureWithPrimaryImage

class MiniatureDetailsViewModel(application: Application, id: Int) : AndroidViewModel(application) {
    private var _miniature: LiveData<MiniatureWithPrimaryImage>

    val miniature: LiveData<MiniatureWithPrimaryImage>
        get() = _miniature

    init {
        val db = MiniatureRoomDatabase.getDatabase(application)
        val miniatureDao = db.miniatureDao()
        val repository = MiniatureRepository(miniatureDao)
        _miniature = repository.getMiniature(id)
    }
}