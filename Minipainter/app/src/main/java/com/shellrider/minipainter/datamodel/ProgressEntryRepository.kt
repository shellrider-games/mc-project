package com.shellrider.minipainter.datamodel

import kotlinx.coroutines.runBlocking

class ProgressEntryRepository(private val progressEntryDao: ProgressEntryDao) {
    fun insertProgressEntry(progressEntry: ProgressEntry): Long {
        return runBlocking {
            progressEntryDao.insert(progressEntry)
        }
    }
}