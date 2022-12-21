package com.shellrider.minipainter.filesystem

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.util.*

const val LOG_TAG = "MinipainterFilesystem"
const val ALBUM_NAME = "minipainter"

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

fun getAppSpecificAlbumStorageDir(context: Context, albumName: String): File {
    // Get the pictures directory that's inside the app-specific directory on
    // external storage.
    val file = File(
        context.getExternalFilesDir(
            Environment.DIRECTORY_PICTURES
        ), albumName
    )
    if (!file.mkdirs()) {
        Log.e(
            LOG_TAG,
            "Directory not created, check if directory already exists. If directory already exists this is not a problem."
        )
    }
    return file
}

fun writeImageFileToStorage(context: Context, file: File) {
    if (isExternalStorageWritable()) {
        val dir = getAppSpecificAlbumStorageDir(context, ALBUM_NAME)
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

fun writeBitmapToStorage(
    context: Context,
    bitmap: Bitmap,
    filename: String = UUID.randomUUID().toString()
) {
    if (isExternalStorageWritable()) {
        val dir = getAppSpecificAlbumStorageDir(context, ALBUM_NAME)
        val outputFile = File(dir, "$filename.png")
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, FileOutputStream(outputFile))
        } catch (ex: Exception) {
            Log.e(LOG_TAG, "Error while writing $filename.png to external storage", ex)
        }
    } else throw Exception("External Storage not writeable!")
}

fun writeBitmapToCache(
    context: Context,
    bitmap: Bitmap
): String? {
    val file = File(context.cacheDir, UUID.randomUUID().toString() + ".png")
    try {
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, FileOutputStream(file))
        return file.absolutePath
    } catch (ex: Exception) {
        Log.e(LOG_TAG, "Error while saving bitmap to Cache", ex)
    }
    return null
}

fun imageNameToPath(context: Context, imageName: String): String {
    return "${getAppSpecificAlbumStorageDir(context, ALBUM_NAME)}/$imageName.png"
}