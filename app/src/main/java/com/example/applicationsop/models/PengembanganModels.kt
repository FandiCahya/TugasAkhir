package com.example.applicationsop.models

import kotlinx.serialization.Serializable

@Serializable
data class Pengembangan(
    val id: String,
    val tanggal_mulai: String,
    val tanggal_selesai: String,
    val tahap: String,
    val persentase: Int,
    val keterangan: String,
    val status: String,
    val pengajuan: Pengajuan,  // Use Pengajuan model here
    val user: User? = null
)

@Serializable
data class ResponsePengembangan(
    val success: Boolean,
    val payload: List<Pengembangan>
)


