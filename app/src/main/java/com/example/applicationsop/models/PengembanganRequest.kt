package com.example.applicationsop.models

import kotlinx.serialization.Serializable

@Serializable
data class PengembanganRequest(
    val pengajuan_id: String,
    val tanggal_mulai: String,
    val tanggal_selesai: String,
    val tahap: String,
    val persentase: Int,
    val keterangan: String,
    val status: String
)