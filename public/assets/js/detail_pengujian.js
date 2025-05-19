let DetailPengujianData = [];
let filteredData = [];
let currentPage = 1;
const itemsPerPage = 3;
// Fungsi untuk mengambil token
const getAuthToken = () => localStorage.getItem('token');

// 1. Ambil data dari API
function loadDetailPengujianData() {
    const token = getAuthToken();
    console.log("Token di DetailPengujian: ", token);
    fetch('/api/pengujian-detail', {
            headers: {
            'Accept': 'application/json',
            'Authorization': `Bearer ${token}`
            }
        })
        .then(res => res.json())
        .then(json => {
            // console.log("Data dari API:", json);
            if (!json.success) {
                return Swal.fire('Error', 'ada kesalahan hit api Detail Pengujian', 'error');
            }
            DetailPengujianData = json.payload;
            console.log("Data DetailPengujian: ", DetailPengujianData);
            filteredData = DetailPengujianData;    // awalnya filter = semua data
            currentPage = 1;
            renderTable();
        })
        .catch(err => {
            console.error(err);
            Swal.fire('Error', 'Gagal memuat data.', 'error');
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

// 2. Render tabel berdasarkan filteredData & paging
function renderTable() {
    const tbody = document.getElementById('pengujian-detail-body');
    tbody.innerHTML = '';

    // Step 1: Group seluruh data berdasarkan pengujian.id
    const grouped = groupByPengujian(filteredData);

    // Step 2: Ubah object grouped jadi array supaya bisa paging
    const groupedArray = Object.values(grouped);

    // Step 3: Pagination berdasarkan grup (bukan item)
    const start = (currentPage - 1) * itemsPerPage;
    const pageGroups = groupedArray.slice(start, start + itemsPerPage);

    // Step 4: Render tiap grup yang sudah di-paging
    pageGroups.forEach(group => {
        // Render header grup
        const trGroup = document.createElement('tr');
        trGroup.classList.add('table-secondary');
        trGroup.innerHTML = `
            <td colspan="6">
                <strong>Perangkat Lunak:</strong> ${group.pengujian.perangkat_lunak} |
                <strong>Versi:</strong> ${group.pengujian.versi} |
                <strong>Tujuan:</strong> ${group.pengujian.tujuan} |
                <strong>Metode:</strong> ${group.pengujian.metode} |
                <strong>Tanggal:</strong> ${group.pengujian.tanggal}
            </td>
        `;
        tbody.appendChild(trGroup);

        // Render detail di dalam grup
        group.items.forEach(item => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td class="text-center" style="width:20%">${item.nama_uji}</td>
                <td class="text-center" style="width:20%">${item.kasus_uji}</td>
                <td class="text-center" style="width:20%">${item.hasil_diharapkan}</td>
                <td class="text-center" style="width:15%">${item.hasil_pengujian}</td>
                <td class="text-center" style="width:10%">${item.status}</td>
                <td class="text-center" style="width:15%">
                    <button class="btn btn-sm btn-warning me-2" onclick='openEditModal(${JSON.stringify(item)})'>Edit</button>
                    <button class="btn btn-sm btn-danger" onclick="deleteDetailPengujian('${item.id}')">Delete</button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    });

    // Step 5: Render pagination berdasarkan jumlah grup
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

// 5. Search lokal
function searchDetailPengujian() {
    const q = document.getElementById('search').value.trim().toLowerCase();
    filteredData = DetailPengujianData.filter(item =>
        item.nama_uji.toLowerCase().includes(q) ||
        item.kasus_uji.toLowerCase().includes(q) ||
        item.hasil_diharapkan.toLowerCase().includes(q) ||
        item.hasil_pengujian.toLowerCase().includes(q) ||
        item.status.toLowerCase().includes(q)
    );
    currentPage = 1;
    renderTable();
}

// Hapus Data
function deleteDetailPengujian(id) {
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
            fetch(`/api/pengujian-detail/${encodeURIComponent(id)}`, {
                method: 'DELETE',
                headers: {
                    'Accept': 'application/json',
                    'Authorization': `Bearer ${token}`, // Tambahkan token di header
                    'X-CSRF-TOKEN': csrfToken
                }
            })
                .then(r => r.json())
                .then(res => {
                    if (res.success) {
                        Swal.fire('Dihapus', res.message, 'success');
                        loadDetailPengujianData();
                    } else {
                        Swal.fire('Error', res.message, 'error');
                    }
                })
                .catch(err => {
                    console.error(err);
                    Swal.fire('Error', 'Gagal menghapus.', 'error');
                });
        }
    });
}

//  Open modal Edit Detail Pengujian
function openEditModal(item) {
    document.getElementById('detail_pengujian_id').value = item.id;
    document.getElementById('nama_uji').value = item.nama_uji;
    document.getElementById('kasus_uji').value = item.kasus_uji;
    document.getElementById('hasil_diharapkan').value = item.hasil_diharapkan;
    document.getElementById('hasil_pengujian').value = item.hasil_pengujian;
    document.getElementById('status').value = item.status;

    document.getElementById('DetailPengujianModalLabel').textContent = 'Edit Detail Pengujian';
    document.getElementById('saveDetailPengujianBtn').textContent = 'Update Detail Pengujian';
    new bootstrap.Modal(document.getElementById('DetailPengujianModal')).show();
}

// 8. Inisialisasi ketika dokumen siap
document.addEventListener('DOMContentLoaded', loadDetailPengujianData);


// 9. Event listener untuk tombol tambah dan edit user
document.getElementById('saveDetailPengujianBtn').addEventListener('click', function (e) {
    // Ambil nilai dari form
    const token = getAuthToken();
    const id = document.getElementById('detail_pengujian_id').value;
    const method = id ? 'PUT' : 'POST'; // Jika id ada, gunakan PUT, jika tidak POST
    const url = id ? `/api/pengujian-detail/${id}` : '/api/pengujian-detail';

    const data = {
        nama_uji: document.getElementById('nama_uji').value,
        kasus_uji: document.getElementById('kasus_uji').value,
        hasil_diharapkan: document.getElementById('hasil_diharapkan').value,
        hasil_pengujian: document.getElementById('hasil_pengujian').value,
        status: document.getElementById('status').value
    };

    // Lakukan fetch
    fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
            'Authorization': `Bearer ${token}`, // Tambahkan token di header
            'X-CSRF-TOKEN': csrfToken
        },
        body: JSON.stringify(data)
    })
    .then(res => res.json())
    .then(res => {
        if (res.success) {
            Swal.fire('Berhasil', res.message, 'success');
            document.getElementById('DetailPengujianForm').reset();

            const modalElement = document.getElementById('DetailPengujianModal');
            const modalInstance = bootstrap.Modal.getInstance(modalElement) || new bootstrap.Modal(modalElement);

            // Fix overlay modal backdrop
            document.querySelectorAll('.modal-backdrop').forEach(el => el.remove());
            document.body.classList.remove('modal-open');
            document.body.style.overflow = '';
            document.body.style.paddingRight = '';
            modalInstance.hide();   

            loadDetailPengujianData(); // Load data setelah save/update
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
