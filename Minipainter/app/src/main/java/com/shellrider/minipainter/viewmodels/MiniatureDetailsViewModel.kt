package com.shellrider.minipainter.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.shellrider.minipainter.datamodel.Miniature
import com.shellrider.minipainter.datamodel.MiniatureRepository
import com.shellrider.minipainter.datamodel.MiniatureRoomDatabase
import com.shellrider.minipainter.datamodel.MiniatureWithPrimaryImage
import java.util.*

class MiniatureDetailsViewModel(application: Application, id: Int) : AndroidViewModel(application) {
    private lateinit var repository: MiniatureRepository

    private var _miniature: LiveData<MiniatureWithPrimaryImage>

    val miniature: LiveData<MiniatureWithPrimaryImage>
        get() = _miniature

    init {
        val db = MiniatureRoomDatabase.getDatabase(application)
        val miniatureDao = db.miniatureDao()
        repository = MiniatureRepository(miniatureDao)
        _miniature = repository.getMiniature(id)
    }

    fun deleteRequested() {
        _miniature.value?.let { repository.deleteMiniature(it.miniature) }
    }

    fun updateProgress(progress: Float){
        _miniature.value?.miniature.let {
            if (it != null) {
                repository.updateMiniature(
                    Miniature(it.id, it.name, Date(),it.primaryImageId,progress)
                )
            }
        }
    }
}