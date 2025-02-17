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
                            <select class="form-select" id="pelaksana_id" required>
                                <option value="" disabled selected>Pilih Pelaksana</option>
                            </select>
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
                            <label for="penanggung_jawab_id" class="form-label">Pelaksana</label>
                            <select class="form-select" id="penanggung_jawab_id" required>
                                <option value="" disabled selected>Pilih Pelaksana</option>
                            </select>
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

    <!-- Approval Modal -->
    <div class="modal fade" id="approvalModal" tabindex="-1" aria-labelledby="approvalModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="approvalModalLabel">Persetujuan Pengujian</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form id="approvalForm">
                        @csrf

                        <!-- Admin Section -->
                        <div class="mb-3">
                            <label class="form-label"><strong>Admin</strong></label>
                            <div id="adminUsers" class="form-check">
                                <!-- Admin users will be populated here dynamically -->
                            </div>
                        </div>

                        <!-- User Section -->
                        <div class="mb-3">
                            <label class="form-label"><strong>User</strong></label>
                            <div id="userUsers" class="form-check">
                                <!-- User users will be populated here dynamically -->
                            </div>
                        </div>

                        <!-- MQR Section -->
                        <div class="mb-3">
                            <label class="form-label"><strong>MQR</strong></label>
                            <div id="mqrUsers" class="form-check">
                                <!-- MQR users will be populated here dynamically -->
                            </div>
                        </div>

                        <!-- Kepala Cabang Section -->
                        <div class="mb-3">
                            <label class="form-label"><strong>Kepala Cabang</strong></label>
                            <div id="kepalaCabangUsers" class="form-check">
                                <!-- Kepala Cabang users will be populated here dynamically -->
                            </div>
                        </div>

                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Tutup</button>
                    <button type="button" class="btn btn-primary" id="submitApprovalBtn">Kirim Persetujuan</button>
                </div>
            </div>
        </div>
    </div>

    <style>
        .modal-body {
            max-height: 70vh; /* Set max height for modal body */
            overflow-y: auto; /* Enable scrolling if content exceeds height */
        }
    
        .form-check {
            margin-bottom: 10px; 
            margin-left:10px;
            display: flex;
            align-items: center;
            flex-wrap: wrap; 
            justify-content: space-between; 
        }
    
        .form-check-label {
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
            flex: 1; /* Allow the label to grow and take available space */
        }
    
        .form-check-input {
            margin-right: 10px; /* Space between checkbox and label */
            transform: scale(1.2); /* Optional: Increase checkbox size */
        }
    
        .modal-content {
            width: 100%; /* Ensure modal content stretches fully */
            padding: 20px;
        }
    
        .modal-header, .modal-footer {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
    
        .form-select {
            width: 100%; /* Ensure the select box stretches full width */
        }
    
        /* Optional: Style the modal buttons for better spacing */
        .modal-footer button {
            padding: 10px 20px; /* Increase padding for better usability */
        }
    </style>
    
    <script>
        let pengujian = []
        let editPengujianId = null;
        let addPengujian = null;
        let users = [];

        function fetchUsers() {
            fetch('/api/users') // Adjust the API endpoint accordingly
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        users = data.payload; // Assuming the response has 'payload' containing user data
                        populatePelaksanaDropdown();
                        UsersPenanggungJawab();
                        ApprovalUser();
                    } else {
                        alert('Failed to load users.');
                    }
                })
                .catch(error => {
                    console.error('Error fetching users:', error);
                    alert('An error occurred while fetching users.');
                });
        }

        function ApprovalUser() {
            // Group users by role
            const roles = {
                admin: document.getElementById('adminUsers'),
                user: document.getElementById('userUsers'),
                mqr: document.getElementById('mqrUsers'),
                kepalacabang: document.getElementById('kepalaCabangUsers')
            };

            // Clear the sections
            Object.keys(roles).forEach(role => {
                roles[role].innerHTML = ''; // Clear existing options
            });

            // Loop through users and populate them under their respective role sections
            users.forEach(user => {
                const option = document.createElement('div');
                option.classList.add('form-check');

                const checkbox = document.createElement('input');
                checkbox.type = 'checkbox';
                checkbox.classList.add('form-check-input');
                checkbox.value = user.id; // User ID will be sent in the request
                checkbox.id = `user-${user.id}`;

                const label = document.createElement('label');
                label.classList.add('form-check-label');
                label.setAttribute('for', `user-${user.id}`);
                label.textContent = `${user.name} (${user.role})`; // Display name and role

                option.appendChild(checkbox);
                option.appendChild(label);

                // Append to the appropriate role section
                if (roles[user.role]) {
                    roles[user.role].appendChild(option);
                }
            });
        }

        document.getElementById('submitApprovalBtn').addEventListener('click', function() {
            const selectedUsers = [];

            // Collect selected user IDs from the checkboxes
            document.querySelectorAll('.form-check-input:checked').forEach(checkbox => {
                selectedUsers.push(checkbox.value);
            });

            if (selectedUsers.length === 4) {
                const data = {
                    pengujian_id: currentPengujianId,
                    user_ids: selectedUsers // Send the selected user IDs
                };

                // Send the data to the backend
                fetch('/api/persetujuan-pengujian/create', {
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
                            alert('Persetujuan berhasil dikirim');
                            const modal = bootstrap.Modal.getInstance(document.getElementById('approvalModal'));
                            modal.hide();
                        } else {
                            alert('Gagal mengirim persetujuan');
                        }
                    })
                    .catch(error => console.error('Error:', error));
            } else {
                alert('Harap pilih tepat 4 pengguna');
            }
        });

        function showApprovalModal(pengujianId) {
            currentPengujianId = pengujianId;

            // Show the approval modal
            new bootstrap.Modal(document.getElementById('approvalModal')).show();
        }

        function populatePelaksanaDropdown() {
            const pelaksanaDropdown = document.getElementById('pelaksana_id');
            pelaksanaDropdown.innerHTML = ''; // Clear existing options

            // Create the default option
            const defaultOption = document.createElement('option');
            defaultOption.value = '';
            defaultOption.disabled = true;
            defaultOption.selected = true;
            defaultOption.textContent = 'Pilih Pelaksana';
            pelaksanaDropdown.appendChild(defaultOption);

            // Add each user as an option, including the role
            users.forEach(user => {
                const option = document.createElement('option');
                option.value = user.id; // User ID will be sent to the backend

                // Display user name and role together
                option.textContent = `${user.name} (${user.role})`; // Showing name and role in the dropdown

                pelaksanaDropdown.appendChild(option);
            });
        }

        function UsersPenanggungJawab() {
            const pelaksanaDropdown = document.getElementById('penanggung_jawab_id');
            pelaksanaDropdown.innerHTML = ''; // Clear existing options

            // Create the default option
            const defaultOption = document.createElement('option');
            defaultOption.value = '';
            defaultOption.disabled = true;
            defaultOption.selected = true;
            defaultOption.textContent = 'Pilih Pelaksana';
            pelaksanaDropdown.appendChild(defaultOption);

            // Add each user as an option, including the role
            users.forEach(user => {
                const option = document.createElement('option');
                option.value = user.id; // User ID will be sent to the backend

                // Display user name and role together
                option.textContent = `${user.name} (${user.role})`; // Showing name and role in the dropdown

                pelaksanaDropdown.appendChild(option);
            });
        }

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
                <button class="btn btn-primary btn-sm" onclick="showApprovalModal('${p.id}')" style="margin: 5px;">Approval</button>
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

        // Fetch the users when the page loads
        fetchUsers();
        fetchPengujian();
    </script>
@endsection
