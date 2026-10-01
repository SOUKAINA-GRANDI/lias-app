<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<style>
    .notif-container {
        background: #ffffff;
        border-radius: 12px;
        padding: 24px;
        box-shadow: 0 4px 12px rgba(0,0,0,0.05);
        max-width: 850px;
        margin: 20px auto;
        font-family: system-ui, -apple-system, sans-serif;
    }
    .notif-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        border-bottom: 1px solid #edf2f7;
        padding-bottom: 16px;
        margin-bottom: 20px;
    }
    .notif-title {
        font-size: 22px;
        font-weight: 700;
        color: #1a202c;
        margin: 0;
    }
    .badge-unread {
        background: #e53e3e;
        color: white;
        font-size: 13px;
        font-weight: 600;
        padding: 4px 10px;
        border-radius: 20px;
        margin-left: 10px;
        vertical-align: middle;
    }
    .notif-item {
        display: flex;
        align-items: flex-start; /* Aligne en haut pour éviter les décalages avec les longs textes */
        justify-content: space-between;
        padding: 18px;
        border: 1px solid #e2e8f0;
        border-radius: 8px;
        margin-bottom: 12px;
        transition: all 0.2s ease;
        background: #ffffff;
    }
    .notif-item.unread {
        background-color: #f7fafc;
        border-left: 4px solid #3182ce;
    }
    .notif-left {
        display: flex;
        align-items: flex-start;
        gap: 15px;
        flex: 1;
    }
    .notif-icon {
        width: 40px;
        height: 40px;
        border-radius: 50%;
        background: #edf2f7;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 18px;
        flex-shrink: 0;
        margin-top: 2px;
    }
    .notif-body {
        display: flex;
        flex-direction: column;
        gap: 6px;
        flex: 1;
    }
    .notif-summary {
        font-weight: 700;
        color: #1a202c;
        font-size: 15px;
    }
    .notif-item.unread .notif-summary {
        color: #2b6cb0;
    }
    .notif-description {
        color: #4a5568;
        font-size: 14px;
        line-height: 1.5;
        margin: 0;
        white-space: pre-line; /* Conserve la mise en page et les sauts de ligne */
    }
    .notif-date {
        color: #a0aec0;
        font-size: 12px;
        margin-top: 2px;
    }
    .notif-right {
        margin-left: 15px;
        flex-shrink: 0;
    }
    .notif-btn {
        background: #3182ce;
        color: white;
        font-size: 13px;
        font-weight: 500;
        cursor: pointer;
        border: none;
        padding: 8px 14px;
        border-radius: 6px;
        white-space: nowrap;
        transition: background 0.2s;
    }
    .notif-btn:hover {
        background: #2b6cb0;
    }
    .empty-state {
        text-align: center;
        padding: 50px 20px;
        color: #718096;
    }
</style>

<div class="notif-container">
    <div class="notif-header">
        <h2 class="notif-title">
            Centre de Notifications
            <c:if test="${nbNonLues > 0}">
                <span class="badge-unread">${nbNonLues}</span>
            </c:if>
        </h2>
    </div>

    <c:choose>
        <c:when test="${empty notifications}">
            <div class="empty-state">
                <p style="font-size: 50px; margin-bottom: 15px; filter: grayscale(30%);">🔔</p>
                <p style="font-size: 16px;">Vous n'avez aucune notification pour le moment.</p>
            </div>
        </c:when>
        <c:otherwise>
            <c:forEach var="notif" items="${notifications}">
                <div class="notif-item ${notif.lu ? '' : 'unread'}">
                    <div class="notif-left">
                        <div class="notif-icon">
                            <c:choose>
                                <c:when test="${notif.type eq 'MESSAGE'}">💬</c:when>
                                <c:when test="${notif.type eq 'ALERTE'}">⚠️</c:when>
                                <c:otherwise>📢</c:otherwise>
                            </c:choose>
                        </div>
                        <div class="notif-body">
                            <!-- Titre de la notification -->
                            <span class="notif-summary"><c:out value="${notif.message}"/></span>
                            
                            <!-- Affichage du contenu complet et détaillé -->
                            <p class="notif-description">
                                <c:out value="${not empty notif.contenu ? notif.contenu : 'Aucun détail supplémentaire.'}"/>
                            </p>
                            
                            <!-- Date d'enregistrement -->
                            <span class="notif-date text-muted">
                                Enregistré le ${notif.dateCreation}
                            </span>
                        </div>
                    </div>
                    
                    <div class="notif-right">
                        <c:if test="${!notif.lu}">
                            <!-- Formulaire POST redirigeant vers l'action de mise à jour de l'espace membre -->
                            <form action="${pageContext.request.contextPath}/membre/notifications" method="POST" style="margin: 0;">
                                <input type="hidden" name="id" value="${notif.id}" />
                                <button type="submit" class="notif-btn">
                                    Marquer comme lu
                                </button>
                            </form>
                        </c:if>
                    </div>
                </div>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</div>