package com.example.tsumaps.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

fun loadMap(context: Context, resId: Int, blockSize: Int = 6): Array<IntArray> {
    val bitmap = BitmapFactory.decodeResource(context.resources, resId)

    val width = bitmap.getWidth()
    val height = bitmap.getHeight()

    val cols = width / blockSize
    val rows = height / blockSize

    val mapArray = Array(rows) { IntArray(cols) }

    for (row in 0 until rows) {
        for (col in 0 until cols) {
            val x = col * blockSize + blockSize / 2
            val y = row * blockSize + blockSize / 2

            val pixel = bitmap.getPixel(x, y)

            val isWhite = (pixel == -1)

            if (isWhite == true) {
                mapArray[row][col] = 0
            } else {
                mapArray[row][col] = 1
            }
        }
    }

    bitmap.recycle()

    return mapArray
}
