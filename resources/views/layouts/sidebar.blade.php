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
            <a class="nav-link" href="{{ route('pengujian') }}">
                <i class="mdi mdi mdi-check-decagram menu-icon"></i>
                <span class="menu-title">Pengujian</span>
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="{{ route('history') }}">
                <i class="mdi mdi mdi-timer menu-icon"></i>
                <span class="menu-title">Riwayat Pengajuan</span>
            </a>
        </li>
        <li class="nav-item sidebar-category">
            <p>Logs</p>
            <span></span>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="{{ route('logs') }}">
                <i class="mdi mdi mdi-laptop menu-icon"></i>
                <span class="menu-title">Logs</span>
            </a>
        </li>
    </ul>
</nav>
