package com.example.projectp2.util

import android.content.Context
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

fun <T> saveObjectList(context: Context, list: List<T>, fileName: String) {
    try {
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use { fileOut ->
            ObjectOutputStream(fileOut).use { objOut ->
                objOut.writeObject(list as Serializable)
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

fun <T : Any> loadObjectList(context: Context, fileName: String): ArrayList<T>? {
    return try {
        context.openFileInput(fileName).use { fileIn ->
            ObjectInputStream(fileIn).use { objIn ->
                @Suppress("UNCHECKED_CAST")
                objIn.readObject() as? ArrayList<T>
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}