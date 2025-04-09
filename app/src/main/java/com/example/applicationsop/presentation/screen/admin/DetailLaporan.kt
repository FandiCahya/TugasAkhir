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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
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

    // Title for PDF document
    title.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    title.textSize = 20f
    title.color = ContextCompat.getColor(context, R.color.black)
    val titleText = "Riwayat Pengujian"
    val titleWidth = title.measureText(titleText)
    canvas.drawText(titleText, (pageWidth - titleWidth) / 2, 80f, title)

    // Content for the PDF
    title.textSize = 15f
    title.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)

    val lineSpacing = 40f
    var currentY = 150f

    // Table header
    val headerText = arrayOf("ID", "Nama Sistem", "Versi Perangkat", "Tujuan Pengujian", "Metode Pengujian", "Tanggal Pengujian", "Pelaksana Pengujian", "Status")
    val headerWidth = pageWidth / headerText.size.toFloat()

    // Drawing the table headers
    for (i in headerText.indices) {
        val xPos = headerWidth * i + (headerWidth - title.measureText(headerText[i])) / 2
        canvas.drawText(headerText[i], xPos, currentY, title)
    }

    currentY += lineSpacing

    // Content rows
    val content = arrayOf(id, namaSistem, versiPerangkat, tujuanPengujian, metodePengujian, tanggalPengujian, pelaksanaPengujian, status)
    for (i in content.indices) {
        val xPos = headerWidth * i + (headerWidth - title.measureText(content[i])) / 2
        canvas.drawText(content[i], xPos, currentY, title)
    }

    // Finish the page
    pdfDocument.finishPage(myPage)
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

    // Sample data (in real cases, fetch this data from your backend or view model)
    val namaSistem = "Sistem ABC"
    val versiPerangkat = "1.0.0"
    val tujuanPengujian = "Menguji fitur A"
    val metodePengujian = "Metode X"
    val tanggalPengujian = "2025-04-09"
    val pelaksanaPengujian = "Tim QA"
    val status = "Lulus"

    // Directory to store PDF file (Ensure this is a valid File)
    val directory = context.getExternalFilesDir(null) ?: context.filesDir
    val filePath = File(directory, "Laporan_${id}.pdf")

    // Generate PDF when the screen is loaded or when a button is clicked
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

    // Button to open the generated PDF
    Button(
        onClick = {
            // Use the file object created above
            if (filePath.exists()) {
                // Open the PDF file (use Intent to view PDF)
                val pdfUri: Uri = FileProvider.getUriForFile(
                    context,
                    "com.example.applicationsop.fileprovider", // Ensure this is correct
                    filePath // Ensure this is a valid File
                )

                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(pdfUri, "application/pdf")
                intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Provide read permission
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "Laporan tidak ditemukan", Toast.LENGTH_SHORT).show()
            }
        },
        modifier = Modifier.padding(16.dp)
    ) {
        Text("Buka Laporan")
    }
}

