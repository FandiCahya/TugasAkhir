@extends('layouts.app')
@section('content')
    <div class="col-lg-12 grid-margin stretch-card">
        <div class="card">
            <div class="card-body">
                <h4 class="card-title">Laporan</h4>
                <!-- Search Input -->
                <input type="text" id="search" class="form-control form-control-sm mb-3 me-2" placeholder="Search..."
                    onkeyup="searchLaporan()" />

                <div class="table-responsive pt-3">
                    <table class="table table-bordered" id="Laporan-table">
                        <thead>
                            <tr>
                                <th style="width: 25%;">Nama Sistem</span>
                                </th>
                                <th style="width: 10%;">Status</th>
                                <th style="width: 20%">Tanggal Pengajuan</th>
                                <th style="width: 15%;">Actions</th>
                            </tr>
                        </thead>
                        <tbody id="laporan-list">
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

@endsection
@push('scripts')
    <script>
        const csrfToken = "{{ csrf_token() }}";
    </script>
    <!-- Custom Script -->
    <script src="{{ asset('assets/js/laporan.js') }}"></script>
@endpush

