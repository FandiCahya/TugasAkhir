let PengembanganData = [];
let filteredData = [];
let currentPage = 1;
const itemsPerPage = 10;

const getAuthToken = () => localStorage.getItem('token');

    function loadPengajuanOptions() {
    const token = getAuthToken();
    fetch('/api/pengajuan', {
        headers: {
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
        }
    })
    .then(res => res.json())
    .then(res => {
        if (res.success) {
        const select = document.getElementById('pengajuan_id');
        select.innerHTML = '<option value="">-- Pilih Pengajuan --</option>';
        res.payload.forEach(pengajuan => {
            const option = document.createElement('option');
            option.value = pengajuan.id;
            option.textContent = pengajuan.nama_sistem; // sesuaikan property nama pengajuan
            select.appendChild(option);
        });
        } else {
        console.error('Gagal load pengajuan:', res.message);
        }
    })
    .catch(err => {
        console.error('Error load pengajuan:', err);
    });
    }


function loadPengembanganData() {
    const token = getAuthToken();
    fetch('/api/pengembangan', {
        headers: {
            'Accept': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    })
    .then(res => res.json())
    .then(json => {
        if (!json.success) {
            return Swal.fire('Error', 'Gagal memuat data pengembangan', 'error');
        }
        PengembanganData = json.payload;
        // console.log("Data Pengembangan: ", PengembanganData);
        filteredData = PengembanganData;
        currentPage = 1;
        renderTable();
    })
    .catch(err => {
        console.error(err);
        Swal.fire('Error', 'Terjadi kesalahan koneksi.', 'error');
    });
}

function renderTable() {
    const tbody = document.getElementById('pengembangan-list');
    tbody.innerHTML = '';

    const start = (currentPage - 1) * itemsPerPage;
    const pageData = filteredData.slice(start, start + itemsPerPage);

    pageData.forEach(item => {
        // Format tanggal mulai dan selesai
        const tglMulai = new Date(item.tanggal_mulai).toLocaleDateString('id-ID');
        const tglSelesai = item.tanggal_selesai ? new Date(item.tanggal_selesai).toLocaleDateString('id-ID') : '-';

        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td class="text-center">${item.pengajuan.nama_sistem}</td>
            <td class="text-center">${item.pengajuan.user.name}</td>
            <td class="text-center">${tglMulai}</td>
            <td class="text-center">${tglSelesai}</td>
            <td class="text-center">${item.tahap}</td>
            <td class="text-center">${item.persentase}%</td>
            <td class="text-center">${item.keterangan}</td>
            <td class="text-center">${item.status}</td>
            <td class="text-center">
                <button class="btn btn-sm btn-secondary me-2" onclick='openEditModal(${JSON.stringify(item)})'>Edit</button>
                <button class="btn btn-sm btn-danger" onclick="deletePengembangan('${item.id}')">Delete</button>
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

function searchPengembangan() {
    const q = document.getElementById('search').value.trim().toLowerCase();
    filteredData = PengembanganData.filter(item =>
        item.pengajuan.nama_sistem.toLowerCase().includes(q) ||
        item.pengajuan.user.name.toLowerCase().includes(q) ||
        item.tanggal_mulai.toLowerCase().includes(q) ||
        (item.tanggal_selesai && item.tanggal_selesai.toLowerCase().includes(q)) ||
        item.tahap.toLowerCase().includes(q) ||
        (item.keterangan && item.keterangan.toLowerCase().includes(q)) ||
        item.persentase.toString().includes(q) ||
        item.status.toLowerCase().includes(q)
    );
    currentPage = 1;
    renderTable();
}

function deletePengembangan(id) {
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
            fetch(`/api/pengembangan/${id}`, {
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
                    loadPengembanganData();
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
    const form = document.getElementById('pengembanganForm');
    form.reset();

    document.getElementById('pengembangan-id').value = '';
    document.getElementById('tgl_mulai').value = '';
    document.getElementById('tgl_selesai').value = '';
    document.getElementById('persentase').value = '';
    document.getElementById('keterangan').value = '';
    document.getElementById('status_pengembangan').value = 'developed';

    loadPengajuanOptions(); // isi dropdown

    // tampilkan dropdown, sembunyikan input readonly
    document.getElementById('pengajuan_id').parentElement.style.display = 'block';
    document.getElementById('pengajuan').parentElement.style.display = 'none';
    document.getElementById('user').parentElement.style.display = 'none';

    // reset checkbox tahap
    document.querySelectorAll('.tahap-checkbox').forEach(chk => chk.checked = false);

    document.getElementById('pengembanganModalLabel').textContent = 'Tambah Pengembangan';
    document.getElementById('submitPengembanganBtn').textContent = 'Save';

    new bootstrap.Modal(document.getElementById('pengembanganModal')).show();
}

function openEditModal(item) {
    document.getElementById('pengembangan-id').value = item.id;

    // sembunyikan dropdown, tampilkan input readonly
    document.getElementById('pengajuan_id').parentElement.style.display = 'none';
    document.getElementById('pengajuan').parentElement.style.display = 'block';
    document.getElementById('user').parentElement.style.display = 'block';

    // isi input readonly dengan data pengajuan dan user
    document.getElementById('pengajuan').value = item.pengajuan.nama_sistem;
    document.getElementById('user').value = item.pengajuan.user.name;

    document.getElementById('tgl_mulai').value = new Date(item.tanggal_mulai).toISOString().split('T')[0];
    document.getElementById('tgl_selesai').value = item.tanggal_selesai ? new Date(item.tanggal_selesai).toISOString().split('T')[0] : '';
    document.getElementById('persentase').value = item.persentase;
    document.getElementById('keterangan').value = item.keterangan || '';
    document.getElementById('status_pengembangan').value = item.status;

    // ceklist tahap yang dipilih
    document.querySelectorAll('.tahap-checkbox').forEach(checkbox => {
        checkbox.checked = item.tahap.includes(checkbox.value);
    });

    document.getElementById('pengembanganModalLabel').textContent = 'Edit Pengembangan';
    document.getElementById('submitPengembanganBtn').textContent = 'Update';

    new bootstrap.Modal(document.getElementById('pengembanganModal')).show();
}
document.addEventListener('DOMContentLoaded', loadPengembanganData);

document.querySelectorAll('.tahap-checkbox').forEach(cb => {
    cb.addEventListener('change', updatePersentase);
});

function updatePersentase() {
    const checked = document.querySelectorAll('.tahap-checkbox:checked').length;
    document.getElementById('persentase').value = checked * 20;
}

document.getElementById('submitPengembanganBtn').addEventListener('click', function () {
    const token = getAuthToken();
    const id = document.getElementById('pengembangan-id').value;
    const method = id ? 'PUT' : 'POST';
    const url = id ? `/api/pengembangan/${id}` : '/api/pengembangan';
    const tahap = Array.from(document.querySelectorAll('.tahap-checkbox:checked'))
                    .map(cb => cb.value)
                    .join(', ');
    const data = {
    tanggal_mulai: document.getElementById('tgl_mulai').value,
    tanggal_selesai: document.getElementById('tgl_selesai').value || null,
    tahap: tahap,
    persentase: parseInt(document.getElementById('persentase').value) || 0,
    keterangan: document.getElementById('keterangan').value,
    status: document.getElementById('status_pengembangan').value,
    };

    if (method === 'POST') {
        data.pengajuan_id = document.getElementById('pengajuan_id').value;
    }
    // Jika persentase 100%, set otomatis status menjadi 'finished'
    if (data.persentase === 100) {
        data.status = 'finished';
    }


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
        // console.log('Response:', res);
        if (res.success) {
            Swal.fire('Berhasil', res.message, 'success');
            document.getElementById('pengembanganForm').reset();

            const modalElement = document.getElementById('pengembanganModal');
            const modalInstance = bootstrap.Modal.getInstance(modalElement) || new bootstrap.Modal(modalElement);
            

            // Bersihkan backdrop modal manual agar tidak error
            document.querySelectorAll('.modal-backdrop').forEach(el => el.remove());
            document.body.classList.remove('modal-open');
            document.body.style.overflow = '';
            document.body.style.paddingRight = '';

            modalInstance.hide();

            loadPengembanganData();
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
