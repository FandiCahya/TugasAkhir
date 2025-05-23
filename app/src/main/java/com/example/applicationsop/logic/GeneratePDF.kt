package com.example.applicationsop.logic

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
    jenis: String,
    rencanaAnggaran: String,
    masalah: String,
    output: String,
    tanggalMulai: String,
    tanggalSelesai: String,
    tahap: String,
    keterangan: String,
    perangkatLunak: String,
    versiPerangkat: String,
    tujuanPengujian: String,
    metodePengujian: String,
    status: String
    // detailPersetujuan: List<PersetujuanPengujianDetail> // Jika ini tidak lagi digunakan, bisa dihapus
) {
    val pageHeight = 1120
    val pageWidth = 792
    val pdfDocument = PdfDocument()
    val paint = Paint() // Untuk garis, border, dan fill
    val textPaint = Paint(Paint.ANTI_ALIAS_FLAG) // Untuk teks utama dan konten
    val headerPaint = Paint(Paint.ANTI_ALIAS_FLAG) // Untuk teks header tabel

    val myPageInfo = PageInfo.Builder(pageWidth, pageHeight, 1).create()
    val myPage = pdfDocument.startPage(myPageInfo)
    val canvas: Canvas = myPage.canvas

    // Set padding dan margin
    val leftMargin = 80f
    val rightMargin = pageWidth - 80f
    var currentY = 70f // Posisi Y awal, setelah topMargin

    // Definisi Warna (Pastikan ini ada di res/values/colors.xml)
    val blackColor = ContextCompat.getColor(context, R.color.black)
    val greyHeaderColor = ContextCompat.getColor(context, R.color.material_grey_300)
    // val lightGreyColor = ContextCompat.getColor(context, R.color.material_grey_100) // Jika ingin alternating row colors

    // Pengaturan global untuk Paint
    textPaint.color = blackColor
    headerPaint.color = blackColor

    // ---------- START KOP SURAT ----------
    // 1. Logo
    val logoBitmap: Bitmap? = BitmapFactory.decodeResource(context.resources, R.drawable.lifemedia_logo)

    if (logoBitmap != null) {
        val logoWidth = 100 // Lebar logo yang diinginkan
        val logoHeight = (logoBitmap.height.toFloat() / logoBitmap.width.toFloat() * logoWidth).toInt()
        val scaledLogo = Bitmap.createScaledBitmap(logoBitmap, logoWidth, logoHeight, false)
        canvas.drawBitmap(scaledLogo, leftMargin, currentY, paint)
        currentY += logoHeight + 10f
        scaledLogo.recycle()
    } else {
        Toast.makeText(context, "Logo tidak ditemukan! Pastikan lifemedia_logo.png ada di res/drawable.", Toast.LENGTH_SHORT).show()
    }

    // 2. Nama Perusahaan
    textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    textPaint.textSize = 20f
    val companyName = "PT. SaranaInsan MudaSelaras"
    canvas.drawText(companyName, pageWidth / 2f - textPaint.measureText(companyName) / 2, currentY, textPaint)
    currentY += 25f

    // 3. Alamat Perusahaan
    textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
    textPaint.textSize = 12f
    val address1 = "Jl. Parangtritis No.97, Brontokusuman, Kec. Mergangsan, Kota Yogyakarta,"
    val address2 = "Daerah Istimewa Yogyakarta 55153"
    val contact = "Telp: (+62) 2746055655 | Email: cs@lifemedia.id"

    canvas.drawText(address1, pageWidth / 2f - textPaint.measureText(address1) / 2, currentY, textPaint)
    currentY += 15f
    canvas.drawText(address2, pageWidth / 2f - textPaint.measureText(address2) / 2, currentY, textPaint)
    currentY += 15f
    canvas.drawText(contact, pageWidth / 2f - textPaint.measureText(contact) / 2, currentY, textPaint)
    currentY += 30f

    // 4. Garis Pembatas (Divider)
    paint.strokeWidth = 2f
    paint.color = blackColor // Pastikan warna garis hitam
    canvas.drawLine(leftMargin, currentY, rightMargin, currentY, paint)
    currentY += 40f

    // ---------- END KOP SURAT ----------


    // Title Laporan Utama
    textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    textPaint.textSize = 28f
    val reportTitle = "Laporan Permohonan Perangkat Lunak"
    canvas.drawText(reportTitle, (pageWidth - textPaint.measureText(reportTitle)) / 2, currentY, textPaint)
    currentY += 70f

    // ==============================================================================================
    // Helper function untuk menggambar tabel (didefinisikan di dalam generatePDF agar bisa akses params)
    // ==============================================================================================
    fun drawInfoTable(
        canvas: Canvas,
        currentYRef: FloatArray, // Menggunakan array untuk pass by reference
        title: String,
        data: List<Pair<String, String>>,
        tableLeft: Float,
        tableWidth: Float,
        col1Width: Float,
        rowHeight: Float,
        headerColor: Int,
        contentColor: Int,
        borderColor: Int,
        headerTextSize: Float,
        contentTextSize: Float
    ) {
        var y = currentYRef[0]

        // Gambar Header Tabel
        paint.style = Paint.Style.FILL
        paint.color = headerColor
        canvas.drawRect(tableLeft, y, tableLeft + tableWidth, y + rowHeight, paint)

        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2f
        paint.color = borderColor
        canvas.drawRect(tableLeft, y, tableLeft + tableWidth, y + rowHeight, paint)

        headerPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        headerPaint.textSize = headerTextSize
        headerPaint.color = contentColor // Warna teks header
        canvas.drawText(title, tableLeft + (tableWidth / 2) - (headerPaint.measureText(title) / 2), y + (rowHeight / 2) + (headerTextSize / 3), headerPaint)
        y += rowHeight

        // Gambar setiap baris data
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        textPaint.textSize = contentTextSize
        textPaint.color = contentColor

        val col2Width = tableWidth - col1Width

        for (i in data.indices) {
            val (label, value) = data[i]

            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 1f
            paint.color = borderColor
            canvas.drawRect(tableLeft, y, tableLeft + tableWidth, y + rowHeight, paint)
            canvas.drawLine(tableLeft + col1Width, y, tableLeft + col1Width, y + rowHeight, paint)

            canvas.drawText(label, tableLeft + 10f, y + (rowHeight / 2) + (contentTextSize / 3), textPaint)

            val valueText = ": $value"
            val measuredTextWidth = textPaint.measureText(valueText)
            if (measuredTextWidth > col2Width - 20f) {
                val chars = textPaint.breakText(valueText, true, col2Width - 20f, null)
                val truncatedText = valueText.substring(0, chars) + "..."
                canvas.drawText(truncatedText, tableLeft + col1Width + 10f, y + (rowHeight / 2) + (contentTextSize / 3), textPaint)
            } else {
                canvas.drawText(valueText, tableLeft + col1Width + 10f, y + (rowHeight / 2) + (contentTextSize / 3), textPaint)
            }
            y += rowHeight
        }
        currentYRef[0] = y // Update currentY di scope luar
    }

    // Mendeklarasikan parameter umum untuk tabel
    val tableLeft = leftMargin
    val tableWidth = pageWidth - (2 * leftMargin)
    val col1Width = 200f
    val rowHeight = 40f
    val headerTextSize = 16f
    val contentTextSize = 14f
    val currentYArr = floatArrayOf(currentY) // Array untuk pass by reference

    // ==============================================================================================
    // Informasi Pengajuan (Dalam bentuk Tabel)
    // ==============================================================================================
    val dataPengajuan = listOf(
        "Tanggal" to tanggalMulai,
        "Nama Sistem" to namaSistem,
        "Jenis" to jenis,
        "Rencana Anggaran" to rencanaAnggaran,
        "Masalah" to masalah,
        "Output" to output
    )
    drawInfoTable(
        canvas = canvas,
        currentYRef = currentYArr,
        title = "Informasi Pengajuan",
        data = dataPengajuan,
        tableLeft = tableLeft,
        tableWidth = tableWidth,
        col1Width = col1Width,
        rowHeight = rowHeight,
        headerColor = greyHeaderColor,
        contentColor = blackColor,
        borderColor = blackColor,
        headerTextSize = headerTextSize,
        contentTextSize = contentTextSize
    )
    currentY = currentYArr[0] + 30f // Spasi setelah tabel

    // ==============================================================================================
    // Informasi Pengembangan (Dalam bentuk Tabel)
    // ==============================================================================================
    currentYArr[0] = currentY // Update posisi Y untuk tabel baru
    val dataPengembangan = listOf(
        "Tanggal Mulai" to tanggalMulai, // Menggunakan tanggalMulai
        "Tanggal Selesai" to tanggalSelesai,
        "Tahap" to tahap,
        "Keterangan" to keterangan
    )
    drawInfoTable(
        canvas = canvas,
        currentYRef = currentYArr,
        title = "Informasi Pengembangan",
        data = dataPengembangan,
        tableLeft = tableLeft,
        tableWidth = tableWidth,
        col1Width = col1Width,
        rowHeight = rowHeight,
        headerColor = greyHeaderColor,
        contentColor = blackColor,
        borderColor = blackColor,
        headerTextSize = headerTextSize,
        contentTextSize = contentTextSize
    )
    currentY = currentYArr[0] + 30f // Spasi setelah tabel

    // ==============================================================================================
    // Informasi Pengujian (Dalam bentuk Tabel)
    // ==============================================================================================
    currentYArr[0] = currentY // Update posisi Y untuk tabel baru
    val dataPengujian = listOf(
        "Tanggal" to tanggalSelesai, // Menggunakan tanggalSelesai
        "Perangkat Lunak" to perangkatLunak,
        "Versi" to versiPerangkat,
        "Tujuan" to tujuanPengujian,
        "Metode" to metodePengujian,
        "Status" to status
    )
    drawInfoTable(
        canvas = canvas,
        currentYRef = currentYArr,
        title = "Informasi Pengujian",
        data = dataPengujian,
        tableLeft = tableLeft,
        tableWidth = tableWidth,
        col1Width = col1Width,
        rowHeight = rowHeight,
        headerColor = greyHeaderColor,
        contentColor = blackColor,
        borderColor = blackColor,
        headerTextSize = headerTextSize,
        contentTextSize = contentTextSize
    )
    currentY = currentYArr[0] + 30f // Spasi setelah tabel

    // ==============================================================================================
    // Bagian Approval Tanda Tangan (Dihapus sesuai permintaan)
    // ==============================================================================================
    // Kode untuk "Approval Tanda Tangan" dan tabelnya dihapus di sini

    // Finish the page
    pdfDocument.finishPage(myPage)

    // Save to file
    val file = File(directory.path, "Laporan_${id}.pdf")
    try {
        pdfDocument.writeTo(FileOutputStream(file))
        Toast.makeText(context, "PDF file generated successfully", Toast.LENGTH_SHORT).show()
    } catch (ex: IOException) {
        ex.printStackTrace()
        Toast.makeText(context, "Error generating PDF: ${ex.message}", Toast.LENGTH_LONG).show()
    }
    pdfDocument.close()
}
