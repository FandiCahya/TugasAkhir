document.getElementById('logout-btn').addEventListener('click', function(e) {
    e.preventDefault();

    // Konfirmasi logout dengan SweetAlert2
    Swal.fire({
        title: 'Are you sure?',
        text: 'Do you really want to logout?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, logout!',
        cancelButtonText: 'Cancel'
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
                        window.location.href = '/login'; // Arahkan ke halaman login setelah logout berhasil
                    } else {
                        Swal.fire('Logout Failed', 'Please try again.', 'error');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    Swal.fire('Error', 'An error occurred during logout.', 'error');
                });
        }
    });
});
