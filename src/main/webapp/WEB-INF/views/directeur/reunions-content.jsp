<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:if test="${not empty sessionScope.error}">
    <div style="margin: 0 10px 16px 10px; padding: 12px; background: #FEE2E2; color: #991B1B; border-radius: 6px; font-size: 14px; font-weight: 500;">
        ⚠️ <c:out value="${sessionScope.error}"/>
    </div>
    <c:remove var="error" scope="session"/>
</c:if>

<div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; padding: 0 10px;">
    <div>
        <h2 style="margin: 0; font-size: 22px; color: #0F172A; font-weight: 700;">Gestion des Réunions &amp; PV</h2>
        <span style="font-size: 11px; font-weight: 700; text-transform: uppercase; color: #94A3B8; letter-spacing: 0.05em;">Suivi des conseils de laboratoire</span>
    </div>
    <a href="${pageContext.request.contextPath}/directeur/reunions?action=nouveau" style="background-color: #D97706; color: white; border: none; padding: 10px 18px; font-size: 13px; font-weight: 600; border-radius: 8px; text-decoration: none; display: inline-block;">
        + Planifier une Réunion
    </a>
</div>

<div style="padding: 10px;">
    <c:choose>
        <c:when test="${not empty reunions}">
            <c:forEach var="r" items="${reunions}">
                <div style="background-color: #FFFFFF; border: 1px solid #E2E8F0; border-radius: 16px; padding: 24px; margin-bottom: 16px; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 1px 3px rgba(0,0,0,0.02);">
                    <div>
                        <c:choose>
                            <c:when test="${fn:startsWith(r.pvPath, 'uploads/')}">
                                <span style="background-color: #ECFDF5; color: #065F46; font-size: 10px; font-weight: 700; padding: 4px 8px; border-radius: 6px; text-transform: uppercase;">PV Déposé</span>
                            </c:when>
                            <c:otherwise>
                                <span style="background-color: #FEF3C7; color: #92400E; font-size: 10px; font-weight: 700; padding: 4px 8px; border-radius: 6px; text-transform: uppercase;">En attente de PV</span>
                            </c:otherwise>
                        </c:choose>
                        
                        <h3 style="margin: 10px 0 6px 0; font-size: 16px; color: #0F172A; font-weight: 700;"><c:out value="${r.titre}"/></h3>
                        <p style="margin: 0 0 8px 0; font-size: 13px; color: #475569;"><c:out value="${r.ordreDuJour}"/></p>
                        <div style="font-size: 11px; color: #64748B; font-weight: 600;">📅 Date : <c:out value="${r.dateReunion}"/></div>
                    </div>
                    
                    <div style="display: flex; gap: 8px;">
                        <c:if test="${fn:startsWith(r.pvPath, 'uploads/')}">
                            <a href="${pageContext.request.contextPath}/${r.pvPath}" target="_blank" style="background-color: #ECFDF5; color: #065F46; border: 1px solid #A7F3D0; padding: 8px 14px; font-size: 12px; font-weight: 600; border-radius: 6px; text-decoration: none; display: inline-block;">
                                📄 Voir le PV
                            </a>
                        </c:if>
                        <a href="${pageContext.request.contextPath}/directeur/reunions?action=edit&id=${r.id}" style="background-color: #F8FAFC; color: #334155; border: 1px solid #E2E8F0; padding: 8px 14px; font-size: 12px; font-weight: 600; border-radius: 6px; text-decoration: none; display: inline-block;">
                            <c:choose>
                                <c:when test="${fn:startsWith(r.pvPath, 'uploads/')}">Modifier / Remplacer le PV</c:when>
                                <c:otherwise>Déposer le PV</c:otherwise>
                            </c:choose>
                        </a>
                        <form action="${pageContext.request.contextPath}/directeur/reunions" method="POST" style="margin:0;" onsubmit="return confirm('Archiver cette réunion ?');">
                            <input type="hidden" name="action" value="archiver">
                            <input type="hidden" name="id" value="${r.id}">
                            <button type="submit" style="background-color: #FFF5F5; color: #E53E3E; border: 1px solid #FED7D7; padding: 8px 14px; font-size: 12px; font-weight: 600; border-radius: 6px; cursor: pointer;">
                                Archiver
                            </button>
                        </form>
                    </div>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <div style="background-color: #FFFFFF; border: 1px dashed #E2E8F0; border-radius: 16px; padding: 40px; text-align: center; color: #64748B;">
                Aucune réunion planifiée pour le moment.
            </div>
        </c:otherwise>
    </c:choose>
</div>