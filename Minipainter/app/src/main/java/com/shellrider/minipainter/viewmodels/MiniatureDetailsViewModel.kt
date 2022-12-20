package com.shellrider.minipainter.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shellrider.minipainter.datamodel.Miniature
import com.shellrider.minipainter.datamodel.MiniatureRepository
import com.shellrider.minipainter.datamodel.MiniatureRoomDatabase
import com.shellrider.minipainter.datamodel.MiniatureWithProgress
import java.util.*

class MiniatureDetailsViewModel(application: Application, id: Int) : AndroidViewModel(application) {
    private lateinit var repository: MiniatureRepository

    private var _miniature: LiveData<MiniatureWithProgress>
    val miniature: LiveData<MiniatureWithProgress>
        get() = _miniature

    private var _sliderPositionCache = MutableLiveData<Float>()
    val sliderPositionCache: LiveData<Float>
        get() = _sliderPositionCache


    init {
        val db = MiniatureRoomDatabase.getDatabase(application)
        val miniatureDao = db.miniatureDao()
        repository = MiniatureRepository(miniatureDao)
        _miniature = repository.getMiniatureWithProgress(id)
    }

    fun deleteRequested() {
        _miniature.value?.let { repository.deleteMiniature(it.miniature) }
    }

    fun updateProgress(progress: Float) {
        _miniature.value?.miniature.let {
            if (it != null) {
                repository.updateMiniature(
                    Miniature(it.id, it.name, Date(), it.primaryImageId, progress, it.description)
                )
            }
        }
    }

    fun updateDescription(description: String?) {
        _miniature.value?.miniature.let {
            if (it != null) {
                repository.updateMiniature(
                    Miniature(it.id, it.name, Date(), it.primaryImageId, it.progress, description)
                )
            }
        }
    }

    fun initSliderPositionCache(value: Float) {
        _sliderPositionCache.value = value
    }
}