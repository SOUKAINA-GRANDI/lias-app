<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <jsp:include page="../_partials/head.jsp"/>

    <!-- Anti-flash : applique le thème sauvegardé AVANT le premier rendu -->
    <script>
        (function () {
            var saved = localStorage.getItem('lias-theme');
            var preferDark = window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches;
            document.documentElement.setAttribute('data-theme', saved || (preferDark ? 'dark' : 'light'));
        })();
    </script>

    <!-- Typographie et design system dédiés au site public uniquement -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Space+Grotesk:wght@500;600;700&family=IBM+Plex+Sans:wght@400;500;600&family=IBM+Plex+Mono:wght@400;500&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/public-design-system.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/pages/auth-design.css">
</head>

<body class="public-layout">

    <jsp:include page="_navbar.jsp"/>

    <!-- ✅ MAIN WRAPPER -->
    <main class="main-content fade-in">

        <div class="container">

            <!-- Flash Messages -->
            <jsp:include page="../_partials/flash-messages.jsp"/>

            <!-- Dynamic Page -->
            <jsp:include page="${contentPage}"/>

        </div>

    </main>

    <jsp:include page="_footer.jsp"/>

    <jsp:include page="../_partials/scripts.jsp"/>

</body>
</html>