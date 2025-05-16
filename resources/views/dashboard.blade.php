@extends('layouts.app')

@section('content')
    <div class="content-wrapper">
        <div class="row">
            <!-- Card 1: Jumlah Users -->
            <div class="col-md-4 col-sm-6 grid-margin stretch-card">
                <div class="card bg-facebook shadow" style="border-radius: 1rem;">
                    <div class="card-body py-5 d-flex justify-content-between align-items-center">
                        <!-- Teks di kiri -->
                        <div class="ms-3">
                            <h3 id="count-users" class="text-white fw-bold mb-2">{{ $counts['users'] }}</h3>
                            <p class="text-white fs-5 mb-0">Jumlah Users</p>
                        </div>
                        <!-- Icon di kanan -->
                        <i class="mdi mdi-account-multiple text-white" style="font-size: 4rem;"></i>
                    </div>
                </div>
            </div>

            <!-- Card 2: Pengajuan-->
            <div class="col-md-4 col-sm-6 grid-margin stretch-card">
                <div class="card shadow" style="border-radius: 1rem; background-color: #7da0fa; color: white;">
                    <div class="card-body py-5 d-flex justify-content-between align-items-center">
                        <!-- Teks di kiri -->
                        <div class="ms-3">
                            <h3 id="count-pengajuan" class="text-white fw-bold mb-2">{{ $counts['pengajuan'] }}</h3>
                            <p class="text-white fs-5 mb-0">Jumlah Pengajuan</p>
                        </div>
                        <!-- Icon di kanan -->
                        <i class="mdi mdi-note-plus text-white" style="font-size: 4rem;"></i>
                    </div>
                </div>
            </div>

            <!-- Card 3: Pengembangan -->
            <div class="col-md-4 col-sm-6 grid-margin stretch-card">
                <div class="card shadow" style="border-radius: 1rem; background-color: #265deb; color: white;">
                    <div class="card-body py-5 d-flex justify-content-between align-items-center">
                        <!-- Teks di kiri -->
                        <div class="ms-3">
                            <h3 id="count-pengembangan" class="text-white fw-bold mb-2">{{ $counts['pengembangan'] }}</h3>
                            <p class="text-white fs-5 mb-0">Jumlah Pengembangan</p>
                        </div>
                        <!-- Icon di kanan -->
                        <i class="mdi mdi-code-tags text-white" style="font-size: 4rem;"></i>
                    </div>
                </div>
            </div>


            <!-- Card 4: Pengujian -->
            <div class="col-md-4 col-sm-6 grid-margin stretch-card">
                <div class="card shadow" style="border-radius: 1rem; background-color: #2b3b63; color: white;">
                    <div class="card-body py-5 d-flex justify-content-between align-items-center">
                        <!-- Teks di kiri -->
                        <div class="ms-3">
                            <h3 id="count-pengujian" class="text-white fw-bold mb-2">{{ $counts['pengujian'] }}</h3>
                            <p class="text-white fs-5 mb-0">Jumlah Pengujian</p>
                        </div>
                        <!-- Icon di kanan -->
                        <i class="mdi mdi-clipboard-text text-white" style="font-size: 4rem;"></i>
                    </div>
                </div>
            </div>

            <!-- Card 5: Approval -->
            <div class="col-md-4 col-sm-6 grid-margin stretch-card">
                <div class="card shadow" style="border-radius: 1rem; background-color: #687bad; color: white;">
                    <div class="card-body py-5 d-flex justify-content-between align-items-center">
                        <!-- Teks di kiri -->
                        <div class="ms-3">
                            <h3 id="count-approval" class="text-white fw-bold mb-2">{{ $counts['persetujuan_pengujian'] }}</h3>
                            <p class="text-white fs-5 mb-0">Jumlah Persetujuan</p>
                        </div>
                        <!-- Icon di kanan -->
                        <i class="mdi mdi-checkbox-multiple-marked-circle-outline text-white" style="font-size: 4rem;"></i>
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
                            <tbody id="dashboard-body">
                                <!-- Data will be dynamically filled here using JavaScript -->
                            </tbody>
                        </table>
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
    <script src="{{ asset('assets/js/home.js') }}"></script>
@endpush
