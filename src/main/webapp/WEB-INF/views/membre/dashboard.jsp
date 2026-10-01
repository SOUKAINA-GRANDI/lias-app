<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div style="margin-bottom:32px;">
    <span class="pub-eyebrow" style="margin-bottom:8px;">Espace chercheur</span>
    <h2 style="font-family:var(--pub-display); margin:0 0 6px; font-size:22px; font-weight:600; color:var(--pub-ink);">
        Tableau de bord
    </h2>
    <p style="margin:0; color:var(--pub-ink-soft); font-size:13.5px;">
        Aperçu de vos activités et des ressources récentes du laboratoire.
    </p>
</div>

<div class="m-stats-grid">
    <div class="m-stat-card">
        <div class="m-stat-top">
            <span>Mes publications</span>
            <span class="m-stat-icon"><i class="fa-solid fa-book"></i></span>
        </div>
        <h3>${nbPublications}</h3>
    </div>
    <div class="m-stat-card">
        <div class="m-stat-top">
            <span>Événements</span>
            <span class="m-stat-icon"><i class="fa-solid fa-calendar"></i></span>
        </div>
        <h3>${nbEvenements}</h3>
    </div>
    <div class="m-stat-card">
        <div class="m-stat-top">
            <span>Messages non lus</span>
            <span class="m-stat-icon"><i class="fa-solid fa-envelope"></i></span>
        </div>
        <h3>${nbMessagesNonLus}</h3>
    </div>
</div>

<div style="display:grid; grid-template-columns:1fr; gap:18px;">

    <div class="m-card">
        <h4 class="m-card-title">
            <i class="fa-solid fa-bolt"></i> Actions rapides
        </h4>
        <div style="display:grid; grid-template-columns:repeat(auto-fit,minmax(240px,1fr)); gap:12px;">
            <a href="${pageContext.request.contextPath}/membre/publications?action=ajouter" class="m-quick-action">
                <span class="m-quick-action-icon"><i class="fa-solid fa-circle-plus"></i></span>
                <span>Ajouter une nouvelle publication</span>
            </a>
            <a href="${pageContext.request.contextPath}/messages" class="m-quick-action">
                <span class="m-quick-action-icon"><i class="fa-solid fa-paper-plane"></i></span>
                <span>Accéder à la messagerie d'équipe</span>
            </a>
        </div>
    </div>

    <div class="m-card">
        <h4 class="m-card-title">
            <i class="fa-solid fa-folder-open"></i> Documents récents
        </h4>
        <c:choose>
            <c:when test="${not empty documentsRecents}">
                <ul style="list-style:none; padding:0; margin:0;">
                    <c:forEach var="d" items="${documentsRecents}">
                        <li class="m-list-row">
                            <a href="${pageContext.request.contextPath}/${d.cheminFichier}" target="_blank" class="m-list-row-title">${d.titre}</a>
                            <span class="m-list-row-meta">${d.type} · ${d.dateFormatee}</span>
                        </li>
                    </c:forEach>
                </ul>
                <a href="${pageContext.request.contextPath}/membre/documents" class="team-link" style="display:inline-block; margin-top:14px;">
                    Voir tous les documents →
                </a>
            </c:when>
            <c:otherwise>
                <p style="color:var(--pub-ink-faint); font-size:13px; margin:0;">Aucun document pour le moment.</p>
            </c:otherwise>
        </c:choose>
    </div>

</div>