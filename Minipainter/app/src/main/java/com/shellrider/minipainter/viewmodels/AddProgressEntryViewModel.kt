package com.shellrider.minipainter.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shellrider.minipainter.datamodel.*
import java.util.*

class AddProgressEntryViewModel(application: Application) : AndroidViewModel(application) {

    private val _cachedImagePath = MutableLiveData<String>()
    val cachedImagePath: LiveData<String>
        get() = _cachedImagePath

    private val _description = MutableLiveData<String>()
    val description: LiveData<String>
        get() = _description

    private var miniatureRepository: MiniatureRepository
    private var imageRepository: ImageRepository
    private var progressEntryRepository: ProgressEntryRepository

    fun setCachedImagePath(cachedImagePath: String) {
        _cachedImagePath.value = cachedImagePath
    }

    fun setDescription(description: String) {
        _description.value = description
    }

    init {
        val db = MiniatureRoomDatabase.getDatabase(application)
        val miniatureDao = db.miniatureDao()
        val imageDao = db.imageDao()
        val progressEntryDao = db.progressEntryDao()
        miniatureRepository = MiniatureRepository(miniatureDao)
        imageRepository = ImageRepository(imageDao)
        progressEntryRepository = ProgressEntryRepository(progressEntryDao)
    }

    fun saveEntry(uuid: String, miniatureId: Int) {
        val newImage = Image(0, uuid)
        val imageId = imageRepository.insertImage(newImage)
        progressEntryRepository.insertProgressEntry(
            ProgressEntry(0, imageId.toInt(), miniatureId, _description.value, Date())
        )
    }

}