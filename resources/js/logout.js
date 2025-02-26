document.getElementById('logout-btn').addEventListener('click', function(e) {
    e.preventDefault();

    // Konfirmasi logout
    const confirmLogout = window.confirm('Are you sure you want to logout?');

    if (confirmLogout) {
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
                    alert('Logout failed. Please try again.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('An error occurred during logout.');
            });
    }
});
