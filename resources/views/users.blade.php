@extends('layouts.app')

@section('content')
    <div class="col-lg-12 grid-margin stretch-card">
        <div class="card">
            <div class="card-body">
                <h4 class="card-title">Data Users</h4>

                <!-- Button to Open Add User Modal -->
                <button type="button" class="btn btn-success btn-sm mb-3" onclick="openAddModal()">Tambah User</button>

                <!-- Search Input -->
                <input type="text" id="search" class="form-control form-control-sm mb-3 me-2" placeholder="Search..."
                    onkeyup="searchUsers()" />
                
                <!-- Desktop View (Table) -->
                <div class="table-responsive">
                    <table class="table table-striped" id="users-table">
                        <thead>
                            <tr>
                                <th>Name <span id="sort-name" class="cursor-pointer">🔽</span></th>
                                <th>Email</th>
                                <th>Role</th>
                                <th>Division</th>
                                <th>Tanggal Pembuatan</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody id="user-list">
                            <!-- Data will be dynamically filled here using JavaScript -->
                        </tbody>
                    </table>
                    <div class="d-flex justify-content-between mt-3">
                        <button class="btn btn-primary btn-sm ms-3 py-2 px-3" onclick="prevPage()">Prev</button>
                        <button class="btn btn-primary btn-sm me-3 py-2 px-3" onclick="nextPage()">Next</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- Memanggil modal Add dan Edit dari partials -->
    @include('modals.modal-users')
@endsection

@push('scripts')
    <script>
        const csrfToken = "{{ csrf_token() }}";
    </script>
    <!-- Custom Script -->
    <script src="{{ asset('assets/js/users.js') }}"></script>
@endpush
