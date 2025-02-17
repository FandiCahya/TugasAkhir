@extends('layouts.app')
@section('content')
    <div class="col-lg-12 grid-margin stretch-card">
        <div class="card">
            <div class="card-body">
                <h4 class="card-title">Pengujian</h4>

                <!-- Search Input -->
                <input type="text" id="search" class="form-control mb-3" placeholder="Search by .." />

                <div class="table-responsive pt-3">
                    <table class="table table-bordered" id="pengujian-table">
                        <thead>
                            <tr>
                                <th>Tanggal</th>
                                <th>Name Sistem</th>
                                <th>Perangkat</th>
                                <th>Versi</th>
                                <th>Tujuan</th>
                                <th>Metode</th>
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

    {{-- Modal for Edit Pengujian --}}
    <div class="modal fade" id="editPengujianModal" tabindex="-1" aria-labelledby="addPengujianModalLabel"
        aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="editPengujianModalLabel">Edit Pengujian</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form id="editPengujianForm">
                        @csrf
                        <div class="mb-3">
                            <label for="nama_sistem" class="form-label">Nama Sistem</label>
                            <input type="text" class="form-control" id="nama_sistem" required readonly>
                        </div>
                        <div class="mb-3">
                            <label for="perangkat_lunak" class="form-label">perangkat_lunak</label>
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
                    <button type="button" class="btn btn-primary" id="editPengujianBtn">Edit Pengujian</button>
                </div>
            </div>
        </div>
    </div>

    <div class="modal fade" id="addPengujianDetailModal" tabindex="-1" aria-labelledby="addPengujianDetailModalLabel"
        aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="addPengujianDetailModalLabel">Tambah Detail Pengujian</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form id="addPengujianDetailForm">
                        @csrf
                        <div class="mb-3">
                            <label for="pengujian_id" class="form-label">ID Pengujian</label>
                            <input type="text" class="form-control" id="pengujian_id" required readonly>
                        </div>
                        <div class="mb-3">
                            <label for="nama_uji" class="form-label">Nama Uji</label>
                            <input type="text" class="form-control" id="nama_uji" required>
                        </div>
                        <div class="mb-3">
                            <label for="kasus_uji" class="form-label">Kasus Uji</label>
                            <input type="text" class="form-control" id="kasus_uji" required>
                        </div>
                        <div class="mb-3">
                            <label for="hasil_diharapkan" class="form-label">Hasil Diharapkan</label>
                            <input type="text" class="form-control" id="hasil_diharapkan" required>
                        </div>
                        <div class="mb-3">
                            <label for="hasil_pengujian" class="form-label">Hasil Pengujian</label>
                            <input type="text" class="form-control" id="hasil_pengujian">
                        </div>
                        <div class="mb-3">
                            <label for="status" class="form-label">Status</label>
                            <select class="form-select" id="status" required>
                                <option value="" disabled selected>Pilih Status</option>
                                <option value="OK">Ok</option>
                                <option value="Tidak">Tidak</option>
                            </select>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Tutup</button>
                    <button type="button" class="btn btn-primary" id="addPengujianDetailBtn">Tambah Detail
                        Pengujian</button>
                </div>
            </div>
        </div>
    </div>

    <div class="modal fade" id="addCatatanPengujianModal" tabindex="-1" aria-labelledby="addCatatanPengujianModalLabel"
        aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="addCatatanPengujianModalLabel">Tambah Catatan Pengujian</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form id="addCatatanPengujianForm">
                        @csrf
                        <div class="mb-3">
                            <label for="pengujian_id_catatan" class="form-label">ID Pengujian</label>
                            <input type="text" class="form-control" id="pengujian_id_catatan" required readonly>
                        </div>
                        <div class="mb-3">
                            <label for="uraian" class="form-label">Uraian</label>
                            <input type="text" class="form-control" id="uraian" required>
                        </div>
                        <div class="mb-3">
                            <label for="rencana_tindak_lanjut" class="form-label">Rencana Tindak Lanjut</label>
                            <input type="text" class="form-control" id="rencana_tindak_lanjut" required>
                        </div>
                        <div class="mb-3">
                            <label for="penanggung_jawab_id" class="form-label">Penanggung Jawab</label>
                            <input type="text" class="form-control" id="penanggung_jawab_id" required>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Tutup</button>
                    <button type="button" class="btn btn-primary" id="addCatatanPengujianBtn">Tambah Catatan
                        Pengujian</button>
                </div>
            </div>
        </div>
    </div>


    <script>
        let pengujian = []
        let editPengujianId = null;
        let addPengujian = null;

        function fetchPengujian(query = '') {
            fetch('/api/pengujian')
                .then(response => response.json())
                .then(data => {
                    // console.log('Data dari API:', data); // Debugging

                    if (data.success) {
                        pengujian = data.payload.map(p => ({
                            id: p.id,
                            tanggal: p.tanggal,
                            nama_sistem: p.pengembangan?.pengajuan?.nama_sistem || 'N/A',
                            perangkat_lunak: p.perangkat_lunak || 'N/A',
                            versi: p.versi,
                            tujuan: p.tujuan,
                            metode: p.metode,
                            pelaksana: p.pelaksana.id,
                            nama_pelaksana: p.pelaksana.name
                        }));

                        // Filter berdasarkan query pencarian jika ada
                        if (query) {
                            pengujian = pengujian.filter(p =>
                                p.nama_sistem.toLowerCase().includes(query.toLowerCase())
                            );
                        }

                        renderTable(pengujian);
                    } else {
                        alert('Gagal memuat data pengujian');
                    }
                })
                .catch(error => console.error('Error:', error));
        }

        function renderTable(pengujian) {
            const tableBody = document.querySelector('#pengujian-table tbody');
            tableBody.innerHTML = ''; // Clear the existing table body

            pengujian.forEach(p => {
                const row = document.createElement('tr');
                row.innerHTML = `
            <td>${p.tanggal}</td>
            <td>${p.nama_sistem}</td>
            <td>${p.perangkat_lunak}</td>
            <td>${p.versi}</td>
            <td>${p.tujuan}</td>
            <td>${p.metode}</td>
            <td>
                <button class="btn btn-warning btn-sm" onclick="editPengujian('${p.id}')" style="margin: 5px;">Edit</button>
                <button class="btn btn-danger btn-sm" onclick="deletePengujian('${p.id}')" style="margin: 5px;">Delete</button>
                <button class="btn btn-primary btn-sm" onclick="showPengembangan('${p.id}')" style="margin: 5px;">Approval</button>
                <button class="btn btn-secondary btn-sm" onclick="showCatatanPengujian('${p.id}')" style="margin: 5px;">Tambah Catatan</button>
                <button class="btn btn-info btn-sm" onclick="showPengujianDetail('${p.id}')" style="margin: 5px;">Tambah Detail</button>
            </td>
        `;
                tableBody.appendChild(row);
            });
        }

        function deletePengujian(id) {
            if (confirm('Yakin ingin menghapus pengujian ini?')) {
                fetch(`/api/pengujian/${id}`, {
                        method: 'DELETE',
                        headers: {
                            'Content-Type': 'application/json',
                            'X-CSRF-TOKEN': document.querySelector('meta[name="csrf-token"]').getAttribute('content')
                        },
                    })
                    .then(response => response.json())
                    .then(data => {
                        if (data.success) {
                            fetchPengujian(); // Re-fetch pengajuan setelah hapus
                            alert('Pengujian berhasil dihapus');
                        } else {
                            alert('Gagal menghapus pengujian');
                        }
                    })
                    .catch(error => console.error('Error:', error));
            }
        }

        function editPengujian(id) {
            const p = pengujian.find(p => p.id === id); // Cari data berdasarkan ID
            if (!p) {
                alert('Data tidak ditemukan!');
                return;
            }
            // console.log("Isi Datanya",pengujian);

            // Pastikan id yang digunakan sesuai dengan yang ada di modal
            document.getElementById('nama_sistem').value = p.nama_sistem;
            document.getElementById('perangkat_lunak').value = p.perangkat_lunak;
            document.getElementById('versi').value = p.versi;
            document.getElementById('tujuan').value = p.tujuan;
            document.getElementById('metode_pengujian').value = p.metode;
            document.getElementById('tanggal_pengujian').value = p.tanggal;
            document.getElementById('pelaksana_id').value = p.pelaksana;

            editPengujianId = id;

            // Tampilkan modal edit
            new bootstrap.Modal(document.getElementById('editPengujianModal')).show();
        }

        document.getElementById('editPengujianBtn').addEventListener('click', function() {
            if (!editPengujianId) {
                alert('ID pengujian tidak ditemukan!');
                return;
            }
            // console.log(editPengujianId);

            const data = {
                perangkat_lunak: document.getElementById('perangkat_lunak').value,
                versi: document.getElementById('versi').value,
                tujuan: document.getElementById('tujuan').value,
                metode: document.getElementById('metode_pengujian').value,
                tanggal: document.getElementById('tanggal_pengujian').value,
                pelaksana_id: document.getElementById('pelaksana_id').value,
            };

            // console.log("Mengirim data:", data, "ke ID:", editPengujianId); // Debugging

            fetch(`/api/pengujian/${editPengujianId}`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                        'X-CSRF-TOKEN': document.querySelector('meta[name="csrf-token"]').getAttribute(
                            'content'),
                    },
                    body: JSON.stringify(data),
                })
                .then(response => response.json())
                .then(data => {
                    // console.log("Response dari server:", data); // Debugging
                    if (data.success) {
                        fetchPengujian(); // Refresh tabel
                        alert('Pengujian berhasil diupdate');
                        // Tutup modal
                        let editModal = bootstrap.Modal.getInstance(document.getElementById(
                            'editPengujianModal'));
                        if (editModal) {
                            editModal.hide();
                        }
                    } else {
                        alert('Gagal mengupdate Pengujian');
                    }
                })
                .catch(error => console.error('Error:', error));
        });

        function showPengujianDetail(id) {
            const p = pengujian.find(p => p.id === id); // Cari data berdasarkan ID
            if (!p) {
                alert('Data tidak ditemukan!');
                return;
            }
            // console.log("Isi Datanya",pengujian);

            // Pastikan id yang digunakan sesuai dengan yang ada di modal
            document.getElementById('pengujian_id').value = p.id;

            editPengujianId = id;

            // Tampilkan modal edit
            new bootstrap.Modal(document.getElementById('addPengujianDetailModal')).show();
        }

        document.getElementById('addPengujianDetailBtn').addEventListener('click', function() {
            const data = {
                pengujian_id: document.getElementById('pengujian_id').value,
                nama_uji: document.getElementById('nama_uji').value,
                kasus_uji: document.getElementById('kasus_uji').value,
                hasil_diharapkan: document.getElementById('hasil_diharapkan').value,
                hasil_pengujian: document.getElementById('hasil_pengujian').value, // Optional, bisa dikosongkan
                status: document.getElementById('status').value,
            };

            // Debugging: log data yang dikirim
            console.log("Data yang dikirim:", data);

            // Kirim data ke API backend
            fetch('/api/pengujian-detail', { // Gantilah URL ini sesuai dengan route API yang benar
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'X-CSRF-TOKEN': document.querySelector('meta[name="csrf-token"]').getAttribute(
                            'content'),
                    },
                    body: JSON.stringify(data)
                })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        alert('Detail Pengujian berhasil ditambahkan!');
                        // Refresh atau update tampilan
                        // fetchPengujian(); // Panggil fungsi untuk memperbarui data tabel atau tampilan

                        // Tutup modal setelah berhasil
                        const modal = document.querySelector('#addPengujianDetailModal');
                        const modalInstance = bootstrap.Modal.getInstance(modal);
                        modalInstance.hide();
                    } else {
                        alert('Gagal menambahkan Detail Pengujian');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('Terjadi kesalahan dalam menambahkan Detail Pengujian');
                });
        });

        function showCatatanPengujian(id) {
            const p = pengujian.find(p => p.id === id); // Cari data berdasarkan ID
            if (!p) {
                alert('Data tidak ditemukan!');
                return;
            }
            // console.log("Isi Datanya",pengujian);

            // Pastikan id yang digunakan sesuai dengan yang ada di modal
            document.getElementById('pengujian_id_catatan').value = p.id;
            document.getElementById('penanggung_jawab_id').value = p.pelaksana;

            editPengujianId = id;

            // Tampilkan modal edit
            new bootstrap.Modal(document.getElementById('addCatatanPengujianModal')).show();
        }

        document.getElementById('addCatatanPengujianBtn').addEventListener('click', function() {
            const data = {
                pengujian_id: document.getElementById('pengujian_id_catatan').value,
                uraian: document.getElementById('uraian').value,
                rencana_tindak_lanjut: document.getElementById('rencana_tindak_lanjut').value,
                penanggung_jawab_id: document.getElementById('penanggung_jawab_id').value,
            };

            // Debugging: cek data yang dikirim
            console.log("Data yang dikirim:", data);

            fetch('/api/catatan-pengujian', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'X-CSRF-TOKEN': document.querySelector('meta[name="csrf-token"]').getAttribute(
                            'content'),
                    },
                    body: JSON.stringify(data)
                })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        alert('Catatan Pengujian berhasil ditambahkan');
                        // Refresh tampilan jika perlu
                        // fetchCatatanPengujian(); // Panggil fungsi untuk memperbarui data catatan pengujian

                        // Tutup modal setelah berhasil
                        const modal = document.querySelector('#addCatatanPengujianModal');
                        const modalInstance = bootstrap.Modal.getInstance(modal);
                        modalInstance.hide();
                    } else {
                        alert('Gagal menambahkan Catatan Pengujian');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('Terjadi kesalahan dalam menambahkan Catatan Pengujian');
                });
        });

        fetchPengujian();
    </script>
@endsection
