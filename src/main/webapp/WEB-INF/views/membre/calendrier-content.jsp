<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<style>
    .cal-container {
        width: 100%;
        max-width: 1200px;
        margin: 0 auto;
        padding: 20px;
        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Arial, sans-serif;
    }
    .cal-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 35px;
    }
    .cal-title h2 {
        margin: 0;
        font-size: 20px;
        color: #1E293B;
        font-weight: 700;
    }
    .cal-title .subtitle {
        font-size: 11px;
        font-weight: 600;
        text-transform: uppercase;
        letter-spacing: 0.08em;
        color: #94A3B8;
        display: inline-block;
        margin-top: 5px;
    }

    /* BARRE D'ACTIONS PRO */
    .action-group {
        display: flex;
        gap: 14px;
        align-items: center;
    }
    .year-navigator {
        display: flex;
        align-items: center;
        background: #FFFFFF;
        border: 1px solid #E2E8F0;
        border-radius: 10px;
        padding: 4px;
        box-shadow: 0 1px 3px rgba(0, 0, 0, 0.02);
    }
    .year-btn {
        display: flex;
        align-items: center;
        justify-content: center;
        background: none;
        border: none;
        width: 36px;
        height: 36px;
        color: #64748B;
        border-radius: 8px;
        font-size: 14px;
        cursor: pointer;
        transition: all 0.2s;
    }
    .year-btn:hover {
        background-color: #F8FAFC;
        color: #F57C00;
    }
    .current-year-badge {
        font-size: 14px;
        font-weight: 700;
        color: #0F172A;
        padding: 0 18px;
        letter-spacing: 0.02em;
        user-select: none;
    }
    .btn-back-list {
        background-color: #F1F5F9;
        color: #475569;
        border: 1px solid #E2E8F0;
        padding: 0 16px;
        height: 44px;
        font-size: 13px;
        font-weight: 600;
        border-radius: 10px;
        text-decoration: none;
        display: inline-flex;
        align-items: center;
        gap: 6px;
        transition: all 0.2s;
    }
    .btn-back-list:hover { 
        background-color: #E2E8F0; 
        color: #0F172A;
    }
    
    /* 🌟 SYSTÈME DE GRILLE COMME L'EXEMPLE f8e686.png */
    .evt-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(560px, 1fr));
        gap: 20px;
    }
    .evt-card {
        background: #FFFFFF;
        border: 1px solid #E2E8F0;
        border-top: 4px solid #D97706; /* Ligne supérieure dorée caractéristique */
        border-radius: 16px;
        padding: 20px 24px;
        box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.02), 0 2px 4px -1px rgba(0, 0, 0, 0.01);
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        min-height: 160px;
        transition: transform 0.2s, box-shadow 0.2s;
    }
    .evt-card:hover {
        transform: translateY(-2px);
        box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.04);
    }
    .evt-card-top {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 12px;
    }
    
    /* Dynamic Badges de l'image */
    .badge-type {
        font-size: 11px;
        font-weight: 700;
        padding: 4px 10px;
        border-radius: 20px;
    }
    .badge-conference { background-color: #FEF3C7; color: #D97706; }
    .badge-seminaire { background-color: #F3E8FF; color: #7E3AF2; }
    .badge-workshop { background-color: #FCE7F3; color: #DB2777; }
    
    .evt-date-raw {
        font-size: 13px;
        font-weight: 600;
        color: #94A3B8;
    }
    .evt-card h3 {
        margin: 0 0 16px 0;
        font-size: 15.5px;
        line-height: 1.4;
        font-weight: 700;
        color: #0F172A;
    }
    .evt-card h3 a {
        color: #0F172A;
        text-decoration: none;
    }
    .evt-card h3 a:hover {
        color: #F57C00;
    }
    
    /* Séparateur et Pied de Carte */
    .evt-card-footer {
        border-top: 1px solid #F1F5F9;
        padding-top: 12px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        font-size: 12.5px;
    }
    .evt-lieu {
        color: #94A3B8;
    }
    .evt-lieu strong {
        color: #64748B;
        font-weight: 500;
    }
    .status-badge {
        font-weight: 700;
    }
    .status-avenir { color: #059669; }
    .status-passe { color: #DC2626; }

    /* État vide */
    .empty-state {
        display: none;
        grid-column: 1 / -1;
        background: #FFFFFF;
        border: 1px dashed #E2E8F0;
        border-radius: 12px;
        padding: 60px;
        text-align: center;
        color: #94A3B8;
        font-style: italic;
        font-size: 14px;
    }
</style>

<div class="cal-container">

    <div class="cal-header">
        <div class="cal-title">
            <h2>Planning Annuel des Événements</h2>
            <span class="subtitle">Vue chronologique de l'année scientifique</span>
        </div>
        
        <div class="action-group">
            <div class="year-navigator">
                <button type="button" class="year-btn" onclick="changeYear(-1)" title="Année précédente">&#10094;</button>
                <span class="current-year-badge" id="yearLabel">2026</span>
                <button type="button" class="year-btn" onclick="changeYear(1)" title="Année suivante">&#10095;</button>
            </div>

            <a href="${pageContext.request.contextPath}/membre/evenements?action=liste" class="btn-back-list">
                📋 Vue Liste
            </a>
        </div>
    </div>

    <!-- 🌟 GRILLE D'ÉVÉNEMENTS INTERACTIVE -->
    <div class="evt-grid">
        <c:if test="${not empty evenements}">
            <c:forEach var="evt" items="${evenements}">
                <!-- Analyse de la date brute pour filtrer par JS -->
                <c:set var="dateStr" value="${evt.dateDebut.toString()}" />
                <c:set var="evtYear" value="${fn:substring(dateStr, 0, 4)}" />
                
                <div class="evt-card-wrapper" data-year="${evtYear}" data-full-date="${dateStr}">
                    <div class="evt-card">
                        <div>
                            <div class="evt-card-top">
                                <!-- Badge de type dynamique selon l'énumération -->
                                <c:choose>
                                    <c:when test="${evt.type == 'CONFERENCE'}">
                                        <span class="badge-type badge-conference">Conférence</span>
                                    </c:when>
                                    <c:when test="${evt.type == 'SEMINAIRE'}">
                                        <span class="badge-type badge-seminaire">Séminaire</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge-type badge-workshop">Workshop</span>
                                    </c:otherwise>
                                </c:choose>
                                <span class="evt-date-raw">${fn:substring(dateStr, 0, 10)}</span>
                            </div>
                            
                            <h3>
                                <a href="${pageContext.request.contextPath}/membre/evenements?action=detail&id=${evt.id}">
                                    <c:out value="${evt.titre}"/>
                                </a>
                            </h3>
                        </div>

                        <div class="evt-card-footer">
                            <span class="evt-lieu">Lieu : <strong><c:out value="${evt.lieu}"/></strong></span>
                            <!-- Le statut (À venir / Passé) sera calculé dynamiquement via JS par rapport au jour d'aujourd'hui -->
                            <span class="status-badge" id="status-${evt.id}"></span>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </c:if>

        <div id="emptyState" class="empty-state">
            Aucun événement n'est programmé pour l'année <span id="selectedYearSpan"></span>.
        </div>
    </div>
</div>

<script>
    // Initialisation calée sur 2026
    let currentYear = 2026;

    function updateTimelineFilter() {
        document.getElementById('yearLabel').textContent = currentYear;
        document.getElementById('selectedYearSpan').textContent = currentYear;

        const items = document.querySelectorAll('.evt-card-wrapper');
        const now = new Date();
        let visibleCount = 0;

        items.forEach(item => {
            const itemYear = item.getAttribute('data-year');
            const fullDateStr = item.getAttribute('data-full-date');
            
            if (itemYear === currentYear.toString()) {
                item.style.display = 'block';
                visibleCount++;

                // Calcule si l'événement est passé ou à venir
                const eventId = item.querySelector('.status-badge').id;
                const statusBadge = document.getElementById(eventId);
                const eventDate = new Date(fullDateStr);

                if (eventDate >= now) {
                    statusBadge.textContent = "À venir";
                    statusBadge.className = "status-badge status-avenir";
                } else {
                    statusBadge.textContent = "Passé";
                    statusBadge.className = "status-badge status-passe";
                }
            } else {
                item.style.display = 'none';
            }
        });

        const emptyState = document.getElementById('emptyState');
        if (visibleCount === 0) {
            emptyState.style.display = 'block';
        } else {
            emptyState.style.display = 'none';
        }
    }

    function changeYear(offset) {
        currentYear += offset;
        updateTimelineFilter();
    }

    document.addEventListener("DOMContentLoaded", function() {
        updateTimelineFilter();
    });
</script>