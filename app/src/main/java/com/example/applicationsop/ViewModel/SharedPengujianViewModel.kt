package com.example.applicationsop.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.applicationsop.models.PersetujuanPengujianDetail

class SharedPengujianViewModel : ViewModel() {
    var detailPersetujuan = mutableStateOf<List<PersetujuanPengujianDetail>>(emptyList())
}