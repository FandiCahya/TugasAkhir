package com.example.applicationsop.presentation.screen.pemohon

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import com.example.applicationsop.Api.UpdatePersetujuanDiterima
import com.example.applicationsop.Api.fetchPengajuanList
import com.example.applicationsop.Api.fetchPengujianList
import com.example.applicationsop.Api.postPengajuan
import com.example.applicationsop.core.UserUtils
import com.example.applicationsop.models.Catatan
import com.example.applicationsop.models.Persetujuan
import com.example.applicationsop.models.PersetujuanDetail
import com.example.applicationsop.models.Pengajuan
import com.example.applicationsop.models.PengajuanRequest
import com.example.applicationsop.models.Pengujian
import com.example.applicationsop.presentation.component.ActionButton
import com.example.applicationsop.presentation.component.header.HeaderForm
import com.example.applicationsop.presentation.component.signaturepad.PathState
import com.example.applicationsop.presentation.component.signaturepad.SignatureDialog
import com.example.applicationsop.ui.theme.Maroon
import com.example.applicationsop.ui.theme.Putih
import com.example.applicationsop.ui.theme.abang
import com.example.applicationsop.ui.theme.ijo
import kotlinx.coroutines.launch
import com.example.applicationsop.Api.updatePersetujuanPengujian
import com.example.applicationsop.models.Pelaksana
import java.io.File


@kotlin.OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DetailPengujianPemohon(
    navController: NavController,
    idPengujian: String?,
    namaSistem: String?,
    onAcceptClick: () -> Unit,
    onRejectClick: (String) -> Unit,
    onDismiss: () -> Unit

) {

    var pengujian by remember { mutableStateOf<Pengujian?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    // States for signature dialog and rejection reason input
    val showAlasanInput = remember { mutableStateOf(false) }
    val inputAlasan = remember { mutableStateOf("") }

    // Signature Pad
    val paths = remember { mutableStateOf(mutableListOf<PathState>()) }
    val capturingViewBounds = remember { mutableStateOf<Rect?>(null) }
    val image = remember { mutableStateOf<Bitmap?>(null) }
    val isDialogOpen = remember { mutableStateOf(false) }
    val drawColor = remember { mutableStateOf(Color.Black) }
    val drawBrush = remember { mutableStateOf(5f) }
    val usedColors = remember { mutableStateOf(mutableSetOf(Color.Black, Color.White, Color.Gray)) }

    val coroutineScope = rememberCoroutineScope()

    paths.value.add(PathState(Path(), drawColor.value, drawBrush.value))

    // Informasi User
    val context = LocalContext.current
    val userData = remember { UserUtils.getUserData(context) }
    val userId = userData["userId"]

    var signatureFile by remember { mutableStateOf<File?>(null) }
    var capturedImage by remember { mutableStateOf<ImageBitmap?>(null) }

    print("IDPengujian : $idPengujian")

    val scrollState = rememberScrollState()



    LaunchedEffect(idPengujian) {
        println("LaunchedEffect triggered with idPengujian: $idPengujian")

        if (idPengujian != null) {
            val fetchedPengujian = fetchPengujianList(idPengujian).firstOrNull()
            pengujian = fetchedPengujian
            isLoading = false

//            println("Updated pengujian: $pengujian")

            fetchedPengujian?.catatan?.forEach { catatan ->
//                Log.d("CatatanPengujian", "ID: ${catatan.id}, Uraian: ${catatan.uraian}")
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Putih)
            .verticalScroll(scrollState)
    ) {
        // Header Section
        HeaderForm(title = "Detail Pengujian", navController)
        if (isLoading) {
            Text("Loading...", modifier = Modifier.padding(16.dp), fontSize = 18.sp)
        } else if (pengujian == null) {
            Text("Data tidak ditemukan", modifier = Modifier.padding(16.dp), fontSize = 18.sp)
        } else {
            // Ambil data dari `pengujian`
            val namaSistem = pengujian?.perangkat_lunak ?: "-"
            val versiPerangkat = pengujian?.versi ?: "-"
            val tujuanPengujian = pengujian?.tujuan ?: "-"
            val metodePengujian = pengujian?.metode ?: "-"
            val tanggalPengujian = pengujian?.tanggal ?: "-"
            val pelaksanaPengujian = pengujian?.pelaksana?.name ?: "-"

            // Ambil detail pengujian pertama sebagai contoh
            val detailPengujian = pengujian?.pengujian_detail?.firstOrNull()
            val selectedTestType = detailPengujian?.kategori ?: "-"
            val namaUji = detailPengujian?.nama_uji ?: "-"
            val kasusUji = detailPengujian?.kasus_uji ?: "-"
            val hasilYangDiharapkan = detailPengujian?.hasil_diharapkan ?: "-"
            val hasilPengujian = detailPengujian?.hasil_pengujian ?: "-"
            val keterangan = detailPengujian?.status ?: "-"

            // Menyimpan Catatan Pengujian ke dalam variabel
            val catatanList = pengujian?.catatan?.map {
                Catatan(
                    id = it.id ?: "-",
                    uraian = it.uraian ?: "-",
                    rencana_tindak_lanjut = it.rencana_tindak_lanjut ?: "-",
                    penanggung_jawab = pengujian?.pelaksana?.name ?: "-",
                    created_at = it.created_at ?: "-"
                )
            } ?: emptyList()

            // Menyimpan daftar Persetujuan ke dalam variabel
            val persetujuanList = pengujian?.persetujuan?.map { persetujuan ->
                Persetujuan(
                    id = persetujuan.id ?: "-",
                    status = persetujuan.status ?: "-",
                    tanggal_persetujuan = persetujuan.tanggal_persetujuan ?: "-",
                    persetujuan_detail = persetujuan.persetujuan_detail?.map { detail ->
                        PersetujuanDetail(
                            id = detail.id ?: "-",
                            status = detail.status ?: "-",
                            catatan = detail.catatan ?: "-",
                            signature = detail.signature ?: "-",
                            disetujui_oleh = detail.disetujui_oleh?.let { pelaksana ->
                                Pelaksana(
                                    id = pelaksana.id ?: "-",
                                    name = pelaksana.name ?: "-",
                                    email = pelaksana.email ?: "-",
                                    devisi = pelaksana.devisi ?: "-",
                                    role = pelaksana.role ?: "-"
                                )
                            }
                        )
                    } ?: emptyList() // Jika `persetujuan_detail` null, kembalikan list kosong
                )
            } ?: emptyList()


            val filteredPersetujuanDetailIds = persetujuanList
                .flatMap { it.persetujuan_detail.orEmpty() } // Hindari null dengan orEmpty()
                .filter { it.disetujui_oleh?.id == userId } // Filter berdasarkan userId
                .map { it.id ?: "-" } // Ambil ID, gunakan "-" jika null




            // Form Fields Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Informasi Pengujian
                Text(
                    text = "Informasi Pengujian",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Divider(color = Color.Gray, thickness = 1.dp)

                // Tabel Informasi Pengujian

                Column(modifier = Modifier.fillMaxWidth()) {
                    // Baris kedua (nama perangkat lunak)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, Color.Gray)
                                .padding(8.dp)
                        ) {
                            Text(
                                "Nama  Software",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(2f)
                                .border(1.dp, Color.Gray)
                                .padding(8.dp)
                        ) {
                            Text(namaSistem, color = Color.Black)
                        }
                    }
                    // Baris kedua (Versi perangkat lunak)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, Color.Gray)
                                .padding(8.dp)
                        ) {
                            Text(
                                "Versi Software Perangkat",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(2f)
                                .border(1.dp, Color.Gray)
                                .padding(8.dp)
                        ) {
                            Text(versiPerangkat, color = Color.Black)
                        }
                    }

                    // Baris kedua (Tujuan Pengujian)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, Color.Gray)
                                .padding(8.dp)
                        ) {
                            Text(
                                "Tujuan Pengujian",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(2f)
                                .border(1.dp, Color.Gray)
                                .padding(8.dp)
                        ) {
                            Text(tujuanPengujian, color = Color.Black)
                        }
                    }

                    // Baris ketiga (Metode Pengujian)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, Color.Gray)
                                .padding(8.dp)
                        ) {
                            Text(
                                "Metode Pengujian",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(2f)
                                .border(1.dp, Color.Gray)
                                .padding(8.dp)
                        ) {
                            Text(metodePengujian, color = Color.Black)
                        }
                    }

                    // Baris keempat (Tanggal Pengujian)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, Color.Gray)
                                .padding(8.dp)
                        ) {
                            Text(
                                "Tanggal Pengujian",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(2f)
                                .border(1.dp, Color.Gray)
                                .padding(8.dp)
                        ) {
                            Text(tanggalPengujian, color = Color.Black)
                        }
                    }

                    // Baris keempat (Tanggal Pengujian)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, Color.Gray)
                                .padding(8.dp)
                        ) {
                            Text(
                                "Pelaksana Pengujian",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(2f)
                                .border(1.dp, Color.Gray)
                                .padding(8.dp)
                        ) {
                            Text(pelaksanaPengujian, color = Color.Black)
                        }
                    }

                    // Informasi Pengujian
                    Text(
                        text = "Detail Uraian Pengujian",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                    Divider(
                        color = Color.Gray,
                        thickness = 1.dp,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )

                    // Baris kelima (Jenis Uji)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, Color.Gray)
                                .padding(8.dp)
                        ) {
                            Text(
                                "Detail Jenis Pengujian",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(2f)
                                .border(1.dp, Color.Gray)
                                .padding(8.dp)
                        ) {
                            Text(selectedTestType, color = Color.Black)
                        }
                    }

                    // Baris keenam (Nama Uji)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, Color.Gray)
                                .padding(8.dp)
                        ) {
                            Text(
                                "Detail Nama Pengujian",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(2f)
                                .border(1.dp, Color.Gray)
                                .padding(8.dp)
                        ) {
                            Text(namaUji, color = Color.Black)
                        }
                    }

                    // Baris ketujuh (Kasus Uji)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, Color.Gray)
                                .padding(8.dp)
                        ) {
                            Text(
                                "Detail Kasus Pengujian",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(2f)
                                .border(1.dp, Color.Gray)
                                .padding(8.dp)
                        ) {
                            Text(kasusUji, color = Color.Black)
                        }
                    }

                    // Baris kedelapan (Hasil yang Diharapkan)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, Color.Gray)
                                .padding(8.dp)
                        ) {
                            Text(
                                "Hasil yang Diharapkan",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(2f)
                                .border(1.dp, Color.Gray)
                                .padding(8.dp)
                        ) {
                            Text(hasilYangDiharapkan, color = Color.Black)
                        }
                    }

                    // Baris kesembilan (Hasil Pengujian)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, Color.Gray)
                                .padding(8.dp)
                        ) {
                            Text(
                                "Hasil Pengujian",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(2f)
                                .border(1.dp, Color.Gray)
                                .padding(8.dp)
                        ) {
                            Text(hasilPengujian, color = Color.Black)
                        }
                    }

                    // Baris kesepuluh (Keterangan)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .border(1.dp, Color.Gray)
                                .padding(8.dp)
                        ) {
                            Text(
                                "Detail Keterangan",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(2f)
                                .border(1.dp, Color.Gray)
                                .padding(8.dp)
                        ) {
                            Text(keterangan, color = Color.Black)
                        }
                    }

                    // Informasi Pengujian
                    Text(
                        text = "Catatan Pengujian",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                    Divider(
                        color = Color.Gray,
                        thickness = 1.dp,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )

                    // Detail Catatan Pengujian jika ada
                    if (catatanList.isNotEmpty()) {
                        catatanList.forEach { catatan ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .border(1.dp, Color.Gray)
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        "Catatan Uraian Pengujian",
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .weight(2f)
                                        .border(1.dp, Color.Gray)
                                        .padding(8.dp)
                                ) {
                                    Text(catatan.uraian ?: "-", color = Color.Black)
                                }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .border(1.dp, Color.Gray)
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        "Rencana Tindak Lanjut",
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .weight(2f)
                                        .border(1.dp, Color.Gray)
                                        .padding(8.dp)
                                ) {
                                    Text(catatan.rencana_tindak_lanjut ?: "-", color = Color.Black)
                                }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .border(1.dp, Color.Gray)
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        "Penanggung Jawab",
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .weight(2f)
                                        .border(1.dp, Color.Gray)
                                        .padding(8.dp)
                                ) {
                                    Text(catatan.penanggung_jawab ?: "-", color = Color.Black)
                                }
                            }
                            Divider(
                                color = Color.Gray,
                                thickness = 1.dp,
                                modifier = Modifier.padding(top = 20.dp)
                            )
                        }
                    } else {
                        Text(
                            text = "Tidak ada catatan pengujian",
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                    ) {
                        // Row for "Approve" and "Tolak" buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Tombol untuk menambahkan tanda tangan (Approve) dan catatan
                            Button(
                                onClick = {
                                    isDialogOpen.value = true
                                    showAlasanInput.value = true
                                          },
                                modifier = Modifier.width(125.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = ijo),
                                shape = RoundedCornerShape(15.dp)
                            ) {
                                Text(
                                    text = "Approve",
                                    color = Putih,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }

                            // Tombol Tolak (Reject)
//                            Button(
//                                onClick = {
//                                    showAlasanInput.value = true
//                                },
//                                modifier = Modifier.width(120.dp).shadow(4.dp, RoundedCornerShape(16.dp)),
//                                colors = ButtonDefaults.buttonColors(containerColor = abang),
//                                shape = RoundedCornerShape(16.dp)
//                            ) {
//                                Text("Tolak", color = Color.White)
//                            }
                        }

                        // Show reason input if "Tolak" button is clicked
                        if (showAlasanInput.value) {
                            Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
                                Text(
                                    "Alasan Penolakan",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                TextField(
                                    value = inputAlasan.value,
                                    onValueChange = { inputAlasan.value = it },
                                    placeholder = { Text("Masukkan alasan") },
                                    modifier = Modifier.fillMaxWidth(),
                                    maxLines = 3
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                            }
                        }

                        SignatureDialog(
                            isDialogOpen = isDialogOpen,
                            capturingViewBound = capturingViewBounds,
                            drawColor = drawColor,
                            drawBrush = drawBrush,
                            usedColors = usedColors,
                            paths = paths,
                            image = image
                        )

                        // Displaying the signature result (Image and Signature)
                        if (image.value != null) {
                            Image(
                                bitmap = image.value!!.asImageBitmap(),
                                contentDescription = "Capture Image",
                                modifier = Modifier.padding(top = 16.dp)
                            )
                        }


                        if (!paths.value.isEmpty()) {
                            Text("Tanda Tangan Anda:", color = Maroon, modifier = Modifier.padding(top = 8.dp))
                        }

                        // Submit Button
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 16.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            ActionButton(
                                onClick = {
                                    filteredPersetujuanDetailIds.forEach { id ->
                                        println("ID Persetujuan Detail: $id")
                                    }

                                    val firstPersetujuanDetailId = filteredPersetujuanDetailIds.firstOrNull() ?: "-"
                                    println("ID pertama yang sesuai: $firstPersetujuanDetailId")

                                    val signatureFile = image.value?.let { bitmap ->
                                        val uniqueFileName = "signature_${System.currentTimeMillis()}.png"
                                        val file = File(navController.context.cacheDir, uniqueFileName)
                                        file.outputStream().use { out -> bitmap.compress(Bitmap.CompressFormat.PNG, 100, out) }
                                        file
                                    }

                                        val pengujianRequest = PersetujuanDetail(
                                            status = "setuju",
                                            catatan = if (showAlasanInput.value) inputAlasan.value else ""

                                        )
                                        coroutineScope.launch {
                                            try {
                                                val response = updatePersetujuanPengujian(firstPersetujuanDetailId, pengujianRequest)
                                                if (signatureFile != null) {
                                                    val response = UpdatePersetujuanDiterima(firstPersetujuanDetailId, pengujianRequest, signatureFile)
                                                    if (response.status.value in 200..299) {
                                                        Toast.makeText(navController.context, "Approval berhasil dikirim!", Toast.LENGTH_LONG).show()
                                                        navController.popBackStack()
                                                    } else {
                                                        Toast.makeText(navController.context, "Gagal mengirim. Coba lagi!", Toast.LENGTH_LONG).show()
                                                    }
                                                } else {
                                                    Toast.makeText(navController.context, "Tanda tangan diperlukan.", Toast.LENGTH_LONG).show()
                                                }
                                                if (response.status.value in 200..299) {
                                                    onRejectClick(inputAlasan.value) // Execute the callback
                                                    println("Response success update alasan: $response")
                                                    onDismiss()
                                                } else {
                                                    println("Failed to update status")
                                                }
                                            } catch (e: Exception) {
                                                println("Error: ${e.message}")
                                            }
                                        }

                                },
                                buttonType = "submit"
                            )
                        }
                    }
                }
            }
        }
    }
}