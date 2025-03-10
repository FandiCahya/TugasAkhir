package com.example.applicationsop.models

import kotlinx.serialization.Serializable

@Serializable
data class PengujianRequest(
    // informasi
    val pengembangan_id: String?,
    val perangkat_lunak: String?,
    val versi: String?,
    val tujuan: String?,
    val metode: String?,
    val tanggal: String?,
    val pelaksana_id: String?,
    // uraian
    val nama_uji: String?,
    val kasus_uji: String?,
    val hasil_diharapkan: String?,
    val hasil_pengujian: String?,
    val status: String?,
    val jenis_uji: String?
)

@Serializable
data class PengujianDetailRequest(
    val id: String,
    val nama_uji: String,
    val kasus_uji: String,
    val hasil_diharapkan: String,
    val hasil_pengujian: String,
    val status: String
)