<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<style>
    .detail-container {
        width: 100%;
        max-width: 900px;
        margin: 0 auto;
        padding: 20px;
        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Arial, sans-serif;
    }
    .btn-back {
        display: inline-flex;
        align-items: center;
        gap: 6px;
        color: #64748B;
        text-decoration: none;
        font-size: 13px;
        font-weight: 600;
        margin-bottom: 24px;
        transition: color 0.2s;
    }
    .btn-back:hover { color: #0F172A; }
    
    .detail-card {
        background: #FFFFFF;
        border: 1px solid #E2E8F0;
        border-radius: 16px;
        padding: 32px;
    }
    .detail-badge-type {
        font-size: 10px;
        font-weight: 700;
        text-transform: uppercase;
        letter-spacing: 0.06em;
        color: #B45309;
        background-color: #FEF3C7;
        padding: 4px 10px;
        border-radius: 6px;
        display: inline-block;
        margin-bottom: 16px;
    }
    .detail-card h1 {
        margin: 0 0 20px 0;
        font-size: 24px;
        color: #0F172A;
        font-weight: 800;
    }
    .info-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
        gap: 16px;
        margin-bottom: 28px;
        padding-bottom: 24px;
        border-bottom: 1px solid #F1F5F9;
    }
    .info-item {
        display: flex;
        align-items: center;
        gap: 12px;
        background: #F8FAFC;
        padding: 14px 18px;
        border-radius: 10px;
        border: 1px solid #F1F5F9;
    }
    .info-icon { font-size: 20px; }
    .info-label { font-size: 11px; color: #94A3B8; text-transform: uppercase; font-weight: 600; margin: 0; }
    .info-value { font-size: 13.5px; color: #334155; font-weight: 700; margin: 2px 0 0 0; }
    .description-box h3 { font-size: 15px; color: #1E293B; font-weight: 700; margin: 0 0 10px 0; }
    .description-text { font-size: 14px; color: #475569; line-height: 1.6; margin: 0; }
</style>

<div class="detail-container">

    <a href="${pageContext.request.contextPath}/membre/evenements?action=liste" class="btn-back">
        ⬅️ Retour à la liste des événements
    </a>

    <!-- 🛡️ PROTECTION ANTI-ERREUR 500 : On vérifie si l'objet existe avant de l'afficher -->
    <c:choose>
        <c:when test="${not empty evenement}">
            <div class="detail-card">
                <span class="detail-badge-type">
                    <c:out value="${evenement.type}"/>
                </span>
                
                <h1><c:out value="${evenement.titre}"/></h1>

                <div class="info-grid">
                    <div class="info-item">
                        <span class="info-icon">📅</span>
                        <div>
                            <p class="info-label">Date de l'événement</p>
                            <!-- Sécurité toString() automatique pour le format de date -->
                            <p class="info-value"><c:out value="${evenement.dateDebut.toString()}"/></p>
                        </div>
                    </div>
                    
                    <div class="info-item">
                        <span class="info-icon">📍</span>
                        <div>
                            <p class="info-label">Lieu / Salle</p>
                            <p class="info-value"><c:out value="${evenement.lieu}"/></p>
                        </div>
                    </div>
                </div>

                <div class="description-box">
                    <h3>Description &amp; Objectifs</h3>
                    <p class="description-text">
                        <c:out value="${not empty evenement.description ? evenement.description : 'Aucune description additionnelle n\'a été fournie pour cet événement.'}"/>
                    </p>
                </div>
            </div>
        </c:when>
        
        <c:otherwise>
            <!-- S'affiche proprement au lieu de faire planter le serveur entier -->
            <div style="background: #FFFBEB; border: 1px solid #FCD34D; border-radius: 12px; padding: 24px; text-align: center; color: #B45309; font-size: 14px; font-weight: 600;">
                ⚠️ Impossible de charger les détails : cet événement n'existe pas ou a été supprimé.
            </div>
        </c:otherwise>
    </c:choose>
</div>