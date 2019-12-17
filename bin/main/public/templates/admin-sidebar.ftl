<!-- Sidebar -->
<ul class="sidebar navbar-nav">
    <li class="nav-item">
        <a class="nav-link" href="/admin">
            <i class="fas fa-fw fa-tachometer-alt"></i>
            <span>Dashboard</span>
        </a>
    </li>
    <#if usuario.administrator>
        <li class="nav-item">
            <a class="nav-link" href="/adminStatistics">
                <i class="fas fa-fw fa-chart-area"></i>
                <span>Charts</span></a>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="/usuarios">
                <i class="fas fa-fw fa-table"></i>
                <span>Usuarios</span></a>
        </li>
    </#if>
</ul>