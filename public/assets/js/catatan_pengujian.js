let CatatanPengujianData = [];
let filteredData = [];
let currentPage = 1;
const itemsPerPage = 3;
const getAuthToken = () => localStorage.getItem('token');

// 1. Load data catatan pengujian
function loadCatatanPengujianData() {
    const token = getAuthToken();
    // console.log("Token di CatatanPengujian: ", token);
    fetch('/api/catatan-pengujian', {
        headers: {
            'Accept': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    })
    .then(res => res.json())
    .then(json => {
        if (!json.success) {
            return Swal.fire('Error', 'Gagal memuat data catatan pengujian', 'error');
        }
        CatatanPengujianData = json.payload;
        // console.log("Data CatatanPengujian: ", CatatanPengujianData);
        filteredData = CatatanPengujianData;
        currentPage = 1;
        renderTable();
    })
    .catch(err => {
        console.error(err);
        Swal.fire('Error', 'Terjadi kesalahan saat memuat data.', 'error');
    });
}

function groupByPengujian(data) {
    return data.reduce((groups, item) => {
        const key = item.pengujian.id;
        if (!groups[key]) {
            groups[key] = {
                pengujian: item.pengujian,
                items: []
            };
        }
        groups[key].items.push(item);
        return groups;
    }, {});
}

function renderTable() {
    const tbody = document.getElementById('pengujian-catatan-body');
    tbody.innerHTML = '';
    const grouped = groupByPengujian(filteredData);
    const groupedArray = Object.values(grouped);
    const start = (currentPage - 1) * itemsPerPage;
    const pageGroups = groupedArray.slice(start, start + itemsPerPage);

    pageGroups.forEach(group => {
        const trGroup = document.createElement('tr');
        trGroup.classList.add('table-secondary');
        trGroup.innerHTML = `
            <td colspan="5">
                <strong>Perangkat Lunak:</strong> ${group.pengujian.perangkat_lunak} |
                <strong>Versi:</strong> ${group.pengujian.versi} |
                <strong>Tujuan:</strong> ${group.pengujian.tujuan} |
                <strong>Metode:</strong> ${group.pengujian.metode} |
                <strong>Tanggal:</strong> ${group.pengujian.tanggal}
            </td>
        `;
        tbody.appendChild(trGroup);

        group.items.forEach(item => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${item.uraian}</td>
                <td>${item.rencana_tindak_lanjut || '-'}</td>
                <td>${item.penanggung_jawab?.name || '-'}</td>
                <td>${new Date(item.updated_at).toLocaleString()}</td>
                <td>
                    <button class="btn btn-sm btn-warning me-2" onclick='openEditModal(${JSON.stringify(item)})'>Edit</button>
                    <button class="btn btn-sm btn-danger" onclick="deleteCatatanPengujian('${item.id}')">Delete</button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    });

    renderPagination(groupedArray.length);
}

function renderPagination(totalGroups) {
    const totalPages = Math.ceil(totalGroups / itemsPerPage);
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
    const grouped = groupByPengujian(filteredData);
    const groupedArray = Object.values(grouped);
    const totalPages = Math.ceil(groupedArray.length / itemsPerPage);
    if (currentPage < totalPages) {
        currentPage++;
        renderTable();
    }
}

function searchCatatanPengujian() {
    const q = document.getElementById('search').value.trim().toLowerCase();
    filteredData = CatatanPengujianData.filter(item =>
        item.uraian.toLowerCase().includes(q) ||
        (item.rencana_tindak_lanjut || '').toLowerCase().includes(q) ||
        (item.penanggung_jawab?.name || '').toLowerCase().includes(q)
    );
    currentPage = 1;
    renderTable();
}

function deleteCatatanPengujian(id) {
    const token = getAuthToken();
    Swal.fire({
        title: 'Yakin ingin menghapus?',
        text: 'Data yang dihapus tidak bisa dikembalikan!',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Ya, hapus!',
        cancelButtonText: 'Batal'
    }).then((result) => {
        if (result.isConfirmed) {
            fetch(`/api/catatan-pengujian/${id}`, {
                method: 'DELETE',
                headers: {
                    'Accept': 'application/json',
                    'Authorization': `Bearer ${token}`,
                    'X-CSRF-TOKEN': csrfToken
                }
            })
            .then(res => res.json())
            .then(json => {
                if (json.success) {
                    Swal.fire('Berhasil', json.message, 'success');
                    loadCatatanPengujianData();
                } else {
                    Swal.fire('Error', json.message, 'error');
                }
            })
            .catch(err => {
                console.error(err);
                Swal.fire('Error', 'Gagal menghapus data.', 'error');
            });
        }
    });
}

function openEditModal(item) {
    document.getElementById('catatan_pengujian_id').value = item.id;
    document.getElementById('uraian').value = item.uraian;
    document.getElementById('rencana_tindak_lanjut').value = item.rencana_tindak_lanjut || '';
    document.getElementById('CatatanPengujianModalLabel').textContent = 'Edit Catatan Pengujian';
    document.getElementById('saveCatatanPengujianBtn').textContent = 'Update';
    new bootstrap.Modal(document.getElementById('CatatanPengujianModal')).show();
}

document.addEventListener('DOMContentLoaded', loadCatatanPengujianData);

document.getElementById('saveCatatanPengujianBtn').addEventListener('click', function () {
    const token = getAuthToken();
    const id = document.getElementById('catatan_pengujian_id').value;
    const method = id ? 'PUT' : 'POST';
    const url = id ? `/api/catatan-pengujian/${id}` : '/api/catatan-pengujian';

    const data = {
        uraian: document.getElementById('uraian').value,
        rencana_tindak_lanjut: document.getElementById('rencana_tindak_lanjut').value
    };

    fetch(url, {
        method,
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
            'Authorization': `Bearer ${token}`,
            'X-CSRF-TOKEN': csrfToken
        },
        body: JSON.stringify(data)
    })
    .then(res => res.json())
    .then(json => {
        if (json.success) {
            Swal.fire('Berhasil', json.message, 'success');
            document.getElementById('CatatanPengujianForm').reset();

            const modalElement = document.getElementById('CatatanPengujianModal');
            const modalInstance = bootstrap.Modal.getInstance(modalElement) || new bootstrap.Modal(modalElement);
            document.querySelectorAll('.modal-backdrop').forEach(el => el.remove());
            document.body.classList.remove('modal-open');
            document.body.style.overflow = '';
            document.body.style.paddingRight = '';
            modalInstance.hide();

            loadCatatanPengujianData();
        } else {
            Swal.fire('Gagal', json.message, 'error');
        }
    })
    .catch(err => {
        console.error(err);
        Swal.fire('Error', 'Terjadi kesalahan saat menyimpan.', 'error');
    });
});
