<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html><html lang="fr">
<head><jsp:include page="../_partials/head.jsp"/></head>
<body>
<div class="login-page">
  <div class="login-form-side">
    <div class="login-card fade-in">

      <div class="lc-header">
        <h2>Activation du compte</h2>
        <p>Bienvenue au Laboratoire LIAS — choisissez votre mot de passe</p>
      </div>

      <c:if test="${not empty error}">
        <p style="color:#EF4444; font-size:13px; margin-bottom:14px;">${error}</p>
      </c:if>

      <c:choose>
        <c:when test="${not empty token}">
          <form method="post" action="${pageContext.request.contextPath}/activation">
            <input type="hidden" name="token" value="${token}">

            <div class="form-group">
              <label>Email</label>
              <input type="email" value="${email}" disabled>
            </div>

            <div class="form-group">
              <label>Nouveau mot de passe</label>
              <input type="password" name="password" minlength="8" required>
            </div>

            <div class="form-group">
              <label>Confirmer le mot de passe</label>
              <input type="password" name="confirmPassword" minlength="8" required>
            </div>

            <button type="submit" class="login-btn">
              Activer mon compte →
            </button>
          </form>
        </c:when>
        <c:otherwise>
          <p style="font-size:13px; color:#64748B;">
            Demandez à la direction de vous renvoyer un lien d'activation si besoin.
          </p>
          <a href="${pageContext.request.contextPath}/login" class="login-btn" style="display:inline-block; text-align:center; text-decoration:none;">
            Retour à la connexion
          </a>
        </c:otherwise>
      </c:choose>

    </div>
  </div>
</div>
<jsp:include page="../_partials/scripts.jsp"/>
</body></html>
