package com.shellrider.minipainter.extensions

import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface

fun Bitmap.rotate(degrees: Float): Bitmap {
    val matrix = Matrix().apply { postRotate(degrees) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

fun Bitmap.rotateAccordingToExifInterface(exifInterface: ExifInterface): Bitmap {
    val orientation = exifInterface.getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_UNDEFINED
    )
    val matrix = rotationMatrix(orientation)
    return Bitmap.createBitmap(this,0,0,width,height,matrix,true)
}

fun rotationMatrix(exifOrientation: Int) : Matrix {
    val matrix = Matrix()
    when(exifOrientation) {
        ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.setScale(-1f,1f)
        ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
        ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
            matrix.setRotate(180f)
            matrix.postScale(-1f,1f)
        }
        ExifInterface.ORIENTATION_TRANSPOSE -> {
            matrix.setRotate(90f)
            matrix.postScale(-1f, 1f)
        }
        ExifInterface.ORIENTATION_ROTATE_90 -> {
            matrix.setRotate(90f)
        }
        ExifInterface.ORIENTATION_TRANSVERSE -> {
            matrix.setRotate(-90f)
            matrix.postScale(-1f, 1f)
        }
        ExifInterface.ORIENTATION_ROTATE_270 -> {
            matrix.setRotate(-90f)
        }
    }
    return matrix
}