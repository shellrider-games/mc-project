package com.shellrider.minipainter.datamodel

import androidx.lifecycle.LiveData
import kotlinx.coroutines.runBlocking

class MiniatureRepository(private val miniatureDao: MiniatureDao) {
    val miniatures = miniatureDao.getAllMiniatures()
    val miniaturesWithPrimaryImages = miniatureDao.getAllMiniaturesWithPrimaryImages()
    val miniatureWithLatestProgress = miniatureDao.getMiniaturesWithNewestEntry()


    fun insertMiniature(miniature: Miniature): Long {
        return runBlocking {
            miniatureDao.insertMiniature(miniature)
        }
    }

    fun getMiniature(id: Int): LiveData<MiniatureWithPrimaryImage> {
        return miniatureDao.getMiniature(id.toString())
    }

    fun getMiniatureWithProgress(id: Int): LiveData<MiniatureWithProgress> {
        return miniatureDao.getMiniatureWithProgress(id.toString())
    }

    fun deleteMiniature(miniature: Miniature) {
        runBlocking {
            miniatureDao.deleteMiniature(miniature)
        }
    }

    fun updateMiniature(miniature: Miniature) {
        runBlocking {
            miniatureDao.updateMiniature(miniature)
        }
    }

}