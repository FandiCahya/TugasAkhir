package com.example.applicationsop.data

import com.example.applicationsop.models.Catatan
import com.example.applicationsop.models.GetPengujianDetail
import com.example.applicationsop.models.PengujianDetail
import com.example.applicationsop.models.Persetujuan

// Data class untuk menyimpan informasi detail usulan
data class DetailInfo(
    val id: String, // Tambahkan ID untuk identifikasi yang unik
    val namaSistem: String = "",
    val tanggal: String = "",
    val jenisSistem: String = "",
    val rencanaAnggaran: String = "",
    val masalahSistem: String = "",
    val outputHasil: String = "",
    var status: String = "",
    var alasan_penolakan: String = ""
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

// Data class for storing detailed information
data class DetailPengujian(
    val id: String, // Unique ID for identification
    val namaSistem: String = "",
    val versiPerangkat: String = "", // Add this field
    val tujuanPengujian: String = "", // Add this field
    val metodePengujian: String = "", // Add this field
    val tanggalPengujian: String = "", // Add this field
    val pelaksanaPengujian: String = "", // Add this field
    var status: String = "",
    var persetujuanId: String = ""
)

// Data model for the schedule
data class ScheduleItemFix(
    val task: String,
    val id: String,
    val startDate: String,
    val endDate: String,
    val description: String,
    val stage: String,
    val progressPercentage: Int,
    val status: String
)

