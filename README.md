<p align="center">
  <a href="{{ asset('') }}" target="_blank">
    <img src="https://raw.githubusercontent.com/laravel/art/master/logo-lockup/5%20SVG/2%20CMYK/1%20Full%20Color/laravel-logolockup-cmyk-red.svg" width="400" alt="Laravel Logo">
  </a>
</p>

<p align="center">
  <a href="https://packagist.org/packages/laravel/framework"><img src="https://img.shields.io/packagist/dt/laravel/framework" alt="Total Downloads"></a>
  <a href="https://packagist.org/packages/laravel/framework"><img src="https://img.shields.io/packagist/v/laravel/framework" alt="Latest Stable Version"></a>
  <a href="https://packagist.org/packages/laravel/framework"><img src="https://img.shields.io/packagist/l/laravel/framework" alt="License"></a>
</p>

---

# SOPilot - Sistem Pengajuan Perangkat Lunak

**SOPilot** adalah aplikasi berbasis web yang digunakan untuk proses pengajuan, pengembangan, pengujian, dan persetujuan perangkat lunak dalam sebuah organisasi. Aplikasi ini dirancang dengan Laravel dan dilengkapi dengan fitur manajemen pengguna, approval berjenjang, dan laporan yang bisa diunduh.

## ✨ Fitur Utama

- ✅ CRUD User (Admin Panel)
- ✅ Pengajuan Perangkat Lunak
- ✅ Proses Pengembangan & Pengujian
- ✅ Approval 4 User
- ✅ Download Laporan PDF
- ✅ Role-based access control

## 🚀 Tech Stack

- Laravel 11.x
- MySQL
- Bootstrap / Blade Templating
- Laravel Sanctum (untuk API token)
- DOMPDF / Snappy untuk laporan PDF

## 🛠️ Instalasi Lokal

```bash
git clone https://github.com/FandiCahya/magangMBKM.git
cd magangMBKM

composer install
npm install && npm run dev

cp .env.example .env
php artisan key:generate

php artisan migrate --seed

php artisan serve
