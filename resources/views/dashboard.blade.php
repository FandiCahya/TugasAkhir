@extends('layouts.app')

@section('content')
    <div class="content-wrapper">
        <div class="row">
            <!-- Card 1: Facebook Color -->
            <div class="col-md-4 col-sm-6 grid-margin stretch-card">
                <div class="card bg-facebook d-flex align-items-center">
                    <div class="card-body py-5 text-center">
                        <i class="mdi mdi-account-multiple text-white icon-lg"></i>
                        <div class="ms-3 mt-3">
                            <h5 id="count-users" class="text-white font-weight-bold">Count user...</h5>
                            <p class="mt-2 text-white card-text">Jumlah Users</p>
                        </div>
                    </div>
                </div>
            </div>
        
            <!-- Card 2: Pengajuan-->
            <div class="col-md-4 col-sm-6 grid-margin stretch-card">
                <div class="card" style="background-color: #db4437; color: white;">
                    <div class="card-body py-5 text-center">
                        <i class="mdi mdi-note-plus text-white icon-lg"></i>
                        <div class="ms-3 mt-3">
                            <h5 id="count-pengajuan" class="text-white font-weight-bold">Count Pengajuan...</h5>
                            <p class="mt-2 text-white card-text">Jumlah Pengajuan</p>
                        </div>
                    </div>
                </div>
            </div>
        
            <!-- Card 3: Pengembangan -->
            <div class="col-md-4 col-sm-6 grid-margin stretch-card">
                <div class="card bg-twitter d-flex align-items-center">
                    <div class="card-body py-5 text-center">
                        <i class="mdi mdi-settings text-white icon-lg"></i>
                        <div class="ms-3 mt-3">
                            <h5 id="count-pengembangan" class="text-white font-weight-bold">Count Pengembangan...</h5>
                            <p class="mt-2 text-white card-text">Jumlah Pengembangan</p>
                        </div>
                    </div>
                </div>
            </div>
        
            <!-- Card 4: Pengujian -->
            <div class="col-md-4 col-sm-6 grid-margin stretch-card">
                <div class="card bg-youtube d-flex align-items-center">
                    <div class="card-body py-5 text-center">
                        <i class="mdi mdi-check-decagram text-white icon-lg"></i>
                        <div class="ms-3 mt-3">
                            <h5 id="count-pengujian" class="text-white font-weight-bold">Count Pengujian...</h5>
                            <p class="mt-2 text-white card-text">Jumlah Pengujian</p>
                        </div>
                    </div>
                </div>
            </div>
        
            <!-- Card 5: Approval -->
            <div class="col-md-4 col-sm-6 grid-margin stretch-card">
                <div class="card bg-linkedin d-flex align-items-center">
                    <div class="card-body py-5 text-center">
                        <i class="mdi mdi-checkbox-multiple-marked-circle-outline text-white icon-lg"></i>
                        <div class="ms-3 mt-3">
                            <h5 id="count-approval" class="text-white font-weight-bold">Count Approval...</h5>
                            <p class="mt-2 text-white card-text">Jumlah Approval</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="col-lg-12 grid-margin stretch-card">
            <div class="card">
                <div class="card-body">
                    <h4 class="card-title">SOP</h4>
                    <div class="table-responsive">
                        <table class="table table-striped" id="dashboard">
                            <thead>
                                <tr>
                                    <th>
                                        Nama Sistem
                                    </th>
                                    <th>
                                        Versi
                                    </th>
                                    <th>
                                        Jenis
                                    </th>
                                    <th>
                                        Status
                                    </th>
                                    <th>
                                        Tanggal Pengajuan
                                    </th>
                                </tr>
                            </thead>
                            <tbody>

                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
@endsection


<script>
    let users = [];
    let pengajuan = [];
    let pengujian = [];
    let pengembangan = [];
    let approval = [];
    let dashboard = [];

    function fetchUsers() {
        const token = localStorage.getItem('token');
        console.log(token);
        fetch('/api/users') // Adjust the API endpoint accordingly
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    users = data.payload; // Assuming the response has 'payload' containing user data

                    // Update the count for connected suppliers
                    document.getElementById('count-users').textContent = users.length;

                } else {
                    alert('Failed to load users.');
                }
            })
            .catch(error => {
                console.error('Error fetching users:', error);
                alert('An error occurred while fetching users.');
            });
    }

    function fetchPengujian() {
        const token = localStorage.getItem('token');
        fetch('/api/pengujian') // Adjust the API endpoint accordingly
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    pengujian = data.payload; // Assuming the response has 'payload' containing user data

                    // Update the count for connected suppliers
                    document.getElementById('count-pengujian').textContent = pengujian.length;

                    // You can add similar logic here for other metrics if required
                    // Example:
                    // document.getElementById('pending-requests').textContent = data.pendingRequests;
                    // document.getElementById('unreceived-orders').textContent = data.unreceivedOrders;
                    // document.getElementById('outstanding-invoices').textContent = data.outstandingInvoices;

                } else {
                    alert('Failed to load users.');
                }
            })
            .catch(error => {
                console.error('Error fetching users:', error);
                alert('An error occurred while fetching users.');
            });
    }

    function fetchPengembangan() {
        const token = localStorage.getItem('token');
        fetch('/api/pengembangan') // Adjust the API endpoint accordingly
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    pengembangan = data.payload; // Assuming the response has 'payload' containing user data

                    // Update the count for connected suppliers
                    document.getElementById('count-pengembangan').textContent = pengembangan.length;

                    // You can add similar logic here for other metrics if required
                    // Example:
                    // document.getElementById('pending-requests').textContent = data.pendingRequests;
                    // document.getElementById('unreceived-orders').textContent = data.unreceivedOrders;
                    // document.getElementById('outstanding-invoices').textContent = data.outstandingInvoices;

                } else {
                    alert('Failed to load pengembangan.');
                }
            })
            .catch(error => {
                console.error('Error fetching pengembangan:', error);
                alert('An error occurred while fetching pengembangan.');
            });
    }

    function fetchPengajuan() {
        const token = localStorage.getItem('token');
        fetch('/api/pengajuan') // Adjust the API endpoint accordingly
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    pengajuan = data.payload; // Assuming the response has 'payload' containing user data

                    // Update the count for connected suppliers
                    document.getElementById('count-pengajuan').textContent = pengajuan.length;

                    // You can add similar logic here for other metrics if required
                    // Example:
                    // document.getElementById('pending-requests').textContent = data.pendingRequests;
                    // document.getElementById('unreceived-orders').textContent = data.unreceivedOrders;
                    // document.getElementById('outstanding-invoices').textContent = data.outstandingInvoices;

                } else {
                    alert('Failed to load pengembangan.');
                }
            })
            .catch(error => {
                console.error('Error fetching pengembangan:', error);
                alert('An error occurred while fetching pengembangan.');
            });
    }

    function fetchApproval() {
        const token = localStorage.getItem('token');
        fetch('/api/approval') // Adjust the API endpoint accordingly
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    approval = data.payload; // Assuming the response has 'payload' containing user data
                    // Update the count for connected suppliers
                    document.getElementById('count-approval').textContent = approval.length;

                    // You can add similar logic here for other metrics if required
                    // Example:
                    // document.getElementById('pending-requests').textContent = data.pendingRequests;
                    // document.getElementById('unreceived-orders').textContent = data.unreceivedOrders;
                    // document.getElementById('outstanding-invoices').textContent = data.outstandingInvoices;

                } else {
                    alert('Failed to load approval.');
                }
            })
            .catch(error => {
                console.error('Error fetching approval:', error);
                alert('An error occurred while fetching approval.');
            });
    }

    function fetchDashboard(query = '') {
        const token = localStorage.getItem('token');
        fetch('/api/pengujian')
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    dashboard = data.payload;

                    // If query filtering is needed, you can add filtering logic here
                    // Example:
                    // if (query) {
                    //     dashboard = dashboard.filter(p => p.persetujuan_pengujian.pengujian.pengajuan.nama_sistem.toLowerCase().includes(query.toLowerCase()));
                    // }

                    renderTable(dashboard); // Render the table with the fetched data
                } else {
                    alert('Failed to load the approval data');
                }
            })
            .catch(error => console.error('Error:', error));
    }

    function renderTable(pengajuan) {
        const token = localStorage.getItem('token');
        const tableBody = document.querySelector('#dashboard tbody');
        tableBody.innerHTML = ''; // Clear existing table body

        pengajuan.forEach(p => {
            const row = document.createElement('tr');
            const createdAt = new Date(p.pengembangan.pengajuan.created_at);
            const formattedDate = createdAt.toISOString().split('T')[0]; 
            row.innerHTML = `
            <td>${p.pengembangan.pengajuan.nama_sistem}</td>
            <td>${p.versi}</td>
            <td>${p.pengembangan.pengajuan.jenis}</td>
            <td>${p.pengembangan.pengajuan.status}</td> <!-- Assuming 'status' refers to pengajuan's status -->
            <td>${formattedDate}</td><!-- Assuming created_at is the deadline -->
        `;
            tableBody.appendChild(row);
        });
    }

    fetchDashboard();

    fetchApproval();
    fetchPengajuan();
    fetchPengujian();
    fetchPengembangan();
    fetchUsers();
</script>
