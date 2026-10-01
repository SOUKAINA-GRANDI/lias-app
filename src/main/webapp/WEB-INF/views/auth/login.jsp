<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<div class="auth-split">

    <!-- ✅ PANNEAU DE MARQUE -->
    <div class="auth-brand">
        <svg class="auth-brand-graph" viewBox="0 0 260 260" fill="none" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
            <g stroke="#fff" stroke-width="1.3" opacity=".5">
                <line x1="30" y1="40" x2="120" y2="20"/>
                <line x1="120" y1="20" x2="220" y2="60"/>
                <line x1="30" y1="40" x2="60" y2="140"/>
                <line x1="220" y1="60" x2="200" y2="150"/>
                <line x1="60" y1="140" x2="140" y2="180"/>
                <line x1="200" y1="150" x2="140" y2="180"/>
                <line x1="120" y1="20" x2="140" y2="180"/>
                <line x1="60" y1="140" x2="30" y2="230"/>
                <line x1="140" y1="180" x2="180" y2="235"/>
            </g>
            <g fill="#fff" opacity=".85">
                <circle cx="30" cy="40" r="4"/>
                <circle cx="120" cy="20" r="5"/>
                <circle cx="220" cy="60" r="4"/>
                <circle cx="60" cy="140" r="4.5"/>
                <circle cx="200" cy="150" r="4"/>
                <circle cx="140" cy="180" r="6"/>
                <circle cx="30" cy="230" r="3.5"/>
                <circle cx="180" cy="235" r="3.5"/>
            </g>
        </svg>

        <a class="pub-logo" href="${pageContext.request.contextPath}/public/accueil">
            <img src="${pageContext.request.contextPath}/assets/images/logo-lias-crop.png"
                 alt="LIAS" class="pub-logo-img">
        </a>

        <div class="auth-brand-text">
            <h2>Bienvenue dans votre espace de recherche</h2>
            <p>Accédez à vos publications, événements, équipes et documents du laboratoire LIAS.</p>
        </div>
    </div>

    <!-- ✅ FORMULAIRE -->
    <div class="auth-form-side">
        <div class="auth-card">

            <span class="pub-eyebrow" style="margin-bottom:10px;">Espace membre</span>
            <h1 class="auth-title">Connexion</h1>
            <p class="auth-subtitle">Accédez à votre espace LIAS</p>

            <c:if test="${param.activated == '1'}">
                <div class="auth-alert auth-alert-success">
                    Compte activé avec succès. Vous pouvez maintenant vous connecter.
                </div>
            </c:if>

            <c:if test="${not empty error}">
                <div class="auth-alert auth-alert-error">${error}</div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/login" class="auth-form">

                <div class="auth-field">
                    <label>Adresse email</label>
                    <input type="email" name="email" required autocomplete="email">
                </div>

                <div class="auth-field">
                    <label>Mot de passe</label>
                    <input type="password" name="password" required autocomplete="current-password">
                </div>

                <button type="submit" class="btn-accent auth-submit">
                    Se connecter →
                </button>

            </form>
            <a href="${pageContext.request.contextPath}/mot-de-passe-oublie" class="team-link" style="display:block; margin-top:14px; font-size:13px;">
                Mot de passe oublié ?
            </a>

            <a href="${pageContext.request.contextPath}/public/accueil" class="team-link" style="display:inline-block; margin-top:22px;">
                ← Retour au site public
            </a>

        </div>
    </div>

</div>
