<h2>Tableau de bord Directeur</h2>

<%@ page contentType="text/html;charset=UTF-8" %>
<div class="dashboard-grid">

    <div class="dashboard-card">
        <h4>Demandes en attente</h4>
        <p>${nbDemandes}</p>
    </div>

    <div class="dashboard-card">
        <h4>Membres actifs</h4>
        <p>${nbMembres}</p>
    </div>

    <div class="dashboard-card">
        <h4>Événements</h4>
        <p>${nbEvenements}</p>
    </div>

    <div class="dashboard-card">
        <h4>Publications</h4>
        <p>${nbPublications}</p>
    </div>

    <div class="dashboard-card">
        <h4>Documents</h4>
        <p>${nbDocuments}</p>
    </div>

</div>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="dashboard-card" style="margin-top:20px;">
    <h4>Documents récents</h4>
    <c:choose>
        <c:when test="${not empty documentsRecents}">
            <ul style="list-style:none; padding:0; margin:10px 0 0;">
                <c:forEach var="d" items="${documentsRecents}">
                    <li style="padding:8px 0; border-bottom:1px solid #F1F5F9; font-size:13px;">
                        <a href="${pageContext.request.contextPath}/${d.cheminFichier}" target="_blank">${d.titre}</a>
                        <span style="color:#94A3B8;"> — ${d.type} · ${d.dateUpload}</span>
                    </li>
                </c:forEach>
            </ul>
            <a href="${pageContext.request.contextPath}/directeur/documents" style="font-size:12px; color:#2563EB; text-decoration:none;">
                Voir tous les documents →
            </a>
        </c:when>
        <c:otherwise>
            <p style="color:#94A3B8; font-size:13px;">Aucun document pour le moment.</p>
        </c:otherwise>
    </c:choose>
</div>