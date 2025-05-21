let ApprovalData = [];
let filteredData = [];
let currentPage = 1;
const itemsPerPage = 3;
const getAuthToken = () => localStorage.getItem('token');
// console.log("Token di Approval: ", getAuthToken());
let isDrawing = false;
let lastX = 0;
let lastY = 0;
let canvas, ctx;

window.addEventListener('load', () => {
    canvas = document.getElementById('signatureCanvas');
    if (canvas) {
        ctx = canvas.getContext('2d');
        ctx.lineWidth = 2;
        ctx.lineCap = 'round'; // buat garis ujung membulat
        ctx.strokeStyle = '#000';

        canvas.addEventListener('mousedown', (e) => {
            isDrawing = true;
            [lastX, lastY] = [e.offsetX, e.offsetY];
        });

        canvas.addEventListener('mousemove', draw);
        canvas.addEventListener('mouseup', () => isDrawing = false);
        canvas.addEventListener('mouseout', () => isDrawing = false);
    }
});

function draw(e) {
    if (!isDrawing) return;
    ctx.beginPath();
    ctx.moveTo(lastX, lastY);
    ctx.lineTo(e.offsetX, e.offsetY);
    ctx.stroke();
    [lastX, lastY] = [e.offsetX, e.offsetY];
}

function clearSignature() {
    if (ctx && canvas) {
        ctx.clearRect(0, 0, canvas.width, canvas.height);
    }
}

// Load data dari /api/showall
function loadApprovalData() {
    const token = getAuthToken();

    fetch('/api/showall', {
        headers: {
            'Accept': 'application/json',
            'Authorization': `Bearer ${token}`
        }
    })
    .then(res => res.json())
    .then(json => {
        if (!json.success) {
            return Swal.fire('Error', 'Gagal memuat data approval', 'error');
        }

        ApprovalData = json.payload;
        // console.log("Data Approval: ", ApprovalData);
        filteredData = ApprovalData;
        currentPage = 1;
        renderTable();
    })
    .catch(err => {
        console.error(err);
        Swal.fire('Error', 'Terjadi kesalahan saat memuat data.', 'error');
    });
}

// Group data by pengujian.id
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
    const tbody = document.getElementById('approval-body');
    tbody.innerHTML = '';

    const grouped = groupByPengujian(filteredData);
    const groupedArray = Object.values(grouped);
    const start = (currentPage - 1) * itemsPerPage;
    const pageGroups = groupedArray.slice(start, start + itemsPerPage);

    pageGroups.forEach(group => {
        const trGroup = document.createElement('tr');
        trGroup.classList.add('table-secondary', 'fw-semibold');
        trGroup.innerHTML = `
            <td colspan="6" class="py-3">
                <div>
                    <strong>Perangkat Lunak:</strong> ${group.pengujian.perangkat_lunak} |
                    <strong>Versi:</strong> ${group.pengujian.versi} |
                    <strong>Tujuan:</strong> ${group.pengujian.tujuan} |
                    <strong>Metode:</strong> ${group.pengujian.metode} |
                    <strong>Tanggal:</strong> ${group.pengujian.tanggal}
                </div>
            </td>
        `;
        tbody.appendChild(trGroup);

        group.items.forEach(item => {
            item.persetujuan_pengujian_details.forEach(detail => {
                const tr = document.createElement('tr');
                const statusBadge = getStatusBadge(detail.status);
                tr.innerHTML = `
                    <td class="align-middle text-center">${detail.user.name}</td>
                    <td class="align-middle text-center">${statusBadge}</td>
                    <td class="align-middle text-center">${detail.catatan || '-'}</td>
                    <td class="align-middle text-center">
                    ${detail.signature 
                        ? `<img src="${window.location.origin}/storage/${detail.signature}" alt="Tanda Tangan" class="img-fluid border rounded" style="max-width: 100%; max-height: 200px;">` 
                        : '-'}
                    </td>
                    <td class="align-middle text-center">${new Date(item.persetujuan_pengujian.updated_at).toLocaleString()}</td>
                    <td class="align-middle text-center">
                        <button class="btn btn-sm btn-warning me-2" onclick='openEditModal(${JSON.stringify(detail)})'>
                            <i class="bi bi-pencil-square"></i> Edit
                        </button>
                        <button class="btn btn-sm btn-danger" onclick="deleteApproval('${detail.id}')">
                            <i class="bi bi-trash"></i> Delete
                        </button>
                    </td>
                `;
                tbody.appendChild(tr);
            });
        });
    });

    renderPagination(groupedArray.length);
}

// Tambahan fungsi untuk memberi badge status
function getStatusBadge(status) {
    if (!status) return '<span class="badge bg-secondary">-</span>';
    switch (status.toLowerCase()) {
        case 'approved':
            return '<span class="badge bg-success">Approved</span>';
        case 'rejected':
            return '<span class="badge bg-danger">Rejected</span>';
        case 'pending':
            return '<span class="badge bg-warning text-dark">Pending</span>';
        default:
            return `<span class="badge bg-secondary">${status}</span>`;
    }
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

function searchApproval() {
    const q = document.getElementById('search').value.trim().toLowerCase();
    filteredData = ApprovalData.filter(item =>
        (item.status || '').toLowerCase().includes(q) ||
        (item.catatan || '').toLowerCase().includes(q) ||
        (item.pengujian.perangkat_lunak || '').toLowerCase().includes(q) ||
            item.persetujuan_pengujian_details.some(detail =>
        (detail.user?.name || '').toLowerCase().includes(q)
    )
    );
    currentPage = 1;
    renderTable();
}

function deleteApproval(id) {
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
            fetch(`/api/persetujuan-pengujian/${id}`, {
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
                    loadApprovalData();
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
    // console.log("Item yang diedit: ", item);
    document.getElementById('approval_id').value = item.id;
    document.getElementById('status').value = item.status;
    document.getElementById('catatan').value = item.catatan || '';
    
    // Reset metode tanda tangan
    document.querySelector('input[value="upload"]').checked = true;
    document.getElementById('signature-upload-wrapper').classList.remove('d-none');
    document.getElementById('signature-draw-wrapper').classList.add('d-none');
    clearCanvas();
    document.getElementById('signature').value = null;

    // Tampilkan tanda tangan lama (jika ada)
    const preview = document.getElementById('signature-preview');
    preview.innerHTML = '';

    if (item.signature) {
    const imageUrl = `${window.location.origin}/storage/${item.signature}`;
    preview.innerHTML = `<p class="mb-2">Tanda tangan sebelumnya:</p>
                        <img src="${imageUrl}" class="img-fluid border rounded" alt="Tanda Tangan Sebelumnya" style="max-width: 7rem;">`;
}


    document.getElementById('approvalModalLabel').textContent = 'Edit Approval';
    document.getElementById('approveButton').textContent = 'Update';
    new bootstrap.Modal(document.getElementById('approvalModal')).show();
}


function clearCanvas() {
    const canvas = document.getElementById('signatureCanvas');
    const ctx = canvas.getContext('2d');
    ctx.clearRect(0, 0, canvas.width, canvas.height);
}

document.addEventListener('DOMContentLoaded', loadApprovalData);

document.querySelectorAll('input[name="signatureMethod"]').forEach(el => {
    el.addEventListener('change', function () {
        const method = this.value;
        document.getElementById('signature-upload-wrapper').classList.toggle('d-none', method !== 'upload');
        document.getElementById('signature-draw-wrapper').classList.toggle('d-none', method !== 'draw');
    });
});

function isCanvasBlank(canvas) {
    const blank = document.createElement('canvas');
    blank.width = canvas.width;
    blank.height = canvas.height;
    return canvas.toDataURL() === blank.toDataURL();
}

document.getElementById('approveButton').addEventListener('click', function () {
    const token = getAuthToken();
    const id = document.getElementById('approval_id').value;
    const method = id ? 'POST' : 'POST'; // Kamu route POST saja karena Laravel kamu pakai POST untuk update
    const url = `/api/persetujuan-pengujian-detail/${id}/approval`; // Sesuaikan endpoint

    const status = document.getElementById('status').value;
    const catatan = document.getElementById('catatan').value;
    const selectedMethod = document.querySelector('input[name="signatureMethod"]:checked')?.value;

    let formData = new FormData();
    formData.append('status', status);
    formData.append('catatan', catatan);

    if (!id) {
        return Swal.fire('Error', 'ID persetujuan tidak ditemukan.', 'error');
    }

    if (selectedMethod === 'draw') {
        const canvas = document.getElementById('signatureCanvas');
        if (isCanvasBlank(canvas)) {
            return Swal.fire('Error', 'Tanda tangan digital tidak boleh kosong.', 'error');
        }
        // Convert canvas to Blob and append to FormData
        canvas.toBlob(function (blob) {
        const uniqueName = `signature_${Date.now()}.png`;
        formData.append('signature', blob, uniqueName);
        sendRequest();
    }, 'image/png');
    } else if (selectedMethod === 'upload') {
        const fileInput = document.getElementById('signature');
        const file = fileInput.files[0];
        if (!file) {
            return Swal.fire('Error', 'Silakan unggah tanda tangan terlebih dahulu.', 'error');
        }
        formData.append('signature', file, file.name);
        sendRequest();
    } else {
        // Jika tidak ada tanda tangan (optional, sesuai validasi backend)
        sendRequest();
    }

    function sendRequest() {
        fetch(url, {
            method: 'POST', // sesuai route backend-mu
            headers: {
                'Accept': 'application/json',
                'Authorization': `Bearer ${token}`,
                'X-CSRF-TOKEN': csrfToken
                // Jangan set 'Content-Type' kalau pakai FormData, biarkan browser atur otomatis
            },
            body: formData,
        })
        .then(res => res.json())
        .then(json => {
            if (json.success) {
                Swal.fire('Berhasil', json.message, 'success');
                document.getElementById('approvalForm').reset();
                clearCanvas();

                const modal = bootstrap.Modal.getInstance(document.getElementById('approvalModal'));
                modal.hide();
                loadApprovalData();
            } else {
                Swal.fire('Gagal', json.message || 'Terjadi kesalahan', 'error');
            }
        })
        .catch(err => {
            console.error(err);
            Swal.fire('Error', 'Terjadi kesalahan saat menyimpan.', 'error');
        });
    }
});

