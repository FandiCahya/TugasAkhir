@extends('layouts.app')
@section('content')
    <div class="col-lg-12 grid-margin stretch-card">
        <div class="card">
            <div class="card-body">
                <h4 class="card-title">Pengembangan</h4>

                <!-- Search Input -->
                <input type="text" id="search" class="form-control mb-3" placeholder="Search by name sistem..." />

                <div class="table-responsive pt-3">
                    <table class="table table-bordered" id="pengembangan-table">
                        <thead>
                            <tr>
                                <th>Nama Sistem</th>
                                <th>Nama User</th>
                                <th>Tgl Mulai</th>
                                <th>Tgl Selesai</th>
                                <th>Tahap</th>
                                <th>Persentase</th>
                                <th>Keterangan</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    {{-- Modal for Edit Pengembangan --}}
    <div class="modal fade" id="editPengembanganModal" tabindex="-1" aria-labelledby="addPengembanganModalLabel"
        aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="editPengembanganModalLabel">Edit Pengembangan</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form id="editPengembanganForm">
                        @csrf
                        <div class="mb-3">
                            <label for="pengajuan" class="form-label">Nama Sistem</label>
                            <input type="text" class="form-control" id="pengajuan" required readonly>
                        </div>
                        <div class="mb-3">
                            <label for="user" class="form-label">User</label>
                            <input type="text" class="form-control" id="user" required readonly>
                        </div>
                        <div class="mb-3">
                            <label for="tgl_mulai" class="form-label">Tanggal Mulai</label>
                            <input type="date" class="form-control" id="tgl_mulai" required>
                        </div>
                        <div class="mb-3">
                            <label for="tgl_selesai" class="form-label">Tanggal Selesai</label>
                            <input type="date" class="form-control" id="tgl_selesai" required>
                        </div>
                        <div class="mb-3">
                            <label for="tahap" class="form-label">Tahap</label>
                            <input type="text" class="form-control" id="tahap" required>
                        </div>
                        <div class="mb-3">
                            <label for="persentase" class="form-label">persentase</label>
                            <input type="text" class="form-control" id="persentase" required>
                        </div>
                        <div class="mb-3">
                            <label for="keterangan" class="form-label">Keterangan</label>
                            <textarea class="form-control" id="keterangan" required></textarea>
                        </div>
                        <div class="mb-3">
                            <label for="status_pengembangan" class="form-label">Status Pengembangan</label>
                            <select class="form-control" id="status_pengembangan" required>
                                <option value="developed">Developed</option>
                                <option value="finished">Finished</option>
                            </select>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Tutup</button>
                    <button type="button" class="btn btn-primary" id="editPengembanganBtn">Edit Pengembangan</button>
                </div>
            </div>
        </div>
    </div>

    {{-- Modal for Add Pengujian --}}
    <div class="modal fade" id="addPengujianModal" tabindex="-1" aria-labelledby="addPengujianModalLabel"
        aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="addPengujianModalLabel">Add Pengujian</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form id="addPengujianForm">
                        @csrf
                        <div class="mb-3">
                            <label for="pengembangan_id" class="form-label">ID Pengembangan</label>
                            <input type="text" class="form-control" id="pengembangan_id" required readonly>
                        </div>
                        <div class="mb-3">
                            <label for="perangkat_lunak" class="form-label">Perangkat Lunak</label>
                            <input type="text" class="form-control" id="perangkat_lunak" required>
                        </div>
                        <div class="mb-3">
                            <label for="versi" class="form-label">Versi</label>
                            <input type="text" class="form-control" id="versi" required>
                        </div>
                        <div class="mb-3">
                            <label for="tujuan" class="form-label">Tujuan</label>
                            <input type="text" class="form-control" id="tujuan" required>
                        </div>
                        <div class="mb-3">
                            <label for="metode_pengujian" class="form-label">Metode</label>
                            <input type="text" class="form-control" id="metode_pengujian" required>
                        </div>
                        <div class="mb-3">
                            <label for="tanggal_pengujian" class="form-label">Tanggal</label>
                            <input type="date" class="form-control" id="tanggal_pengujian" required>
                        </div>
                        <div class="mb-3">
                            <label for="pelaksana_id" class="form-label">Pelaksana</label>
                            <input type="text" class="form-control" id="pelaksana_id" required>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Tutup</button>
                    <button type="button" class="btn btn-primary" id="addPengujianBtn">Tambah Pengujian</button>
                </div>
            </div>
        </div>
    </div>

    <script>
        let pengembangan = [];
        let editPengembanganId = null;

        function fetchPengembangan(query = '') {
            fetch('/api/pengembangan')
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        pengembangan = data.payload; // Simpan data asli

                        // console.log(pengembangan);

                        // Filter berdasarkan query pencarian jika ada
                        if (query) {
                            pengembangan = pengembangan.filter(p =>
                                p.pengajuan.nama_sistem.toLowerCase().includes(query.toLowerCase())
                            );
                        }

                        renderTable(pengembangan); // Render tabel dengan data pengembangan
                    } else {
                        alert('Gagal memuat data pengembangan');
                    }
                })
                .catch(error => console.error('Error:', error));
        }

        function renderTable(pengembangan) {
            const tableBody = document.querySelector('#pengembangan-table tbody');
            tableBody.innerHTML = ''; // Clear the existing table body

            pengembangan.forEach(p => {
                const row = document.createElement('tr');
                row.innerHTML = `
            <td>${p.pengajuan ? p.pengajuan.nama_sistem : '-'}</td>
            <td>${p.pengajuan && p.pengajuan.user ? p.pengajuan.user.name : '-'}</td>
            <td>${p.tanggal_mulai || '-'}</td>
            <td>${p.tanggal_selesai || '-'}</td>
            <td>${p.tahap || '-'}</td>
            <td>${p.persentase || '-'}</td>
            <td>${p.keterangan || '-'}</td>
            <td>${p.status || '-'}</td>
            <td>
                <button class="btn btn-warning btn-sm" onclick="editPengembangan('${p.id}')" style="margin: 5px;">Edit</button>
                <button class="btn btn-danger btn-sm" onclick="deletePengembangan('${p.id}')" style="margin: 5px;">Delete</button>
                <button class="btn btn-success btn-sm" onclick="showPengujian('${p.id}')" style="margin: 5px;">Pengujian</button>
            </td>
        `;
                tableBody.appendChild(row);
            });
        }

        document.getElementById('search').addEventListener('input', function(event) {
            fetchPengembangan(event.target.value); // Re-fetch pengajuan with search query
        });

        function deletePengembangan(id) {
            if (confirm('Yakin ingin menghapus pengembangan ini?')) {
                fetch(`/api/pengembangan/${id}`, {
                        method: 'DELETE',
                        headers: {
                            'Content-Type': 'application/json',
                            'X-CSRF-TOKEN': document.querySelector('meta[name="csrf-token"]').getAttribute('content')
                        },
                    })
                    .then(response => response.json())
                    .then(data => {
                        if (data.success) {
                            fetchPengajuan(); // Re-fetch pengajuan setelah hapus
                            alert('Pengembangan berhasil dihapus');
                        } else {
                            alert('Gagal menghapus pengembangan');
                        }
                    })
                    .catch(error => console.error('Error:', error));
            }
        }

        function editPengembangan(id) {
            const p = pengembangan.find(p => p.id === id); // Find pengembangan by ID
            if (!p) {
                alert('Data tidak ditemukan!');
                return;
            }
            document.getElementById('pengajuan').value = p.pengajuan.nama_sistem;
            document.getElementById('user').value = p.pengajuan.user.name;
            document.getElementById('tgl_mulai').value = p.tanggal_mulai;
            document.getElementById('tgl_selesai').value = p.tanggal_selesai;
            document.getElementById('tahap').value = p.tahap;
            document.getElementById('persentase').value = p.persentase;
            document.getElementById('keterangan').value = p.keterangan;
            document.getElementById('status_pengembangan').value = p.status;
            editPengembanganId = id;

            // Show the modal
            new bootstrap.Modal(document.getElementById('editPengembanganModal')).show();
        }
        
        document.getElementById('editPengembanganBtn').addEventListener('click', function() {
            const data = {
                tanggal_mulai: document.getElementById('tgl_mulai').value,
                tanggal_selesai: document.getElementById('tgl_selesai').value,
                tahap: document.getElementById('tahap').value,
                persentase: parseInt(document.getElementById('persentase').value) || 0,
                keterangan: document.getElementById('keterangan').value,
                status: document.getElementById('status_pengembangan').value,
            };
            // console.log("Data yang dikirim:", data);

            fetch(`/api/pengembangan/${editPengembanganId}`, {
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
                        fetchPengembangan(); // Re-fetch pengajuan setelah update
                        alert('Pengembangan berhasil diupdate');
                        let editModal = bootstrap.Modal.getInstance(document.getElementById(
                            'editPengembanganModal'));
                        if (editModal) {
                            editModal.hide();
                        }
                    } else {
                        alert('Gagal mengupdate Pengembangan');
                    }
                })
                .catch(error => console.error('Error:', error));
        });

        function showPengujian(id) {
            const p = pengembangan.find(p => p.id === id); // Find pengajuan by ID
            // console.log("Hasil find:", p);
            document.getElementById('pengembangan_id').value = p.id;
            // Show the modal
            new bootstrap.Modal(document.getElementById('addPengujianModal')).show();
        }

        document.getElementById('addPengujianBtn').addEventListener('click', function() {
            const data = {
                pengembangan_id: document.getElementById('pengembangan_id').value,
                perangkat_lunak: document.getElementById('perangkat_lunak').value,
                versi: document.getElementById('versi').value,
                tujuan: document.getElementById('tujuan').value,
                metode: document.getElementById('metode_pengujian').value,
                tanggal: document.getElementById('tanggal_pengujian').value,
                pelaksana_id: document.getElementById('pelaksana_id').value,
            };

            // console.log("Data yang dikirim:", data); // Cek apakah datanya lengkap

            fetch(`/api/pengujian`, {
                    method: 'POST',
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
                        fetchPengembangan(); // Re-fetch pengajuan setelah update
                        alert('Pengujian berhasil ditambahkan');
                        const modal = document.querySelector('#addPengujianModal');
                        const modalInstance = bootstrap.Modal.getInstance(modal);
                        modalInstance.hide();
                    } else {
                        alert('Gagal menambahkan pengujian');
                    }
                })
                .catch(error => console.error('Error:', error));
        });

        fetchPengembangan();
    </script>
@endsection
