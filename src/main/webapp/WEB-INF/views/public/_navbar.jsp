<div class="navbar">

    <div class="container navbar-wrapper">

        <a class="pub-logo" href="${pageContext.request.contextPath}/public/accueil">
            <img src="${pageContext.request.contextPath}/assets/images/logo-lias-crop.png"
                 alt="LIAS" class="pub-logo-img">
        </a>

        <nav class="nav-links">

            <a href="${pageContext.request.contextPath}/public/accueil">Accueil</a>
            <a href="${pageContext.request.contextPath}/public/presentation">Présentation</a>
            <a href="${pageContext.request.contextPath}/public/membres">Membres</a>
            <a href="${pageContext.request.contextPath}/public/equipes" class="nav-link">ÉQUIPES</a>
            <a href="${pageContext.request.contextPath}/public/evenements">Événements</a>
            <a href="${pageContext.request.contextPath}/public/publications">Publications</a>
            <a href="${pageContext.request.contextPath}/public/calendrier">Calendrier</a>

        </nav>

        <div class="nav-actions">
            <jsp:include page="/WEB-INF/views/shared/_search-box.jsp">
                <jsp:param name="compact" value="true"/>
            </jsp:include>
            <a href="${pageContext.request.contextPath}/login"
               class="btn-login">
                Se connecter
            </a>

            <a class="btn-accent"
               href="${pageContext.request.contextPath}/public/demande-adhesion">
                Rejoindre le labo
            </a>

            <button type="button" class="pub-theme-toggle" id="pubThemeToggle" aria-label="Changer de thème">
                <svg class="icon-moon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"/>
                </svg>
                <svg class="icon-sun" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <circle cx="12" cy="12" r="4"/>
                    <path d="M12 2v2M12 20v2M4.93 4.93l1.41 1.41M17.66 17.66l1.41 1.41M2 12h2M20 12h2M6.34 17.66l-1.41 1.41M19.07 4.93l-1.41 1.41"/>
                </svg>
            </button>

        </div>

    </div>

</div>

<script>
(function () {
    var btn = document.getElementById('pubThemeToggle');
    if (!btn) return;

    btn.addEventListener('click', function () {
        var html = document.documentElement;
        var theme = html.getAttribute('data-theme') === 'dark' ? 'light' : 'dark';
        html.setAttribute('data-theme', theme);
        localStorage.setItem('lias-theme', theme);
    });
})();
</script>
