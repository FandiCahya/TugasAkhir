@extends('layouts.app')
@section('content')
    <div class="col-lg-12 grid-margin stretch-card">
        <div class="card">
            <div class="card-body">
                <h4 class="card-title">Pengajuan</h4>

                <!-- Button to Open Add pengajuan Modal -->
                <button class="btn btn-success btn-sm mb-3" data-bs-toggle="modal" data-bs-target="#addPengajuanModal">Tambah
                    Pengajuan</button>

                <!-- Search Input -->
                <input type="text" id="search" class="form-control mb-3" placeholder="Search by name sistem..." />

                <div class="table-responsive pt-3">
                    <table class="table table-bordered" id="pengajuan-table">
                        <thead>
                            <tr>
                                <th style="width: 15%;">Nama Sistem <span id="sort-name" class="cursor-pointer">🔽</span>
                                </th>
                                <th style="width: 15%;">Nama User</th>
                                <th style="width: 5%;">Devisi</th>
                                <th style="width: 10%;">Jenis</th>
                                <th style="width: 10%;">Rencana Anggaran</th>
                                <th style="width: 15%;">Masalah</th>
                                <th style="width: 10%;">Output</th>
                                <th style="width: 10%;">Status</th>
                                <th style="width: 15%;">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <!-- Data will be dynamically filled here using JavaScript -->
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal for Adding Pengajuan -->
    <div class="modal fade" id="addPengajuanModal" tabindex="-1" aria-labelledby="addPengajuanModalLabel"
        aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="addPengajuanModalLabel">Tambah Pengajuan Baru</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form id="addPengajuanForm">
                        @csrf
                        <div class="mb-3">
                            <label for="tgl" class="form-label">Tanggal Pengajuan</label>
                            <input type="date" class="form-control" id="tgl" required>
                        </div>
                        <div class="mb-3">
                            <label for="nama_sistem" class="form-label">Nama Sistem</label>
                            <input type="text" class="form-control" id="nama_sistem" required>
                        </div>
                        <div class="mb-3">
                            <label for="jenis" class="form-label">Jenis Pengajuan</label>
                            <select class="form-control" id="jenis" required>
                                <option value="sistem_baru">Sistem Baru</option>
                                <option value="pengembangan">Pengembangan</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="rencana_anggaran" class="form-label">Rencana Anggaran</label>
                            <select class="form-control" id="rencana_anggaran" required>
                                <option value="termasuk_dalam_perencanaan">Termasuk dalam Perencanaan</option>
                                <option value="tidak_termasuk_perencanaan">Tidak Termasuk dalam Perencanaan</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="masalah" class="form-label">Masalah yang Dihadapi</label>
                            <textarea class="form-control" id="masalah" rows="3" required></textarea>
                        </div>
                        <div class="mb-3">
                            <label for="output" class="form-label">Output yang Diharapkan</label>
                            <textarea class="form-control" id="output" rows="3" required></textarea>
                        </div>
                        <div class="mb-3">
                            <label for="status" class="form-label">Status Pengajuan</label>
                            <select class="form-control" id="status" required>
                                <option value="draft">Draft</option>
                                <option value="accepted">Accepted</option>
                                <option value="rejected">Rejected</option>
                            </select>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Tutup</button>
                    <button type="button" class="btn btn-primary" id="savePengajuanBtn">Simpan Pengajuan</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal for Editing Pengajuan -->
    <div class="modal fade" id="editPengajuanModal" tabindex="-1" aria-labelledby="editPengajuanModalLabel"
        aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="editPengajuanModalLabel">Edit Pengajuan</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form id="editPengajuanForm">
                        @csrf
                        <div class="mb-3">
                            <label for="tgl" class="form-label">Tanggal Pengajuan</label>
                            <input type="text" class="form-control" id="edit-tgl" required readonly>
                        </div>
                        <div class="mb-3">
                            <label for="nama_sistem" class="form-label">Nama Sistem</label>
                            <input type="text" class="form-control" id="edit-nama_sistem" required readonly>
                        </div>
                        <div class="mb-3">
                            <label for="jenis" class="form-label">Jenis Pengajuan</label>
                            <input type="text" class="form-control" id="edit-jenis" required readonly>
                        </div>
                        <div class="mb-3">
                            <label for="rencana_anggaran" class="form-label">Rencana Anggaran</label>
                            <input type="text" class="form-control" id="edit-rencana_anggaran" required readonly>
                        </div>
                        <div class="mb-3">
                            <label for="masalah" class="form-label">Masalah yang Dihadapi</label>
                            <textarea class="form-control" id="edit-masalah" rows="3" required readonly></textarea>
                        </div>
                        <div class="mb-3">
                            <label for="output" class="form-label">Output yang Diharapkan</label>
                            <textarea class="form-control" id="edit-output" rows="3" required readonly></textarea>
                        </div>
                        <div class="mb-3">
                            <label for="status" class="form-label">Status Pengajuan</label>
                            <select class="form-control" id="edit-status" required>
                                <option value="draft">Draft</option>
                                <option value="rejected">Rejected</option>
                                <option value="accepted">Accepted</option>
                            </select>
                        </div>
                        <div class="mb-3" id="alasan-penolakan-div" style="display: none;">
                            <label for="alasan_penolakan" class="form-label">Alasan Penolakan</label>
                            <textarea class="form-control" id="edit-alasan_penolakan" rows="3"></textarea>
                        </div>

                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Tutup</button>
                    <button type="button" class="btn btn-primary" id="updatePengajuanBtn">Update Pengajuan</button>
                </div>
            </div>
        </div>
    </div>

    {{-- Modal for Add Pengembangan --}}
    <div class="modal fade" id="addPengembanganModal" tabindex="-1" aria-labelledby="addPengembanganModalLabel"
        aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="addPengembanganModalLabel">Add Pengembangan</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form id="addPengembanganForm">
                        @csrf
                        <div class="mb-3">
                            <label for="pengajuan_id" class="form-label">ID Pengajuan</label>
                            <input type="text" class="form-control" id="pengajuan_id" required readonly>
                        </div>
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
                    <button type="button" class="btn btn-primary" id="addPengembanganBtn">Tambah Pengembangan</button>
                </div>
            </div>
        </div>
    </div>

    <script>
        let pengajuan = []; // Array to hold fetched pengajuan
        let editPengajuanId = null;
        let addPengembangan = null;

        // Fetch Pengajuan from API
        function fetchPengajuan(query = '') {
            fetch('/api/pengajuan')
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        pengajuan = data.payload;

                        // Filter berdasarkan query pencarian jika ada
                        if (query) {
                            pengajuan = pengajuan.filter(p => p.nama_sistem.toLowerCase().includes(query
                                .toLowerCase()));
                        }

                        renderTable(pengajuan); // Render tabel dengan data pengajuan
                    } else {
                        alert('Gagal memuat data pengajuan');
                    }
                })
                .catch(error => console.error('Error:', error));
        }

        // Render data in the table
        function renderTable(pengajuan) {
            const tableBody = document.querySelector('#pengajuan-table tbody');
            tableBody.innerHTML = ''; // Clear the existing table body
            pengajuan.forEach(p => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${p.nama_sistem}</td>
                    <td>${p.user ? p.user.name : ''}</td>
                    <td>${p.user ? p.user.devisi : ''}</td>
                    <td>${p.jenis}</td>
                    <td>${p.rencana_anggaran}</td>
                    <td>${p.masalah}</td>
                    <td>${p.output}</td>
                    <td>${p.status}</td>
                    <td>
                        <button class="btn btn-warning btn-sm" onclick="editPengajuan('${p.id}')" style="margin: 5px;">Edit</button>
                        <button class="btn btn-danger btn-sm" onclick="deletePengajuan('${p.id}')" style="margin: 5px;">Delete</button>
                        ${p.status === 'accepted' ? `<button class="btn btn-success btn-sm" onclick="showPengembangan('${p.id}')" style="margin: 5px;">Pengembangan</button>` : ''}
                    </td>

                `;
                tableBody.appendChild(row);
            });
        }

        // Show Pengembangan
        function showPengembangan(id) {
            const p = pengajuan.find(p => p.id === id); // Find pengajuan by ID
            // console.log("Hasil find:", p);
            document.getElementById('pengajuan_id').value = p.id;
            document.getElementById('pengajuan').value = p.nama_sistem;
            document.getElementById('user').value = p.user.name;
            // Show the modal
            new bootstrap.Modal(document.getElementById('addPengembanganModal')).show();
        }

        // tambah Pengembangan
        document.getElementById('addPengembanganBtn').addEventListener('click', function() {
            const data = {
                pengajuan_id: document.getElementById('pengajuan_id').value,
                tanggal_mulai: document.getElementById('tgl_mulai').value,
                tanggal_selesai: document.getElementById('tgl_selesai').value,
                tahap: document.getElementById('tahap').value,
                persentase: parseInt(document.getElementById('persentase').value) || 0,
                keterangan: document.getElementById('keterangan').value,
                status: document.getElementById('status_pengembangan').value,
            };

            // console.log("Data yang dikirim:", data); // Cek apakah datanya lengkap

            fetch(`/api/pengembangan`, {
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
                        fetchPengajuan(); // Re-fetch pengajuan setelah update
                        alert('Pengembangan berhasil ditambahkan');
                        const modal = document.querySelector('#addPengembanganModal');
                        const modalInstance = bootstrap.Modal.getInstance(modal);
                        modalInstance.hide();
                    } else {
                        alert('Gagal menambahkan pengembangan');
                    }
                })
                .catch(error => console.error('Error:', error));
        });

        function editPengajuan(id) {
            const p = pengajuan.find(p => p.id === id); // Find pengajuan by ID
            document.getElementById('edit-tgl').value = p.tgl;
            document.getElementById('edit-nama_sistem').value = p.nama_sistem;
            document.getElementById('edit-jenis').value = p.jenis;
            document.getElementById('edit-rencana_anggaran').value = p.rencana_anggaran;
            document.getElementById('edit-masalah').value = p.masalah;
            document.getElementById('edit-output').value = p.output;
            document.getElementById('edit-status').value = p.status;
            editPengajuanId = id;

            // Show the modal
            new bootstrap.Modal(document.getElementById('editPengajuanModal')).show();
        }


        // Save new pengajuan
        document.getElementById('savePengajuanBtn').addEventListener('click', function() {
            const data = {
                tgl: document.getElementById('tgl').value,
                nama_sistem: document.getElementById('nama_sistem').value,
                jenis: document.getElementById('jenis').value,
                rencana_anggaran: document.getElementById('rencana_anggaran').value,
                masalah: document.getElementById('masalah').value,
                output: document.getElementById('output').value,
                status: document.getElementById('status').value,
            };
            fetch('/api/pengajuan', {
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
                        fetchPengajuan(); // Re-fetch pengajuan setelah menambah data
                        alert('Pengajuan berhasil ditambahkan');
                    } else {
                        alert('Gagal menambahkan pengajuan');
                    }
                })
                .catch(error => console.error('Error:', error));
        });

        // Update pengajuan
        document.getElementById('updatePengajuanBtn').addEventListener('click', function() {
            const data = {
                nama_sistem: document.getElementById('edit-nama_sistem').value,
                jenis: document.getElementById('edit-jenis').value,
                rencana_anggaran: document.getElementById('edit-rencana_anggaran').value,
                masalah: document.getElementById('edit-masalah').value,
                output: document.getElementById('edit-output').value,
                status: document.getElementById('edit-status').value,
            };

            fetch(`/api/pengajuan/${editPengajuanId}`, {
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
                        fetchPengajuan(); // Re-fetch pengajuan setelah update
                        alert('Pengajuan berhasil diupdate');
                        let editModal = bootstrap.Modal.getInstance(document.getElementById(
                            'editPengajuanModal'));
                        if (editModal) {
                            editModal.hide();
                        }
                    } else {
                        alert('Gagal mengupdate pengajuan');
                    }
                })
                .catch(error => console.error('Error:', error));
        });

        // Delete pengajuan
        function deletePengajuan(id) {
            if (confirm('Yakin ingin menghapus pengajuan ini?')) {
                fetch(`/api/pengajuan/${id}`, {
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
                            alert('Pengajuan berhasil dihapus');
                        } else {
                            alert('Gagal menghapus pengajuan');
                        }
                    })
                    .catch(error => console.error('Error:', error));
            }
        }

        // Search functionality
        document.getElementById('search').addEventListener('input', function(event) {
            fetchPengajuan(event.target.value); // Re-fetch pengajuan with search query
        });

        document.getElementById('edit-status').addEventListener('change', function() {
            const status = this.value;
            const alasanDiv = document.getElementById('alasan-penolakan-div');

            // Jika status = "rejected", tampilkan alasan penolakan
            if (status === 'rejected') {
                alasanDiv.style.display = 'block';
            } else {
                alasanDiv.style.display = 'none';
            }
        });

        // Fetch pengajuan initially
        fetchPengajuan();
    </script>
@endsection
