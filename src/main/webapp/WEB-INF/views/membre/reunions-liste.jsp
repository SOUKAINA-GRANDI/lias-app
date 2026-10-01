<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<style>
    .reunions-container {
        max-width: 1000px;
        margin: 20px auto;
        padding: 0 20px;
        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
    }

    .page-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 24px;
        flex-wrap: wrap;
        gap: 16px;
    }

    .page-header h2 {
        margin: 0;
        font-size: 22px;
        color: #0F172A;
        font-weight: 700;
    }

    .page-header p {
        margin: 4px 0 0 0;
        color: #64748B;
        font-size: 14px;
    }

    /* BARRE DE RECHERCHE */
    .search-form {
        display: flex;
        gap: 8px;
    }

    .search-input {
        padding: 8px 14px;
        border: 1px solid #CBD5E1;
        border-radius: 8px;
        font-size: 13.5px;
        outline: none;
        width: 240px;
    }

    .search-input:focus {
        border-color: #4F46E5;
    }

    .btn-search {
        background-color: #4F46E5;
        color: white;
        border: none;
        padding: 8px 16px;
        font-size: 13px;
        font-weight: 600;
        border-radius: 8px;
        cursor: pointer;
    }

    /* TIMELINE STRUCTURE */
    .timeline {
        position: relative;
        border-left: 2px solid #E2E8F0;
        padding-left: 32px;
        margin-left: 20px;
    }

    .timeline-item {
        position: relative;
        margin-bottom: 32px;
    }

    .timeline-dot {
        position: absolute;
        left: -42px;
        top: 4px;
        width: 18px;
        height: 18px;
        border-radius: 50%;
        background-color: #FFFFFF;
        border: 4px solid #4F46E5;
    }

    .reunion-card {
        background: #FFFFFF;
        border: 1px solid #E2E8F0;
        border-radius: 12px;
        padding: 20px;
        box-shadow: 0 1px 3px rgba(0,0,0,0.02);
    }

    .reunion-meta {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 10px;
    }

    .reunion-date {
        font-size: 13px;
        font-weight: 600;
        color: #64748B;
        background: #F1F5F9;
        padding: 4px 10px;
        border-radius: 6px;
    }

    .badge-pv {
        padding: 4px 8px;
        font-size: 11px;
        font-weight: 600;
        border-radius: 6px;
    }

    .pv-available { background-color: #DCFCE7; color: #15803D; }
    .pv-missing { background-color: #F1F5F9; color: #64748B; }

    .reunion-title {
        margin: 0 0 10px 0;
        font-size: 17px;
        color: #0F172A;
        font-weight: 600;
    }

    .reunion-section-title {
        font-size: 12px;
        text-transform: uppercase;
        letter-spacing: 0.05em;
        color: #94A3B8;
        margin-bottom: 4px;
        font-weight: 700;
    }

    .reunion-odj {
        font-size: 13.5px;
        color: #334155;
        line-height: 1.5;
        background: #F8FAFC;
        padding: 12px;
        border-radius: 8px;
        border-left: 3px solid #CBD5E1;
        margin-bottom: 16px;
        white-space: pre-line;
    }

    .card-footer {
        display: flex;
        justify-content: flex-end;
        border-top: 1px solid #F1F5F9;
        padding-top: 14px;
    }

    .btn-download-pv {
        display: inline-flex;
        align-items: center;
        gap: 6px;
        background-color: #0F172A;
        color: white;
        text-decoration: none;
        padding: 8px 16px;
        font-size: 12.5px;
        font-weight: 600;
        border-radius: 8px;
        transition: background 0.2s;
    }

    .btn-download-pv:hover {
        background-color: #1E293B;
    }

    .empty-state {
        text-align: center;
        padding: 50px 20px;
        background: #F8FAFC;
        border: 1px dashed #CBD5E1;
        border-radius: 12px;
        color: #64748B;
    }
</style>

<div class="reunions-container">
    <div class="page-header">
        <div>
            <h2>Réunions de Laboratoire & PV</h2>
            <p>Consultez les ordres du jour et téléchargez les procès-verbaux des séances du LIAS.</p>
        </div>
        
        <form action="${pageContext.request.contextPath}/membre/reunions" method="GET" class="search-form">
            <input type="text" name="search" class="search-input" placeholder="Rechercher une réunion..." value="${searchKeyword}" autocomplete="off">
            <button type="submit" class="btn-search">Filtrer</button>
            <c:if test="${not empty searchKeyword}">
                <a href="${pageContext.request.contextPath}/membre/reunions" style="padding: 8px; font-size: 13px; color: #EF4444; text-decoration: none;">Réinitialiser</a>
            </c:if>
        </form>
    </div>

    <c:choose>
        <c:when test="${not empty reunions}">
            <div class="timeline">
                <c:forEach var="reunion" items="${reunions}">
                    <div class="timeline-item">
                        <div class="timeline-dot"></div>
                        
                        <div class="reunion-card">
                            <div class="reunion-meta">
                                <span class="reunion-date">📅 <c:out value="${reunion.dateReunion}"/></span>
                                
                                <c:choose>
                                    <c:when test="${fn:startsWith(reunion.pvPath, 'uploads/')}">
                                        <span class="badge-pv pv-available">✓ PV Disponible</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge-pv pv-missing">⏳ PV en attente de dépôt</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <h3 class="reunion-title"><c:out value="${reunion.titre}"/></h3>
                            
                            <div class="reunion-section-title">Ordre du jour</div>
                            <div class="reunion-odj"><c:out value="${reunion.ordreDuJour}"/></div>

                            <%-- Pied de carte : Téléchargement du fichier PV lié s'il existe --%>
                            <c:if test="${fn:startsWith(reunion.pvPath, 'uploads/')}">
                                <div class="card-footer">
                                    <%-- Utilise l'infrastructure de dossiers d'uploads que nous avons configurée pour les CV / Conventions --%>
                                    <a href="${pageContext.request.contextPath}/${reunion.pvPath}" class="btn-download-pv" target="_blank">
                                        📥 Télécharger le Procès-Verbal
                                    </a>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:when>
        
        <c:otherwise>
            <div class="empty-state">
                <div style="font-size: 40px; margin-bottom: 12px;">📆</div>
                <h3>Aucune réunion trouvée</h3>
                <p>Aucun compte-rendu ou planification ne correspond aux critères actuels.</p>
            </div>
        </c:otherwise>
    </c:choose>
</div>