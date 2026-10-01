<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div style="margin-bottom:28px;">
    <span class="pub-eyebrow" style="margin-bottom:8px;">Annuaire interne — ${fn:length(membres)} membres</span>
    <h2 style="font-family:var(--pub-display); margin:0 0 6px; font-size:22px; font-weight:600; color:var(--pub-ink);">
        Membres du laboratoire
    </h2>
    <p style="margin:0; color:var(--pub-ink-soft); font-size:13.5px;">
        Retrouvez l'ensemble des chercheurs, permanents, associés et doctorants du LIAS.
    </p>
</div>

<div class="m-researcher-grid">

    <c:forEach items="${membres}" var="m">
        <div class="m-card m-researcher-card">

            <div class="m-profile-avatar" style="width:48px; height:48px; font-size:15px; flex-shrink:0;">
                ${fn:substring(m.prenom,0,1)}${fn:substring(m.nom,0,1)}
            </div>

            <div style="flex:1; min-width:0;">

                <h3 style="margin:0 0 6px; font-size:15px; font-weight:700; color:var(--pub-ink);">
                    ${m.prenom} ${m.nom}
                </h3>

                <div style="display:flex; gap:6px; flex-wrap:wrap; margin-bottom:8px;">
                    <c:if test="${not empty m.statut}">
                        <span class="m-pill">${m.statut}</span>
                    </c:if>
                    <c:if test="${not empty m.equipeNom}">
                        <span class="m-pill alt">${m.equipeNom}</span>
                    </c:if>
                </div>

                <a href="mailto:${m.email}" class="m-researcher-email">${m.email}</a>

                <div style="margin-top:12px; display:flex; justify-content:flex-end;">
                    <c:choose>
                        <c:when test="${m.utilisateurId == sessionScope.user.id}">
                            <span class="m-pill alt"><i class="fa-solid fa-user-check"></i> C'est vous</span>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/messages?action=start&with=${m.utilisateurId}" class="btn-accent" style="padding:7px 14px; font-size:12.5px;">
                                <i class="fa-solid fa-comment-dots"></i> Discuter
                            </a>
                        </c:otherwise>
                    </c:choose>
                </div>

            </div>

        </div>
    </c:forEach>

</div>