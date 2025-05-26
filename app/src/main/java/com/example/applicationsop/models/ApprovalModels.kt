package com.example.applicationsop.models

import com.example.applicationsop.core.UrlConfig
import kotlinx.serialization.Serializable

@Serializable
data class ApprovalResponse(
    val success: Boolean,
    val payload: List<Approval>
)

@Serializable
data class Approval(
    val id: String,
    val status: String,
    val catatan: String?,
    val signature: String?, // Hanya menyimpan path saja
    val persetujuan_pengujian: PersetujuanPengujian,
    val user: User
) {
    // Fungsi untuk mendapatkan URL lengkap signature
    fun getSignatureUrl(): String? {
        return signature?.let { "${UrlConfig.BASE_URL}storage/$it" }
    }
}

@Serializable
data class PersetujuanPengujian(
    val id: String,
    val status: String,
    val created_at: String,
    val updated_at: String
)

@Serializable
data class UserApproval(
    val id: String,
    val name: String,
    val email: String,
    val role: String,
    val devisi: String
)
