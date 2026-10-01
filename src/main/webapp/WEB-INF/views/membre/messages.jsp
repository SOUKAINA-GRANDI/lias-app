<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<style>
    .messaging-container {
        display: grid;
        grid-template-columns: 320px 1fr;
        background-color: #FFFFFF;
        border: 1px solid #E2E8F0;
        border-radius: 16px;
        height: calc(100vh - 140px);
        max-width: 1250px;
        margin: 20px auto;
        box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.02);
        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
        overflow: hidden;
    }

    /* SIDEBAR */
    .msg-sidebar {
        background-color: #FAFAFA;
        border-right: 1px solid #E2E8F0;
        display: flex;
        flex-direction: column;
    }
    .sidebar-header {
        padding: 20px;
        border-bottom: 1px solid #F1F5F9;
    }
    .sidebar-header h3 {
        margin: 0 0 12px 0;
        font-size: 16px;
        font-weight: 700;
        color: #0F172A;
    }
    .channel-tabs {
        display: flex;
        gap: 6px;
        background: #F1F5F9;
        padding: 4px;
        border-radius: 8px;
    }
    .tab-btn {
        flex: 1;
        text-align: center;
        padding: 6px 4px;
        font-size: 12px;
        font-weight: 600;
        color: #64748B;
        text-decoration: none;
        border-radius: 6px;
    }
    .tab-btn.active {
        background: #FFFFFF;
        color: #0F172A;
        box-shadow: 0 1px 2px rgba(0,0,0,0.05);
    }

    .conversations-list {
        flex: 1;
        overflow-y: auto;
        padding: 10px;
    }
    
    /* ZONE VIDE DROITE */
    .chat-main-empty {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        background: #F8FAFC;
        color: #94A3B8;
        text-align: center;
        padding: 40px;
    }
    .empty-icon {
        font-size: 48px;
        margin-bottom: 16px;
    }
    .chat-main-empty h3 {
        margin: 0 0 8px 0;
        color: #334155;
        font-size: 16px;
        font-weight: 600;
    }
    .chat-main-empty p {
        margin: 0;
        font-size: 13px;
        max-width: 320px;
        line-height: 1.5;
    }
</style>

<div class="messaging-container">
    
    <!-- Sidebar -->
    <div class="msg-sidebar">
        <div class="sidebar-header">
            <h3>Messagerie Interne</h3>
            <div class="channel-tabs">
                <a href="${pageContext.request.contextPath}/messages?action=prive" class="tab-btn active">Privés</a>
                <a href="${pageContext.request.contextPath}/messages?action=equipe" class="tab-btn">Équipe</a>
                <a href="${pageContext.request.contextPath}/messages?action=evenement&id=1" class="tab-btn">Événements</a>
            </div>
        </div>

        <div class="conversations-list">
            <c:forEach items="${conversations}" var="conv">
                <c:set var="expediteurId" value="${sessionScope.user.id}" />
                <c:set var="autreMembreId" value="${conv.user1Id == expediteurId ? conv.user2Id : conv.user1Id}" />
                <c:set var="interlocuteur" value="${membresMap[autreMembreId]}" />

                <c:choose>
                    <c:when test="${not empty interlocuteur}">
                        <a href="${pageContext.request.contextPath}/messages?action=prive&id=${interlocuteur.utilisateurId}" 
                           class="chat-item ${param.id == interlocuteur.utilisateurId ? 'active' : ''}" 
                           style="display: flex; align-items: center; gap: 12px; padding: 12px; border-radius: 10px; text-decoration: none; color: inherit; margin-bottom: 8px;">
                            
                            <div class="chat-avatar" style="width: 38px; height: 38px; border-radius: 50%; background-color: #FEF3C7; color: #D97706; display: flex; align-items: center; justify-content: center; font-weight: 700; text-transform: uppercase;">
                                <c:out value="${interlocuteur.prenom.substring(0,1)}"/><c:out value="${interlocuteur.nom.substring(0,1)}"/>
                            </div>
                            
                            <div class="chat-details" style="flex: 1; min-width: 0;">
                                <h4 style="margin: 0; font-size: 13.5px; font-weight: 600; color: #0F172A;"><c:out value="${interlocuteur.prenom} ${interlocuteur.nom}"/></h4>
                                <p style="margin: 2px 0 0 0; font-size: 12px; color: #64748B; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;"><c:out value="${interlocuteur.email}"/></p>
                            </div>
                        </a>
                    </c:when>
                    
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/messages?action=prive&id=${autreMembreId}" 
                           class="chat-item" 
                           style="display: flex; align-items: center; gap: 12px; padding: 12px; border-radius: 10px; text-decoration: none; color: inherit; margin-bottom: 8px;">
                            
                            <div class="chat-avatar" style="width: 38px; height: 38px; border-radius: 50%; background-color: #F1F5F9; color: #64748B; display: flex; align-items: center; justify-content: center; font-weight: 700;">?</div>
                            
                            <div class="chat-details" style="flex: 1; min-width: 0;">
                                <h4 style="margin: 0; font-size: 13.5px; font-weight: 600; color: #475569;">Utilisateur N° <c:out value="${autreMembreId}"/></h4>
                                <p style="margin: 2px 0 0 0; font-size: 11px; color: #94A3B8;">Ouvrir la discussion</p>
                            </div>
                        </a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
    </div>

    <!-- Zone droite : Vide au départ -->
    <div class="chat-main-empty">
        <div class="empty-icon">💬</div>
        <h3>Vos discussions professionnelles</h3>
        <p>Sélectionnez un contact à gauche pour afficher l'historique ou lancer une nouvelle discussion sécurisée au LIAS.</p>
    </div>

</div>