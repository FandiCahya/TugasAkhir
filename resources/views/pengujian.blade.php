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

        fetchPengujian();
    </script>
@endsection
