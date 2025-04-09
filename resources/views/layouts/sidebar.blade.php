<nav class="sidebar sidebar-offcanvas" id="sidebar">
    <ul class="nav">
        <li class="nav-item sidebar-category">
            <p>Home</p>
            <span></span>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="{{ route('dashboard') }}">
                <i class="mdi mdi-view-quilt menu-icon"></i>
                <span class="menu-title">Dashboard</span>
                <div class="badge badge-info badge-pill"></div>
            </a>
        </li>
        <li class="nav-item sidebar-category">
            <p>Users</p>
            <span></span>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="{{ route('users') }}">
                <i class="mdi mdi-account menu-icon"></i>
                <span class="menu-title">Users</span>
            </a>
        </li>
        <li class="nav-item sidebar-category">
            <p>Data</p>
            <span></span>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="{{ route('pengajuan') }}">
                <i class="mdi mdi mdi-note-plus menu-icon"></i>
                <span class="menu-title">Pengajuan</span>
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="{{ route('pengembangan') }}">
                <i class="mdi mdi mdi-settings menu-icon"></i>
                <span class="menu-title">Pengembangan</span>
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" data-bs-toggle="collapse" href="#auth" aria-expanded="false" aria-controls="auth">
                <i class="mdi mdi-check-decagram menu-icon"></i>
                <span class="menu-title">Pengujian</span>
                <i class="menu-arrow"></i>
            </a>
            <div class="collapse" id="auth">
                <ul class="nav flex-column sub-menu">
                    <li class="nav-item"> <a class="nav-link" href="{{ route('pengujian') }}"> Pengujian </a></li>
                    <li class="nav-item"> <a class="nav-link" href="{{ route('pengujian-detail') }}"> Detail Pengujian </a></li>
                    <li class="nav-item"> <a class="nav-link" href="{{ route("pengujian-catatan") }}"> Catatan Pengujian </a></li>
                    </li>
                </ul>
            </div>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="{{ route('approval') }}">
                <i class="mdi mdi mdi mdi-checkbox-multiple-marked-circle-outline menu-icon"></i>
                <span class="menu-title">Approval</span>
            </a>
        </li>
        <li class="nav-item sidebar-category">
            <p>Hasil</p>
            <span></span>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="{{ route('laporan') }}" id="laporan-btn">
                <i class="mdi mdi-file-document menu-icon"></i>
                <span class="menu-title">Laporan</span>
            </a>
        </li>
        <li class="nav-item sidebar-category">
            <p>Logout</p>
            <span></span>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="#" id="logout-btn">
                <i class="mdi mdi-logout menu-icon"></i>
                <span class="menu-title">Logout</span>
            </a>
        </li>
        
    </ul>
</nav>
