package com.shellrider.minipainter.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.shellrider.minipainter.datamodel.*

class HomeScreenViewModel(application: Application) : AndroidViewModel(application) {
    val miniaturesWithPrimaryImage: LiveData<List<MiniatureWithPrimaryImage>>
    val miniatureWithLatestProgress: LiveData<List<MiniatureWithLatestProgress>>
    lateinit var imageRepository: ImageRepository

    init {
        val db = MiniatureRoomDatabase.getDatabase(application)
        val miniatureDao = db.miniatureDao()
        val imageDao = db.imageDao()
        val repository = MiniatureRepository(miniatureDao)
        miniaturesWithPrimaryImage = repository.miniaturesWithPrimaryImages
        miniatureWithLatestProgress = repository.miniatureWithLatestProgress
        imageRepository = ImageRepository(imageDao)
    }

    fun getImageFilenameFromId(id: Int): LiveData<Image>{
        return imageRepository.getImage(id)
    }
}