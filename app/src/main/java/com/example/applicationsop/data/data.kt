package com.example.applicationsop.data

// Data class untuk menyimpan informasi detail usulan
data class DetailInfo(
    val namaSistem: String = "",
    val tanggal: String = "",
    val jenisSistem: String = "",
    val rencanaAnggaran: String = "",
    val masalahSistem: String = "",
    val outputHasil: String = "",
    val status: String = ""
)

// Sample data yang bisa dipakai untuk menampilkan usulan
fun getSampleSubmissions(): List<DetailInfo> {
    return listOf(
        DetailInfo("Nama Sistem 1", "25/10/2025", "Sistem Baru", "Termasuk dalam perencanaan", "Bug tampilan", "Hasil yang diinginkan", "Menunggu konfirmasi"),
        DetailInfo("Nama Sistem 2", "26/10/2025", "Sistem Baru", "Perencanaan sudah dilakukan", "Error server", "Hasil yang optimal", "Pengujian ditolak"),
        DetailInfo("Nama Sistem 3", "27/10/2025", "Sistem Lama", "Perbaikan bug", "Tampilan tidak sesuai", "Tampilan diperbaiki", "Pengajuan Diterima")
    )
}
