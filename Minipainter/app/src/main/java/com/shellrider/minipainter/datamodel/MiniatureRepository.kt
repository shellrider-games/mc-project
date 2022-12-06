package com.shellrider.minipainter.datamodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MiniatureRepository(private val miniatureDao: MiniatureDao) {
    val miniatures = miniatureDao.getAllMiniatures()
}