@extends('layouts.app')
@section('content')
    <div class="col-lg-12 grid-margin stretch-card">
        <div class="card">
            <div class="card-body">
                <h4 class="card-title">Detail Pengujian</h4>

                <!-- Search Input -->
                <input type="text" id="search" class="form-control form-control-sm mb-3 me-2" placeholder="Search..."
                    onkeyup="searchDetailPengujian()" />

                <div class="table-responsive pt-3">
                    <table class="table table-bordered" id="pengujian-table">
                        <thead>
                            
                        </thead>
                        <tbody id="pengujian-detail-body">

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
        @include('modals.modal-pengujian_detail')
@endsection
@push('scripts')
    <script>
        const csrfToken = "{{ csrf_token() }}";
    </script>
    <!-- Custom Script -->
    <script src="{{ asset('assets/js/detail_pengujian.js') }}"></script>
@endpush
