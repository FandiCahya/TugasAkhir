@extends('layouts.app')
@section('content')
    <div class="col-lg-12 grid-margin stretch-card">
        <div class="card">
            <div class="card-body">
                <h4 class="card-title">Approval</h4>

                <!-- Search Input -->
                <input type="text" id="search" class="form-control mb-3" placeholder="Search by .." />

                <div class="table-responsive pt-3">
                    <table class="table table-bordered" id="approval-table">
                        <thead>
                            <tr>
                                <th>Nama User</th>
                                <th>Status</th>
                                <th>Catatan</th>
                                <th>Signature</th>
                                <th>Role</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <!-- Data rows will be injected here by JavaScript -->
                        </tbody>
                    </table>
                    <div id="pagination-controls" class="mt-3 d-flex justify-content-center"></div>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal for Approval -->
    <div class="modal fade" id="approvalModal" tabindex="-1" aria-labelledby="approvalModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="approvalModalLabel">Approval Pengujian</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <!-- Form untuk approval -->
                    <form id="approvalForm">
                        <div class="mb-3">
                            <label for="status" class="form-label">Status</label>
                            <select class="form-select" id="status" name="status">
                                <option value="setuju">Setuju</option>
                                <option value="tidak_setuju">Tidak Setuju</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="catatan" class="form-label">Catatan</label>
                            <textarea class="form-control" id="catatan" name="catatan" rows="3"></textarea>
                        </div>
                        <!-- Add this in the modal body where the signature is shown -->
                        <div class="mb-3" id="signature-preview">
                        </div>

                        <div class="mb-3">
                            <label for="signature" class="form-label">Signature</label>
                            <input class="form-control" type="file" id="signature" name="signature">
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-primary" id="approveButton">Approve</button>
                </div>
            </div>
        </div>
    </div>


    <script>
        let DetailApproval = [];
        let currentApprovalId = null;
        let currentPengujianId = null;
        let groupedApproval = {};
        let currentPage = 1;
        const rowsPerPage = 1;

        function fetchapprovalDetail(query = '') {
            fetch('/api/approval')
                .then(response => response.json())
                .then(data => {
                    if (data.success && Array.isArray(data.payload)) {
                        let filteredData = data.payload;

                        if (query) {
                            filteredData = filteredData.filter(p =>
                                p.status.toLowerCase().includes(query.toLowerCase()) ||
                                p.user.name.toLowerCase().includes(query.toLowerCase()) ||
                                p.catatan.toLowerCase().includes(query.toLowerCase()) ||
                                p.user.role.toLowerCase().includes(query.toLowerCase())
                            );
                        }

                        groupedApproval = groupByApprovalId(filteredData);
                        renderTable(groupedApproval, currentPage);
                    } else {
                        console.error('Failed to load approval data or data is not an array.');
                        alert('Failed to load approval data');
                    }
                })
                .catch(error => console.error('Error:', error));
        }


        function groupByApprovalId(data) {
            return data.reduce((acc, item) => {
                const approvalId = item.persetujuan_pengujian.id;
                if (!acc[approvalId]) {
                    acc[approvalId] = [];
                }
                acc[approvalId].push(item); // Menambahkan item ke grup yang sesuai
                return acc;
            }, {});
        }

        function renderTable(groupedApproval, page = 1) {
            const tableBody = document.querySelector('#approval-table tbody');
            tableBody.innerHTML = ''; // Clear the existing table body

            const keys = Object.keys(groupedApproval); // Perbaikan di sini
            const totalPages = Math.ceil(keys.length / rowsPerPage);

            // Tentukan indeks awal dan akhir untuk slicing data
            const startIndex = (page - 1) * rowsPerPage;
            const endIndex = startIndex + rowsPerPage;

            // Ambil data sesuai halaman
            const paginatedKeys = keys.slice(startIndex, endIndex);
            // Iterasi melalui setiap grup berdasarkan approvalId
            paginatedKeys.forEach(approvalId => {
                const group = groupedApproval[approvalId]; // Perbaikan di sini

                // Buat baris header grup berdasarkan approvalId
                const groupHeaderRow = document.createElement('tr');
                groupHeaderRow.classList.add('table-group-header');
                groupHeaderRow.innerHTML = `
            <td colspan="7" class="group-header">
                <span>Status Pengujian: <strong>${group[0].persetujuan_pengujian.status}</strong></span>
            </td>
        `;
                tableBody.appendChild(groupHeaderRow);

                // Render setiap row detail dalam grup
                group.forEach(approval => {
                    const row = document.createElement('tr');
                    row.classList.add('table-row');

                    row.innerHTML = `
                <td>${approval.user.name || '-'}</td>
                <td>${approval.status}</td>
                <td>${approval.catatan || '-'}</td>
                <td><img src="/storage/${approval.signature}" alt="Signature" style="width: 50px; height: auto;"></td>
                <td>${approval.user.role || '-'}</td>
                <td class="action-buttons">
                    <button class="btn btn-warning btn-sm" onclick="openApprovalModal('${approval.id}', '${approval.persetujuan_pengujian.id}')" style="margin: 5px;" data-bs-toggle="modal" data-bs-target="#approvalModal">Approve</button>
                </td>
            `;
                    tableBody.appendChild(row);
                });
            });

            // Render pagination controls
            renderPaginationControls(totalPages);
        }

        function changePage(page) {
            const totalPages = Math.ceil(Object.keys(groupedApproval).length / rowsPerPage);
            if (page < 1 || page > totalPages) return;

            currentPage = page;
            renderTable(groupedApproval, currentPage);
        }


        function renderPaginationControls(totalPages) {
            const paginationContainer = document.querySelector('#pagination-controls');
            paginationContainer.innerHTML = '';

            if (totalPages <= 1) return;

            let paginationHTML = '';

            if (currentPage > 1) {
                paginationHTML +=
                    `<button onclick="changePage(${currentPage - 1})" class="btn btn-secondary mx-1">Previous</button>`;
            }

            for (let i = 1; i <= totalPages; i++) {
                paginationHTML +=
                    `<button onclick="changePage(${i})" class="btn ${i === currentPage ? 'btn-secondary' : 'btn-outline-secondary'} mx-1">${i}</button>`;
            }

            if (currentPage < totalPages) {
                paginationHTML +=
                    `<button onclick="changePage(${currentPage + 1})" class="btn btn-secondary mx-1">Next</button>`;
            }

            paginationContainer.innerHTML = paginationHTML;
        }

        // Event listener untuk input pencarian
        document.getElementById('search').addEventListener('input', function(event) {
            fetchapprovalDetail(event.target.value);
        });

        function openApprovalModal(approvalId, pengujianId) {
            document.getElementById('approvalForm').reset();
            currentApprovalId = approvalId;
            currentPengujianId = pengujianId;

            const approval = DetailApproval.find(item => item.id === approvalId);
            if (approval) {
                document.getElementById('status').value = approval.status;
                document.getElementById('catatan').value = approval.catatan || '';

                // Pastikan signature tidak kosong sebelum menampilkannya
                if (approval.signature) {
                    const signaturePreview = document.createElement('img');
                    signaturePreview.src = '/storage/' + approval.signature;
                    signaturePreview.alt = 'Signature';
                    signaturePreview.style = 'width: 100px; height: auto;';
                    document.getElementById('signature-preview').innerHTML = '';
                    document.getElementById('signature-preview').appendChild(signaturePreview);
                } else {
                    document.getElementById('signature-preview').innerHTML = '';
                }
            }
        }

        document.getElementById('approveButton').addEventListener('click', function() {
            const status = document.getElementById('status').value;
            const catatan = document.getElementById('catatan').value;
            const signature = document.getElementById('signature').files[0]; // File signature (jika ada)

            const formData = new FormData();
            formData.append('status', status);
            formData.append('catatan', catatan);
            // formData.append('user_id', 'user_id_value'); // Ganti dengan ID user yang valid

            if (signature) {
                formData.append('signature', signature); // Jika ada signature, tambahkan ke formData
            }

            // Kirim ke API
            fetch(`api/persetujuan-pengujian-detail/${currentApprovalId}/approval`, {
                    method: 'POST',
                    body: formData,
                })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        // alert(data.message);
                        // $('#approvalModal').modal('hide'); // Menutup modal
                        let editModal = bootstrap.Modal.getInstance(document.getElementById(
                            'approvalModal'));
                        if (editModal) {
                            editModal.hide();
                        }
                        fetchapprovalDetail(); // Reload data approval
                    } else {
                        alert(data.message);
                    }
                })
                .catch(error => console.error('Error:', error));
        });



        fetchapprovalDetail(); // Memuat data pertama kali
    </script>
@endsection
