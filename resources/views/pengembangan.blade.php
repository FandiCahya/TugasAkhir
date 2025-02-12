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

    <script>
        let pengembangan = [];

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

        fetchPengembangan();
    </script>
@endsection
