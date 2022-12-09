package com.shellrider.minipainter.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shellrider.minipainter.datamodel.*
import java.util.*


class CreateEntryViewModel(application: Application) : AndroidViewModel(application) {
    private val _cachedImagePath = MutableLiveData<String>()
    val cachedImagePath: LiveData<String>
        get() = _cachedImagePath

    private val _miniatureName = MutableLiveData<String>()
    val miniatureName: LiveData<String>
        get() = _miniatureName

    private lateinit var miniatureRepository: MiniatureRepository
    private lateinit var imageRepository: ImageRepository

    fun setCachedImagePath(cachedImagePath: String) {
        _cachedImagePath.value = cachedImagePath
    }

    fun setMiniatureName(miniatureName: String) {
        _miniatureName.value = miniatureName
    }

    fun saveEntry(uuid: String) {
        val miniatureName = _miniatureName.value
        val newImage = Image(0, uuid)
        val imageId = imageRepository.insertImage(newImage)
        val newMini = Miniature(0, miniatureName ?: "<unnamed>", Date(), imageId.toInt())
        kotlin.run { miniatureRepository.insertMiniature(newMini) }

    }

    init {
        val db = MiniatureRoomDatabase.getDatabase(application)
        val miniatureDao = db.miniatureDao()
        val imageDao = db.imageDao()
        miniatureRepository = MiniatureRepository(miniatureDao)
        imageRepository = ImageRepository(imageDao)
    }
}