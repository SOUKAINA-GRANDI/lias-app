<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<style>
    .evt-container {
        width: 100%;
        max-width: 1200px;
        margin: 0 auto;
        padding: 20px;
        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Arial, sans-serif;
    }
    .evt-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 35px;
    }
    .evt-title h2 {
        margin: 0;
        font-size: 20px;
        color: #1E293B;
        font-weight: 700;
    }
    .evt-title .subtitle {
        font-size: 11px;
        font-weight: 600;
        text-transform: uppercase;
        letter-spacing: 0.08em;
        color: #94A3B8;
        display: inline-block;
        margin-top: 5px;
    }
    .btn-view-calendar {
        background-color: #F1F5F9;
        color: #475569;
        border: 1px solid #E2E8F0;
        padding: 10px 16px;
        font-size: 13px;
        font-weight: 600;
        border-radius: 8px;
        text-decoration: none;
        display: inline-flex;
        align-items: center;
        gap: 6px;
    }
    .btn-view-calendar:hover { background-color: #E2E8F0; }

    /* 🌟 COMPOSANT GRILLE ALIGNÉ SUR L'IMAGE f8e686.png */
    .evt-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(560px, 1fr));
        gap: 20px;
    }
    .evt-card {
        background: #FFFFFF;
        border: 1px solid #E2E8F0;
        border-top: 4px solid #D97706; /* Bordure dorée haut de carte */
        border-radius: 16px;
        padding: 20px 24px;
        box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.02), 0 2px 4px -1px rgba(0, 0, 0, 0.01);
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        min-height: 180px;
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
    
    /* Badges de types */
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
        margin: 0 0 8px 0;
        font-size: 15.5px;
        line-height: 1.4;
        font-weight: 700;
    }
    .evt-card h3 a {
        color: #0F172A;
        text-decoration: none;
    }
    .evt-card h3 a:hover {
        color: #F57C00;
    }
    
    .evt-participants-count {
        font-size: 12px;
        color: #64748B;
        margin: 0 0 14px 0;
    }

    /* Footer de carte & Actions de participation */
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
    
    .footer-right-actions {
        display: flex;
        align-items: center;
        gap: 12px;
    }

    .status-badge {
        font-weight: 700;
    }
    .status-avenir { color: #059669; }
    .status-passe { color: #DC2626; }

    /* Boutons interactifs discrets intégrés au footer */
    .btn-action-link {
        background: none;
        border: none;
        font-size: 12.5px;
        font-weight: 700;
        cursor: pointer;
        padding: 4px 8px;
        border-radius: 4px;
        transition: all 0.2s;
    }
    .btn-join-link { color: #F57C00; }
    .btn-join-link:hover { background-color: #FFF3E0; }
    .btn-leave-link { color: #64748B; }
    .btn-leave-link:hover { background-color: #FFEEF0; color: #EF4444; }
</style>

<div class="evt-container">

    <c:if test="${not empty sessionScope.error}">
        <div style="margin-bottom: 20px; padding: 12px 16px; border-radius: 8px; font-size: 13px; background: #fee2e2; color: #991b1b;">
            ⚠️ ${sessionScope.error}
            <c:remove var="error" scope="session" />
        </div>
    </c:if>
    <c:if test="${not empty sessionScope.success}">
        <div style="margin-bottom: 20px; padding: 12px 16px; border-radius: 8px; font-size: 13px; background: #dcfce7; color: #166534;">
            ✅ ${sessionScope.success}
            <c:remove var="success" scope="session" />
        </div>
    </c:if>

    <div class="evt-header">
        <div class="evt-title">
            <h2>Événements &amp; Rencontres Scientifiques</h2>
            <span class="subtitle">AGENDA COMPLET DES ACTIVITÉS DU LIAS</span>
        </div>
        <a href="${pageContext.request.contextPath}/membre/evenements?action=calendrier" class="btn-view-calendar">
            📅 Vue Calendrier
        </a>
    </div>

    <!-- 🌟 GRILLE PRINCIPALE CONFORME -->
    <div class="evt-grid">
        <c:choose>
            <c:when test="${not empty evenements}">
                <c:forEach var="evt" items="${evenements}">
                    <c:set var="strKey" value="${evt.id.toString()}" />
                    <c:set var="dateStr" value="${evt.dateDebut.toString()}" />
                    
                    <div class="evt-card" data-full-date="${dateStr}">
                        <div>
                            <div class="evt-card-top">
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
                            
                            <p class="evt-participants-count">
                                👥 <strong><c:out value="${not empty compteurs[strKey] ? compteurs[strKey] : 0}"/></strong> inscrit(s)
                            </p>
                        </div>

                        <div class="evt-card-footer">
                            <span class="evt-lieu">Lieu : <strong><c:out value="${evt.lieu}"/></strong></span>
                            
                            <div class="footer-right-actions">
                                <!-- Statut géré dynamiquement par JS -->
                                <span class="status-badge" id="status-${evt.id}"></span>
                                
                                <!-- Formulaire d'action JSTL discret -->
                                <form action="${pageContext.request.contextPath}/membre/evenements" method="POST" style="margin: 0; display: inline;">
                                    <input type="hidden" name="id" value="${evt.id}" />
                                    <c:choose>
                                        <c:when test="${inscriptions[strKey]}">
                                            <input type="hidden" name="action" value="annuler" />
                                            <button type="submit" class="btn-action-link btn-leave-link" title="Se désinscrire de l'événement">
                                                (Inscrit ✕)
                                            </button>
                                        </c:when>
                                        <c:otherwise>
                                            <input type="hidden" name="action" value="participer" />
                                            <button type="submit" class="btn-action-link btn-join-link">
                                                S'inscrire →
                                            </button>
                                        </c:otherwise>
                                    </c:choose>
                                </form>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div style="grid-column: 1 / -1; background: #FFFFFF; border: 1px dashed #E2E8F0; border-radius: 12px; padding: 40px; text-align: center; color: #94A3B8; font-style: italic; font-size: 13px;">
                    Aucun événement n'est programmé au LIAS pour le moment.
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<script>
    // Calcul dynamique instantané des statuts temporels
    document.addEventListener("DOMContentLoaded", function() {
        const now = new Date();
        const cards = document.querySelectorAll('.evt-card');

        cards.forEach(card => {
            const fullDateStr = card.getAttribute('data-full-date');
            const statusBadge = card.querySelector('.status-badge');
            
            if (statusBadge && fullDateStr) {
                const eventDate = new Date(fullDateStr);
                if (eventDate >= now) {
                    statusBadge.textContent = "À venir";
                    statusBadge.className = "status-badge status-avenir";
                } else {
                    statusBadge.textContent = "Passé";
                    statusBadge.className = "status-badge status-passe";
                }
            }
        });
    });
</script>