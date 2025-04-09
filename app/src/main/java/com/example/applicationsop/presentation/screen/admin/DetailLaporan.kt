package com.example.applicationsop.presentation.screen.admin

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.applicationsop.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun generatePDF(
    context: Context,
    directory: File,
    id: String,
    namaSistem: String,
    versiPerangkat: String,
    tujuanPengujian: String,
    metodePengujian: String,
    tanggalPengujian: String,
    pelaksanaPengujian: String,
    status: String
) {
    val pageHeight = 1120
    val pageWidth = 792
    val pdfDocument = PdfDocument()
    val paint = Paint()
    val title = Paint()
    val myPageInfo = PageInfo.Builder(pageWidth, pageHeight, 1).create()
    val myPage = pdfDocument.startPage(myPageInfo)
    val canvas: Canvas = myPage.canvas

    // Title for PDF document
    title.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    title.textSize = 20f
    title.color = ContextCompat.getColor(context, R.color.purple_200)
    canvas.drawText("Riwayat Pengujian", 220f, 80f, title)

    // Content for the PDF
    title.textSize = 15f
    title.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)

    val lineSpacing = 40f
    var currentY = 150f

    // Add all the content from the popup to the PDF
    canvas.drawText("ID: $id", 40f, currentY, title)
    currentY += lineSpacing
    canvas.drawText("Nama Sistem: $namaSistem", 40f, currentY, title)
    currentY += lineSpacing
    canvas.drawText("Versi Perangkat: $versiPerangkat", 40f, currentY, title)
    currentY += lineSpacing
    canvas.drawText("Tujuan Pengujian: $tujuanPengujian", 40f, currentY, title)
    currentY += lineSpacing
    canvas.drawText("Metode Pengujian: $metodePengujian", 40f, currentY, title)
    currentY += lineSpacing
    canvas.drawText("Tanggal Pengujian: $tanggalPengujian", 40f, currentY, title)
    currentY += lineSpacing
    canvas.drawText("Pelaksana Pengujian: $pelaksanaPengujian", 40f, currentY, title)
    currentY += lineSpacing
    canvas.drawText("Status: $status", 40f, currentY, title)

    // Finish the page
    pdfDocument.finishPage(myPage)
    val file = File(directory, "riwayat_pengujian_$id.pdf")

    try {
        pdfDocument.writeTo(FileOutputStream(file))
        Toast.makeText(context, "PDF file generated successfully", Toast.LENGTH_SHORT).show()
    } catch (ex: IOException) {
        ex.printStackTrace()
        Toast.makeText(context, "Error generating PDF", Toast.LENGTH_SHORT).show()
    }
    pdfDocument.close()
}
