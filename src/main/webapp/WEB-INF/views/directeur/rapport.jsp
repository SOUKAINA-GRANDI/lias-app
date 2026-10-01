<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="page-header">
    <h2>Rapport Annuel ${rapport.year}</h2>

    <div class="export-actions">
        <a href="${pageContext.request.contextPath}/directeur/rapport?action=pdf&annee=${rapport.year}"
           class="btn-primary">
            📄 Export PDF
        </a>

        <a href="${pageContext.request.contextPath}/directeur/rapport?action=excel&annee=${rapport.year}"
           class="btn-secondary">
            📊 Export Excel
        </a>
    </div>
</div>

<!-- ✅ Statistiques comparatives -->
<div class="report-wrapper">

    <div class="report-summary">

        <!-- Publications -->
        <div class="report-card">
            <h4>Publications</h4>
            <div class="report-values">
                <div class="value-current">
                    <span class="value">${rapport.pubCurrent}</span>
                    <span class="label">${rapport.year}</span>
                </div>
                <div class="value-prev">
                    <span class="value">${rapport.pubPrev}</span>
                    <span class="label">${rapport.prevYear}</span>
                </div>
            </div>
            <div class="report-trend">
                <c:choose>
                    <c:when test="${rapport.pubCurrent > rapport.pubPrev}">
                        <span class="trend-up">📈 En hausse</span>
                    </c:when>
                    <c:when test="${rapport.pubCurrent < rapport.pubPrev}">
                        <span class="trend-down">📉 En baisse</span>
                    </c:when>
                    <c:otherwise>
                        <span class="trend-stable">➡️ Stable</span>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <!-- Événements -->
        <div class="report-card">
            <h4>Événements</h4>
            <div class="report-values">
                <div class="value-current">
                    <span class="value">${rapport.eventsCurrent}</span>
                    <span class="label">${rapport.year}</span>
                </div>
                <div class="value-prev">
                    <span class="value">${rapport.eventsPrev}</span>
                    <span class="label">${rapport.prevYear}</span>
                </div>
            </div>
            <div class="report-trend">
                <c:choose>
                    <c:when test="${rapport.eventsCurrent > rapport.eventsPrev}">
                        <span class="trend-up">📈 En hausse</span>
                    </c:when>
                    <c:when test="${rapport.eventsCurrent < rapport.eventsPrev}">
                        <span class="trend-down">📉 En baisse</span>
                    </c:when>
                    <c:otherwise>
                        <span class="trend-stable">➡️ Stable</span>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <!-- Conventions -->
        <div class="report-card">
            <h4>Conventions</h4>
            <div class="report-values">
                <div class="value-current">
                    <span class="value">${rapport.convCurrent}</span>
                    <span class="label">${rapport.year}</span>
                </div>
                <div class="value-prev">
                    <span class="value">${rapport.convPrev}</span>
                    <span class="label">${rapport.prevYear}</span>
                </div>
            </div>
        </div>

        <!-- Membres -->
        <div class="report-card">
            <h4>Membres actifs</h4>
            <div class="report-values">
                <div class="value-current">
                    <span class="value">${rapport.membresActifs}</span>
                    <span class="label">Actuellement</span>
                </div>
            </div>
        </div>

    </div>

    <!-- ✅ Liste événements de l'année -->
    <div class="report-section">

        <h3>Événements ${rapport.year}</h3>

        <c:choose>

            <c:when test="${not empty rapport.evenements}">

                <div class="table-container">

                    <table class="modern-table">

                        <thead>
                        <tr>
                            <th>Titre</th>
                            <th>Type</th>
                            <th>Date</th>
                            <th>Lieu</th>
                        </tr>
                        </thead>

                        <tbody>

                        <c:forEach var="e" items="${rapport.evenements}">

                            <tr>
                                <td>${e.titre}</td>
                                <td>
                                    <span class="badge badge-accent">
                                        ${e.type}
                                    </span>
                                </td>
                                <td>${e.dateDebut}</td>
                                <td>${e.lieu}</td>
                            </tr>

                        </c:forEach>

                        </tbody>
                    </table>

                </div>

            </c:when>

            <c:otherwise>
                <div class="empty-state">
                    <div class="empty-icon">📅</div>
                    <h3>Aucun événement cette année</h3>
                </div>
            </c:otherwise>

        </c:choose>

    </div>

</div>