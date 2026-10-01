<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="page-header">
    <h2>Résultats pour "${query}"</h2>
</div>

<c:if test="${empty membres and empty publications and empty evenements and empty conventions and empty reunions and empty documents}">
    <div class="empty-state">
        <div class="empty-icon">🔍</div>
        <h3>Aucun résultat trouvé</h3>
        <p>Essayez une autre recherche.</p>
    </div>
</c:if>

<c:if test="${not empty membres}">
    <div class="card">
        <h3>Membres</h3>
        <c:forEach var="m" items="${membres}">
            <p>${m.nom} ${m.prenom}</p>
        </c:forEach>
    </div>
</c:if>

<c:if test="${not empty publications}">
    <div class="card">
        <h3>Publications</h3>
        <c:forEach var="p" items="${publications}">
            <p>${p.titre}</p>
        </c:forEach>
    </div>
</c:if>

<c:if test="${not empty evenements}">
    <div class="card">
        <h3>Événements</h3>
        <c:forEach var="e" items="${evenements}">
            <p>${e.titre}</p>
        </c:forEach>
    </div>
</c:if>

<c:if test="${not empty conventions}">
    <div class="card">
        <h3>Conventions</h3>
        <c:forEach var="c" items="${conventions}">
            <p>${c.titre} <span style="color:#94A3B8">— ${c.partenaire}</span></p>
        </c:forEach>
    </div>
</c:if>

<c:if test="${not empty reunions}">
    <div class="card">
        <h3>Réunions</h3>
        <c:forEach var="r" items="${reunions}">
            <p>${r.titre}</p>
        </c:forEach>
    </div>
</c:if>

<c:if test="${not empty documents}">
    <div class="card">
        <h3>Documents</h3>
        <c:forEach var="d" items="${documents}">
            <p>
                <a href="${pageContext.request.contextPath}/${d.cheminFichier}" target="_blank">${d.titre}</a>
                <span style="color:#94A3B8">— ${d.type}</span>
            </p>
        </c:forEach>
    </div>
</c:if>