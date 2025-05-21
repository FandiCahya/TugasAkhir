@extends('layouts.app')
@section('content')
    <div class="col-lg-12 grid-margin stretch-card">
        <div class="card">
            <div class="card-body">
                <h4 class="card-title">Pengembangan</h4>

                <!-- Button to Open Add Pengembangan Modal -->
                <button type="button" class="btn btn-primary btn-sm mb-3" onclick="openAddModal()">Tambah Pengembangan</button>

                <!-- Search Input -->
                <input type="text" id="search" class="form-control form-control-sm mb-3 me-2" placeholder="Search..."
                    onkeyup="searchPengembangan()" />

                <div class="table-responsive pt-3">
                    <table class="table table-bordered" id="pengembangan-table">
                        <thead>
                            <tr>
                                <th>Nama Sistem</th>
                                <th>Nama User</th>
                                <th>Tgl Mulai</th>
                                <th>Tgl Selesai</th>
                                <th>Tahap</th>
                                <th>Persentase</th>
                                <th>Keterangan</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody id="pengembangan-list">

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
        @include('modals.modal-pengembangan')
@endsection
@push('scripts')
    <script>
        const csrfToken = "{{ csrf_token() }}";
    </script>
    <!-- Custom Script -->
    <script src="{{ asset('assets/js/pengembangan.js') }}"></script>
@endpush