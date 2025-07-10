@extends('layouts.app')
@section('content')
    <div class="col-lg-12 grid-margin stretch-card">
        <div class="card">
            <div class="card-body">
                <h4 class="card-title">Pengajuan</h4>

                {{-- <!-- Button to Open Add Pengajuan Modal -->
                <button type="button" class="btn btn-success btn-sm mb-3" onclick="openAddModal()">Tambah Pengajuan</button> --}}

                <!-- Search Input -->
                <input type="text" id="search" class="form-control form-control-sm mb-3 me-2" placeholder="Search..."
                    onkeyup="searchPengajuan()" />

                <div class="table-responsive pt-3">
                    <table class="table table-bordered" id="pengajuan-table">
                        <thead>
                            <tr>
                                <th style="width: 15%;">Nama Sistem <span id="sort-name" class="cursor-pointer">🔽</span>
                                </th>
                                <th style="width: 10%;">Yang Mengajukan</th>
                                <th style="width: 10%;">Jenis</th>
                                <th style="width: 10%;">Rencana Anggaran</th>
                                <th style="width: 15%;">Masalah</th>
                                <th style="width: 10%;">Output</th>
                                <th style="width: 10%;">Status</th>
                                <th style="width: 15%;">Tanggal Pengajuan</th>
                                <th style="width: 15%;">Actions</th>
                                
                            </tr>
                        </thead>
                        <tbody id="pengajuan-list">
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
    @include('modals.modal-pengajuan')
@endsection

@push('scripts')
    <script>
        const csrfToken = "{{ csrf_token() }}";
    </script>
    <!-- Custom Script -->
    <script src="{{ asset('assets/js/pengajuan.js') }}"></script>
@endpush
