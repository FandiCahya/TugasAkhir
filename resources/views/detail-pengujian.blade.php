@extends('layouts.app')
@section('content')
    <div class="col-lg-12 grid-margin stretch-card">
        <div class="card">
            <div class="card-body">
                <h4 class="card-title">Detail Pengujian</h4>

                <!-- Search Input -->
                <input type="text" id="search" class="form-control mb-3" placeholder="Search by Perangkat Lunak atau Nama Uji .." />

                <div class="table-responsive pt-3">
                    <table class="table table-bordered" id="pengujian-table">
                        <thead>

                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                    <div id="pagination-controls" class="mt-3 d-flex justify-content-center"></div>
                </div>
            </div>
        </div>
    </div>

    <!-- Edit Modal -->
    <div class="modal fade" id="editPengujianModal" tabindex="-1" aria-labelledby="editPengujianModalLabel"
        aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="editPengujianModalLabel">Edit Detail Pengujian</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form id="editPengujianForm">
                        @csrf
                        <div class="mb-3">
                            <label for="edit_nama_uji" class="form-label">Nama Uji</label>
                            <input type="text" class="form-control" id="edit_nama_uji" required>
                        </div>
                        <div class="mb-3">
                            <label for="edit_kasus_uji" class="form-label">Kasus Uji</label>
                            <input type="text" class="form-control" id="edit_kasus_uji" required>
                        </div>
                        <div class="mb-3">
                            <label for="edit_hasildiharapkan" class="form-label">Hasil Diharapkan</label>
                            <input type="text" class="form-control" id="edit_hasildiharapkan" required>
                        </div>
                        <div class="mb-3">
                            <label for="edit_hasilpengujian" class="form-label">Hasil Pengujian</label>
                            <input type="text" class="form-control" id="edit_hasilpengujian" required>
                        </div>
                        <div class="mb-3">
                            <label for="edit_status" class="form-label">Status</label>
                            <select class="form-select" id="edit_status" required>
                                <option value="OK">OK</option>
                                <option value="Tidak">Tidak</option>
                            </select>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Tutup</button>
                    <button type="button" class="btn btn-primary" id="saveEditPengujianBtn">Simpan Perubahan</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Delete Confirmation Modal -->
    <div class="modal fade" id="deletePengujianModal" tabindex="-1" aria-labelledby="deletePengujianModalLabel"
        aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="deletePengujianModalLabel">Konfirmasi Hapus</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    Apakah Anda yakin ingin menghapus pengujian ini?
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Batal</button>
                    <button type="button" class="btn btn-danger" id="deletePengujianBtn">Hapus</button>
                </div>
            </div>
        </div>
    </div>


    <script>
        let DetailPengujian = [];
        let currentEditId = null;
        let currentDeleteId = null;
        let groupedPengujian = {};
        let currentPage = 1;
        const rowsPerPage = 2;

        // Fungsi untuk mengambil data pengujian-detail dan merendernya
        function fetchPengujianDetail(query = '') {
            fetch('/api/pengujian-detail')
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        let filteredData = data.payload;
                        // Lakukan filter sebelum pengelompokan
                        if (query) {
                            filteredData = filteredData.filter(p =>
                                p.nama_uji.toLowerCase().includes(query.toLowerCase()) ||
                                p.pengujian.perangkat_lunak.toLowerCase().includes(query.toLowerCase()) // Tambahkan kolom lain
                            );
                        }
                        // Kelompokkan hasil yang sudah difilter
                        groupedPengujian = groupByPengujianId(filteredData);
                        renderTable(groupedPengujian, 1);
                    } else {
                        alert('Gagal memuat data pengujian');
                    }
                })
                .catch(error => console.error('Error:', error));
        }


        document.getElementById('search').addEventListener('input', function(event) {
            fetchPengujianDetail(event.target.value); // Re-fetch pengujian with search query
        });


        // Fungsi untuk mengelompokkan data berdasarkan pengujian.id
        function groupByPengujianId(data) {
            return data.reduce((acc, p) => {
                const pengujianId = p.pengujian.id;
                if (!acc[pengujianId]) {
                    acc[pengujianId] = [];
                }
                acc[pengujianId].push(p); // Menambahkan item ke grup yang sesuai
                return acc;
            }, {});
        }

        // Fungsi untuk filter data yang sudah dikelompokkan
        function filterGroupedPengujian(groupedData, query) {
            let filteredData = {};

            for (const key in groupedData) {
                // Filter setiap grup berdasarkan nama_sistem
                const filteredGroup = groupedData[key].filter(p =>
                    p.nama_uji.toLowerCase().includes(query.toLowerCase())
                );

                if (filteredGroup.length > 0) {
                    filteredData[key] = filteredGroup;
                }
            }

            return filteredData;
        }

        // Fungsi untuk merender tabel
        function renderTable(groupedPengujian, page = 1) {
            const tableBody = document.querySelector('#pengujian-table tbody');
            tableBody.innerHTML = ''; // Clear the existing table body

            const keys = Object.keys(groupedPengujian);
            const totalPages = Math.ceil(keys.length / rowsPerPage);

            // Tentukan indeks awal dan akhir untuk slicing data
            const startIndex = (page - 1) * rowsPerPage;
            const endIndex = startIndex + rowsPerPage;

            // Ambil data sesuai halaman
            const paginatedKeys = keys.slice(startIndex, endIndex);

            paginatedKeys.forEach(pengujianId => {
                const group = groupedPengujian[pengujianId];

                // Create the group header row
                const groupRow = document.createElement('tr');
                groupRow.classList.add('table-group-header'); // Add a class for custom styling

                groupRow.innerHTML = `
            <td colspan="7" class="group-header">
                <span>Perangkat Lunak: <strong>${group[0].pengujian.perangkat_lunak}</strong></span> | 
                <span>Versi: <strong>${group[0].pengujian.versi}</strong></span> | 
                <span>Tujuan: <strong>${group[0].pengujian.tujuan}</strong></span> | 
                <span>Metode: <strong>${group[0].pengujian.metode}</strong></span> | 
                <span>Tanggal: <strong>${group[0].pengujian.tanggal}</strong></span>
            </td>
        `;
                tableBody.appendChild(groupRow);

                // Render each detail row in the group
                group.forEach(p => {
                    const row = document.createElement('tr');
                    row.classList.add('table-row'); // Add a class for custom styling

                    row.innerHTML = `
                <td>${p.nama_uji}</td>
                <td>${p.kasus_uji}</td>
                <td>${p.hasil_diharapkan}</td>
                <td>${p.hasil_pengujian}</td>
                <td>${p.status}</td>
                <td class="action-buttons">
                    <button class="btn btn-warning btn-sm" onclick="editPengujian('${p.id}')" style="margin: 5px;">Edit</button>
                    <button class="btn btn-danger btn-sm" onclick="deletePengujian('${p.id}')" style="margin: 5px;">Delete</button>
                </td>
            `;
                    tableBody.appendChild(row);
                });
            });

            // Tampilkan pagination controls
            renderPaginationControls(totalPages);
        }

        function changePage(page) {
            const totalPages = Math.ceil(Object.keys(groupedPengujian).length / rowsPerPage);
            if (page < 1 || page > totalPages) return;

            currentPage = page;
            renderTable(groupedPengujian, currentPage);
        }

        function renderPaginationControls(totalPages) {
            const paginationContainer = document.querySelector('#pagination-controls');
            paginationContainer.innerHTML = '';

            if (totalPages <= 1) return;

            let paginationHTML = '';

            if (currentPage > 1) {
                paginationHTML +=
                    `<button onclick="changePage(${currentPage - 1})" class="btn btn-secondary mx-1">Previous</button>`;
            }

            for (let i = 1; i <= totalPages; i++) {
                paginationHTML +=
                    `<button onclick="changePage(${i})" class="btn ${i === currentPage ? 'btn-secondary' : 'btn-outline-secondary'} mx-1">${i}</button>`;
            }

            if (currentPage < totalPages) {
                paginationHTML +=
                    `<button onclick="changePage(${currentPage + 1})" class="btn btn-secondary mx-1">Next</button>`;
            }

            paginationContainer.innerHTML = paginationHTML;
        }


        function editPengujian(id) {
            const p = DetailPengujian.find(p => p.id === id);
            if (!p) {
                alert('Data tidak ditemukan!');
                return;
            }

            // Populate the form fields with the current values
            document.getElementById('edit_nama_uji').value = p.nama_uji;
            document.getElementById('edit_kasus_uji').value = p.kasus_uji;
            document.getElementById('edit_hasildiharapkan').value = p.hasil_diharapkan;
            document.getElementById('edit_hasilpengujian').value = p.hasil_pengujian;
            document.getElementById('edit_status').value = p.status;

            editDetailPengujianId = id;

            // Show the modal
            new bootstrap.Modal(document.getElementById('editPengujianModal')).show();
        }

        document.getElementById('saveEditPengujianBtn').addEventListener('click', function() {
            const data = {
                nama_uji: document.getElementById('edit_nama_uji').value,
                kasus_uji: document.getElementById('edit_kasus_uji').value,
                hasil_diharapkan: document.getElementById('edit_hasildiharapkan').value,
                hasil_pengujian: document.getElementById('edit_hasilpengujian').value,
                status: document.getElementById('edit_status').value
            };
            console.log("Data : ", data)

            // Send the updated data to the backend
            fetch(`/api/pengujian-detail/${editDetailPengujianId}`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                        'X-CSRF-TOKEN': document.querySelector('meta[name="csrf-token"]').getAttribute(
                            'content')
                    },
                    body: JSON.stringify(data)
                })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        fetchPengujianDetail(); // Refresh the data
                        alert('Pengujian berhasil diperbarui');
                        const modal = bootstrap.Modal.getInstance(document.getElementById(
                            'editPengujianModal'));
                        modal.hide();
                    } else {
                        alert('Gagal memperbarui pengujian');
                    }
                })
                .catch(error => console.error('Error:', error));
        });

        function deletePengujian(id) {
            editDetailPengujianId = id;

            // Show the confirmation modal
            new bootstrap.Modal(document.getElementById('deletePengujianModal')).show();
        }

        document.getElementById('deletePengujianBtn').addEventListener('click', function() {
            // Send the delete request to the backend
            fetch(`/api/pengujian-detail/${currentDeleteId}`, {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json',
                        'X-CSRF-TOKEN': document.querySelector('meta[name="csrf-token"]').getAttribute(
                            'content')
                    }
                })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        fetchPengujianDetail(); // Refresh the data
                        alert('Pengujian berhasil dihapus');
                        const modal = bootstrap.Modal.getInstance(document.getElementById(
                            'deletePengujianModal'));
                        modal.hide();
                    } else {
                        alert('Gagal menghapus pengujian');
                    }
                })
                .catch(error => console.error('Error:', error));
        });
        // Memanggil fetchPengujianDetail ketika halaman dimuat
        fetchPengujianDetail();
    </script>
@endsection
