package com.example.applicationsop.presentation.component.popup

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.applicationsop.models.PersetujuanPengujianDetail
import com.example.applicationsop.ui.theme.Maroon
import com.example.applicationsop.ui.theme.biru
import androidx.activity.ComponentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applicationsop.ViewModel.SharedPengujianViewModel
import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.applicationsop.logic.downloadPdfToPublicDirectory
import com.example.applicationsop.logic.generatePDF
import java.io.File


@Composable
fun RiwayatPopup(
    onDismiss: () -> Unit,
    navController: NavController,
    id: String,
    tgl: String,
    namaSistem: String,
    jenis: String,
    rencanaAnggaran: String,
    masalah: String,
    output: String,
    tanggalMulai: String?,
    tanggalSelesai: String?,
    tahap: String?,
    keterangan: String?,
    perangkatLunak: String?,
    versiPerangkat: String?,
    tujuanPengujian: String?,
    metodePengujian: String?,
    detailPersetujuan: List<PersetujuanPengujianDetail>,
    status: String?
) {
    val context = LocalContext.current
    val activity = context as? Activity
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

    val sharedViewModel = viewModel<SharedPengujianViewModel>(context as ComponentActivity)
    sharedViewModel.detailPersetujuan.value = detailPersetujuan

    val internalPdfDirectory = context.getExternalFilesDir(null) ?: context.filesDir
    val pdfFileName = "Laporan_${id}.pdf"
    val generatedPdfFile = File(internalPdfDirectory, pdfFileName)

    val displayNames = mapOf(
        "sistem_baru" to "Sistem Baru",
        "pengembangan" to "Pengembangan",
        "termasuk_dalam_perencanaan" to "Termasuk Anggaran",
        "tidak_termasuk_perencanaan" to "Tidak Termasuk Anggaran"
    )

    val jenisSistemDisplay = displayNames[jenis] ?: jenis
    val rencanaAnggaranDisplay = displayNames[rencanaAnggaran] ?: rencanaAnggaran

    // Directory untuk menyimpan file PDF
    val directory = context.getExternalFilesDir(null) ?: context.filesDir
    val filePath = File(directory, "Laporan_${id}.pdf")

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
                .clickable { onDismiss() }
        )

        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(20.dp)
                .shadow(8.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Timer,
                        contentDescription = "Timer Icon",
                        modifier = Modifier.size(24.dp),
                        tint = Maroon
                    )
                }
                Text(
                    text = "Detail Riwayat",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                // Content
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Row for Nama Sistem
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween // Menggunakan SpaceBetween untuk jarak antar elemen
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Tanggal Pengajuan",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                        Column(modifier = Modifier.weight(2f)) {
                            Text(
                                ": $tgl",
                                color = Color.Black,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Row for Versi Perangkat
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween // Menggunakan SpaceBetween untuk jarak antar elemen
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Nama Sistem", fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                        Column(modifier = Modifier.weight(2f)) {
                            Text(
                                ": $namaSistem",
                                color = Color.Black,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Row for Tujuan Pengujian
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween // Menggunakan SpaceBetween untuk jarak antar elemen
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Jenis", fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                        Column(modifier = Modifier.weight(2f)) {
                            Text(
                                ": $jenisSistemDisplay",
                                color = Color.Black,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Row for Metode Pengujian
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween // Menggunakan SpaceBetween untuk jarak antar elemen
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Rencana Anggaran",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                        Column(modifier = Modifier.weight(2f)) {
                            Text(
                                ": $rencanaAnggaranDisplay",
                                color = Color.Black,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Row for Tanggal Pengujian
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween // Menggunakan SpaceBetween untuk jarak antar elemen
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Masalah", fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                        Column(modifier = Modifier.weight(2f)) {
                            Text(
                                ": $masalah",
                                color = Color.Black,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Row for Pelaksana Pengujian
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween // Menggunakan SpaceBetween untuk jarak antar elemen
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Output", fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                        Column(modifier = Modifier.weight(2f)) {
                            Text(
                                ": $output",
                                color = Color.Black,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
                Divider(modifier = Modifier.padding(top = 8.dp))
            }

            // Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.End // Agar tombol berada di kanan
            ) {
                // Tombol "Laporan" (Buka PDF)
                Button(
                    onClick = {
                        id?.let {
                            // 1. Generate PDF
                            generatePDF(
                                context = context,
                                directory = internalPdfDirectory, // Simpan ke internal app
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
                                // detailPersetujuan = detailPersetujuan // Uncomment jika dibutuhkan di generatePDF
                            )

                            // 2. Buka PDF setelah dibuat
                            if (generatedPdfFile.exists()) {
                                try {
                                    val pdfUri: Uri = FileProvider.getUriForFile(
                                        context,
                                        "${context.packageName}.fileprovider",
                                        generatedPdfFile
                                    )

                                    val intent = Intent(Intent.ACTION_VIEW)
                                    intent.setDataAndType(pdfUri, "application/pdf")
                                    intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                                    val packageManager = context.packageManager
                                    if (intent.resolveActivity(packageManager) != null) {
                                        context.startActivity(intent)
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Tidak ada aplikasi untuk membuka PDF.",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        context,
                                        "Gagal membuka PDF: ${e.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    e.printStackTrace()
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    "Laporan PDF belum digenerate.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },
                    modifier = Modifier
                        .width(120.dp)
                        .shadow(4.dp, RoundedCornerShape(16.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = biru),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Laporan", color = Color.White)
                }

                Spacer(modifier = Modifier.width(8.dp)) // Jarak antara dua tombol

                // Tombol "Download PDF" (Simpan ke Publik)
                Button(
                    onClick = {
                        id?.let {
                            // 1. Generate PDF (pastikan sudah ada di internal)
                            generatePDF(
                                context = context,
                                directory = internalPdfDirectory, // Simpan ke internal app
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
                                // detailPersetujuan = detailPersetujuan // Uncomment jika dibutuhkan di generatePDF
                            )

                            // 2. Download ke Public Directory
                            if (generatedPdfFile.exists()) {
                                downloadPdfToPublicDirectory(
                                    context = context,
                                    sourceFile = generatedPdfFile,
                                    fileName = pdfFileName // Gunakan nama file yang sama
                                )
                            } else {
                                Toast.makeText(
                                    context,
                                    "Gagal mengunduh: PDF belum digenerate.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },
                    modifier = Modifier
                        .width(120.dp) // Ukuran yang sama atau disesuaikan
                        .shadow(4.dp, RoundedCornerShape(16.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = Maroon), // Warna berbeda agar mudah dibedakan
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Download", color = Color.White)
                }
            }
        }
    }
}