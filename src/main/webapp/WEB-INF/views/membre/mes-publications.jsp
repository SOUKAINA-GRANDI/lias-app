<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div style="display:flex; justify-content:space-between; align-items:flex-start; margin-bottom:28px; gap:16px; flex-wrap:wrap;">
    <div>
        <span class="pub-eyebrow" style="margin-bottom:8px;">Bibliographie du laboratoire</span>
        <h2 style="font-family:var(--pub-display); margin:0 0 6px; font-size:22px; font-weight:600; color:var(--pub-ink);">
            Publications
        </h2>
        <p style="margin:0; color:var(--pub-ink-soft); font-size:13.5px;">
            Vos contributions et celles des autres membres du LIAS.
        </p>
    </div>
    <a href="${pageContext.request.contextPath}/membre/publications?action=nouveau" class="btn-accent">
        <i class="fa-solid fa-circle-plus"></i> Ajouter une publication
    </a>
</div>

<c:if test="${not empty sessionScope.error}">
    <div class="m-alert m-alert-error"><i class="fa-solid fa-circle-exclamation"></i> ${sessionScope.error}</div>
    <c:remove var="error" scope="session"/>
</c:if>

<!-- ══ MES PUBLICATIONS ══ -->
<h4 class="m-card-title" style="margin-bottom:14px;">
    <i class="fa-solid fa-pen-nib"></i> Mes contributions (${fn:length(mesPublications)})
</h4>

<div style="display:flex; flex-direction:column; gap:12px; margin-bottom:36px;">
    <c:choose>
        <c:when test="${not empty mesPublications}">
            <c:forEach var="pub" items="${mesPublications}">
                <div class="m-card" style="border-left:3px solid var(--pub-signal); display:flex; justify-content:space-between; align-items:flex-start; gap:16px;">
                    <div style="min-width:0;">
                        <span class="pub-type-tag" style="margin-bottom:8px; display:inline-block;">${pub.type} — ${pub.annee}</span>
                        <h3 style="margin:0 0 6px; font-size:15px; color:var(--pub-ink); font-weight:700;">${pub.titre}</h3>
                        <p style="margin:0; font-size:12.5px; color:var(--pub-ink-soft);">
                            ${pub.auteurs}
                            <c:if test="${not empty pub.description}"> · <span style="color:var(--pub-ink-faint); font-style:italic;">${pub.description}</span></c:if>
                        </p>
                    </div>
                    <div style="display:flex; gap:6px; flex-shrink:0;">
                        <a href="${pageContext.request.contextPath}/membre/publications?action=edit&id=${pub.id}" class="m-icon-btn" title="Modifier">
                            <i class="fa-solid fa-pen"></i>
                        </a>
                        <form action="${pageContext.request.contextPath}/membre/publications" method="POST"
                              onsubmit="return confirm('Supprimer cette publication ?');">
                            <input type="hidden" name="action" value="supprimer">
                            <input type="hidden" name="id" value="${pub.id}">
                            <button type="submit" class="m-icon-btn" title="Supprimer" style="color:#EF4444; border:none; background:none; cursor:pointer;">
                                <i class="fa-solid fa-trash"></i>
                            </button>
                        </form>
                    </div>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <div class="m-card" style="text-align:center; color:var(--pub-ink-faint); font-size:13px; border-style:dashed;">
                Vous n'avez pas encore enregistré de publication personnelle.
            </div>
        </c:otherwise>
    </c:choose>
</div>

<!-- ══ AUTRES MEMBRES ══ -->
<h4 class="m-card-title" style="margin-bottom:14px;">
    <i class="fa-solid fa-users"></i> Publications des autres membres
</h4>

<div style="display:flex; flex-direction:column; gap:12px;">
    <c:set var="hasAutresPubs" value="false"/>
    <c:forEach var="pub" items="${toutesPublications}">
        <c:if test="${pub.membreId != sessionScope.user.id}">
            <c:set var="hasAutresPubs" value="true"/>
            <div class="m-card">
                <span class="pub-type-tag" style="margin-bottom:8px; display:inline-block;">${pub.type} — ${pub.annee}</span>
                <h3 style="margin:0 0 6px; font-size:15px; color:var(--pub-ink); font-weight:700;">${pub.titre}</h3>
                <p style="margin:0; font-size:12.5px; color:var(--pub-ink-soft);">
                    ${pub.auteurs}
                    <c:if test="${not empty pub.description}"> · <span style="color:var(--pub-ink-faint); font-style:italic;">${pub.description}</span></c:if>
                </p>
            </div>
        </c:if>
    </c:forEach>
    <c:if test="${hasAutresPubs == 'false'}">
        <div class="m-card" style="text-align:center; color:var(--pub-ink-faint); font-size:13px; border-style:dashed;">
            Aucune publication d'autres membres disponible.
        </div>
    </c:if>
</div>