package com.example.applicationsop.data

// Data class untuk menyimpan informasi detail usulan
data class DetailInfo(
    val id: String, // Tambahkan ID untuk identifikasi yang unik
    val namaSistem: String = "",
    val tanggal: String = "",
    val jenisSistem: String = "",
    val rencanaAnggaran: String = "",
    val masalahSistem: String = "",
    val outputHasil: String = "",
    var status: String = ""
)

// Data model for the schedule
data class ScheduleItem(
    val task: String,
    val startDate: String,
    val endDate: String,
    val description: String,
    val stage: String,
    val progressPercentage: Int // Ensure this is an Int
)


//fun getSampleSubmissions(): List<DetailInfo> {
//    return listOf(
//        DetailInfo(1, "Nama Sistem 1", "25/10/2025", "Sistem Baru", "Termasuk dalam perencanaan", "Bug tampilan", "Hasil yang diinginkan", "Menunggu konfirmasi"),
//        DetailInfo(2, "Nama Sistem 2", "26/10/2025", "Sistem Baru", "Perencanaan sudah dilakukan", "Error server", "Hasil yang optimal", "Pengujian ditolak"),
//        DetailInfo(3, "Nama Sistem 3", "27/10/2025", "Sistem Lama", "Perbaikan bug", "Tampilan tidak sesuai", "Tampilan diperbaiki", "Pengajuan Diterima")
//    )
//}
//
//package com.example.applicationsop.data
//
//// Data class untuk menyimpan informasi detail usulan
//// Data class untuk menyimpan informasi detail usulan
//data class DetailInfo(
//    val id: Int, // Tambahkan ID untuk identifikasi yang unik
//    val namaSistem: String = "",
//    val tanggal: String = "",
//    val jenisSistem: String = "",
//    val rencanaAnggaran: String = "",
//    val masalahSistem: String = "",
//    val outputHasil: String = "",
//    var status: String = ""
//)
//
//
//fun getSampleSubmissions(): List<DetailInfo> {
//    return listOf(
//        DetailInfo(1, "Nama Sistem 1", "25/10/2025", "Sistem Baru", "Termasuk dalam perencanaan", "Bug tampilan", "Hasil yang diinginkan", "Menunggu konfirmasi"),
//        DetailInfo(2, "Nama Sistem 2", "26/10/2025", "Sistem Baru", "Perencanaan sudah dilakukan", "Error server", "Hasil yang optimal", "Pengujian ditolak"),
//        DetailInfo(3, "Nama Sistem 3", "27/10/2025", "Sistem Lama", "Perbaikan bug", "Tampilan tidak sesuai", "Tampilan diperbaiki", "Pengajuan Diterima"),
//        DetailInfo(4, "Nama Sistem 4", "28/10/2025", "Sistem Lama", "Perbaikan eror", "output tidak sesuai", "error selesai", "Pengembangan"),
//        DetailInfo(5, "Nama Sistem 5", "29/10/2025", "Sistem Lama", "Perbaikan filter", "filter salah", "filter bisa sesuai", "Pengujian"),
//        DetailInfo(6, "Nama Sistem 6", "30/10/2025", "Sistem Lama", "Perbaikan sistem", "sistem demokrasi indonesia", "sistem diperbaiki", "Selesai"),
//    )
//}

