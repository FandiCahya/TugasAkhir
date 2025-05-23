package com.example.applicationsop.logic

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.OutputStream

fun downloadPdfToPublicDirectory(context: Context, sourceFile: File, fileName: String) {
    if (!sourceFile.exists()) {
        Toast.makeText(context, "File PDF sumber tidak ditemukan.", Toast.LENGTH_SHORT).show()
        return
    }

    var outputStream: OutputStream? = null
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { // Android 10 (API 29) ke atas
            val resolver = context.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }
            val uri: Uri? = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

            if (uri != null) {
                outputStream = resolver.openOutputStream(uri)
                if (outputStream != null) {
                    val input = FileInputStream(sourceFile)
                    input.copyTo(outputStream)
                    input.close()
                    Toast.makeText(context, "PDF berhasil diunduh ke folder Downloads", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Gagal membuka output stream untuk PDF.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Gagal membuat URI untuk PDF di MediaStore.", Toast.LENGTH_SHORT).show()
            }
        } else { // Android 9 (API 28) ke bawah
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (!downloadsDir.exists()) {
                downloadsDir.mkdirs() // Buat direktori jika belum ada
            }
            val destinationFile = File(downloadsDir, fileName)
            outputStream = FileOutputStream(destinationFile)
            val input = FileInputStream(sourceFile)
            input.copyTo(outputStream)
            input.close()
            Toast.makeText(context, "PDF berhasil diunduh ke folder Downloads", Toast.LENGTH_LONG).show()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Gagal mengunduh PDF: ${e.message}", Toast.LENGTH_LONG).show()
    } finally {
        outputStream?.close()
    }
}