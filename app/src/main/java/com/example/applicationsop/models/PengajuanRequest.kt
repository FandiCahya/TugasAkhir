package com.example.applicationsop.models

import kotlinx.serialization.Serializable

@Serializable
data class PengajuanRequest(
    val tgl: String?= null,
    val nama_sistem: String?= null,
    val jenis: String?= null,
    val rencana_anggaran: String?= null,
    val masalah: String?= null,
    val output: String?= null,
    val alasan_penolakan: String?= null,
    val status: String? = null,
    val user_id: String?= null
)

@Serializable
data class UpdateStatusPengajuan(
    val status: String? = null,
)
