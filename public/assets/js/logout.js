document.getElementById('logout-btn').addEventListener('click', function(e) {
    e.preventDefault();

    // Konfirmasi logout dengan SweetAlert2
    Swal.fire({
        title: 'Yakin Ingin Keluar?',
        text: 'Apakah Anda yakin ingin keluar?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Ya, Saya Yakin!',
        cancelButtonText: 'Batal'
    }).then((result) => {
        if (result.isConfirmed) {
            // Mengirim request logout ke API
            fetch('/api/logout', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': 'Bearer ' + localStorage.getItem('token'), // Token diambil dari localStorage
                    },
                })
                .then(response => {
                    if (response.ok) {
                        // Menghapus token dan mengarahkan pengguna ke halaman login
                        localStorage.removeItem('token');
                        Swal.fire(
                            'Berhasil!',
                            'Anda telah keluar.',
                            'success'
                        ).then(() => {
                            window.location.href = '/login'; // Arahkan ke halaman login setelah logout berhasil
                        });
                    } else {
                        Swal.fire('Logout Gagal', 'Silakan coba lagi.', 'error');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    Swal.fire('Error', 'Terjadi kesalahan saat logout.', 'error');
                });
        }
    });
});
