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
    val updated_at: String,
    val created_at: String,
    val pelaksana: Pelaksana?,
    val pengembangan: GetPengembangan?,
    val pengujian_detail: List<GetPengujianDetail>?,
    val catatan: List<Catatan>?,
    val persetujuan: List<Persetujuan>?
)

@Serializable
data class GetPengujianDetail(
    val id: String,
    val nama_uji: String,
    val kasus_uji: String,
    val hasil_diharapkan: String,
    val hasil_pengujian: String,
    val kategori: String,
    val status: String
)

@Serializable
data class Pelaksana(
    val id: String?,
    val name: String? = null,
    val email: String?,
    val devisi: String? = null,
    val role: String? = null
)

@Serializable
data class GetPengembangan(
    val id: String?,
    val tanggal_mulai: String?,
    val tanggal_selesai: String?,
    val tahap: String?,
    val persentase: Int?,
    val keterangan: String?,
    val status: String?,
    val pengajuan: GetPengajuan?
)

@Serializable
data class GetPengajuan(
    val id: String?,
    val tgl: String?,
    val nama_sistem: String?,
    val jenis: String?,
    val rencana_anggaran: String?,
    val masalah: String?,
    val output: String?,
    val status: String?,
    val created_at: String?,
    val user: Pelaksana?
)

@Serializable
data class Catatan(
    val id: String?,
    val uraian: String?,
    val rencana_tindak_lanjut: String?,
    val penanggung_jawab: String?,
    val created_at: String?
)

@Serializable
data class Persetujuan(
    val id: String?,
    val status: String?,
    val tanggal_persetujuan: String?,
    val disetujui_oleh: Pelaksana?
)

@Serializable
data class ResponsePengujian(
    val success: Boolean,
    val payload: List<Pengujian>
)


