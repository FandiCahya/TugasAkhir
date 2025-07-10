(function () {
    let dashboard = [];

    document.addEventListener("DOMContentLoaded", function () {
        fetchDashboard();
    });
    const getAuthToken = () => localStorage.getItem('token');

    function fetchDashboard() {
        const token = getAuthToken();
        fetch('/api/pengajuan', {
            headers: {
                'Accept': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    // console.log('Data fetched successfully:', data);
                    dashboard = data.payload;
                    renderStatistik(dashboard);
                } else {
                    alert('Gagal memuat data SOP');
                }
            })
            .catch(error => console.error('Error:', error));
    }

function renderStatistik(data) {
    const container = document.getElementById('dashboard-body');
    container.innerHTML = '';

    const statusCounter = {};
    const bulanCounter = {};
    console.log(bulanCounter);

    data.forEach(p => {
        const status = p.status || 'unknown';
        statusCounter[status] = (statusCounter[status] || 0) + 1;

        const tgl = new Date(p.tgl);
        const bulanTahun = tgl.toLocaleString('id-ID', {
            month: 'long',
            year: 'numeric'
        });
        bulanCounter[bulanTahun] = (bulanCounter[bulanTahun] || 0) + 1;
    });


    // STATUS SECTION
    const statusLabels = {
        pending: '🕓 Pending',
        accepted: '✅ Accepted',
        rejected: '❌ Rejected',
        developing: '⚙️ Developing',
        testing: '🧪 Testing',
        approval: '🗂️ Approval',
        finished: '🎉 Finished',
        unknown: '❓ Unknown'
    };

    const statusColors = {
        pending: '#FFC107',
        accepted: '#4CAF50',
        rejected: '#F44336',
        developing: '#2196F3',
        testing: '#9C27B0',
        approval: '#00BCD4',
        finished: '#8BC34A',
        unknown: '#9E9E9E'
    };
    Object.entries(statusCounter).forEach(([key, value]) => {
        container.insertAdjacentHTML('beforeend', `
            <div class="col-md-4 col-sm-6 mb-4">
                <div class="card shadow-sm" style="border-radius: 1rem; background-color: #f4f6fa;">
                    <div class="card-body text-center">
                        <h5 class="card-title">${statusLabels[key] || key}</h5>
                        <h2 class="fw-bold" style="color: ${statusColors[key] || '#000'}">${value}</h2>
                    </div>
                </div>
            </div>
        `);
    });

    // 1. Tambahkan canvas statusChart dulu
container.insertAdjacentHTML('beforeend', `
    <div class="col-12 mt-4">
        <div class="card">
            <div class="card-body px-2 px-md-4">
                <h5 class="text-muted">📊 Statistik Status Pengajuan</h5>
                <div class="chart-container" style="position: relative; width: 100%; max-width: 500px; margin: auto;">
                    <canvas id="statusChart" style="max-height: 250px;"></canvas>
                </div>
            </div>
        </div>
    </div>
`);



// 2. Setelah itu baru ambil dan render Chart.js
const statusChartLabels = Object.entries(statusCounter).map(([key]) => statusLabels[key] || key);
const statusChartValues = Object.entries(statusCounter).map(([_, val]) => val);
const statusChartColors = Object.entries(statusCounter).map(([key]) => statusColors[key] || '#000');

const ctxs = document.getElementById('statusChart').getContext('2d');
new Chart(ctxs, {
    type: 'doughnut',
    data: {
        labels: statusChartLabels,
        datasets: [{
            data: statusChartValues,
            backgroundColor: statusChartColors,
            borderWidth: 1
        }]
    },
    options: {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
        legend: {
            position: 'bottom',
            labels: {
                boxWidth: 10,
                font: {
                    size: 12
                }
            }
        }
    }
}

});

// BULAN SECTION (Chart)
container.insertAdjacentHTML('beforeend', `
    <div class="col-12 mt-4">
        <div class="card">
            <div class="card-body">
                <h5 class="text-muted">📅 Statistik Pengajuan per Bulan</h5>
                <canvas id="bulanChart" height="100"></canvas>
            </div>
        </div>
    </div>
`);

const bulanLabels = Object.keys(bulanCounter);
const bulanValues = Object.values(bulanCounter).map(v => Math.floor(v));
console.log(bulanValues);
console.log(bulanLabels);


// Render Chart.js
const ctx = document.getElementById('bulanChart').getContext('2d');
new Chart(ctx, {
    type: 'bar',
    data: {
        labels: bulanLabels,
        datasets: [{
            label: 'Jumlah Pengajuan',
            data: bulanValues,
            backgroundColor: 'rgba(54, 162, 235, 0.6)',
            borderColor: 'rgba(54, 162, 235, 1)',
            borderWidth: 1,
            borderRadius: 10,
        }]
    },
    options: {
        responsive: true,
        plugins: {
            legend: {
                display: false
            },
            tooltip: {
                callbacks: {
                    label: context => `Jumlah: ${context.parsed.y}`
                }
            }
        },
        scales: {
    y: {
        beginAtZero: true,
        suggestedMax: Math.max(...bulanValues) + 1, // Paksa Chart.js kasih ruang
        ticks: {
            stepSize: 1,
            callback: function(value) {
                return Number.isInteger(value) ? value : null;
            }
        },
        title: {
            display: true,
            text: 'Jumlah'
        }
    },
    x: {
        title: {
            display: true,
            text: 'Bulan'
        }
    }
}




    }
});

}

})();
