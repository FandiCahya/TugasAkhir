@extends('layouts.app')
@section('content')
    <div class="col-lg-12 grid-margin stretch-card">
        <div class="card">
            <div class="card-body">
                <h4 class="card-title">Laporan</h4>

                <!-- Search Input -->
                <input type="text" id="search" class="form-control mb-3" placeholder="Search by name sistem..." />

                <div class="table-responsive pt-3">
                    <table class="table table-bordered" id="Laporan-table">
                        <thead>
                            <tr>
                                <th style="width: 15%;">Nama Sistem <span id="sort-name" class="cursor-pointer">🔽</span>
                                </th>
                                <th style="width: 10%;">Status</th>
                                <th style="width: 15%;">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <!-- Data will be dynamically filled here using JavaScript -->
                        </tbody>
                    </table>
                    <div id="pagination-controls" class="mt-3 d-flex justify-content-center"></div>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal Preview PDF -->
    <div class="modal fade" id="pdfPreviewModal" tabindex="-1" role="dialog" aria-labelledby="pdfPreviewLabel"
        aria-hidden="true">
        <div class="modal-dialog modal-xl" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="pdfPreviewLabel">Preview Laporan PDF</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close" onclick="closeModal()">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body position-relative">
                    <!-- Spinner -->
                    <div id="spinner"
                        class="d-flex justify-content-center align-items-center position-absolute top-0 start-0 w-100 h-100 bg-white"
                        style="z-index: 10;">
                        <div class="spinner-border text-primary" role="status">
                            <span class="visually-hidden">Loading...</span>
                        </div>
                    </div>

                    <!-- Iframe PDF -->
                    <iframe id="pdfFrame" src="" width="100%" height="600px" style="border:none;"></iframe>
                </div>
            </div>
        </div>
    </div>
@endsection
@push('scripts')
    <script>
        let Laporan = []; // Array to hold fetched Laporan
        let currentPage = 1;
        const rowsPerPage = 5;

        // Fetch Laporan from API
        function fetchLaporan(query = '') {
            fetch('/api/showall')
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        Laporan = data.payload;

                        // Filter berdasarkan query pencarian jika ada
                        if (query) {
                            Laporan = Laporan.filter(p => p.pengajuan.nama_sistem.toLowerCase().includes(query
                                .toLowerCase()));
                        }

                        renderTable(Laporan, currentPage); // Render tabel dengan data Laporan
                    } else {
                        alert('Gagal memuat data Laporan');
                    }
                })
                .catch(error => console.error('Error:', error));
        }

        // Render data in the table
        function renderTable(Laporan, page = 1) {
            const tableBody = document.querySelector('#Laporan-table tbody');
            tableBody.innerHTML = ''; // Bersihkan tabel sebelum merender

            // Hitung total halaman
            const totalPages = Math.ceil(Laporan.length / rowsPerPage);

            // Tentukan indeks awal dan akhir untuk slicing data
            const startIndex = (page - 1) * rowsPerPage;
            const endIndex = startIndex + rowsPerPage;

            // Ambil data sesuai halaman
            const paginatedData = Laporan.slice(startIndex, endIndex);

            // Render baris tabel
            paginatedData.forEach(p => {
                const row = document.createElement('tr');
                row.innerHTML = `
            <td>${p.pengajuan.nama_sistem}</td>
            <td>${p.pengajuan.status}</td>
            <td>
                    <button class="btn btn-success btn-sm" onclick="downloadLaporan('${p.persetujuan_pengujian.id}')" style="margin: 5px;">Download</button>
            </td>
        `;
                tableBody.appendChild(row);
            });

            // Render Pagination Controls
            renderPaginationControls(totalPages);
        }

        function changePage(page) {
            currentPage = page;
            renderTable(Laporan, currentPage); // Pastikan pakai Laporan, bukan data
        }

        function renderPaginationControls(totalPages) {
            const paginationContainer = document.querySelector('#pagination-controls');
            paginationContainer.innerHTML = '';

            if (totalPages <= 1) return; // Jangan tampilkan pagination jika hanya ada 1 halaman

            let paginationHTML = '';

            // Tombol Previous
            if (currentPage > 1) {
                paginationHTML +=
                    `<button onclick="changePage(${currentPage - 1})" class="btn btn-secondary mx-1">Previous</button>`;
            }

            // Tombol angka halaman
            for (let i = 1; i <= totalPages; i++) {
                paginationHTML +=
                    `<button onclick="changePage(${i})" class="btn ${i === currentPage ? 'btn-secondary' : 'btn-outline-secondary'} mx-1">${i}</button>`;
            }

            // Tombol Next
            if (currentPage < totalPages) {
                paginationHTML +=
                    `<button onclick="changePage(${currentPage + 1})" class="btn btn-secondary mx-1">Next</button>`;
            }

            paginationContainer.innerHTML = paginationHTML;
        }

        function hideSpinner() {
            document.getElementById('spinner').style.display = 'none';
        }

        function closeModal() {
            document.getElementById('pdfFrame').src = ""; // reset iframe biar nggak ngeload terus
            const modalElement = document.getElementById('pdfPreviewModal');
            const modalInstance = bootstrap.Modal.getInstance(modalElement);
            modalInstance.hide();
        }


        function downloadLaporan(id) {
            const downloadUrl = `/api/laporan/${id}/download`; // route untuk download PDF
            window.open(downloadUrl, '_blank');
        }

        // Fetch Laporan initially
        fetchLaporan();

        document.addEventListener("DOMContentLoaded", function() {
            const iframe = document.getElementById("pdfFrame");
            iframe.addEventListener("load", function() {
                hideSpinner();
            });
        });
    </script>
@endpush
