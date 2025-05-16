let PengajuanData = [];
let filteredData = [];
let currentPage = 1;
const itemsPerPage = 10;

const getAuthToken = () => localStorage.getItem('token');

// Ambil data pengajuan dari API
function loadPengajuanData() {
    const token = getAuthToken();
    // console.log("Token di Pengajuan: ", token);
    fetch('/api/pengajuan', {
        headers: {
            'Accept': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    })
    .then(res => res.json())
    .then(json => {
        if (!json.success) {
            return Swal.fire('Error', 'Gagal memuat data pengajuan', 'error');
        }
        PengajuanData = json.payload;
        // console.log("Data Pengajuan: ", PengajuanData);
        filteredData = PengajuanData;
        currentPage = 1;
        renderTable();
    })
    .catch(err => {
        console.error(err);
        Swal.fire('Error', 'Terjadi kesalahan koneksi.', 'error');
    });
}

function formatJenis(text) {
    return text
        .split('_')
        .map(word => word.charAt(0).toUpperCase() + word.slice(1))
        .join(' ');
}


function renderTable() {
    const tbody = document.getElementById('pengajuan-list');
    tbody.innerHTML = '';

    const start = (currentPage - 1) * itemsPerPage;
    const pageData = filteredData.slice(start, start + itemsPerPage);

    pageData.forEach(item => {
        const createdAt = new Date(item.created_at).toLocaleString('id-ID', {
            year: 'numeric', month: '2-digit', day: '2-digit',
            hour: '2-digit', minute: '2-digit',
            hour12: false
        });

        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td class="text-center">${item.nama_sistem}</td>
            <td class="text-center">${formatJenis(item.jenis)}</td>
            <td class="text-center">${formatJenis(item.rencana_anggaran)}</td>
            <td class="text-center">${item.masalah}</td>
            <td class="text-center">${item.output}</td>
            <td class="text-center">${item.status}</td>
            <td class="text-center">${createdAt}</td>
            <td class="text-center">
                <button class="btn btn-sm btn-secondary me-2" onclick='openEditModal(${JSON.stringify(item)})'>Edit</button>
                <button class="btn btn-sm btn-danger" onclick="deletePengajuan('${item.id}')">Delete</button>
            </td>
        `;
        tbody.appendChild(tr);
    });

    renderPagination();
}

function renderPagination() {
    const totalPages = Math.ceil(filteredData.length / itemsPerPage);
    document.querySelector('button[onclick="prevPage()"]').disabled = currentPage === 1;
    document.querySelector('button[onclick="nextPage()"]').disabled = currentPage === totalPages;
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

function searchPengajuan() {
    const q = document.getElementById('search').value.trim().toLowerCase();
    filteredData = PengajuanData.filter(item =>
        item.nama_sistem.toLowerCase().includes(q) ||
        item.jenis.toLowerCase().includes(q) ||
        item.rencana_anggaran.toLowerCase().includes(q) ||
        item.masalah.toLowerCase().includes(q) ||
        item.output.toLowerCase().includes(q) ||
        item.status.toLowerCase().includes(q) 
    );
    currentPage = 1;
    renderTable();
}

function deletePengajuan(id) {
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
            fetch(`/api/pengajuan/${id}`, {
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
                    loadPengajuanData();
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
    document.getElementById('pengajuanForm').reset();
    document.getElementById('pengajuan-id').value = '';
    document.getElementById('pengajuanModalLabel').textContent = 'Tambah Pengajuan';
    document.getElementById('submitPengajuanBtn').textContent = 'Save';
    new bootstrap.Modal(document.getElementById('pengajuanModal')).show();
}

function openEditModal(item) {
    document.getElementById('pengajuan-id').value = item.id;
    document.getElementById('tgl').value = new Date(item.tgl).toISOString().split('T')[0];
    document.getElementById('nama_sistem').value = item.nama_sistem;
    document.getElementById('jenis').value = item.jenis;
    document.getElementById('rencana_anggaran').value = item.rencana_anggaran;
    document.getElementById('masalah').value = item.masalah;
    document.getElementById('output').value = item.output;
    document.getElementById('status').value = item.status;

    document.getElementById('pengajuanModalLabel').textContent = 'Edit Pengajuan';
    document.getElementById('submitPengajuanBtn').textContent = 'Update';
    new bootstrap.Modal(document.getElementById('pengajuanModal')).show();
}

document.addEventListener('DOMContentLoaded', loadPengajuanData);

document.getElementById('submitPengajuanBtn').addEventListener('click', function () {
    const token = getAuthToken();
    const id = document.getElementById('pengajuan-id').value;
    const method = id ? 'PUT' : 'POST';
    const url = id ? `/api/pengajuan/${id}` : '/api/pengajuan';

    const data = {
        tgl: document.getElementById('tgl').value,
        nama_sistem: document.getElementById('nama_sistem').value,
        jenis: document.getElementById('jenis').value,
        rencana_anggaran: document.getElementById('rencana_anggaran').value,
        masalah: document.getElementById('masalah').value,
        output: document.getElementById('output').value,
        status: document.getElementById('status').value,
    };

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
            document.getElementById('pengajuanForm').reset();
            const modalElement = document.getElementById('pengajuanModal');
            const modalInstance = bootstrap.Modal.getInstance(modalElement) || new bootstrap.Modal(modalElement);

            document.querySelectorAll('.modal-backdrop').forEach(el => el.remove());
            document.body.classList.remove('modal-open');
            document.body.style.overflow = '';
            document.body.style.paddingRight = '';
            modalInstance.hide();

            loadPengajuanData();
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
