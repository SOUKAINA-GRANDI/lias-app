<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Mot de passe oublié - LIAS</title>
    <jsp:include page="/WEB-INF/views/_partials/head.jsp"/>
</head>
<body class="login-page">

    <div class="login-form-side">
        <div class="login-card">

            <div class="lc-header">
                <h2>Mot de passe oublié</h2>
            </div>

            <c:choose>
                <c:when test="${messageEnvoye}">
                    <p style="background:#DCFCE7; color:#166534; padding:12px 14px; border-radius:8px; font-size:14px;">
                        Si un compte existe avec cette adresse, un lien de réinitialisation vient de lui être envoyé
                        par email. Il est valable 1 heure.
                    </p>
                </c:when>
                <c:otherwise>
                    <p style="color:#64748B; font-size:14px; margin-bottom:20px;">
                        Indique ton adresse email : si un compte actif y est associé, tu recevras un lien pour choisir un nouveau mot de passe.
                    </p>

                    <form method="post" action="${pageContext.request.contextPath}/mot-de-passe-oublie">
                        <div class="form-group">
                            <label>Adresse email</label>
                            <input type="email" name="email" required autocomplete="email">
                        </div>
                        <button type="submit" class="login-btn">Envoyer le lien →</button>
                    </form>
                </c:otherwise>
            </c:choose>

            <a href="${pageContext.request.contextPath}/login" style="display:inline-block; margin-top:22px; font-size:13px;">
                ← Retour à la connexion
            </a>

        </div>
    </div>

</body>
</html>