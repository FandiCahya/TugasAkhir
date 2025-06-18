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
import com.example.applicationsop.R // Pastikan R diimport dengan benar
import com.example.applicationsop.core.urlSignature
import com.example.applicationsop.models.PersetujuanPengujianDetail
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlinx.coroutines.*

fun generatePDF(
    context: Context,
    directory: File, // Direktori tempat menyimpan file PDF
    id: String,
    namaSistem: String,
    jenis: String,
    rencanaAnggaran: String,
    masalah: String,
    output: String,
    tanggalMulai: String, // Pastikan tidak null jika digunakan langsung
    tanggalSelesai: String, // Pastikan tidak null
    tahap: String, // Pastikan tidak null
    keterangan: String, // Pastikan tidak null
    perangkatLunak: String, // Pastikan tidak null
    versiPerangkat: String, // Pastikan tidak null
    tujuanPengujian: String, // Pastikan tidak null
    metodePengujian: String, // Pastikan tidak null
    status: String, // Pastikan tidak null
    detailPersetujuan: List<PersetujuanPengujianDetail> // Jika tidak digunakan, bisa dihapus
) {
    val pageHeight = 1120
    val pageWidth = 792

    for ((index, item) in detailPersetujuan.withIndex()) {
        println("Detail Persetujuan #${index + 1}")
        println("nama    : ${item.user.name}")
        println("Status      : ${item.status}")
        println("signature     : ${item.signature}")
        println("Catatan     : ${item.catatan}")
        println("-----------------------------")
    }

    val pdfDocument = PdfDocument()
    val paint = Paint() // Untuk garis, border, dan fill
    val textPaint = Paint(Paint.ANTI_ALIAS_FLAG) // Untuk teks utama dan konten
    val headerPaint = Paint(Paint.ANTI_ALIAS_FLAG) // Untuk teks header tabel

    // Definisi Margin Global (sesuaikan jika perlu)
    val G_TOP_MARGIN = 60f
    val G_BOTTOM_MARGIN = 60f
    // val G_PAGE_CONTENT_HEIGHT = pageHeight - G_TOP_MARGIN - G_BOTTOM_MARGIN // Bisa digunakan untuk perhitungan

    // Margin untuk konten di dalam halaman (setelah kop surat / di halaman baru)
    val leftMargin = 70f // Menggunakan nilai dari kode asli Anda, bisa disesuaikan
    val rightMargin = pageWidth - 70f

    // Variabel halaman dan kanvas yang dapat diubah
    var pageNumber = 1
    var myPageInfo = PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
    var myPage = pdfDocument.startPage(myPageInfo)
    var canvas: Canvas = myPage.canvas

    // currentY akan menjadi variabel utama yang dilacak dan direset per halaman
    var currentY = G_TOP_MARGIN // Y awal untuk konten di halaman (atau setelah kop surat)

    // Definisi Warna
    val blackColor = ContextCompat.getColor(context, R.color.black)
    val greyHeaderColor = ContextCompat.getColor(context, R.color.material_grey_300)

    // Pengaturan global untuk Paint
    textPaint.color = blackColor
    headerPaint.color = blackColor // Untuk teks di header tabel

    // Helper function untuk memulai halaman baru
    fun startNewPage() {
        pdfDocument.finishPage(myPage)
        pageNumber++
        myPageInfo = PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
        myPage = pdfDocument.startPage(myPageInfo)
        canvas = myPage.canvas
        currentY = G_TOP_MARGIN // Reset Y ke margin atas untuk halaman baru
        // Di sini Anda bisa menambahkan header/footer yang berulang di setiap halaman baru jika perlu
        // Misalnya: canvas.drawText("Halaman $pageNumber", pageWidth - 100f, G_TOP_MARGIN - 20f, textPaint)
    }

    // ---------- START KOP SURAT (HANYA HALAMAN PERTAMA) ----------
    var kopSuratCurrentY = G_TOP_MARGIN // Y sementara untuk menggambar kop surat
    // 1. Logo
    val logoBitmap: Bitmap? = BitmapFactory.decodeResource(context.resources, R.drawable.lifemedia_logo)
    if (logoBitmap != null) {
        val logoWidth = 100
        val logoHeight = (logoBitmap.height.toFloat() / logoBitmap.width.toFloat() * logoWidth).toInt()
        val scaledLogo = Bitmap.createScaledBitmap(logoBitmap, logoWidth, logoHeight, false)
        canvas.drawBitmap(scaledLogo, leftMargin, kopSuratCurrentY, paint) // Gunakan paint biasa untuk bitmap
        kopSuratCurrentY += logoHeight + 10f
        // scaledLogo.recycle() // Jangan recycle di sini jika bitmap masih akan digunakan atau jika ini satu-satunya instance
    } else {
        Toast.makeText(context, "Logo tidak ditemukan!", Toast.LENGTH_SHORT).show()
        kopSuratCurrentY += 50f // Beri ruang jika logo tidak ada
    }

    // 2. Nama Perusahaan
    val tempTextPaintForKop = Paint(textPaint) // Buat salinan agar tidak mengubah textPaint global
    tempTextPaintForKop.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    tempTextPaintForKop.textSize = 20f
    val companyName = "PT. SaranaInsan MudaSelaras"
    canvas.drawText(companyName, pageWidth / 2f - tempTextPaintForKop.measureText(companyName) / 2, kopSuratCurrentY, tempTextPaintForKop)
    kopSuratCurrentY += 25f

    // 3. Alamat Perusahaan
    tempTextPaintForKop.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
    tempTextPaintForKop.textSize = 12f
    val address1 = "Jl. Parangtritis No.97, Brontokusuman, Kec. Mergangsan, Kota Yogyakarta,"
    val address2 = "Daerah Istimewa Yogyakarta 55153"
    val contact = "Telp: (+62) 2746055655 | Email: cs@lifemedia.id"
    canvas.drawText(address1, pageWidth / 2f - tempTextPaintForKop.measureText(address1) / 2, kopSuratCurrentY, tempTextPaintForKop)
    kopSuratCurrentY += 15f
    canvas.drawText(address2, pageWidth / 2f - tempTextPaintForKop.measureText(address2) / 2, kopSuratCurrentY, tempTextPaintForKop)
    kopSuratCurrentY += 15f
    canvas.drawText(contact, pageWidth / 2f - tempTextPaintForKop.measureText(contact) / 2, kopSuratCurrentY, tempTextPaintForKop)
    kopSuratCurrentY += 30f

    // 4. Garis Pembatas (Divider)
    paint.strokeWidth = 2f
    paint.color = blackColor
    canvas.drawLine(leftMargin, kopSuratCurrentY, rightMargin, kopSuratCurrentY, paint)
    kopSuratCurrentY += 30f // Spasi setelah garis

    currentY = kopSuratCurrentY // Set currentY utama setelah kop surat selesai digambar
    // ---------- END KOP SURAT ----------


    // Title Laporan Utama
    val reportTitleTextPaint = Paint(textPaint) // Salinan untuk judul
    reportTitleTextPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    reportTitleTextPaint.textSize = 24f // Sedikit lebih kecil dari sebelumnya agar muat
    val reportTitle = "Laporan Permohonan Perangkat Lunak"
    val reportTitleHeight = reportTitleTextPaint.descent() - reportTitleTextPaint.ascent() // Perkiraan tinggi teks

    if (currentY + reportTitleHeight > pageHeight - G_BOTTOM_MARGIN) {
        startNewPage()
    }
    canvas.drawText(reportTitle, (pageWidth - reportTitleTextPaint.measureText(reportTitle)) / 2, currentY + reportTitleHeight/2 , reportTitleTextPaint) // Penyesuaian Y untuk drawText
    currentY += reportTitleHeight + 40f // Spasi setelah judul utama


    // ==============================================================================================
    // Helper function untuk menggambar tabel (MODIFIKASI UNTUK PAGINATION)
    // ==============================================================================================
    fun drawInfoTable(
        title: String,
        data: List<Pair<String, String>>,
        tableLeft: Float,
        tableWidth: Float,
        col1WidthRatio: Float, // Gunakan rasio untuk fleksibilitas
        rowHeight: Float,
        headerColor: Int,
        contentColor: Int, // Untuk teks di dalam tabel
        borderColor: Int,
        headerTextSize: Float,
        contentTextSize: Float
    ) {
        val col1Width = tableWidth * col1WidthRatio
        val col2Width = tableWidth * (1 - col1WidthRatio)

        val actualHeaderTextPaint = Paint(headerPaint) // Gunakan salinan untuk modifikasi lokal
        actualHeaderTextPaint.textSize = headerTextSize
        actualHeaderTextPaint.color = contentColor // Teks header biasanya sama dengan content color

        val actualContentTextPaint = Paint(textPaint)
        actualContentTextPaint.textSize = contentTextSize
        actualContentTextPaint.color = contentColor

        // Cek apakah header tabel muat
        if (currentY + rowHeight > pageHeight - G_BOTTOM_MARGIN) {
            startNewPage()
        }

        // Gambar Header Tabel
        paint.style = Paint.Style.FILL
        paint.color = headerColor
        canvas.drawRect(tableLeft, currentY, tableLeft + tableWidth, currentY + rowHeight, paint)

        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2f
        paint.color = borderColor
        canvas.drawRect(tableLeft, currentY, tableLeft + tableWidth, currentY + rowHeight, paint) // Border header

        val titleTextY = currentY + (rowHeight / 2) + (actualHeaderTextPaint.descent() - actualHeaderTextPaint.ascent()) / 2 - actualHeaderTextPaint.descent()
        canvas.drawText(title, tableLeft + (tableWidth / 2) - (actualHeaderTextPaint.measureText(title) / 2), titleTextY, actualHeaderTextPaint)
        currentY += rowHeight

        // Gambar setiap baris data
        for ((label, value) in data) {
            // Cek apakah baris data muat
            if (currentY + rowHeight > pageHeight - G_BOTTOM_MARGIN) {
                startNewPage()
                // Gambar ulang header tabel di halaman baru
                paint.style = Paint.Style.FILL
                paint.color = headerColor
                canvas.drawRect(tableLeft, currentY, tableLeft + tableWidth, currentY + rowHeight, paint)
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 2f
                paint.color = borderColor
                canvas.drawRect(tableLeft, currentY, tableLeft + tableWidth, currentY + rowHeight, paint)
                canvas.drawText(title, tableLeft + (tableWidth / 2) - (actualHeaderTextPaint.measureText(title) / 2), titleTextY.let { it - (currentY - G_TOP_MARGIN) + currentY }, actualHeaderTextPaint) // Y disesuaikan
                currentY += rowHeight
            }

            // Gambar baris data
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 1f
            paint.color = borderColor
            // Border luar baris
            canvas.drawRect(tableLeft, currentY, tableLeft + tableWidth, currentY + rowHeight, paint)
            // Garis vertikal pemisah kolom
            canvas.drawLine(tableLeft + col1Width, currentY, tableLeft + col1Width, currentY + rowHeight, paint)

            val labelTextY = currentY + (rowHeight / 2) + (actualContentTextPaint.descent() - actualContentTextPaint.ascent()) / 2 - actualContentTextPaint.descent()
            canvas.drawText(label, tableLeft + 10f, labelTextY, actualContentTextPaint)

            val valueText = ": $value"
            val valueTextX = tableLeft + col1Width + 10f
            val availableWidthForValue = col2Width - 20f // 10f padding kiri dan kanan

            val measuredValueWidth = actualContentTextPaint.measureText(valueText)
            if (measuredValueWidth > availableWidthForValue) {
                val chars = actualContentTextPaint.breakText(valueText, true, availableWidthForValue, null)
                val truncatedText = valueText.substring(0, chars) + "..."
                canvas.drawText(truncatedText, valueTextX, labelTextY, actualContentTextPaint)
            } else {
                canvas.drawText(valueText, valueTextX, labelTextY, actualContentTextPaint)
            }
            currentY += rowHeight
        }
    }

    // Mendeklarasikan parameter umum untuk tabel
    val tableLeft = leftMargin
    val tableWidth = pageWidth - (2 * leftMargin)
    val col1WidthRatio = 0.35f // Lebar kolom pertama 35% dari total lebar tabel
    val rowHeight = 35f // Sedikit diperkecil agar lebih banyak muat
    val headerTextSize = 15f
    val contentTextSize = 13f

    // ==============================================================================================
    // Informasi Pengajuan
    // ==============================================================================================
    val dataPengajuan = listOf(
        "Tanggal" to tanggalMulai,
        "Nama Sistem" to namaSistem,
        "Jenis" to jenis,
        "Rencana Anggaran" to rencanaAnggaran,
        "Masalah" to masalah,
        "Output" to output
    )
    if (currentY + rowHeight > pageHeight - G_BOTTOM_MARGIN) { startNewPage() } // Cek sebelum tabel pertama
    drawInfoTable(
        title = "Informasi Pengajuan",
        data = dataPengajuan,
        tableLeft = tableLeft,
        tableWidth = tableWidth,
        col1WidthRatio = col1WidthRatio,
        rowHeight = rowHeight,
        headerColor = greyHeaderColor,
        contentColor = blackColor,
        borderColor = blackColor,
        headerTextSize = headerTextSize,
        contentTextSize = contentTextSize
    )
    currentY += 25f // Spasi setelah tabel

    // ==============================================================================================
    // Informasi Pengembangan
    // ==============================================================================================
    val dataPengembangan = listOf(
        "Tanggal Mulai" to tanggalMulai,
        "Tanggal Selesai" to tanggalSelesai,
        "Tahap" to tahap,
        "Keterangan" to keterangan
    )
    if (currentY + rowHeight > pageHeight - G_BOTTOM_MARGIN) { startNewPage() }
    drawInfoTable(
        title = "Informasi Pengembangan",
        data = dataPengembangan,
        tableLeft = tableLeft,
        tableWidth = tableWidth,
        col1WidthRatio = col1WidthRatio,
        rowHeight = rowHeight,
        headerColor = greyHeaderColor,
        contentColor = blackColor,
        borderColor = blackColor,
        headerTextSize = headerTextSize,
        contentTextSize = contentTextSize
    )
    currentY += 25f // Spasi setelah tabel

    // ==============================================================================================
    // Informasi Pengujian
    // ==============================================================================================
    val dataPengujian = listOf(
        "Tanggal" to tanggalSelesai,
        "Perangkat Lunak" to perangkatLunak,
        "Versi" to versiPerangkat,
        "Tujuan" to tujuanPengujian,
        "Metode" to metodePengujian,
        "Status" to status
    )
    if (currentY + rowHeight > pageHeight - G_BOTTOM_MARGIN) { startNewPage() }
    drawInfoTable(
        title = "Informasi Pengujian",
        data = dataPengujian,
        tableLeft = tableLeft,
        tableWidth = tableWidth,
        col1WidthRatio = col1WidthRatio,
        rowHeight = rowHeight,
        headerColor = greyHeaderColor,
        contentColor = blackColor,
        borderColor = blackColor,
        headerTextSize = headerTextSize,
        contentTextSize = contentTextSize
    )
    currentY += 25f // Spasi setelah tabel

    // ==============================================================================================
// Tampilkan Signature Persetujuan
// ==============================================================================================
    if (detailPersetujuan.isNotEmpty()) {
        for ((index, item) in detailPersetujuan.withIndex()) {
            // Cek jika mendekati batas halaman, buat halaman baru
            if (currentY + 100f > pageHeight - G_BOTTOM_MARGIN) {
                startNewPage()
            }

            val signatureUrl = "${urlSignature.BASE_URL}${item.signature}"
            println("Signature URL: $signatureUrl")

            // Tampilkan Nama Reviewer
            val reviewerLabel = "Reviewer #${index + 1}"
            canvas.drawText(reviewerLabel, leftMargin, currentY, textPaint)
            currentY += 20f

            canvas.drawText("Nama     : ${item.user.name}", leftMargin, currentY, textPaint)
            currentY += 20f
            canvas.drawText("Status   : ${item.status}", leftMargin, currentY, textPaint)
            currentY += 20f
            canvas.drawText("Catatan  : ${item.catatan ?: "-"}", leftMargin, currentY, textPaint)
            currentY += 20f

            // Gambar tanda tangan dari file atau resource
            runBlocking {
                try {
                    val bitmap = withContext(Dispatchers.IO) {
                        val inputStream = java.net.URL(signatureUrl).openStream()
                        BitmapFactory.decodeStream(inputStream)
                    }

                    if (bitmap != null) {
                        val scaledSignature = Bitmap.createScaledBitmap(bitmap, 150, 80, false)
                        canvas.drawBitmap(scaledSignature, leftMargin, currentY, paint)
                        currentY += 100f
                    } else {
                        canvas.drawText("Gagal memuat tanda tangan.", leftMargin, currentY, textPaint)
                        currentY += 30f
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    canvas.drawText("Gagal memuat tanda tangan.", leftMargin, currentY, textPaint)
                    currentY += 30f
                }
            }

            // Spasi antara reviewer
            currentY += 10f
        }
    }

    // Finish the LAST page
    pdfDocument.finishPage(myPage)

    // Save to file
    val file = File(directory, "Laporan_${id}.pdf") // Menggunakan 'directory' dari parameter
    try {
        val fos = FileOutputStream(file)
        pdfDocument.writeTo(fos)
        fos.close() // Pastikan stream ditutup
        pdfDocument.close() // Tutup dokumen PDF
        Toast.makeText(context, "PDF file generated successfully", Toast.LENGTH_SHORT).show()    } catch (ex: IOException) {
        ex.printStackTrace()
        Toast.makeText(context, "Error generating PDF: ${ex.message}", Toast.LENGTH_LONG).show()
        pdfDocument.close() // Tutup dokumen PDF jika terjadi error setelah dibuka
    }
}