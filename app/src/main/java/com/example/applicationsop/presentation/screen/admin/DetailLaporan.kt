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
import com.example.applicationsop.ViewModel.SharedPengujianViewModel
import com.example.applicationsop.ui.theme.Putih
import java.io.FileOutputStream
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applicationsop.logic.generatePDF

@Composable
fun ShowLaporanScreen(
    navController: NavController,
    id: String?,
    namaSistem: String?,
    jenis: String?,
    rencanaAnggaran: String?,
    masalah: String?,
    output: String?,
    tanggalMulai: String?,
    tanggalSelesai: String?,
    tahap: String?,
    keterangan: String?,
    perangkatLunak: String?,
    versiPerangkat: String?,
    tujuanPengujian: String?,
    metodePengujian: String?,
    status: String?
) {
    val context = LocalContext.current
    // Decode the values
    val decodedNamaSistem = Uri.decode(namaSistem)
    val decodedJenis = Uri.decode(jenis)
    val decodedRencanaAnggaran = Uri.decode(rencanaAnggaran)
    val decodedMasalah = Uri.decode(masalah)
    val decodedOutput = Uri.decode(output)
    val decodedTanggalMulai = Uri.decode(tanggalMulai)
    val decodedTanggalSelesai = Uri.decode(tanggalSelesai)
    val decodedTahap = Uri.decode(tahap)
    val decodedKeterangan = Uri.decode(keterangan)
    val decodedPerangkatLunak = Uri.decode(perangkatLunak)
    val decodedVersiPerangkat = Uri.decode(versiPerangkat)
    val decodedTujuanPengujian = Uri.decode(tujuanPengujian)
    val decodedMetodePengujian = Uri.decode(metodePengujian)
    val decodedStatus = Uri.decode(status)

    // Mendapatkan instance ViewModel
    val sharedViewModel: SharedPengujianViewModel = viewModel()

    // Mengambil data dari ViewModel jika ada
    val detailPersetujuan = sharedViewModel.detailPersetujuan.value


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
        Text("Tanggal: $decodedTanggalMulai")
        Text("Nama Sistem: $decodedNamaSistem")
        Text("Jenis: $decodedJenis")
        Text("Rencana Anggaran: $decodedRencanaAnggaran")
        Text("Masalah: $decodedMasalah")
        Text("Output: $decodedOutput")

        Spacer(modifier = Modifier.height(16.dp))

        // Informasi Pengembangan
        Text(
            text = "Informasi Pengembangan",
            fontFamily = titleFont,
            fontSize = titleSize,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Informasi Pengembangan content (Name: value format)
        Text("Tanggal Mulai: $decodedTanggalMulai")
        Text("Tanggal Selesai: $decodedTanggalSelesai")
        Text("Tahap: $decodedTahap")

        Spacer(modifier = Modifier.height(16.dp))

        // Informasi Pengujian
        Text(
            text = "Informasi Pengujian",
            fontFamily = titleFont,
            fontSize = titleSize,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Informasi Pengujian content (Name: value format)
        Text("Perangkat Lunak: $decodedPerangkatLunak")
        Text("Versi: $decodedVersiPerangkat")
        Text("Tujuan: $decodedTujuanPengujian")
        Text("Metode: $decodedMetodePengujian")

        Spacer(modifier = Modifier.height(16.dp))

        // Generate PDF saat layar dimuat atau saat tombol diklik
        LaunchedEffect(id) {
            id?.let {
                generatePDF(
                    context = context,
                    directory = directory,
                    id = it,
                    namaSistem = decodedNamaSistem,
                    jenis = decodedJenis,
                    rencanaAnggaran = decodedRencanaAnggaran,
                    masalah = decodedMasalah,
                    output = decodedOutput,
                    tanggalMulai = decodedTanggalMulai,
                    tanggalSelesai = decodedTanggalSelesai,
                    tahap = decodedTahap,
                    keterangan = decodedKeterangan,
                    perangkatLunak = decodedPerangkatLunak,
                    versiPerangkat = decodedVersiPerangkat,
                    tujuanPengujian = decodedTujuanPengujian,
                    metodePengujian = decodedMetodePengujian,
                    status = decodedStatus,
                    detailPersetujuan = detailPersetujuan
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


