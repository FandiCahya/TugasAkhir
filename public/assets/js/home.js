(function () {
    let dashboard = [];

    document.addEventListener("DOMContentLoaded", function () {
        fetchDashboard();
    });

    function fetchDashboard(query = '') {
        fetch('/api/showall')
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    dashboard = data.payload;
                    renderTable(dashboard);
                } else {
                    alert('Gagal memuat data SOP');
                }
            })
            .catch(error => console.error('Error:', error));
    }

    function renderTable(pengajuan) {
        const tableBody = document.getElementById('dashboard-body');
        tableBody.innerHTML = '';

        pengajuan.forEach(p => {
            const row = document.createElement('tr');

            const createdAt = new Date(p.pengajuan.created_at);
            const formattedDate = createdAt.toLocaleString('id-ID', {
                weekday: 'long',
                day: '2-digit',
                month: 'long',
                year: 'numeric',
                hour: '2-digit',
                minute: '2-digit'
            });

            row.innerHTML = `
                <td>${p.pengajuan.nama_sistem}</td>
                <td>${p.pengujian.versi}</td>
                <td>${p.pengajuan.jenis}</td>
                <td>${p.pengajuan.status}</td>
                <td>${formattedDate}</td>
            `;

            tableBody.appendChild(row);
        });
    }
})();
