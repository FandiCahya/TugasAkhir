<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
    <!-- Tambahkan Bootstrap atau framework CSS jika diperlukan -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5">
        {{-- <!-- Logo -->
        <div class="text-center mb-4">
            <img src="{{ asset('assets/images/lifemedia_logo.png') }}" alt="LifeMedia Logo" style="max-width: 200px;">
        </div> --}}

        <h2 class="text-center">Login</h2>

        <!-- Error Message -->
        @if(session('error'))
            <div class="alert alert-danger">
                {{ session('error') }}
            </div>
        @endif

        <form id="loginForm">
            <!-- Email -->
            <div class="mb-3">
                <label for="email" class="form-label">Email</label>
                <input type="email" class="form-control" id="email" name="email" required>
                <div class="invalid-feedback" id="emailError"></div>
            </div>

            <!-- Password -->
            <div class="mb-3">
                <label for="password" class="form-label">Password</label>
                <input type="password" class="form-control" id="password" name="password" required>
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

            const formData = new FormData(this);

            try {
                const response = await fetch('/api/login', {
                    method: 'POST',
                    body: formData,
                    headers: {
                        'Accept': 'application/json'
                    }
                });

                const data = await response.json();

                if (response.ok) {
                    // If login is successful, save token to localStorage
                    localStorage.setItem('token', data.token);

                    // Redirect to dashboard after successful login
                    window.location.href = '/dashboard';  // Redirect to dashboard
                } else {
                    // Handle validation errors or invalid credentials
                    if (data.errors) {
                        if (data.errors.email) {
                            document.getElementById('emailError').textContent = data.errors.email[0];
                        }
                        if (data.errors.password) {
                            document.getElementById('passwordError').textContent = data.errors.password[0];
                        }
                    } else {
                        alert(data.message || 'Login failed. Please try again.');
                    }
                }
            } catch (error) {
                console.error(error);
                alert('An error occurred during login.');
            }
        });

    </script>
</body>
</html>
