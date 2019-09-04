package org.cipo.statsit

import java.util.ArrayList
import java.util.HashMap
import android.util.Log
import java.io.*

class FileList(internalFilesDirectory : File) {

    /**
     * An array of file items.
     */
    val files: MutableList<FileItem> = ArrayList()

    /**
     * A map of sample items, by ID.
     */
    val files_map: MutableMap<String, FileItem> = HashMap()

    init {
        try {
            if (!internalFilesDirectory.exists()) {
                internalFilesDirectory.mkdirs()
                Log.d("d", "\t\tMaking Dir ${internalFilesDirectory.name}")
            }
            Log.d("Files", "Path: $internalFilesDirectory")
            val files = internalFilesDirectory.listFiles()
            Log.d("Files", "Size: " + files.size)
            for (file in files) {
                addItem(
                    createFileItem(
                        file
                    )
                )
                Log.d("Files", "FileName:" + file.name)
            }
        } catch (e: Exception) {
            Log.d("d", "Something went wrong!")
        }
    }

    private fun addItem(item: FileItem) {
        files.add(item)
        files_map[item.name] = item
    }

    private fun createFileItem(file: File): FileItem {
        return FileItem(
            count(file),
            file.name
        )
    }

    /**
     * A file item representing a piece of content.
     */
    data class FileItem(val lineCount: Int, val name: String) {
        override fun toString(): String = name
    }

    @Throws(IOException::class)
    fun count(file: File): Int {
        val inputStream = BufferedInputStream(FileInputStream(file.path))
        inputStream.use {
            val c = ByteArray(1024)
            var count = 0
            var readChars = inputStream.read(c)
            var endsWithoutNewLine = false
            while ((readChars) != -1) {
                for (i in 0 until readChars) {
                    if (c[i] == '\n'.toByte())
                        ++count
                }
                endsWithoutNewLine = c[readChars - 1] != '\n'.toByte()
                readChars = inputStream.read(c)
            }
            if (endsWithoutNewLine) {
                ++count
            }
            return count
        }
    }

}