// Fitur hide/show password
// Buat tombol toggle (pakai ikon)
const togglePassword = document.createElement('button');
togglePassword.type = 'button';
togglePassword.classList.add('btn', 'btn-sm', 'btn-icon');
togglePassword.style.position = 'absolute';
togglePassword.style.top = '50%';
togglePassword.style.right = '10px';
togglePassword.style.transform = 'translateY(-50%)';
togglePassword.style.zIndex = '2';
togglePassword.style.background = 'transparent';
togglePassword.style.border = 'none';

// Tambahkan ikon mata
const eyeIcon = document.createElement('i');
eyeIcon.classList.add('mdi', 'mdi-eye'); // awalnya mata terbuka
togglePassword.appendChild(eyeIcon);

// Sisipkan ke dalam form
const passwordInput = document.getElementById('password');
const passwordGroup = passwordInput.parentElement;
passwordGroup.classList.add('position-relative');
passwordGroup.appendChild(togglePassword);

// Event untuk toggle
togglePassword.addEventListener('click', function () {
    const type = passwordInput.type === 'password' ? 'text' : 'password';
    passwordInput.type = type;

    // Ganti ikon
    if (type === 'text') {
        eyeIcon.classList.remove('mdi-eye');
        eyeIcon.classList.add('mdi-eye-off');
    } else {
        eyeIcon.classList.remove('mdi-eye-off');
        eyeIcon.classList.add('mdi-eye');
    }
});

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
        
        // Cek token dan role
        if (response.ok && data.token) {
            console.log('Data Login:', data);
            if (data.user && data.user.role === 'admin') {
                localStorage.setItem('token', data.token);
                Swal.fire({
                    icon: 'success',
                    title: 'Login Berhasil!',
                    text: 'Selamat datang, Admin!',
                    timer: 2000,
                    showConfirmButton: false
                }).then(() => {
                    window.location.href = '/dashboard';
                });
            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'Gagal Login',
                    text: 'Hanya admin yang dapat login ke sistem ini.'
                });
                document.getElementById('error-message').textContent = 'Role tidak diizinkan.';
                document.getElementById('error-message').classList.remove('d-none');
            }
        } else {
            const errorMsg = data.message || 'Login gagal. Silakan periksa email dan password.';
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
