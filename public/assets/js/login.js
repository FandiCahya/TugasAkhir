document.getElementById('loginForm').addEventListener('submit', async function(e) {
    e.preventDefault();

    // Reset error message
    document.getElementById('emailError').textContent = '';
    document.getElementById('passwordError').textContent = '';
    document.getElementById('error-message').classList.add('d-none');
    document.getElementById('error-message').textContent = '';

    const formData = new FormData(this);

    try {
        const response = await fetch('/api/login', {
            method: 'POST',
            body: formData,
            headers: {
                'Accept': 'application/json',
                'X-XSRF-TOKEN': getCookie('XSRF-TOKEN') // Pastikan CSRF token tersedia
            },
            credentials: 'include'
        });

        const data = await response.json();

        if (response.ok && data.token) {
            // Simpan token ke localStorage
            localStorage.setItem('token', data.token);
            console.log('Token:', data.token);
            Swal.fire({
                icon: 'success',
                title: 'Login Berhasil!',
                text: 'Anda Berhasil Login',
                timer: 2000,
                showConfirmButton: false
            }).then(() => {
                window.location.href = '/dashboard';
            });
        } else {
            // Tampilkan error dari response (validasi atau lainnya)
            const errorMsg = data.message || 'Login gagal. Silakan periksa kembali email dan password Anda.';
            Swal.fire({
                icon: 'error',
                title: 'Gagal Login',
                text: errorMsg
            });

            document.getElementById('error-message').textContent = errorMsg;
            document.getElementById('error-message').classList.remove('d-none');
        }
    } catch (error) {
        console.error('Login error:', error);
        Swal.fire({
            icon: 'error',
            title: 'Terjadi Kesalahan',
            text: 'Tidak dapat terhubung ke server.'
        });

        document.getElementById('error-message').textContent = 'Kesalahan jaringan atau server.';
        document.getElementById('error-message').classList.remove('d-none');
    }
});

// Fungsi ambil CSRF token dari cookie
function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}
