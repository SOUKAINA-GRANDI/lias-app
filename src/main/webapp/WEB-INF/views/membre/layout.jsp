<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${pageTitle} | LIAS</title>
    <meta name="csrf-token" content="${csrfToken}">
    <script src="${pageContext.request.contextPath}/assets/js/csrf.js"></script>

    <script>
        (function () {
            var saved = localStorage.getItem('lias-theme');
            var preferDark = window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches;
            document.documentElement.setAttribute('data-theme', saved || (preferDark ? 'dark' : 'light'));
        })();
    </script>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Space+Grotesk:wght@500;600;700&family=IBM+Plex+Sans:wght@400;500;600&family=IBM+Plex+Mono:wght@400;500&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/public-design-system.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/membre-app.css">
</head>
<body class="public-layout membre-app">

    <aside class="m-sidebar" id="mSidebar">
        <div class="m-brand">
            <img src="${pageContext.request.contextPath}/assets/images/logo-lias-crop.png" alt="LIAS">
            <div class="m-brand-text"><span>Espace membre</span></div>
        </div>

        <div class="m-section-title">Espace membre</div>
        <ul class="m-menu">
            <li class="${pageTitle == 'Tableau de bord' ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/membre/dashboard"><i class="fa-solid fa-chart-pie"></i> <span class="m-menu-label">Tableau de bord</span></a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/membre/profil"><i class="fa-solid fa-user"></i> <span class="m-menu-label">Mon profil</span></a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/membre/equipe"><i class="fa-solid fa-users"></i> <span class="m-menu-label">Membres du lab</span></a>
            </li>
        </ul>

        <div class="m-section-title">Administratif</div>
        <ul class="m-menu">
            <li><a href="${pageContext.request.contextPath}/membre/publications"><i class="fa-solid fa-book"></i> <span class="m-menu-label">Publications</span></a></li>
            <c:if test="${statutMembreConnecte != 'DOCTORANT'}">
            <li><a href="${pageContext.request.contextPath}/membre/documents"><i class="fa-solid fa-folder-open"></i> <span class="m-menu-label">Documents</span></a></li>
            <li><a href="${pageContext.request.contextPath}/membre/evenements"><i class="fa-solid fa-calendar"></i> <span class="m-menu-label">Événements</span></a></li>
            <c:if test="${statutMembreConnecte != 'ASSOCIE'}">
            <li><a href="${pageContext.request.contextPath}/membre/pv"><i class="fa-solid fa-file-lines"></i> <span class="m-menu-label">Réunions &amp; PV</span></a></li>
            <li><a href="${pageContext.request.contextPath}/membre/conventions"><i class="fa-solid fa-file-contract"></i> <span class="m-menu-label">Conventions</span></a></li>
            <li><a href="${pageContext.request.contextPath}/membre/materiel"><i class="fa-solid fa-microchip"></i> <span class="m-menu-label">Matériel</span></a></li>
            </c:if>
            </c:if>
        </ul>

        <div class="m-section-title">Communication</div>
        <ul class="m-menu">
            <c:if test="${statutMembreConnecte != 'DOCTORANT'}">
            <li><a href="${pageContext.request.contextPath}/messages"><i class="fa-solid fa-comments"></i> <span class="m-menu-label">Chat interne</span></a></li>
            </c:if>
            <li>
                <a href="${pageContext.request.contextPath}/membre/notifications">
                    <i class="fa-solid fa-bell"></i> <span class="m-menu-label">Notifications</span>
                    <c:if test="${globalNbNonLues > 0}"><span class="m-badge">${globalNbNonLues}</span></c:if>
                </a>
            </li>
        </ul>
    </aside>

    <div class="m-main">

        <header class="m-topbar">
            <div style="display:flex; align-items:center; gap:14px;">
                <button type="button" class="m-sidebar-toggle" id="mSidebarToggle" aria-label="Replier/déplier le menu">
                    <i class="fa-solid fa-bars"></i>
                </button>
                <span class="m-topbar-title">${pageTitle}</span>
            </div>

            <div class="m-topbar-actions">
                <jsp:include page="/WEB-INF/views/shared/_search-box.jsp"/>
                <button type="button" class="m-icon-btn pub-theme-toggle" id="pubThemeToggle" aria-label="Changer de thème">
                    <i class="fa-solid fa-moon icon-moon"></i>
                    <i class="fa-solid fa-sun icon-sun"></i>
                </button>

                <a href="${pageContext.request.contextPath}/membre/notifications" class="m-icon-btn">
                    <i class="fa-solid fa-bell"></i>
                    <c:if test="${globalNbNonLues > 0}"><span class="m-badge">${globalNbNonLues}</span></c:if>
                </a>

                <a href="${pageContext.request.contextPath}/messages" class="m-icon-btn">
                    <i class="fa-solid fa-comments"></i>
                </a>

                <a href="${pageContext.request.contextPath}/membre/profil" class="m-user-link">
                    ${sessionScope.user.email}
                </a>

                <a href="${pageContext.request.contextPath}/logout" class="btn-logout">
                    Déconnexion
                </a>

            </div>
        </header>

        <main class="m-content">
            <jsp:include page="${contentPage}" />
        </main>

    </div>

    <script>
        (function () {
            var themeBtn = document.getElementById('pubThemeToggle');
            if (themeBtn) {
                themeBtn.addEventListener('click', function () {
                    var html = document.documentElement;
                    var theme = html.getAttribute('data-theme') === 'dark' ? 'light' : 'dark';
                    html.setAttribute('data-theme', theme);
                    localStorage.setItem('lias-theme', theme);
                });
            }

            var sidebar = document.getElementById('mSidebar');
            var toggleBtn = document.getElementById('mSidebarToggle');
            if (sidebar && toggleBtn) {
                if (localStorage.getItem('lias-sidebar') === 'collapsed') {
                    sidebar.classList.add('collapsed');
                }
                toggleBtn.addEventListener('click', function () {
                    sidebar.classList.toggle('collapsed');
                    localStorage.setItem('lias-sidebar', sidebar.classList.contains('collapsed') ? 'collapsed' : 'expanded');
                });
            }
        })();
    </script>

</body>
</html>