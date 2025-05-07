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
                            <h5 id="count-users" class="text-white font-weight-bold">{{ $counts['users'] }}</h5>
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
                            <h5 id="count-pengajuan" class="text-white font-weight-bold">{{ $counts['pengajuan'] }}</h5>
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
                            <h5 id="count-pengembangan" class="text-white font-weight-bold">{{ $counts['pengembangan'] }}</h5>
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
                            <h5 id="count-pengujian" class="text-white font-weight-bold">{{ $counts['pengujian'] }}</h5>
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
                            <h5 id="count-approval" class="text-white font-weight-bold">{{ $counts['persetujuan_pengujian'] }}</h5>
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

