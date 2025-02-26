<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login Page</title>
    <script>
        // Function to handle the login request
        async function login() {
            // Get the values from the email and password input fields
            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;

            // Prepare the data to send to the API
            const data = new FormData();
            data.append('email', email);
            data.append('password', password);

            try {
                // Send a POST request to the login API
                const response = await fetch('http://172.16.100.137:8000/api/login', {
                    method: 'POST',
                    body: data
                });

                // Parse the JSON response
                const result = await response.json();

                // Check if login was successful
                if (response.ok && result.message === 'Login successful') {
                    // Save the token in localStorage or sessionStorage
                    localStorage.setItem('token', result.token);

                    // Redirect to the dashboard page
                    window.location.href = '/dashboard';
                } else {
                    // Show error message if login failed
                    alert(result.message || 'Login failed. Please check your credentials.');
                }
            } catch (error) {
                console.error('Error during login:', error);
                alert('An error occurred during login. Please try again.');
            }
        }
    </script>
</head>
<body>
    <h2>Login</h2>
    
    <form onsubmit="event.preventDefault(); login();">
        <label for="email">Email:</label>
        <input type="email" id="email" name="email" required><br><br>

        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required><br><br>

        <button type="submit">Login</button>
    </form>
</body>
</html>
