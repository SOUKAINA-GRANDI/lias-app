<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<style>
    /* Structure globale identique à la page membres */
    .events-container {
        background: #ffffff;
        border: 1px solid #E5E7EB;
        border-radius: 16px;
        padding: 24px;
        box-shadow: 0 1px 3px rgba(0,0,0,0.02);
        margin-top: 16px;
    }
    .events-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 24px;
        border-bottom: 1px solid #F1F5F9;
        padding-bottom: 16px;
    }
    .events-title-block h3 {
        margin: 0;
        font-size: 18px;
        color: #0F172A;
        font-weight: 700;
    }
    .events-title-block span {
        font-size: 12px;
        color: #64748B;
        font-weight: 600;
    }
    .btn-orange-action {
        background-color: #D97706;
        color: #FFFFFF;
        text-decoration: none;
        padding: 10px 18px;
        font-size: 13px;
        font-weight: 600;
        border-radius: 8px;
        display: inline-flex;
        align-items: center;
        gap: 8px;
        box-shadow: 0 2px 4px rgba(217, 119, 6, 0.15);
        border: none;
        cursor: pointer;
        transition: background-color 0.2s;
    }
    .btn-orange-action:hover {
        background-color: #B45309;
    }

    /* Table Design */
    .events-table {
        width: 100%;
        border-collapse: collapse;
        text-align: left;
    }
    .events-table th {
        background: #F8FAFC;
        color: #64748B;
        font-size: 11px;
        text-transform: uppercase;
        letter-spacing: 0.05em;
        padding: 14px 16px;
        font-weight: 700;
        border-bottom: 2px solid #E2E8F0;
    }
    .events-table td {
        padding: 16px;
        border-bottom: 1px solid #F1F5F9;
        color: #334155;
        font-size: 14px;
        vertical-align: middle;
    }
    .events-table tbody tr:hover {
        background-color: #F8FAFC;
    }

    /* Badge type d'événement */
    .type-badge {
        display: inline-block;
        padding: 4px 10px;
        border-radius: 6px;
        font-size: 11px;
        font-weight: 700;
        text-transform: uppercase;
        letter-spacing: 0.02em;
    }
    .badge-seminaire { background: #EFF6FF; color: #2563EB; }
    .badge-conference { background: #E6F4EA; color: #137333; }
    .badge-workshop { background: #F3E8FF; color: #6B21A8; } /* Magnifique violet pour vos Workshops */
    .badge-defaut { background: #F1F5F9; color: #475569; }

    /* Badge accès associés */
    .badge-restreint {
        display: inline-block;
        background: #FEF3C7;
        color: #92400E;
        padding: 2px 8px;
        border-radius: 6px;
        font-size: 11px;
        font-weight: 700;
        margin-top: 4px;
    }

    /* Actions */
    .action-buttons-group {
        display: flex;
        gap: 8px;
        justify-content: flex-start;
    }
    .btn-outline-edit {
        text-decoration: none;
        background: #ffffff;
        border: 1px solid #E2E8F0;
        color: #475569;
        padding: 6px 14px;
        border-radius: 6px;
        font-size: 12px;
        font-weight: 600;
        display: inline-flex;
        align-items: center;
        gap: 6px;
        transition: all 0.2s;
    }
    .btn-outline-edit:hover {
        background: #F8FAFC;
        border-color: #CBD5E1;
    }
    .btn-outline-archive {
        background: #ffffff;
        border: 1px solid #FCA5A5;
        color: #DC2626;
        padding: 6px 14px;
        border-radius: 6px;
        font-size: 12px;
        font-weight: 600;
        cursor: pointer;
        display: inline-flex;
        align-items: center;
        gap: 6px;
        transition: all 0.2s;
    }
    .btn-outline-archive:hover {
        background: #FEE2E2;
    }
</style>

<div class="events-container">
    <div class="events-header">
        <div class="events-title-block">
            <h3>Gestion des Événements</h3>
            <span>${evenements.size()} événements au calendrier</span>
        </div>
        <a href="${pageContext.request.contextPath}/directeur/evenements?action=nouveau" class="btn-orange-action">
            <i class="fa-solid fa-plus"></i> Ajouter un Événement
        </a>
    </div>

    <c:if test="${not empty sessionScope.error}">
        <div style="margin-bottom: 20px; padding: 12px; background: #fee2e2; color: #991b1b; border-radius: 6px; font-size: 14px; font-weight: 500;">
            ⚠️ ${sessionScope.error}
            <c:remove var="error" scope="session"/>
        </div>
    </c:if>

    <table class="events-table">
        <thead>
            <tr>
                <th>Titre / Description</th>
                <th>Type</th>
                <th>Lieu</th>
                <th>Date Début</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${not empty evenements}">
                    <c:forEach var="e" items="${evenements}">
                        <tr>
                            <td style="max-width: 300px;">
                                <div style="font-weight: 600; color: #1E293B; margin-bottom: 2px;">${e.titre}</div>
                                <div style="font-size: 12px; color: #64748B; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">${e.description}</div>
                                <c:if test="${not empty organisateursParEvenement[e.id]}">
                                    <div style="font-size:12px; color:#0369A1; margin-top:3px;">👥 Organisateurs : <c:out value="${organisateursParEvenement[e.id]}"/>
                                    </div>
                                </c:if>
                                <c:if test="${!e.ouvertAuxAssocies}">
                                    <span class="badge-restreint">🔒 Réservé aux permanents</span>
                                </c:if>
                            </td>
                            <td>
                                <span class="type-badge ${e.type == 'SEMINAIRE' ? 'badge-seminaire' : (e.type == 'CONFERENCE' ? 'badge-conference' : (e.type == 'WORKSHOP' ? 'badge-workshop' : 'badge-defaut'))}">
                                    ${e.type}
                                </span>
                            </td>
                            <td>
                                <span style="font-weight: 500; color: #475569; display: inline-flex; align-items: center; gap: 6px;">
                                    <i class="fa-solid fa-location-dot" style="color: #EA4335;"></i> ${e.lieu}
                                </span>
                            </td>
                            <td>
                                <span style="font-size: 13px; color: #334155; font-weight: 600;">
                                    ${e.dateDebut.toLocalDate()} 
                                    <small style="color: #94A3B8; font-weight: 500; margin-left: 4px;">(${e.dateDebut.toLocalTime()})</small>
                                </span>
                            </td>
                            <td>
                                <div class="action-buttons-group">
                                    <a href="${pageContext.request.contextPath}/directeur/evenements?action=edit&id=${e.id}" class="btn-outline-edit">
                                        <i class="fa-solid fa-pen-to-square"></i> Modifier
                                    </a>
                                    
                                    <form action="${pageContext.request.contextPath}/directeur/evenements" method="POST" style="margin: 0;" onsubmit="return confirm('Archiver cet événement ?');">
                                        <input type="hidden" name="action" value="archiver" />
                                        <input type="hidden" name="id" value="${e.id}" />
                                        <button type="submit" class="btn-outline-archive">
                                            <i class="fa-solid fa-box-archive"></i> Archiver
                                        </button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="5" style="text-align: center; padding: 40px; color: #64748B;">Aucun événement planifié pour le moment.</td>
                    </tr>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>
</div>