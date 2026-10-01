<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Nouveau mot de passe - LIAS</title>
    <jsp:include page="/WEB-INF/views/_partials/head.jsp"/>
</head>
<body class="login-page">

    <div class="login-form-side">
        <div class="login-card">

            <div class="lc-header">
                <h2>Nouveau mot de passe</h2>
            </div>

            <c:if test="${not empty error}">
                <p style="background:#FEE2E2; color:#991B1B; padding:12px 14px; border-radius:8px; font-size:14px; margin-bottom:16px;">
                    ${error}
                </p>
            </c:if>

            <c:if test="${not empty token}">
                <form method="post" action="${pageContext.request.contextPath}/reinitialiser-mot-de-passe">
                    <input type="hidden" name="token" value="${token}">

                    <div class="form-group">
                        <label>Nouveau mot de passe</label>
                        <input type="password" name="password" required minlength="8" autocomplete="new-password">
                    </div>

                    <div class="form-group">
                        <label>Confirmer le mot de passe</label>
                        <input type="password" name="confirmPassword" required minlength="8" autocomplete="new-password">
                    </div>

                    <button type="submit" class="login-btn">Réinitialiser →</button>
                </form>
            </c:if>

            <c:if test="${empty token}">
                <a href="${pageContext.request.contextPath}/mot-de-passe-oublie" style="display:inline-block; margin-top:12px; font-size:13px;">
                    Demander un nouveau lien →
                </a>
            </c:if>

        </div>
    </div>

</body>
</html>