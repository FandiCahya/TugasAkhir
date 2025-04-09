@extends('layouts.app')

@section('content')
    <div class="col-lg-12 grid-margin stretch-card">
        <div class="card">
            <div class="card-body">
                <h4 class="card-title">Users</h4>

                <!-- Button to Open Add User Modal -->
                <button class="btn btn-success btn-sm mb-3" data-bs-toggle="modal" data-bs-target="#addUserModal">Tambah
                    User</button>

                <!-- Search Input -->
                <input type="text" id="search" class="form-control mb-3" placeholder="Search by name..." />

                <div class="table-responsive">
                    <table class="table table-striped" id="users-table">
                        <thead>
                            <tr>
                                <th>Name <span id="sort-name" class="cursor-pointer">🔽</span></th>
                                <th>Email</th>
                                <th>Role</th>
                                <th>Division</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <!-- Data will be dynamically filled here using JavaScript -->
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal for Adding User -->
    <div class="modal fade" id="addUserModal" tabindex="-1" aria-labelledby="addUserModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="addUserModalLabel">Add New User</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form id="addUserForm">
                        @csrf
                        <div class="mb-3">
                            <label for="name" class="form-label">Name</label>
                            <input type="text" class="form-control" id="name" required>
                        </div>
                        <div class="mb-3">
                            <label for="email" class="form-label">Email</label>
                            <input type="email" class="form-control" id="email" required>
                        </div>
                        <div class="mb-3">
                            <label for="role" class="form-label">Role</label>
                            <select class="form-control" id="role" required>
                                <option value="user">User</option>
                                <option value="admin">Admin</option>
                                <option value="qmr">QMR</option>
                                <option value="kepalacabang">Kepalacabang</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="devisi" class="form-label">Division</label>
                            <select class="form-control" id="devisi" name="devisi" required>
                                <option value="egov">E-Gov</option>
                                <option value="opj">OPJ</option>
                                <option value="legal">Legal</option>
                                <option value="c-care">C-Care</option>
                                <option value="nro">NRO</option>
                                <option value="noc">NOC</option>
                                <option value="rumah tangga">Rumah Tangga</option>
                                <option value="helpdesk">Helpdesk</option>
                                <option value="hrd">HRD</option>
                                <option value="retail">Retail</option>
                                <option value="vas">VAS</option>
                                <option value="corp">Corporate</option>
                                <option value="finance">Finance</option>
                            </select>
                        </div>                        
                        <!-- Changed foto_profile to password -->
                        <div class="mb-3">
                            <label for="password" class="form-label">Password</label>
                            <input type="password" class="form-control" id="password" required>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-primary" id="saveUserBtn">Save User</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal for Editing User -->
    <div class="modal fade" id="editUserModal" tabindex="-1" aria-labelledby="editUserModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="editUserModalLabel">Edit User</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form id="editUserForm">
                        @csrf
                        <div class="mb-3">
                            <label for="edit-name" class="form-label">Name</label>
                            <input type="text" class="form-control" id="edit-name" required>
                        </div>
                        <div class="mb-3">
                            <label for="edit-email" class="form-label">Email</label>
                            <input type="email" class="form-control" id="edit-email" required>
                        </div>
                        <div class="mb-3">
                            <label for="edit-role" class="form-label">Role</label>
                            <select class="form-control" id="edit-role" required>
                                <option value="user">User</option>
                                <option value="admin">Admin</option>
                                <option value="qmr">QMR</option>
                                <option value="kepalacabang">Kepalacabang</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="devisi" class="form-label">Division</label>
                            <select class="form-control" id="edit-devisi" name="devisi" required>
                                <option value="egov">E-Gov</option>
                                <option value="opj">OPJ</option>
                                <option value="legal">Legal</option>
                                <option value="c-care">C-Care</option>
                                <option value="nro">NRO</option>
                                <option value="noc">NOC</option>
                                <option value="rumah tangga">Rumah Tangga</option>
                                <option value="helpdesk">Helpdesk</option>
                                <option value="hrd">HRD</option>
                                <option value="retail">Retail</option>
                                <option value="vas">VAS</option>
                                <option value="corp">Corporate</option>
                                <option value="finance">Finance</option>
                            </select>
                        </div>                        
                        <div class="mb-3">
                            <label for="edit-password" class="form-label">Password</label>
                            <input type="password" class="form-control" id="edit-password" required>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-primary" id="updateUserBtn">Update User</button>
                </div>
            </div>
        </div>
    </div>

    <script>
        let users = []; // Array to hold fetched users
        let editUserId = null;

        // Fetch users from the API
        function fetchUsers(query = '') {
            fetch('/api/users') // Assuming you have this API endpoint to get all users
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        users = data.payload;

                        // Filter users based on the search query
                        if (query) {
                            users = users.filter(user => user.name.toLowerCase().includes(query.toLowerCase()));
                        }

                        renderTable(users); // Render the filtered or all users
                    } else {
                        alert('Failed to load users');
                    }
                })
                .catch(error => console.error('Error:', error));
        }

        // Render data in the table
        function renderTable(users) {
            const tableBody = document.querySelector('#users-table tbody');
            tableBody.innerHTML = ''; // Clear the existing table body
            users.forEach(user => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${user.name}</td>
                    <td>${user.email}</td>
                    <td>${user.role}</td>
                    <td>${user.devisi ? user.devisi : 'N/A'}</td>
                    <td>
                        <button class="btn btn-warning btn-sm" onclick="editUser('${user.id}')">Edit</button>
                        <button class="btn btn-danger btn-sm" onclick="deleteUser('${user.id}')">Delete</button>
                    </td>
                `;
                tableBody.appendChild(row);
            });
        }

        // Sorting functionality by division
        function sortByDevisi() {
            const selectedDevisi = document.getElementById('sort-devisi').value;

            if (selectedDevisi) {
                // If a division is selected, sort users by that division
                users.sort((a, b) => {
                    if (a.devisi < b.devisi) return -1;
                    if (a.devisi > b.devisi) return 1;
                    return 0;
                });
            } else {
                // If no division is selected, show all users in the original order
                fetchUsers(); // Re-fetch users without filtering or sorting
            }

            renderTable(users); // Render the sorted users
        }


        // Edit user
        function editUser(id) {
            const user = users.find(u => u.id === id);
            document.getElementById('edit-name').value = user.name;
            document.getElementById('edit-email').value = user.email;
            document.getElementById('edit-role').value = user.role;
            document.getElementById('edit-devisi').value = user.devisi;
            document.getElementById('edit-password').value = '';

            editUserId = id;
            const modal = new bootstrap.Modal(document.getElementById('editUserModal'));
            modal.show();
        }

        // Update user
        document.getElementById('updateUserBtn').addEventListener('click', () => {
            const name = document.getElementById('edit-name').value;
            const email = document.getElementById('edit-email').value;
            const role = document.getElementById('edit-role').value;
            const devisi = document.getElementById('edit-devisi').value;
            const password = document.getElementById('edit-password').value;

            fetch(`/api/users/${editUserId}`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                        'X-CSRF-TOKEN': document.querySelector('meta[name="csrf-token"]').getAttribute(
                            'content')
                    },
                    body: JSON.stringify({
                        name,
                        email,
                        role,
                        devisi,
                        password
                    })
                })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        alert('User updated successfully');
                        fetchUsers();
                        const modal = document.querySelector('#editUserModal');
                        const modalInstance = bootstrap.Modal.getInstance(modal);
                        modalInstance.hide();
                    } else {
                        alert('Failed to update user');
                    }
                })
                .catch(error => console.error('Error:', error));
        });

        // Delete user
        function deleteUser(id) {
            if (confirm('Are you sure you want to delete this user?')) {
                fetch(`/api/users/${id}`, {
                        method: 'DELETE',
                    })
                    .then(response => response.json())
                    .then(data => {
                        if (data.success) {
                            alert('User deleted successfully');
                            fetchUsers(); // Refresh the table after deletion
                        } else {
                            alert('Failed to delete user');
                        }
                    })
                    .catch(error => console.error('Error:', error));
            }
        }

        // Handle search input
        document.getElementById('search').addEventListener('input', (event) => {
            const query = event.target.value;
            fetchUsers(query); // Call fetchUsers with search query
        });

        // Sorting functionality (Sort by name, devisi, or role)
        function sortBy(column) {
            users.sort((a, b) => {
                if (a[column] < b[column]) return -1;
                if (a[column] > b[column]) return 1;
                return 0;
            });
            renderTable(users);
        }

        // Handle the save user button click
        document.getElementById('saveUserBtn').addEventListener('click', () => {
            const name = document.getElementById('name').value;
            const email = document.getElementById('email').value;
            const role = document.getElementById('role').value;
            const devisi = document.getElementById('devisi').value;
            const password = document.getElementById('password').value;

            // Add user via API
            fetch('/api/users', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'X-CSRF-TOKEN': document.querySelector('meta[name="csrf-token"]').getAttribute(
                            'content')
                    },
                    body: JSON.stringify({
                        name,
                        email,
                        role,
                        devisi,
                        password
                    })
                })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        alert('User added successfully');
                        fetchUsers(); // Refresh the user list after adding
                        // Close the modal
                        const modal = document.querySelector('#addUserModal');
                        const modalInstance = bootstrap.Modal.getInstance(modal);
                        modalInstance.hide();
                    } else {
                        alert('Failed to add user');
                    }
                })
                .catch(error => console.error('Error:', error));
        });

        // Initial fetch of users
        fetchUsers(); // Initially fetch users when the page loads
    </script>
@endsection
