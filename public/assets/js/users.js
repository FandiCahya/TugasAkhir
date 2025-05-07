let UsersData = [];
let filteredData = [];
let currentPage = 1;
const itemsPerPage = 10;
// Fungsi untuk mengambil token
const getAuthToken = () => localStorage.getItem('token'); 


// 1. Ambil data dari API
function loadUsersData() {
    const token = getAuthToken();
    console.log("Token di Users: ", token);
    fetch('/api/users', {
            headers: {
            'Accept': 'application/json',
            'Authorization': `Bearer ${token}`
            }
        })
        .then(res => res.json())
        .then(json => {
            // console.log("Data dari API:", json);
            if (!json.success) {
                return Swal.fire('Error', 'ada kesalahan hit api users', 'error');
            }
            UsersData = json.data;
            // console.log("Data Users: ", UsersData);
            filteredData = UsersData;    // awalnya filter = semua data
            currentPage = 1;
            renderTable();
        })
        .catch(err => {
            console.error(err);
            Swal.fire('Error', 'Gagal memuat data.', 'error');
        });
}

// 2. Render tabel berdasarkan filteredData & paging
function renderTable() {
    const tbody = document.getElementById('user-list');
    tbody.innerHTML = '';

    // paging
    const start = (currentPage - 1) * itemsPerPage;
    const pageData = filteredData.slice(start, start + itemsPerPage);

    pageData.forEach(item => {
        const createdAt = new Date(item.created_at);
            const formattedDate = createdAt.toLocaleString('id-ID', {
            weekday: 'short', // opsional, menampilkan hari (Sen, Sel, dst)
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit',
            hour12: false // jika ingin format 24 jam
            });
        const tr = document.createElement('tr');
        // console.log(item)
        tr.innerHTML = `
        <td class="text-center">${item.name}</td>
        <td class="text-center">${item.email}</td>
        <td class="text-center">${item.role}</td>
        <td class="text-center">${item.devisi}</td>
        <td class="text-center">${formattedDate}</td>
        <td class="text-center">
            <button class="btn btn-sm btn-primary me-1" onclick='openEditModal(${JSON.stringify(item)})'>Edit</button>
            <button class="btn btn-sm btn-danger" onclick="deleteUsers('${item.id}')">Delete</button>
        </td>

        `;
        tbody.appendChild(tr);
    });

    renderPagination();
}

// 3. Render tombol pagination
function renderPagination() {
    const totalPages = Math.ceil(filteredData.length / itemsPerPage);
    document.querySelector('button[onclick="prevPage()"]').disabled = currentPage === 1;
    document.querySelector('button[onclick="nextPage()"]').disabled = currentPage === totalPages;
}

// 4. Prev / Next
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

// 5. Search lokal
function searchUsers() {
    const q = document.getElementById('search').value.trim().toLowerCase();
    filteredData = UsersData.filter(item =>
        item.name.toLowerCase().includes(q) ||
        item.email.toLowerCase().includes(q) ||
        item.role.toLowerCase().includes(q) ||
        item.devisi.toLowerCase().includes(q)
    );
    currentPage = 1;
    renderTable();
}

// Hapus Data
function deleteUsers(id) {
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
            fetch(`/api/users/${encodeURIComponent(id)}`, {
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
                        loadUsersData();
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

// 6. Open modal Add User
function openAddModal() {
    document.getElementById('userForm').reset();
    document.getElementById('user-id').value = '';
    document.getElementById('userModalLabel').textContent = 'Tambah User';
    document.getElementById('submitUserBtn').textContent = 'Save';
    new bootstrap.Modal(document.getElementById('userModal')).show();
}

// 7. Open modal Edit User
function openEditModal(item) {
    document.getElementById('user-id').value = item.id;
    document.getElementById('user-name').value = item.name;
    document.getElementById('user-email').value = item.email;
    document.getElementById('user-role').value = item.role;
    document.getElementById('user-devisi').value = item.devisi;

    document.getElementById('userModalLabel').textContent = 'Edit User';
    document.getElementById('submitUserBtn').textContent = 'Update';
    new bootstrap.Modal(document.getElementById('userModal')).show();
}

// 8. Inisialisasi ketika dokumen siap
document.addEventListener('DOMContentLoaded', loadUsersData);


// 9. Event listener untuk tombol tambah dan edit user
document.getElementById('submitUserBtn').addEventListener('click', function (e) {
    // Ambil nilai dari form
    const token = getAuthToken();
    const id = document.getElementById('user-id').value;
    const method = id ? 'PUT' : 'POST';
    const url = id ? `/api/users/${id}` : '/api/users';

    const data = {
        name: document.getElementById('user-name').value,
        email: document.getElementById('user-email').value,
        role: document.getElementById('user-role').value,
        devisi: document.getElementById('user-devisi').value,
        password: document.getElementById('user-password').value
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
            document.getElementById('userForm').reset();

            const modalElement = document.getElementById('userModal');
            const modalInstance = bootstrap.Modal.getInstance(modalElement) || new bootstrap.Modal(modalElement);

            // Fix overlay modal backdrop
            document.querySelectorAll('.modal-backdrop').forEach(el => el.remove());
            document.body.classList.remove('modal-open');
            document.body.style.overflow = '';
            document.body.style.paddingRight = '';
            modalInstance.hide();   

            loadUsersData(); // Load data setelah save/update
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
