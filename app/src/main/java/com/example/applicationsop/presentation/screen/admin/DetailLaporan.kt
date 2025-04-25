import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.applicationsop.R
import java.io.File
import java.io.IOException
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.example.applicationsop.ui.theme.Putih
import java.io.FileOutputStream

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

    // Set padding
    val leftMargin = 80f
    val topMargin = 70f

    var currentY = topMargin

    // Title
    title.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    title.textSize = 28f
    title.color = ContextCompat.getColor(context, R.color.black)
    val titleText1 = "Laporan Permohonan"
    val titleText2 = "Perangkat Lunak"
    canvas.drawText(titleText1, (pageWidth - title.measureText(titleText1)) / 2, currentY, title)
    currentY += 40f // Add space after title
    canvas.drawText(titleText2, (pageWidth - title.measureText(titleText2)) / 2, currentY, title)
    currentY += 70f // Add space after title

    // Setting up content text
    title.textSize = 12f
    title.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)

    val lineSpacing = 30f // space between lines

    // Pengaturan font untuk judul besar (Bold & Bigger font)
    val titleFont = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    val contentFont = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
    val titleSize = 18f
    val contentSize = 14f

    // Pengaturan margin
    val labelMargin = leftMargin
    val valueMargin = leftMargin + 150f  // Sesuaikan jarak antara label dan nilai

    // Informasi Pengajuan (Bold dan Ukuran Font Lebih Besar untuk Judul)
    val informasiPengajuanTitle = "Informasi Pengajuan"
    title.typeface = titleFont
    title.textSize = titleSize
    canvas.drawText(informasiPengajuanTitle, leftMargin, currentY, title)
    currentY += lineSpacing

    // Set font untuk konten
    title.typeface = contentFont
    title.textSize = contentSize

    // Informasi Pengajuan content (Name: value format)
    canvas.drawText("Tanggal", labelMargin, currentY, title)
    canvas.drawText(": 2025-03-14", valueMargin, currentY, title)
    currentY += lineSpacing

    canvas.drawText("Nama Sistem", labelMargin, currentY, title)
    canvas.drawText(": SIPM", valueMargin, currentY, title)
    currentY += lineSpacing

    canvas.drawText("Jenis", labelMargin, currentY, title)
    canvas.drawText(": Sistem Baru", valueMargin, currentY, title)
    currentY += lineSpacing

    canvas.drawText("Rencana Anggaran", labelMargin, currentY, title)
    canvas.drawText(": Termasuk dalam perencanaan", valueMargin, currentY, title)
    currentY += lineSpacing

    canvas.drawText("Masalah", labelMargin, currentY, title)
    canvas.drawText(": Sistem Penjadwalan Perbaikan Mesin", valueMargin, currentY, title)
    currentY += lineSpacing

    canvas.drawText("Output", labelMargin, currentY, title)
    canvas.drawText(": Aplikasi dan Web", valueMargin, currentY, title)
    currentY += lineSpacing

    currentY += 15f  // Add extra space after the subheading

    // Informasi Pengembangan (Bold dan Ukuran Font Lebih Besar untuk Judul)
    val informasiPengembanganTitle = "Informasi Pengembangan"
    title.typeface = titleFont
    title.textSize = titleSize
    canvas.drawText(informasiPengembanganTitle, leftMargin, currentY, title)
    currentY += lineSpacing

    // Set font untuk konten
    title.typeface = contentFont
    title.textSize = contentSize

    // Informasi Pengembangan content (Name: value format)
    canvas.drawText("Tanggal Mulai", labelMargin, currentY, title)
    canvas.drawText(": 2025-03-18", valueMargin, currentY, title)
    currentY += lineSpacing

    canvas.drawText("Tanggal Selesai", labelMargin, currentY, title)
    canvas.drawText(": 2025-03-24", valueMargin, currentY, title)
    currentY += lineSpacing

    canvas.drawText("Tahap", labelMargin, currentY, title)
    canvas.drawText(
        ": Penyelesaian, Testing, Analisis, Desain UI/UX, Pengerjaan",
        valueMargin,
        currentY,
        title
    )
    currentY += lineSpacing

    // Setelah Informasi Pengembangan
    currentY += 15f  // Add extra space after the subheading

    // Informasi Pengujian (Bold dan Ukuran Font Lebih Besar untuk Judul)
    val informasiPengujianTitle = "Informasi Pengujian"
    title.typeface = titleFont
    title.textSize = titleSize
    canvas.drawText(informasiPengujianTitle, leftMargin, currentY, title)
    currentY += lineSpacing

    // Set font untuk konten
    title.typeface = contentFont
    title.textSize = contentSize

    // Informasi Pengujian content (Name: value format)
    canvas.drawText("Tanggal", labelMargin, currentY, title)
    canvas.drawText(": 2025-03-29", valueMargin, currentY, title)
    currentY += lineSpacing

    canvas.drawText("Perangkat Lunak", labelMargin, currentY, title)
    canvas.drawText(": SIPM", valueMargin, currentY, title)
    currentY += lineSpacing

    canvas.drawText("Versi", labelMargin, currentY, title)
    canvas.drawText(": 1.0", valueMargin, currentY, title)
    currentY += lineSpacing

    canvas.drawText("Tujuan", labelMargin, currentY, title)
    canvas.drawText(": pengujian", valueMargin, currentY, title)
    currentY += lineSpacing

    canvas.drawText("Metode", labelMargin, currentY, title)
    canvas.drawText(": watermark", valueMargin, currentY, title)
    currentY += lineSpacing

    // Tabel Approval Tanda Tangan (Kosong, tanpa gambar)
    val approvalTitle = "Approval Tanda Tangan"
    title.typeface = titleFont
    title.textSize = titleSize
    canvas.drawText(approvalTitle, leftMargin, currentY, title)
    currentY += lineSpacing

// Set font untuk konten
    title.typeface = contentFont
    title.textSize = contentSize

// Gambar Border Tabel
    val tableLeft = leftMargin
    val tableTop = currentY
    val tableWidth = pageWidth - leftMargin - 80f  // Lebar tabel
    val tableHeight = 200f  // Tinggi tabel

    // Siapkan paint untuk border tabel
    paint.style = Paint.Style.STROKE
    paint.strokeWidth = 2f
    paint.color = ContextCompat.getColor(context, R.color.black)

// Header kolom Tabel (Nama dan Tanda Tangan)
    canvas.drawRect(tableLeft, tableTop, tableLeft + tableWidth, tableTop + 40f, paint)  // Header border
    canvas.drawText("Persetujuan", tableLeft + 10f, tableTop + 25f, title)  // Kolom Persetujuan
    canvas.drawText("Nama Pihak", tableLeft + tableWidth / 2 + 10f, tableTop + 25f, title)  // Kolom Nama Pihak

// Baris pertama (Nama Pihak: User)
    canvas.drawRect(tableLeft, tableTop + 40f, tableLeft + tableWidth, tableTop + 80f, paint)  // Border untuk baris pertama
    canvas.drawText("User", tableLeft + 10f, tableTop + 65f, title)  // Nama User
    canvas.drawText("______________", tableLeft + tableWidth / 2 + 10f, tableTop + 65f, title)  // Tempat untuk tanda tangan (kosong)

    currentY += 80f  // Update posisi Y untuk baris berikutnya

// Baris kedua (Nama Pihak: Admin)
    canvas.drawRect(tableLeft, tableTop + 80f, tableLeft + tableWidth, tableTop + 120f, paint)  // Border untuk baris kedua
    canvas.drawText("Admin", tableLeft + 10f, tableTop + 105f, title)  // Nama Admin
    canvas.drawText("______________", tableLeft + tableWidth / 2 + 10f, tableTop + 105f, title)  // Tempat untuk tanda tangan (kosong)

    currentY += 80f  // Update posisi Y untuk tabel berikutnya

// Menambahkan garis border di bawah tabel untuk membatasi bagian TTD
    canvas.drawRect(tableLeft, tableTop + 120f, tableLeft + tableWidth, tableTop + tableHeight, paint)


    // Finish the page
    pdfDocument.finishPage(myPage)

    // Save to file
    val file = File(directory.path, "Laporan_${id}.pdf")
    try {
        pdfDocument.writeTo(FileOutputStream(file))
        Toast.makeText(context, "PDF file generated successfully", Toast.LENGTH_SHORT).show()
    } catch (ex: IOException) {
        ex.printStackTrace()
        Toast.makeText(context, "Error generating PDF", Toast.LENGTH_SHORT).show()
    }
    pdfDocument.close()
}

@Composable
fun ShowLaporanScreen(
    navController: NavController,
    id: String?
) {
    val context = LocalContext.current

    // Data statis (contoh data sementara)
    val namaSistem = "Sistem Pengujian"
    val versiPerangkat = "1.0.0"
    val tujuanPengujian = "Mengetahui kelayakan sistem"
    val metodePengujian = "Uji coba fungsional"
    val tanggalPengujian = "2025-03-29"
    val pelaksanaPengujian = "Tim Pengujian"
    val status = "Belum selesai"

    // Directory untuk menyimpan file PDF
    val directory = context.getExternalFilesDir(null) ?: context.filesDir
    val filePath = File(directory, "Laporan_${id}.pdf")

    val scrollState = rememberScrollState()

    // Menampilkan detail laporan statis
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Putih)
            .verticalScroll(scrollState)
    ) {
        val titleFont = FontFamily.Serif
        val titleSize = 20.sp
        val contentFont = FontFamily.SansSerif
        val contentSize = 16.sp
        val leftMargin = 16f
        val valueMargin = 250f
        val lineSpacing = 30f

        // Informasi Pengajuan
        Text(
            text = "Informasi Pengajuan",
            fontFamily = titleFont,
            fontSize = titleSize,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Informasi Pengajuan content (Name: value format)
        Text("Tanggal: 2025-03-14")
        Text("Nama Sistem: SIPM")
        Text("Jenis: Sistem Baru")
        Text("Rencana Anggaran: Termasuk dalam perencanaan")
        Text("Masalah: Sistem Penjadwalan Perbaikan Mesin")
        Text("Output: Aplikasi dan Web")

        Spacer(modifier = Modifier.height(16.dp))

        // Informasi Pengembangan
        Text(
            text = "Informasi Pengembangan",
            fontFamily = titleFont,
            fontSize = titleSize,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Informasi Pengembangan content (Name: value format)
        Text("Tanggal Mulai: 2025-03-18")
        Text("Tanggal Selesai: 2025-03-24")
        Text("Tahap: Penyelesaian, Testing, Analisis, Desain UI/UX, Pengerjaan")

        Spacer(modifier = Modifier.height(16.dp))

        // Informasi Pengujian
        Text(
            text = "Informasi Pengujian",
            fontFamily = titleFont,
            fontSize = titleSize,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Informasi Pengujian content (Name: value format)
        Text("Tanggal: 2025-03-29")
        Text("Perangkat Lunak: SIPM")
        Text("Versi: 1.0")
        Text("Tujuan: pengujian")
        Text("Metode: watermark")

        Spacer(modifier = Modifier.height(16.dp))

        // Generate PDF saat layar dimuat atau saat tombol diklik
        LaunchedEffect(id) {
            id?.let {
                generatePDF(
                    context = context,
                    directory = directory,
                    id = it,
                    namaSistem = namaSistem,
                    versiPerangkat = versiPerangkat,
                    tujuanPengujian = tujuanPengujian,
                    metodePengujian = metodePengujian,
                    tanggalPengujian = tanggalPengujian,
                    pelaksanaPengujian = pelaksanaPengujian,
                    status = status
                )
            }
        }

        // Tombol untuk membuka PDF yang dihasilkan
        Button(
            onClick = {
                // Gunakan objek file yang telah dibuat di atas
                if (filePath.exists()) {
                    // Buka file PDF menggunakan Intent
                    val pdfUri: Uri = FileProvider.getUriForFile(
                        context,
                        "com.example.applicationsop.fileprovider", // Pastikan ini sesuai
                        filePath // Pastikan ini adalah file yang valid
                    )

                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setDataAndType(pdfUri, "application/pdf")
                    intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Memberikan izin untuk membaca
                    context.startActivity(intent)
                } else {
                    Toast.makeText(context, "Laporan tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Buka Laporan PDF")
        }
    }
}


