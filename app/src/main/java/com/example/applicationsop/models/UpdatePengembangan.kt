package com.example.applicationsop.models

import kotlinx.serialization.Serializable

@Serializable
data class UpdatePengembangan(
    val tahap: String? = null,
    val persentase: Int? = null,
    val status: String? = null
)