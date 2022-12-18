package com.shellrider.minipainter.datamodel

import androidx.lifecycle.LiveData
import kotlinx.coroutines.runBlocking

class ImageRepository(private val imageDao: ImageDao) {

    fun insertImage(image: Image): Long {
        return runBlocking {
            imageDao.insert(image)
        }
    }

    fun getImage(id: Int): LiveData<Image> {
        return imageDao.getImageById(id.toString())
    }
}