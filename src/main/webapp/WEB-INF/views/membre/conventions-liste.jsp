<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="java.time.LocalDate" %>

<style>
    .conventions-container {
        max-width: 1200px;
        margin: 20px auto;
        padding: 0 20px;
        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
    }

    .page-header {
        margin-bottom: 24px;
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

    /* GRID DES CARTES */
    .conventions-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(340px, 1fr));
        gap: 20px;
    }

    .convention-card {
        background: #FFFFFF;
        border: 1px solid #E2E8F0;
        border-radius: 12px;
        padding: 20px;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        box-shadow: 0 1px 3px rgba(0, 0, 0, 0.02);
        transition: transform 0.2s, box-shadow 0.2s;
    }

    .convention-card:hover {
        transform: translateY(-2px);
        box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.05);
    }

    .card-top {
        margin-bottom: 16px;
    }

    .badge-status {
        display: inline-block;
        padding: 4px 8px;
        font-size: 11px;
        font-weight: 600;
        border-radius: 6px;
        margin-bottom: 12px;
    }

    .badge-active {
        background-color: #DCFCE7;
        color: #15803D;
    }

    .badge-expired {
        background-color: #FEE2E2;
        color: #B91C1C;
    }

    .convention-title {
        margin: 0 0 6px 0;
        font-size: 16px;
        font-weight: 600;
        color: #1E293B;
        line-height: 1.4;
    }

    .convention-partner {
        font-size: 13px;
        color: #4F46E5;
        font-weight: 500;
        margin-bottom: 12px;
    }

    .convention-desc {
        font-size: 13px;
        color: #64748B;
        line-height: 1.5;
        margin-bottom: 16px;
        display: -webkit-box;
        -webkit-line-clamp: 3;
        -webkit-box-orient: vertical;
        overflow: hidden;
    }

    .card-meta {
        border-top: 1px solid #F1F5F9;
        padding-top: 12px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        font-size: 12px;
        color: #94A3B8;
    }

    .date-box strong {
        color: #475569;
    }

    .btn-download {
        display: flex;
        align-items: center;
        gap: 6px;
        background-color: #0F172A;
        color: #FFFFFF;
        text-decoration: none;
        padding: 8px 14px;
        font-size: 12px;
        font-weight: 600;
        border-radius: 8px;
        transition: background 0.2s;
    }

    .btn-download:hover {
        background-color: #1E293B;
    }

    /* VUE VIDE */
    .empty-state {
        text-align: center;
        padding: 60px 20px;
        background: #F8FAFC;
        border: 1px dashed #CBD5E1;
        border-radius: 12px;
        color: #64748B;
    }
</style>

<div class="conventions-container">
    <div class="page-header">
        <h2>Conventions & Partenariats</h2>
        <p>Consultez les accords-cadres et partenariats scientifiques actifs du laboratoire (LIAS).</p>
    </div>

    <c:choose>
        <c:when test="${not empty conventions}">
            <div class="conventions-grid">
                <c:forEach var="conv" items="${conventions}">
                    
                    <%-- Détermination dynamique du statut de la convention --%>
                    <c:set var="isExpired" value="false" />
                    <c:if test="${not empty conv.dateFin && conv.dateFin.isBefore(LocalDate.now())}">
                        <c:set var="isExpired" value="true" />
                    </c:if>

                    <div class="convention-card">
                        <div class="card-top">
                            <c:choose>
                                <c:when test="${isExpired}">
                                    <span class="badge-status badge-expired">Expirée</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge-status badge-active">En cours</span>
                                </c:otherwise>
                            </c:choose>

                            <h3 class="convention-title"><c:out value="${conv.titre}"/></h3>
                            <div class="convention-partner">🤝 Partenaire : <c:out value="${conv.partenaire}"/></div>
                            <p class="convention-desc"><c:out value="${conv.description}"/></p>
                        </div>

                        <div>
                            <div class="card-meta">
                                <div class="date-box">
                                    <div>Du : <strong><c:out value="${conv.dateDebut}"/></strong></div>
                                    <div>Au : <strong><c:out value="${conv.dateFin != null ? conv.dateFin : 'Indéterminé'}"/></strong></div>
                                </div>
                                
                                <c:if test="${not empty conv.cheminFichier}">
    <a href="${pageContext.request.contextPath}/${conv.cheminFichier}" class="btn-download" target="_blank">
        📄 Ouvrir
    </a>
</c:if>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <div class="empty-state">
                <div style="font-size: 40px; margin-bottom: 12px;">📁</div>
                <h3>Aucune convention disponible</h3>
                <p>Il n'y a actuellement aucune convention active enregistrée pour consultation.</p>
            </div>
        </c:otherwise>
    </c:choose>
</div>