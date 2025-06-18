package com.example.applicationsop.models

import kotlinx.serialization.Serializable

@Serializable
data class ResponseLaporan(
    val success: Boolean,
    val payload: List<Laporan>
)

@Serializable
data class Laporan(
    val pengajuan: PengajuanLaporan,
    val pengembangan: PengembanganLaporan? = null,
    val pengujian: PengujianLaporan? = null,
    val persetujuan_pengujian: PersetujuanPengujianLaporan? = null,
    val persetujuan_pengujian_details: List<PersetujuanPengujianDetail> = emptyList()
)

@Serializable
data class PengajuanLaporan(
    val id: String,
    val tgl: String,
    val nama_sistem: String,
    val jenis: String,
    val rencana_anggaran: String,
    val masalah: String,
    val output: String,
    val status: String,
    val created_at: String,
    val user: UserLaporan
)

@Serializable
data class UserLaporan(
    val id: String,
    val name: String,
    val email: String,
    val devisi: String,
    val role: String,
    val created_at: String
)

@Serializable
data class PengembanganLaporan(
    val id: String,
    val tanggal_mulai: String,
    val tanggal_selesai: String,
    val tahap: String,
    val persentase: Int,
    val keterangan: String,
    val status: String
)

@Serializable
data class PengujianLaporan(
    val id: String,
    val perangkat_lunak: String,
    val versi: String,
    val tujuan: String,
    val metode: String,
    val tanggal: String
)

@Serializable
data class PersetujuanPengujianLaporan(
    val id: String,
    val status: String,
    val created_at: String,
    val updated_at: String
)

@Serializable
data class PersetujuanPengujianDetail(
    val id: String,
    val status: String,
    val catatan: String? = null,
    val signature: String? = null, // <- Ubah jadi nullable
    val role: String,
    val user: UserDetail
)


@Serializable
data class UserDetail(
    val id: String,
    val name: String,
    val email: String,
)
