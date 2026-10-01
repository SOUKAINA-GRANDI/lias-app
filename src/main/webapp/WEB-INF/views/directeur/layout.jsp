<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <%-- ✅ Chemin absolu vers head.jsp --%>
    <jsp:include page="/WEB-INF/views/_partials/head.jsp"/>
</head>

<body class="app-layout">

    <div class="app-wrapper" style="display: flex; width: 100%; min-height: 100vh;">

        <%-- ✅ Chemin absolu vers la sidebar directeur --%>
        <jsp:include page="/WEB-INF/views/directeur/_sidebar.jsp"/>

        <div class="app-main" style="flex: 1; margin-left: 260px; display: flex; flex-direction: column; min-width: 0; background: var(--color-bg);">

            <%-- ✅ Chemin absolu vers la topbar directeur --%>
            <jsp:include page="/WEB-INF/views/directeur/_topbar.jsp"/>

            <main class="app-content" style="padding: 30px; flex: 1;">
                <c:choose>
                    <c:when test="${not empty contentPage}">
                        <jsp:include page="${contentPage}"/>
                    </c:when>
                    <c:otherwise>
                        <div class="alert alert-danger">
                            Erreur : Aucune page de contenu n'a été spécifiée.
                        </div>
                    </c:otherwise>
                </c:choose>
            </main>

        </div>

    </div>

    <%-- ✅ Chemin absolu vers scripts.jsp --%>
    <jsp:include page="/WEB-INF/views/_partials/scripts.jsp"/>

</body>
</html>