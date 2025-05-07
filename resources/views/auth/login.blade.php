<!DOCTYPE html>
<html lang="en">

<head>

    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>SOPilot</title>
    <!-- base:css -->
    <link rel="stylesheet" href="{{ asset('assets/vendors/mdi/css/materialdesignicons.min.css') }}">
    <link rel="stylesheet" href="{{ asset('assets/vendors/css/vendor.bundle.base.css') }}">
    <!-- endinject -->
    <!-- inject:css -->
    <link rel="stylesheet" href="{{ asset('assets/css/style.css') }}">
    <!-- endinject -->
    <link rel="shortcut icon" href="{{ asset('assets/images/favicon.png') }}" />
    <style>
        .content-wrapper {
            background-image: url('{{ asset('assets/images/background.jpg') }}');
            background-size: cover;
            background-position: center;
            background-repeat: no-repeat;
            min-height: 100vh;
        }

        .auth-form-light {
            background-color: rgba(255, 255, 255, 0.9);
            /* agar form tetap readable */
            border-radius: 10px;
        }
    </style>
    {{-- Js Cookies --}}
    <script src="https://cdn.jsdelivr.net/npm/js-cookie@3.0.1/dist/js.cookie.min.js"></script>
    {{-- Swwetalert --}}
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>

<body>

    <div class="container-scroller d-flex">
        <div class="container-fluid page-body-wrapper full-page-wrapper d-flex">
            <div class="content-wrapper d-flex align-items-center auth px-0">
                <div class="row w-100 mx-0">
                    <div class="col-lg-4 mx-auto">
                        <div class="auth-form-light text-left py-5 px-4 px-sm-5">
                            <div class="brand-logo">
                                <img src="{{ asset('assets/images/lifemedia_logo.png') }}" alt="logo">
                            </div>
                            <h4>Hello! </h4>
                            <h6 class="font-weight-light">Login untuk melanjutkan.</h6>
                            <form class="pt-3" id="loginForm" method="POST">
                                @csrf
                                <div class="form-group">
                                    <input type="email" class="form-control form-control-lg" id="email"
                                        name="email" placeholder="Email" required>
                                    <small id="emailError" class="text-danger"></small>
                                </div>
                                <div class="form-group">
                                    <input type="password" class="form-control form-control-lg" id="password"
                                        name="password" autocomplete="current-password" required placeholder="Password">
                                    <small id="passwordError" class="text-danger"></small>
                                </div>
                                <div class="mt-3">
                                    <!-- Login Button -->
                                    <button type="submit" class="btn btn-primary w-100">Login</button>
                                </div>
                            </form>
                            <div id="error-message" class="alert alert-danger d-none"></div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- content-wrapper ends -->
        </div>
        <!-- page-body-wrapper ends -->
    </div>


    <script src="{{ asset('assets/vendors/js/vendor.bundle.base.js') }}"></script>
    <!-- endinject -->
    <script src="{{ asset('assets/js/jquery.cookie.js') }}" type="text/javascript"></script>
    <!-- inject:js -->
    <script src="{{ asset('assets/js/off-canvas.js') }}"></script>
    <script src="{{ asset('assets/js/hoverable-collapse.js') }}"></script>
    <script src="{{ asset('assets/js/template.js') }}"></script>

    <!-- JS and Fetch API for login -->
    <script src="{{ asset('assets/js/login.js') }}"></script>
</body>

</html>
