package com.shellrider.minipainter.datamodel

import kotlinx.coroutines.runBlocking

class ImageRepository(private val imageDao: ImageDao) {

    fun insertImage(image: Image): Long {
        return runBlocking {
            imageDao.insert(image)
        }
    }
}