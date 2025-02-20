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


        function fetchapprovalDetail(query = '') {
            fetch('/api/approval')
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        DetailApproval = data.payload;

                        // Jika ada query pencarian, filter data
                        if (query) {
                            DetailApproval = DetailApproval.filter(p =>
                                p.user.name.toLowerCase().includes(query.toLowerCase()) ||
                                p.status.toLowerCase().includes(query.toLowerCase()) ||
                                p.catatan.toLowerCase().includes(query.toLowerCase())
                            );
                        }

                        // Kelompokkan data berdasarkan pengujian.id
                        const groupedData = groupByApprovalId(DetailApproval);
                        renderTable(groupedData);
                    } else {
                        alert('Failed to load approval data');
                    }
                })
                .catch(error => console.error('Error:', error));
        }

        function groupByApprovalId(data) {
            return data.reduce((acc, item) => {
                const approvalId = item.pengujian.id;
                if (!acc[approvalId]) {
                    acc[approvalId] = [];
                }
                acc[approvalId].push(item); // Menambahkan item ke grup yang sesuai
                return acc;
            }, {});
        }

        function renderTable(groupedData) {
            const tableBody = document.querySelector('#approval-table tbody');
            tableBody.innerHTML = ''; // Clear the existing table body

            // Iterasi melalui setiap grup berdasarkan pengujian.id
            for (const approvalId in groupedData) {
                const group = groupedData[approvalId];

                // Buat baris header grup berdasarkan pengujian.id
                const groupHeaderRow = document.createElement('tr');
                groupHeaderRow.classList.add('table-group-header');
                groupHeaderRow.innerHTML = `
                    <td colspan="7" class="group-header">
                        <strong>Pengujian ID: ${approvalId}</strong><br>
                        <span>Status Pengujian: <strong>${group[0].pengujian.status}</strong></span>
                    </td>
                `;
                tableBody.appendChild(groupHeaderRow);

                // Render setiap row detail dalam grup
                group.forEach(approval => {
                    const row = document.createElement('tr');
                    row.classList.add('table-row');

                    row.innerHTML = `
                        <td>${approval.user.name || 'N/A'}</td>
                        <td>${approval.status}</td>
                        <td>${approval.catatan || 'N/A'}</td>
                        <td><img src="${approval.signature}" alt="Signature" style="width: 50px; height: auto;"></td>
                        <td>${approval.role || 'N/A'}</td>
                        <td class="action-buttons">
                            <button class="btn btn-warning btn-sm" onclick="openApprovalModal('${approval.id}', '${approval.pengujian.id}')" style="margin: 5px;" data-bs-toggle="modal" data-bs-target="#approvalModal">Approve</button>
                        </td>
                    `;
                    tableBody.appendChild(row);
                });
            }
        }

        // Event listener untuk input pencarian
        document.getElementById('search').addEventListener('input', function(event) {
            fetchapprovalDetail(event.target.value);
        });

        function openApprovalModal(approvalId, pengujianId) {
            // Set data di modal
            document.getElementById('approvalForm').reset(); // Reset form sebelum dimunculkan
            currentApprovalId = approvalId;
            currentPengujianId = pengujianId;

            // Bisa menambahkan data lainnya yang diperlukan ke dalam modal jika perlu
            console.log("Approval ID:", approvalId);
            console.log("Pengujian ID:", pengujianId);
        }

        document.getElementById('approveButton').addEventListener('click', function() {
            const status = document.getElementById('status').value;
            const catatan = document.getElementById('catatan').value;
            const signature = document.getElementById('signature').files[0]; // File signature (jika ada)

            const formData = new FormData();
            formData.append('status', status);
            formData.append('catatan', catatan);
            formData.append('user_id', 'user_id_value'); // Ganti dengan ID user yang valid

            if (signature) {
                formData.append('signature', signature); // Jika ada signature, tambahkan ke formData
            }

            // Kirim ke API
            fetch(`/persetujuan-pengujian/${currentPengujianId}/approve`, {
                    method: 'PUT',
                    body: formData,
                })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        alert(data.message);
                        $('#approvalModal').modal('hide'); // Menutup modal
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
