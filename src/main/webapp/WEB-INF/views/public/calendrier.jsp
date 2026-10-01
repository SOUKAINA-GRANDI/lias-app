<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<section class="section" style="border-top:none; padding-top:56px; padding-bottom:24px;">
    <div class="container">
        <span class="pub-eyebrow" style="margin-bottom:14px;">Agenda institutionnel</span>
        <h1 style="font-family:var(--pub-display); font-size:38px; margin:0 0 16px;">
            Calendrier des événements
        </h1>
        <p style="font-size:16.5px; line-height:1.75; color:var(--pub-ink-soft); max-width:64ch; margin:0 0 28px;">
            Visualisation chronologique des activités scientifiques du laboratoire, année par année.
        </p>

        <div class="year-nav">
            <a href="${pageContext.request.contextPath}/public/calendrier?annee=${annee - 1}" class="year-nav-btn">←</a>
            <span class="year-nav-current">${annee}</span>
            <a href="${pageContext.request.contextPath}/public/calendrier?annee=${annee + 1}" class="year-nav-btn">→</a>
            <c:if test="${annee != anneeCourante}">
                <a href="${pageContext.request.contextPath}/public/calendrier?annee=${anneeCourante}" class="year-nav-today">Année en cours</a>
            </c:if>
        </div>
    </div>
</section>

<section class="section" style="padding-top:0;">
    <div class="container">

        <c:choose>

            <c:when test="${not empty evenements}">

                <div class="timeline">

                    <c:forEach var="e" items="${evenements}">

                        <div class="timeline-item">

                            <div class="timeline-date-box">
                                <span class="event-day">${e.jour}</span>
                                <span class="event-month">${e.moisAbrege}</span>
                            </div>

                            <div class="timeline-line"></div>

                            <div class="timeline-content">
                                <span class="event-type-tag">${e.type}</span>
                                <h3>${e.titre}</h3>

                                <div class="event-meta">
    <c:if test="${not empty e.lieu}">
        <span>
            <svg class="meta-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"/>
                <circle cx="12" cy="10" r="3"/>
            </svg>
            ${e.lieu}
        </span>
    </c:if>
    <span>
        <svg class="meta-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <rect x="3" y="4" width="18" height="18" rx="2"/>
            <path d="M16 2v4M8 2v4M3 10h18"/>
        </svg>
        ${e.dateLisible}
    </span>
</div>

                                <p class="event-description">
                                    <c:choose>
                                        <c:when test="${not empty e.description}">${e.description}</c:when>
                                        <c:otherwise>Détails à venir.</c:otherwise>
                                    </c:choose>
                                </p>
                            </div>

                        </div>

                    </c:forEach>

                </div>

            </c:when>

            <c:otherwise>
                <div style="text-align:center; padding:60px 20px; color:var(--pub-ink-faint);">
                    <div style="font-size:36px; margin-bottom:12px;">📅</div>
                    <h3 style="margin:0 0 6px;">Aucun événement en ${annee}</h3>
                    <p style="margin:0;">Essayez une autre année avec les flèches ci-dessus.</p>
                </div>
            </c:otherwise>

        </c:choose>

    </div>
</section>
