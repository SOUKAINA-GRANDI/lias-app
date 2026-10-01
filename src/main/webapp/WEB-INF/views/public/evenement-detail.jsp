<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<section class="section" style="border-top:none; padding-top:56px;">
    <div class="container" style="max-width:760px;">

        <a href="${pageContext.request.contextPath}/public/evenements" class="team-link" style="display:inline-block; margin-bottom:24px;">
            ← Retour aux événements
        </a>

        <span class="event-type-tag">${evenement.type}</span>

        <h1 style="font-family:var(--pub-display); font-size:34px; margin:14px 0 20px; line-height:1.2;">
            ${evenement.titre}
        </h1>

        <div class="event-meta" style="font-size:13px; margin-bottom:28px;">
            <c:if test="${not empty evenement.lieu}">
                <span>
                    <svg class="meta-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"/>
                        <circle cx="12" cy="10" r="3"/>
                    </svg>
                    ${evenement.lieu}
                </span>
            </c:if>
            <span>
                <svg class="meta-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <rect x="3" y="4" width="18" height="18" rx="2"/>
                    <path d="M16 2v4M8 2v4M3 10h18"/>
                </svg>
                ${evenement.dateLisible}
            </span>
                                <c:if test="${not empty organisateurs}">
                        <div class="info-item">
                            <span class="info-icon">👥</span>
                            <div>
                                <p class="info-label">Organisateurs</p>
                                <p class="info-value"><c:out value="${organisateurs}"/></p>
                            </div>
                        </div>
                    </c:if>
        </div>

        <div class="pub-detail-card">
            <c:choose>
                <c:when test="${not empty evenement.description}">
                    <p style="white-space:pre-line; margin:0;">${evenement.description}</p>
                </c:when>
                <c:otherwise>
                    <p style="margin:0; color:var(--pub-ink-faint);">Aucune description supplémentaire pour cet événement.</p>
                </c:otherwise>
            </c:choose>
        </div>

        <c:if test="${not evenement.passe}">
            <div style="margin-top:28px;">
                <a href="${pageContext.request.contextPath}/login" class="btn-accent">
                    Se connecter pour participer
                </a>
            </div>
        </c:if>

    </div>
</section>