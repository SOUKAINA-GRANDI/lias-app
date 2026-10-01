<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%-- 1. Entête de la page d'audit --%>
<div class="ph">
    <div>
        <div class="ph-eye">Sécurité & Traçabilité</div>
        <h1 class="ph-title">Journal d'Audit</h1>
        <p class="ph-sub">Historique complet des actions effectuées par les utilisateurs sur la plateforme LIAS.</p>
    </div>
</div>

<%-- 2. Zone des Filtres de recherche --%>
<div class="tbl-card anim-1" style="padding: 20px; margin-bottom: 24px;">
    <form method="get" action="${pageContext.request.contextPath}/admin/audit" style="display: grid; grid-template-columns: 2fr 1fr 1fr auto; gap: 16px; align-items: end;">
        
        <div style="display: flex; flex-direction: column;">
            <label class="bold" style="font-size: 13px; margin-bottom: 4px; color: #1e293b;">Filtrer par Email</label>
            <input type="text" name="email" value="${param.email}" class="search-box" style="width: 100%; border: 1px solid #e2e8f0; border-radius: 8px; padding: 10px;" placeholder="Ex: admin@lias.ma">
        </div>

        <div style="display: flex; flex-direction: column;">
            <label class="bold" style="font-size: 13px; margin-bottom: 4px; color: #1e293b;">Par Action</label>
            <input type="text" name="action" value="${param.action}" class="search-box" style="width: 100%; border: 1px solid #e2e8f0; border-radius: 8px; padding: 10px;" placeholder="Ex: activer, resetPassword">
        </div>

        <div style="display: flex; flex-direction: column;">
            <label class="bold" style="font-size: 13px; margin-bottom: 4px; color: #1e293b;">Par Élément (Entité)</label>
            <input type="text" name="entity" value="${param.entity}" class="search-box" style="width: 100%; border: 1px solid #e2e8f0; border-radius: 8px; padding: 10px;" placeholder="Ex: Utilisateur">
        </div>

        <div>
            <button type="submit" class="btn" style="background: #2563eb; color: white; border: none; padding: 11px 20px; border-radius: 8px; font-weight: bold; cursor: pointer;">
                Filtrer
            </button>
        </div>
    </form>
</div>

<%-- 3. Tableau d'affichage des Logs --%>
<div class="tbl-card anim-2" style="padding: 20px; background: white; border-radius: 12px; box-shadow: 0 1px 3px rgba(0,0,0,0.1);">
    <div style="overflow-x: auto;">
        <table class="tbl" style="width: 100%; border-collapse: collapse; text-align: left;">
            <thead>
                <tr style="border-bottom: 2px solid #edf2f7; color: #4a5568;">
                    <th style="padding: 12px; width: 80px;"># ID</th>
                    <th style="padding: 12px;">Utilisateur</th>
                    <th style="padding: 12px;">Action effectuée</th>
                    <th style="padding: 12px;">Composant ciblé</th>
                    <th style="padding: 12px;">ID Cible</th>
                    <th style="padding: 12px;">Date & Heure</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${empty logs}">
                        <tr>
                            <td colspan="6" style="text-align: center; padding: 40px; color: #64748b;">
                                Aucun enregistrement trouvé dans le journal d'audit.
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach items="${logs}" var="log">
                            <tr style="border-bottom: 1px solid #edf2f7;">
                                <td style="padding: 12px; font-weight: bold; color: #64748b;">#${log.id}</td>
                                <td style="padding: 12px;">
                                    <span style="font-weight: bold; color: #1e293b;">${log.emailUtilisateur}</span>
                                    <div style="font-size: 11px; color: #94a3b8;">UID: ${log.utilisateurId}</div>
                                </td>
                                <td style="padding: 12px;">
                                    <span style="background: #f1f5f9; color: #475569; padding: 4px 8px; border-radius: 6px; font-size: 12px; font-weight: 500;">
                                        ${log.action}
                                    </span>
                                </td>
                                <td style="padding: 12px; font-weight: bold;">${log.entityName}</td>
                                <td style="padding: 12px;">
                                    <c:choose>
                                        <c:when test="${not empty log.entityId && log.entityId != 0}">
                                            <span style="font-family: monospace; background: #f8fafc; padding: 2px 6px; border: 1px solid #e2e8f0; border-radius: 4px;">${log.entityId}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span style="color: #cbd5e1;">—</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td style="padding: 12px; color: #475569; font-size: 13px;">
                                   <c:choose>
        <c:when test="${log.dateAction.toString().contains('T')}">
            <%-- Sépare la date et l'heure au niveau du 'T' --%>
            <c:set var="parts" value="${log.dateAction.toString().split('T')}" />
            <span class="bold" style="color: var(--dark);">${parts[0]}</span> à ${parts[1].substring(0, 5)}
        </c:when>
        <c:otherwise>
            ${log.dateAction}
        </c:otherwise>
    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>

    <%-- 4. Bloc de Pagination --%>
    <c:if test="${totalPages > 1}">
        <div style="display: flex; justify-content: space-between; align-items: center; padding-top: 16px; margin-top: 16px; border-top: 1px solid #f1f5f9;">
            <div style="font-size: 14px; color: #64748b;">
                Page <strong>${currentPage}</strong> sur <strong>${totalPages}</strong>
            </div>
            <div style="display: flex; gap: 8px;">
                <c:if test="${currentPage > 1}">
                    <a href="${pageContext.request.contextPath}/admin/audit?page=${currentPage - 1}&email=${param.email}&action=${param.action}&entity=${param.entity}" 
                       style="background: #f1f5f9; color: #1e293b; text-decoration: none; padding: 8px 16px; border-radius: 6px; font-size: 13px; font-weight: bold;">
                       &larr; Précédent
                    </a>
                </c:if>
                <c:if test="${currentPage < totalPages}">
                    <a href="${pageContext.request.contextPath}/admin/audit?page=${currentPage + 1}&email=${param.email}&action=${param.action}&entity=${param.entity}" 
                       style="background: #f1f5f9; color: #1e293b; text-decoration: none; padding: 8px 16px; border-radius: 6px; font-size: 13px; font-weight: bold;">
                       Suivant &rarr;
                    </a>
                </c:if>
            </div>
        </div>
    </c:if>
</div>