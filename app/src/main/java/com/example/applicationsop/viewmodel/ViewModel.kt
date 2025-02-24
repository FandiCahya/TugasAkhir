//package com.example.applicationsop.viewmodel
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import com.example.applicationsop.data.DetailInfo
//import com.example.applicationsop.data.getSampleSubmissions
//
//class PermohonanViewModel : ViewModel() {
//
//    // List data permohonan yang akan ditampilkan
//    private val _permohonanList = MutableLiveData<List<DetailInfo>?>()
//    val permohonanList: MutableLiveData<List<DetailInfo>?> get() = _permohonanList
//
//    // Sample data permohonan
//    init {
//        _permohonanList.value = getSampleSubmissions()
//    }
//
//    // Fungsi untuk mengubah status permohonan
//    fun updateStatus(id: Int, newStatus: String) {
//        val updatedList = _permohonanList.value?.map {
//            if (it.id == id) {
//                it.copy(status = newStatus) // Update status sesuai permintaan
//            } else {
//                it
//            }
//        }
//        _permohonanList.value = updatedList
//    }
//
//    // Fungsi untuk memindahkan permohonan ke history (status selesai)
//    fun markAsCompleted(id: Int) {
//        updateStatus(id, "Selesai")
//    }
//}
