        let LaporanData = []; // Array to hold fetched Laporan
        let filteredData = [];
        let currentPage = 1;
        const itemsPerPage = 5;
        const getAuthToken = () => localStorage.getItem('token'); 

        // Fetch Laporan from API
        function fetchLaporan(query = '') {
            const token = getAuthToken(); // Pastikan fungsi ini mengembalikan token yang valid

            fetch('/api/showall?status_pengajuan=finished', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                }
            })
            .then(res => res.json())
            .then(json => {
                if (!json.success) {
                    return Swal.fire('Error', 'Gagal memuat data laporan', 'error');
                }
                LaporanData = json.payload;
                // console.log('LaporanData:', LaporanData);
                filteredData = LaporanData;
                currentPage = 1;
                renderTable();
            })
            .catch(err => {
                console.error(err);
                Swal.fire('Error', 'Terjadi kesalahan koneksi.', 'error');
            });
        } 

        // Render data in the table
        function renderTable() {
                const tbody = document.getElementById('laporan-list');
                tbody.innerHTML = '';

                // paging
            const start = (currentPage - 1) * itemsPerPage;
            const pageData = filteredData.slice(start, start + itemsPerPage);

            pageData.forEach(item => {
                const createdAt = new Date(item.pengajuan.created_at);
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
                <td class="text-center">${item.pengajuan.nama_sistem}</td>
                <td class="text-center">${item.pengajuan.status}</td>
                <td class="text-center">${formattedDate}</td>
                <td class="text-center">
                    <button class="btn btn-success" onclick="downloadLaporan('${item.persetujuan_pengujian.id}')">Download</button>
                    </td>
                `;
                tbody.appendChild(tr);
            });

            // Render Pagination Controls
            renderPagination();
        }

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

        function searchLaporan() {
            const q = document.getElementById('search').value.trim().toLowerCase();
            filteredData = LaporanData.filter(item =>
                item.pengajuan.nama_sistem.toLowerCase().includes(q) ||
                item.pengajuan.status.toLowerCase().includes(q)
            );
            currentPage = 1;
            renderTable();
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
            if (iframe) {
                iframe.addEventListener("load", function() {
                    hideSpinner();
                });
            }
        });
