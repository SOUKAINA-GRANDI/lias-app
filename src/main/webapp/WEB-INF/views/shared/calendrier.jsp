<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="page-header">
    <h2>Calendrier</h2>
</div>

<c:choose>

    <c:when test="${not empty evenements}">

        <div class="events-grid">

            <c:forEach var="e" items="${evenements}">
                <div class="event-card">
                    <h3>${e.titre}</h3>
                    <p>${e.dateDebut}</p>
                    <p class="text-muted">${e.lieu}</p>
                </div>
            </c:forEach>

        </div>

    </c:when>

    <c:otherwise>
        <div class="empty-state">
            <div class="empty-icon">📅</div>
            <h3>Aucun événement</h3>
        </div>
    </c:otherwise>

</c:choose>