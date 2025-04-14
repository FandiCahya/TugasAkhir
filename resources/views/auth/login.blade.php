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
    <script>
        document.getElementById('loginForm').addEventListener('submit', async function(e) {
            e.preventDefault();

            // Clear previous errors
            document.getElementById('emailError').textContent = '';
            document.getElementById('passwordError').textContent = '';
            document.getElementById('error-message').classList.add('d-none');

            const formData = new FormData(this);

            try {
                const response = await fetch('/api/login', {
                    method: 'POST',
                    body: formData,
                    headers: {
                        'Accept': 'application/json',
                        'X-XSRF-TOKEN': getCookie('XSRF-TOKEN') // Include CSRF token
                    },
                    credentials: 'include' // Ensure cookies are sent
                });

                const data = await response.json();

                if (response.ok) {
                    // Jika login berhasil, simpan token ke localStorage
                    localStorage.setItem('token', data.token);
                    console.log('Token saved:', data.token);

                    // Redirect ke dashboard setelah login berhasil
                    console.log("Redirecting to dashboard...");
                    window.location.href = '/dashboard';
                } else {
                    // Menampilkan pesan error jika login gagal
                    console.log("Login failed:", data);
                    document.getElementById('error-message').textContent = data.message || 'Login failed.';
                    document.getElementById('error-message').classList.remove('d-none');
                }
            } catch (error) {
                console.error(error);
                console.error("Login error:", error);
                document.getElementById('error-message').textContent = 'An error occurred during login.';
                document.getElementById('error-message').classList.remove('d-none');
            }
        });

        // Fungsi untuk mengambil CSRF token dari cookie
        function getCookie(name) {
            let value = "; " + document.cookie;
            let parts = value.split("; " + name + "=");
            if (parts.length === 2) return parts.pop().split(";").shift();
        }
    </script>
</body>

</html>
