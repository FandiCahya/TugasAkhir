<!DOCTYPE html>
<html lang="en">

<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Spica Admin</title>
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
    <div class="container mt-5">

        <h2 class="text-center">Login</h2>

        <!-- Error Message -->
        <div id="error-message" class="alert alert-danger d-none"></div>

        <form id="loginForm" >
            <!-- Email -->
            @csrf
            <div class="mb-3">
                <label for="email" class="form-label">Email</label>
                <input type="email" class="form-control" id="email" name="email" autocomplete="username"
                    required>
                <div class="invalid-feedback" id="emailError"></div>
            </div>

            <!-- Password -->
            <div class="mb-3">
                <label for="password" class="form-label">Password</label>
                <input type="password" class="form-control" id="password" name="password"
                    autocomplete="current-password" required>
                <div class="invalid-feedback" id="passwordError"></div>
            </div>

            <!-- Login Button -->
            <button type="submit" class="btn btn-primary w-100">Login</button>
        </form>

        {{-- <div class="text-center mt-3">
            <p>Don't have an account? <a href="{{ route('register') }}">Register here</a></p>
        </div> --}}
    </div>

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
                    window.location.href = '/dashboard';
                } else {
                    // Menampilkan pesan error jika login gagal
                    document.getElementById('error-message').textContent = data.message || 'Login failed.';
                    document.getElementById('error-message').classList.remove('d-none');
                }
            } catch (error) {
                console.error(error);
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
