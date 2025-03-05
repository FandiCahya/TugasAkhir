package com.example.applicationsop.models

import kotlinx.serialization.Serializable

@Serializable
data class PengujianRequest(
    val pengembangan_id: String?,
    val perangkat_lunak: String?,
    val versi: String?,
    val tujuan: String?,
    val metode: String?,
    val tanggal: String?,
    val pelaksana_id: String?
)
