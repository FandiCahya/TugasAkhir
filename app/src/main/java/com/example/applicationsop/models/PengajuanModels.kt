package com.example.applicationsop.models

import kotlinx.serialization.Serializable

@Serializable
data class Pengajuan(
    val id: String,
    val tgl: String,
    val nama_sistem: String,
    val jenis: String,
    val rencana_anggaran: String,
    val masalah: String,
    val output: String,
    val status: String,
    val alasan_penolakan: String? = null,
    val user: User
)

@Serializable
data class ResponsePengajuan(
    val success: Boolean,
    val payload: List<Pengajuan>
)


