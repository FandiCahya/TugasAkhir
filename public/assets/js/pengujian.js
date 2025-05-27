let PengujianData = [];
let filteredData = [];
let currentPage = 1;
const itemsPerPage = 10;
let detailIndex = 0;
let userIdFromPengembangan = null;

const getAuthToken = () => localStorage.getItem('token');

function getUserFromPengembangan(pengembanganId) {
    const token = getAuthToken(); // Fungsi ini harus mengembalikan token autentikasi
    fetch('/api/pengembangan', {
        headers: {
            'Accept': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    })
    .then(res => res.json())
    .then(res => {
        if (res.success && Array.isArray(res.payload)) {
            const pengembangan = res.payload.find(item => item.id === pengembanganId);
            if (pengembangan && pengembangan.pengajuan && pengembangan.pengajuan.user) {
                userIdFromPengembangan = pengembangan.pengajuan.user.id;
                console.log('✅ ID user berhasil diambil:', userIdFromPengembangan);
            } else {
                userIdFromPengembangan = null;
                console.warn('⚠️ Data user tidak ditemukan dalam pengajuan pengembangan');
            }
        } else {
            console.error('⚠️ Payload tidak sesuai format atau kosong');
        }
    })
    .catch(error => {
        console.error('❌ Gagal memuat data pengembangan:', error);
    });
}

document.getElementById('pengembangan_id').addEventListener('change', function () {
    const selectedId = this.value;
    if (selectedId) {
        getUserFromPengembangan(selectedId);
    } else {
        userIdFromPengembangan = null;
    }
});

document.getElementById('addDetailBtn').addEventListener('click', function () {
    const container = document.getElementById('pengujian_detail_container');

    const detailRow = document.createElement('div');
    detailRow.classList.add('pengujian-detail-row', 'position-relative', 'border', 'rounded', 'p-3', 'mb-3', 'bg-light');

    detailRow.innerHTML = `
    <div class="pengujian-detail-item position-relative border rounded p-4 mb-4 bg-light">
        <button type="button" class="btn btn-sm btn-danger remove-detail-btn"
            style="position: absolute; top: -20px; right: 2px; z-index: 10;">&times;</button>

        <div class="row">
            <div class="col-md-4 mb-2">
                <input class="form-control" name="pengujian_detail[${detailIndex}][nama_uji]" placeholder="Nama Uji" required>
            </div>
            <div class="col-md-4 mb-2">
                <input class="form-control" name="pengujian_detail[${detailIndex}][kasus_uji]" placeholder="Kasus Uji" required>
            </div>
            <div class="col-md-4 mb-2">
                <input class="form-control" name="pengujian_detail[${detailIndex}][hasil_diharapkan]" placeholder="Hasil Diharapkan" required>
            </div>
            <div class="col-md-4 mb-2">
                <input class="form-control" name="pengujian_detail[${detailIndex}][hasil_pengujian]" placeholder="Hasil Pengujian">
            </div>
            <div class="col-md-4 mb-2">
                <select class="form-select" name="pengujian_detail[${detailIndex}][kategori]" required>
                    <option value="">Pilih Kategori</option>
                    <option value="uji_positif">Uji Positif</option>
                    <option value="uji_negatif">Uji Negatif</option>
                </select>
            </div>
            <div class="col-md-4 mb-2">
                <select class="form-select" name="pengujian_detail[${detailIndex}][status]" required>
                    <option value="">Pilih Status</option>
                    <option value="OK">OK</option>
                    <option value="Tidak">Tidak</option>
                </select>
            </div>
        </div>
    </div>
    `;

    container.appendChild(detailRow);
    detailIndex++;
});
// Hapus row detail jika tombol "×" ditekan
document.getElementById('pengujian_detail_container').addEventListener('click', function (e) {
    if (e.target.classList.contains('remove-detail-btn')) {
        e.target.closest('.pengujian-detail-row').remove();
    }
});

function loadPengembanganOptions() {
    const token = getAuthToken();
    fetch('/api/pengembangan?status=finished', {
        headers: {
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
        }
    })
    .then(res => res.json())
    .then(res => {
        if (res.success) {
            // console.log('Pengembangan:', res.payload);
        const select = document.getElementById('pengembangan_id');
        select.innerHTML = '<option value="">-- Pilih pengembangan --</option>';
        res.payload.forEach(pengembangan => {
            const option = document.createElement('option');
            option.value = pengembangan.id;
            option.textContent = pengembangan.pengajuan.nama_sistem; // sesuaikan property nama pengajuan
            select.appendChild(option);
        });
        } else {
        console.error('Gagal load pengembangan:', res.message);
        }
    })
    .catch(err => {
        console.error('Error load pengajuan:', err);
    });}

function loadUsersOptions() {
    const token = getAuthToken();
    fetch('/api/users', {
        headers: {
            'Accept': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    })
    .then(res => res.json())
    .then(response => {
        // console.log('Raw response data:', response);

        if (response.success && Array.isArray(response.data)) {
            const pelaksanaSelect = document.getElementById('pelaksana_id');
            const penanggungJawabSelect = document.getElementById('penanggung_jawab_id');
            const userIdsContainer = document.getElementById('user_ids_container');

            pelaksanaSelect.innerHTML = '<option value="">-- Pilih Pelaksana --</option>';
            penanggungJawabSelect.innerHTML = '<option value="">-- Pilih Penanggung Jawab --</option>';
            userIdsContainer.innerHTML = '';

            // Kelompokkan user berdasarkan role
            const groupedUsers = {};
            response.data.forEach(user => {
                // Tambahkan ke dropdown pelaksana/penanggung jawab
                const optionPelaksana = document.createElement('option');
                optionPelaksana.value = user.id;
                optionPelaksana.textContent = user.name;
                pelaksanaSelect.appendChild(optionPelaksana);

                const optionPenanggungJawab = document.createElement('option');
                optionPenanggungJawab.value = user.id;
                optionPenanggungJawab.textContent = user.name;
                penanggungJawabSelect.appendChild(optionPenanggungJawab);

                // Kelompokkan berdasarkan role
                if (!groupedUsers[user.role]) {
                    groupedUsers[user.role] = [];
                }
                groupedUsers[user.role].push(user);
            });

            // Tampilkan user per role dalam dua kolom
            const roles = Object.keys(groupedUsers);
            const chunkSize = Math.ceil(roles.length / 2);

            const rowDiv = document.createElement('div');
            rowDiv.classList.add('row', 'px-2');

            [0, 1].forEach(colIndex => {
                const colDiv = document.createElement('div');
                colDiv.classList.add('col-md-6', 'ps-3');

                const slicedRoles = roles.slice(colIndex * chunkSize, (colIndex + 1) * chunkSize);
                slicedRoles.forEach(role => {
                    if (role === 'user') return; // ❌ skip role "user"

                    const roleWrapper = document.createElement('div');
                    roleWrapper.classList.add('mb-3');

                    const roleTitle = document.createElement('h6');
                    roleTitle.classList.add('fw-bold');
                    roleTitle.textContent = role.charAt(0).toUpperCase() + role.slice(1);
                    roleWrapper.appendChild(roleTitle);

                    groupedUsers[role].forEach(user => {
                        const checkboxDiv = document.createElement('div');
                        checkboxDiv.classList.add('form-check', 'ms-4');

                        const checkbox = document.createElement('input');
                        checkbox.classList.add('form-check-input');
                        checkbox.type = 'checkbox';
                        checkbox.name = 'user_ids[]';
                        checkbox.value = user.id;
                        checkbox.id = `user_${user.id}`;

                        const label = document.createElement('label');
                        label.classList.add('form-check-label');
                        label.htmlFor = `user_${user.id}`;
                        label.textContent = user.name;

                        checkboxDiv.appendChild(checkbox);
                        checkboxDiv.appendChild(label);
                        roleWrapper.appendChild(checkboxDiv);
                    });

                    colDiv.appendChild(roleWrapper);
                });


                rowDiv.appendChild(colDiv);
            });

            userIdsContainer.appendChild(rowDiv);

        } else {
            console.error('Gagal load users atau data tidak sesuai format:', response);
        }
    })
    .catch(err => {
        console.error('Error load Users:', err);
    });
}

function loadPengujianData() {
    const token = getAuthToken();
    fetch('/api/pengujian', {
        headers: {
            'Accept': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    })
    .then(res => res.json())
    .then(json => {
        if (!json.success) {
            return Swal.fire('Error', 'Gagal memuat data pengujian', 'error');
        }
        PengujianData = json.payload;
        // console.log('PengujianData:', PengujianData);
        filteredData = PengujianData;
        currentPage = 1;
        renderTable();
    })
    .catch(err => {
        console.error(err);
        Swal.fire('Error', 'Terjadi kesalahan koneksi.', 'error');
    });
}

function renderTable() {
    const tbody = document.getElementById('pengujian-list');
    tbody.innerHTML = '';

    const start = (currentPage - 1) * itemsPerPage;
    const pageData = filteredData.slice(start, start + itemsPerPage);

    pageData.forEach(item => {
        const tgl = new Date(item.tanggal).toLocaleDateString('id-ID');
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${tgl}</td>
            <td>${item.pengembangan.pengajuan.nama_sistem}</td>
            <td>${item.perangkat_lunak}</td>
            <td>${item.versi}</td>
            <td>${item.tujuan}</td>
            <td>${item.metode}</td>
            <td>
                <button class="btn btn-sm btn-warning me-2" onclick='openEditModal(${JSON.stringify(item)})'>Edit</button>
                <button class="btn btn-sm btn-danger" onclick="deletePengujian('${item.id}')">Delete</button>
            </td>
        `;
        tbody.appendChild(tr);
    });

    renderPagination();
}

function renderPagination() {
    const totalPages = Math.ceil(filteredData.length / itemsPerPage);
    document.querySelector('button[onclick="prevPage()"]').disabled = currentPage === 1;
    document.querySelector('button[onclick="nextPage()"]').disabled = currentPage === totalPages || totalPages === 0;
}

function prevPage() {
    if (currentPage > 1) {
        currentPage--;
        renderTable();
    }
}

function nextPage() {
    const totalPages = Math.ceil(filteredData.length / itemsPerPage);
    if (currentPage < totalPages) {
        currentPage++;
        renderTable();
    }
}

function searchPengujian() {
    const q = document.getElementById('search').value.trim().toLowerCase();
    filteredData = PengujianData.filter(item =>
        item.perangkat_lunak.toLowerCase().includes(q) ||
        item.pengembangan.pengajuan.nama_sistem.toLowerCase().includes(q) ||
        item.versi.toLowerCase().includes(q) ||
        item.tujuan.toLowerCase().includes(q) ||
        item.metode.toLowerCase().includes(q) ||
        item.status.toLowerCase().includes(q) ||
        new Date(item.tanggal).toLocaleDateString('id-ID').toLowerCase().includes(q)
    );
    currentPage = 1;
    renderTable();
}

function deletePengujian(id) {
    const token = getAuthToken();
    Swal.fire({
        title: 'Yakin ingin menghapus?',
        text: 'Data yang dihapus tidak bisa dikembalikan!',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Ya, hapus!',
        cancelButtonText: 'Batal'
    }).then(result => {
        if (result.isConfirmed) {
            fetch(`/api/pengujian/${id}`, {
                method: 'DELETE',
                headers: {
                    'Accept': 'application/json',
                    'Authorization': `Bearer ${token}`,
                    'X-CSRF-TOKEN': csrfToken
                }
            })
            .then(r => r.json())
            .then(res => {
                if (res.success) {
                    Swal.fire('Dihapus', res.message, 'success');
                    loadPengujianData();
                } else {
                    Swal.fire('Gagal', res.message, 'error');
                }
            })
            .catch(err => {
                console.error(err);
                Swal.fire('Error', 'Terjadi kesalahan saat menghapus.', 'error');
            });
        }
    });
}

function openAddModal() {
    // Tampilkan dropdown pelaksana, sembunyikan readonly
    document.getElementById('pelaksana_select_container').style.display = '';
    document.getElementById('pelaksana_readonly_container').style.display = 'none';

    document.getElementById('pengembangan_id').disabled = false;

    document.getElementById('pengujian_id').value = '';
    document.getElementById('pengembangan_id').value = '';
    document.getElementById('perangkat_lunak').value = '';
    document.getElementById('versi').value = '';
    document.getElementById('tujuan').value = '';
    document.getElementById('metode').value = '';
    document.getElementById('tanggal').value = '';
    document.getElementById('pelaksana_id').value = '';
    
    loadPengembanganOptions();

    loadUsersOptions();
    // Reset multiple select user_ids
    const userIds = document.getElementById('user_ids');
    if(userIds) {
        for(let i = 0; i < userIds.options.length; i++) {
        userIds.options[i].selected = false;
        }
    }

    // Kosongkan container detail pengujian
    document.getElementById('pengujian_detail_container').innerHTML = '';

    // Kosongkan catatan
    document.getElementById('catatan_uraian').value = '';
    document.getElementById('catatan_rencana').value = '';
    document.getElementById('penanggung_jawab_id').value = '';

    document.getElementById('pengembangan_id').style.display = '';
    document.getElementById('pengembangan_nama').style.display = 'none';
    document.getElementById('pengembangan_id').disabled = false;

    // Tampilkan kembali section yang disembunyikan saat edit
    document.getElementById('persetujuan_section').style.display = '';
    document.getElementById('pengujian_detail_section').style.display = '';
    document.getElementById('catatan_section').style.display = '';
    document.getElementById('catatan_uraian').parentElement.style.display = '';
    document.getElementById('catatan_rencana').parentElement.style.display = '';
    document.getElementById('penanggung_jawab_id').parentElement.style.display = '';


        document.getElementById('pengujianModalLabel').textContent = 'Tambah Pengujian';
    document.getElementById('savePengujianBtn').textContent = 'Simpan Pengujian';

    // Tampilkan modal dengan Bootstrap 5
    const pengujianModal = new bootstrap.Modal(document.getElementById('pengujianModal'));
    pengujianModal.show();
}

function openEditModal(item) {
    document.getElementById('pengembangan_id').disabled = true;

    loadPengembanganOptions();
    loadUsersOptions();

    document.getElementById('pengujian_id').value = item.id;
    document.getElementById('pengembangan_id').value = item.pengembangan_id ?? '';
    document.getElementById('perangkat_lunak').value = item.perangkat_lunak ?? '';
    document.getElementById('versi').value = item.versi ?? '';
    document.getElementById('tujuan').value = item.tujuan ?? '';
    document.getElementById('metode').value = item.metode ?? '';
    if (item.tanggal) {
        document.getElementById('tanggal').value = new Date(item.tanggal).toISOString().split('T')[0];
    }

    // Sembunyikan bagian persetujuan, detail, dan catatan
    document.getElementById('persetujuan_section').style.display = 'none';
    document.getElementById('pengujian_detail_section').style.display = 'none';
    document.getElementById('catatan_section').style.display = 'none';
    document.getElementById('catatan_uraian').parentElement.style.display = 'none';
    document.getElementById('catatan_rencana').parentElement.style.display = 'none';
    document.getElementById('penanggung_jawab_id').parentElement.style.display = 'none';

    // Sembunyikan select dan tampilkan input readonly
    document.getElementById('pengembangan_id').style.display = 'none';
    document.getElementById('pengembangan_nama').style.display = '';
    document.getElementById('pengembangan_nama').value = item.pengembangan?.pengajuan?.nama_sistem || '(Tidak diketahui)';

      // Sembunyikan dropdown pelaksana, tampilkan readonly
    document.getElementById('pelaksana_select_container').style.display = 'none';
    document.getElementById('pelaksana_readonly_container').style.display = '';
        // Simpan pelaksana_id di hidden input agar terkirim saat submit
    document.getElementById('pelaksana_id_hidden').value = item.pelaksana?.id ?? '';
    // Tampilkan nama pelaksana di readonly input
    document.getElementById('pelaksana_readonly').value = item.pelaksana?.name || '(Tidak diketahui)';

    // console.log('Set pelaksana_id_hidden:', document.getElementById('pelaksana_id_hidden').value);

    
    document.getElementById('pengujianModalLabel').textContent = 'Edit Pengujian';
    document.getElementById('savePengujianBtn').textContent = 'Update';

    new bootstrap.Modal(document.getElementById('pengujianModal')).show();
}

document.addEventListener('DOMContentLoaded', loadPengujianData);

document.getElementById('savePengujianBtn').addEventListener('click', function () {
    const token = getAuthToken();
    const id = document.getElementById('pengujian_id').value;
    // console.log('ID Pengujian:', id);
    const url = id ? `/api/pengujian/${id}` : '/api/pengujian';
    const method = id ? 'PUT' : 'POST';

    const pelaksanaSelectContainer = document.getElementById('pelaksana_select_container');
    const pelaksanaReadOnlyContainer = document.getElementById('pelaksana_readonly_container');

    // console.log('Hidden pelaksana_id:', document.getElementById('pelaksana_id_hidden').value);

    let pelaksanaId = '';
    if (pelaksanaSelectContainer.style.display !== 'none') {
        pelaksanaId = document.getElementById('pelaksana_id').value;
    } else if (pelaksanaReadOnlyContainer.style.display !== 'none') {
        pelaksanaId = document.getElementById('pelaksana_id_hidden').value;
    }
    // console.log('Pelaksana ID:', pelaksanaId);


    const data = {    
    
    perangkat_lunak: document.getElementById('perangkat_lunak').value,
    versi: document.getElementById('versi')?.value || '',
    tujuan: document.getElementById('tujuan').value,
    metode: document.getElementById('metode').value,
    tanggal: document.getElementById('tanggal').value,
    pelaksana_id: pelaksanaId,
};

    if (!id) {  // artinya tambah
    data.pengembangan_id = document.getElementById('pengembangan_id').value;
    // data.user_ids = Array.from(document.querySelectorAll('input[name="user_ids[]"]:checked')).map(cb => cb.value);

    // Baru
    // ✅ Ambil dari checkbox user tambahan
    data.user_ids = Array.from(document.querySelectorAll('input[name="user_ids[]"]:checked')).map(cb => cb.value);

    // ✅ Tambahkan user_id dari pengajuan (via pengembangan)
    // Ambil user_id dari pengajuan
    if (userIdFromPengembangan) {
        data.user_id = userIdFromPengembangan;

        // Gabungkan ke user_ids[] jika belum ada
        if (!data.user_ids.includes(userIdFromPengembangan)) {
            data.user_ids.push(userIdFromPengembangan);
        }
    } else {
        Swal.fire('Gagal', 'User dari pengajuan belum diambil. Silakan pilih pengembangan terlebih dahulu.', 'warning');
        return;
    }
    console.log('User IDs:', data.user_ids);
    
    data.pengujian_detail = [];
    const rows = document.querySelectorAll('.pengujian-detail-row');
    rows.forEach((row, index) => {
        const detail = {};
        const inputs = row.querySelectorAll('input, select');
        inputs.forEach(input => {
            const match = input.name.match(/\[([^\]]+)\]$/);
            if (match) {
                const key = match[1];
                detail[key] = input.value;
            }
        });
        data.pengujian_detail.push(detail);
    });

        const catatanUraian = document.getElementById('catatan_uraian').value.trim();
        const catatanRencana = document.getElementById('catatan_rencana').value.trim();
        const catatanPenanggungJawab = document.getElementById('penanggung_jawab_id').value;

        if (catatanUraian || catatanRencana || catatanPenanggungJawab) {
            data.catatan = {
                uraian: catatanUraian,
                rencana_tindak_lanjut: catatanRencana,
                penanggung_jawab_id: catatanPenanggungJawab,
            };
        }

    }

    console.log('Data stringify:', JSON.stringify(data));    
    fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
            'Authorization': `Bearer ${token}`,
            'X-CSRF-TOKEN': csrfToken
        },
        body: JSON.stringify(data)
        
    })
    .then(res => res.json())
    .then(res => {
        if (res.success) {
            Swal.fire('Berhasil', res.message, 'success');
            document.getElementById('pengujianForm').reset();

            const modalElement = document.getElementById('pengujianModal');
            const modalInstance = bootstrap.Modal.getInstance(modalElement) || new bootstrap.Modal(modalElement);

            // Bersihkan backdrop modal manual agar tidak error
            document.querySelectorAll('.modal-backdrop').forEach(el => el.remove());
            document.body.classList.remove('modal-open');
            document.body.style.overflow = '';
            document.body.style.paddingRight = '';

            modalInstance.hide();

            loadPengujianData();
        } else {
            let errorText = '';
            if (res.data && typeof res.data === 'object') {
                for (const key in res.data) {
                    errorText += `${key}: ${res.data[key].join(', ')}\n`;
                }
            } else {
                errorText = res.message;
            }
            Swal.fire('Gagal', errorText, 'error');
        }
    })
    .catch(err => {
        console.error(err);
        Swal.fire('Error', 'Terjadi kesalahan saat menyimpan data.', 'error');
    });
});



