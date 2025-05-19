@extends('layouts.app')
@section('content')
    <div class="col-lg-12 grid-margin stretch-card">
        <div class="card">
            <div class="card-body">
                <h4 class="card-title">Pengujian</h4>
                
                <!-- Button to Open Add Pengembangan Modal -->
                <button type="button" class="btn btn-success btn-sm mb-3" onclick="openAddModal()">Tambah Pengujian</button>

                <!-- Search Input -->
                <input type="text" id="search" class="form-control form-control-sm mb-3 me-2" placeholder="Search..."
                    onkeyup="searchPengujian()" />

                <div class="table-responsive pt-3">
                    <table class="table table-bordered" id="pengujian-table">
                        <thead>
                            <tr>
                                <th>Tanggal</th>
                                <th>Name Sistem</th>
                                <th>Perangkat</th>
                                <th>Versi</th>
                                <th>Tujuan</th>
                                <th>Metode</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody id="pengujian-list">

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
        @include('modals.modal-pengujian')
@endsection
@push('scripts')
    <script>
        const csrfToken = "{{ csrf_token() }}";
    </script>
    <!-- Custom Script -->
    <script src="{{ asset('assets/js/pengujian.js') }}"></script>
@endpush