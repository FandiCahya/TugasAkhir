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
                                <th>Name Sistem</th>
                                <th>Nama User</th>
                                <th>Hasil</th>
                                <th>Catatan</th>
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
                            <label for="user" class="form-label">User</label>
                            <input type="text" class="form-control" id="user" required readonly>
                        </div>
                        <div class="mb-3">
                            <label for="hasil" class="form-label">Hasil</label>
                            <select class="form-control" id="hasil" required>
                                <option value="negatif">Negatif</option>
                                <option value="positif">Positif</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="catatan" class="form-label">Catatan</label>
                            <textarea class="form-control" id="catatan" rows="3" required></textarea>
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

    <script>
        let pengujian = []
        let editPengujianId = null;
        let addPengujian = null;

        function fetchPengujian(query = '') {
            fetch('/api/pengujian')
                .then(response => response.json())
                .then(data => {
                    console.log('Data dari API:', data); // Debugging

                    if (data.success) {
                        pengujian = data.payload.map(p => ({
                            id: p.id,
                            nama_sistem: p.pengembangan?.pengajuan?.nama_sistem || 'N/A',
                            nama_user: p.pengembangan?.user?.name || 'N/A',
                            hasil: p.hasil,
                            catatan: p.catatan
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
            <td>${p.nama_sistem}</td>
            <td>${p.nama_user}</td>
            <td>${p.hasil}</td>
            <td>${p.catatan}</td>
            <td>
                <button class="btn btn-warning btn-sm" onclick="editPengujian('${p.id}')">Edit</button>
                <button class="btn btn-danger btn-sm" onclick="deletePengujian('${p.id}')">Delete</button>
                <button class="btn btn-info btn-sm" onclick="showPengembangan('${p.id}')">Approval</button>
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

            // Pastikan id yang digunakan sesuai dengan yang ada di modal
            document.getElementById('nama_sistem').value = p.nama_sistem;
            document.getElementById('user').value = p.nama_user;
            document.getElementById('hasil').value = p.hasil;
            document.getElementById('catatan').value = p.catatan;
            editPengujianId = id;

            // Tampilkan modal edit
            new bootstrap.Modal(document.getElementById('editPengujianModal')).show();
        }

        document.getElementById('editPengujianBtn').addEventListener('click', function() {
            if (!editPengujianId) {
                alert('ID pengujian tidak ditemukan!');
                return;
            }

            const data = {
                hasil: document.getElementById('hasil').value,
                catatan: document.getElementById('catatan').value,
            };

            console.log("Mengirim data:", data, "ke ID:", editPengujianId); // Debugging

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
                    console.log("Response dari server:", data); // Debugging
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



        fetchPengujian();
    </script>
@endsection
