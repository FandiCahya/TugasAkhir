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
    val user_ids: List<String>, // Menampung daftar user ID
    val pengujian_detail: List<PengujianDetail>, // Menampung detail pengujian
    val catatan_pengujian: CatatanPengujian? = null
)
@Serializable
data class PengujianDetail(
    val nama_uji: String,
    val kasus_uji: String,
    val hasil_diharapkan: String,
    val hasil_pengujian: String,
    val kategori: String, // Perbaikan nama field (ketegori -> kategori)
    val status: String
)

@Serializable
data class CatatanPengujian(
    val uraian: String? = null,
    val rencana_tindak_lanjut: String? = null,
    val penanggung_jawab_id: String? = null
)
