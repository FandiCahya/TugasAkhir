package com.example.applicationsop.models

import kotlinx.serialization.Serializable

@Serializable
data class Pengujian(
    val id: String,
    val perangkat_lunak: String,
    val versi: String,
    val tujuan: String,
    val metode: String,
    val tanggal: String,
    val status: String,
    val pelaksana: Pelaksana,
    val pengembangan: Pengembangan,  // Use Pengajuan model here
    val pengujian_detail: List<pengujian_detail>
)

@Serializable
data class pengujian_detail(
    val id: String,
    val nama_uji: String,
    val kasus_uji: String,
    val hasil_diharapkan: String,
    val hasil_pengujian: String,
    val status: String
)

@Serializable
data class Pelaksana(
    val id: String,
    val name: String,
    val email: String,
    val devisi: String? = null,  // Mengubah menjadi nullable
    val role: String? = null
)

@Serializable
data class ResponsePengujian(
    val success: Boolean,
    val payload: List<Pengujian>
)


