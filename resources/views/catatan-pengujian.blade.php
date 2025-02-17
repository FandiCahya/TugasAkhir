@extends('layouts.app')
@section('content')
    <div class="col-lg-12 grid-margin stretch-card">
        <div class="card">
            <div class="card-body">
                <h4 class="card-title">Catatan Pengujian</h4>

                <!-- Search Input -->
                <input type="text" id="search" class="form-control mb-3" placeholder="Search by .." />

                <div class="table-responsive pt-3">
                    <table class="table table-bordered" id="pengujian-table">
                        <thead>
                            {{-- Thead --}}
                        </thead>
                        <tbody>
                            {{-- Tbody --}}
                        </tbody>
                    </table>
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
                    <h5 class="modal-title" id="editPengujianModalLabel">Edit Catatan Pengujian</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form id="editPengujianForm">
                        @csrf
                        <div class="mb-3">
                            <label for="edit_uraian" class="form-label">Uraian</label>
                            <input type="text" class="form-control" id="edit_uraian" required>
                        </div>
                        <div class="mb-3">
                            <label for="edit_rencana_tindak_lanjut" class="form-label">Rencana Tindak Lanjut</label>
                            <input type="text" class="form-control" id="edit_rencana_tindak_lanjut" required>
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
                    Apakah Anda yakin ingin menghapus catatan pengujian ini?
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Batal</button>
                    <button type="button" class="btn btn-danger" id="deletePengujianBtn">Hapus</button>
                </div>
            </div>
        </div>
    </div>


    <script>
        let CatatanPengujian = [];
        let currentDeleteId = null;
        let currentEditId = null;

        // Fungsi untuk mengambil data pengujian-detail dan merendernya
        function fetchPengujianDetail(query = '') {
            fetch('/api/catatan-pengujian')
                .then(response => response.json())
                .then(data => {
                    if (data.success) {

                        CatatanPengujian = data.payload;
                        // Kelompokkan data berdasarkan pengujian_id
                        let groupedPengujian = groupByPengujianId(CatatanPengujian);

                        // Jika ada query pencarian, filter berdasarkan nama sistem
                        if (query) {
                            groupedPengujian = filterGroupedPengujian(groupedPengujian, query);
                        }

                        renderTable(groupedPengujian);
                    } else {
                        alert('Gagal memuat data pengujian');
                    }
                })
                .catch(error => console.error('Error:', error));
        }

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
                    p.uraian.toLowerCase().includes(query.toLowerCase())
                );

                if (filteredGroup.length > 0) {
                    filteredData[key] = filteredGroup;
                }
            }

            return filteredData;
        }

        // Fungsi untuk merender tabel
        function renderTable(groupedPengujian) {
            const tableBody = document.querySelector('#pengujian-table tbody');
            tableBody.innerHTML = ''; // Clear the existing table body

            for (const pengujianId in groupedPengujian) {
                const group = groupedPengujian[pengujianId];

                // Create the group header row
                const groupRow = document.createElement('tr');
                groupRow.classList.add('table-group-header'); // Add a class for custom styling

                groupRow.innerHTML = `
            <td colspan="7" class="group-header">
                <strong>Pengujian ID: ${group[0].pengujian.id}</strong><br>
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
                
                <td>${p.uraian}</td>
                <td>${p.rencana_tindak_lanjut}</td>
                <td class="action-buttons">
                    <button class="btn btn-warning btn-sm" onclick="editPengujian('${p.id}')" style="margin: 5px;">Edit</button>
                    <button class="btn btn-danger btn-sm" onclick="deletePengujian('${p.id}')" style="margin: 5px;">Delete</button>
                </td>
            `;
                    tableBody.appendChild(row);
                });
            }
        }

        function editPengujian(id) {
            // Find the selected record
            const p = CatatanPengujian.find(p => p.id === id);
            if (!p) {
                alert('Data tidak ditemukan!');
                return;
            }
            console.log("data", p);

            // Populate the form fields with the current values
            document.getElementById('edit_uraian').value = p.uraian;
            document.getElementById('edit_rencana_tindak_lanjut').value = p.rencana_tindak_lanjut;

            currentEditId = id;
            // Show the Edit Modal
            new bootstrap.Modal(document.getElementById('editPengujianModal')).show();
        }

        document.getElementById('saveEditPengujianBtn').addEventListener('click', function() {
            const data = {
                uraian: document.getElementById('edit_uraian').value,
                rencana_tindak_lanjut: document.getElementById('edit_rencana_tindak_lanjut').value,
            };

            // Send the updated data to the backend
            fetch(`/api/catatan-pengujian/${currentEditId}`, {
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
                        alert('Catatan Pengujian berhasil diperbarui');
                        const modal = bootstrap.Modal.getInstance(document.getElementById(
                            'editPengujianModal'));
                        modal.hide();
                    } else {
                        alert('Gagal memperbarui catatan pengujian');
                    }
                })
                .catch(error => console.error('Error:', error));
        });
        function deletePengujian(id) {
            currentDeleteId = id;

            // Show the Delete Confirmation Modal
            new bootstrap.Modal(document.getElementById('deletePengujianModal')).show();
        }

        document.getElementById('deletePengujianBtn').addEventListener('click', function() {
            // Send the delete request to the backend
            fetch(`/api/catatan-pengujian/${currentDeleteId}`, {
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
                        alert('Catatan Pengujian berhasil dihapus');
                        const modal = bootstrap.Modal.getInstance(document.getElementById(
                            'deletePengujianModal'));
                        modal.hide();
                    } else {
                        alert('Gagal menghapus catatan pengujian');
                    }
                })
                .catch(error => console.error('Error:', error));
        });

        // Memanggil fetchPengujianDetail ketika halaman dimuat
        fetchPengujianDetail();
    </script>
@endsection
