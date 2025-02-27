package com.example.applicationsop.models

import kotlinx.serialization.Serializable

@Serializable
data class PengajuanRequest(
    val tgl: String,
    val nama_sistem: String,
    val jenis: String,
    val rencana_anggaran: String,
    val masalah: String,
    val output: String,
    val status: String? = null,  // Status bisa null, default-nya adalah 'pending'
    val user_id: String
)
