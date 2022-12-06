package com.shellrider.minipainter.filesystem

import android.content.Context
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileOutputStream

const val LOG_TAG = "MinipainterFilesystem"

/* Checks if external storage is available for read and write */
fun isExternalStorageWritable(): Boolean {
    val state = Environment.getExternalStorageState()
    return Environment.MEDIA_MOUNTED == state
}

/* Checks if external storage is available to at least read */
fun isExternalStorageReadable(): Boolean {
    val state = Environment.getExternalStorageState()
    return Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state
}

fun getAppSpecificAlbumStorageDir(context: Context, albumName: String): File? {
    // Get the pictures directory that's inside the app-specific directory on
    // external storage.
    val file = File(context.getExternalFilesDir(
        Environment.DIRECTORY_PICTURES), albumName)
    if (!file?.mkdirs()) {
        Log.e(LOG_TAG, "Directory not created, check if directory already exists. If directory already exists this is not a problem.")
    }
    return file
}

fun writeImageToStorage(context: Context, file: File){
    if(isExternalStorageWritable()) {
        val dir = getAppSpecificAlbumStorageDir(context, "minipainter")
        val writeFile = File(dir, file.name)
        try {
            val fileOutputStream = FileOutputStream(writeFile)
            fileOutputStream.write(file.readBytes())
            fileOutputStream.close()
            Log.d(LOG_TAG, "Wrote ${writeFile.path}")
        } catch (ex: Exception) {
            Log.e(LOG_TAG, "Error while writing file to external storage.", ex)
        }
    }
}