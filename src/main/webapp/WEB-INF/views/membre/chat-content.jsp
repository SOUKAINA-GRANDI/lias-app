<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%-- Détection robuste de l'espace de travail actuel --%>
<c:set var="browserURI" value="${not empty requestScope['javax.servlet.forward.request_uri'] ? requestScope['javax.servlet.forward.request_uri'] : pageContext.request.requestURI}" />
<c:set var="isDir" value="${fn:contains(browserURI, '/directeur')}" />
<c:set var="chatRoot" value="${isDir ? '/directeur/messages' : '/messages'}" />
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
        box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.02), 0 2px 4px -1px rgba(0, 0, 0, 0.01);
        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
        overflow: hidden;
    }

    /* SIDEBAR COMPLÈTE */
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
    
    /* Onglets de types de canaux */
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
        transition: all 0.2s;
    }
    .tab-btn.active {
        background: #FFFFFF;
        color: #0F172A;
        box-shadow: 0 1px 2px rgba(0,0,0,0.05);
    }

    /* Liste des conversations */
    .conversations-list {
        flex: 1;
        overflow-y: auto;
        padding: 10px;
    }
    .conv-item {
        display: flex;
        align-items: center;
        gap: 12px;
        padding: 12px;
        border-radius: 10px;
        text-decoration: none;
        color: #334155;
        margin-bottom: 4px;
        transition: background 0.2s;
    }
    .conv-item:hover { background-color: #F1F5F9; }
    .conv-item.active { background-color: #E2E8F0; color: #0F172A; font-weight: 600; }
    
    .avatar-circle {
        width: 38px;
        height: 38px;
        border-radius: 50%;
        background-color: #FEF3C7;
        color: #D97706;
        display: flex;
        align-items: center;
        justify-content: center;
        font-weight: 700;
        font-size: 14px;
        flex-shrink: 0;
    }
    .conv-details {
        flex: 1;
        min-width: 0;
    }
    .conv-name {
        font-size: 13.5px;
        margin: 0 0 2px 0;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }
    .conv-sub {
        font-size: 12px;
        color: #94A3B8;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }

    /* ZONE DE DISCUSSION DE DROITE */
    .chat-main {
        display: flex;
        flex-direction: column;
        background: #FFFFFF;
    }
    .chat-header {
        padding: 18px 24px;
        border-bottom: 1px solid #F1F5F9;
        background-color: #FFFFFF;
    }
    .chat-header h2 { margin: 0; font-size: 15px; color: #0F172A; font-weight: 700; }
    .chat-header p { margin: 2px 0 0 0; font-size: 12px; color: #94A3B8; }

    .chat-body {
        flex: 1;
        padding: 24px;
        overflow-y: auto;
        background-color: #F8FAFC;
        display: flex;
        flex-direction: column;
        gap: 14px;
    }
    
    /* Bulles de message */
    .msg-row { display: flex; width: 100%; }
    .msg-row.received { justify-content: flex-start; }
    .msg-row.sent { justify-content: flex-end; }

    .msg-bubble {
        max-width: 65%;
        padding: 10px 14px;
        border-radius: 12px;
        font-size: 13.5px;
        line-height: 1.5;
    }
    .received .msg-bubble {
        background-color: #FFFFFF;
        color: #0F172A;
        border: 1px solid #E2E8F0;
        border-top-left-radius: 2px;
    }
    .sent .msg-bubble {
        background-color: #0F172A;
        color: #FFFFFF;
        border-top-right-radius: 2px;
    }
    .msg-meta {
        font-size: 10px;
        margin-top: 4px;
        display: block;
        color: #94A3B8;
    }
    .sent .msg-meta { color: #CBD5E1; text-align: right; }

    .chat-footer {
        padding: 16px 24px;
        border-top: 1px solid #F1F5F9;
        background-color: #FFFFFF;
    }
    .chat-form { display: flex; gap: 12px; }
    .chat-input {
        flex: 1;
        background-color: #F8FAFC;
        border: 1px solid #E2E8F0;
        border-radius: 8px;
        padding: 12px 16px;
        font-size: 13.5px;
        outline: none;
    }
    .chat-input:focus { border-color: #D97706; }
    .btn-send {
        background-color: #D97706;
        color: white;
        border: none;
        padding: 0 20px;
        font-size: 13px;
        font-weight: 600;
        border-radius: 8px;
        cursor: pointer;
    }
    .btn-send:hover { background-color: #B45309; }

    .empty-chat-state {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        height: 100%;
        color: #94A3B8;
    }
</style>

<div class="messaging-container">
    
    <!-- BARRE LATÉRALE -->
    <div class="msg-sidebar">
        <div class="sidebar-header">
            <h3>Messagerie Interne</h3>
            <div class="channel-tabs">
                <a href="${pageContext.request.contextPath}${chatRoot}?action=prive" class="tab-btn active">Privés</a>
                <a href="${pageContext.request.contextPath}${chatRoot}?action=equipe" class="tab-btn">Équipe</a>
                <a href="${pageContext.request.contextPath}${chatRoot}?action=evenement&id=1" class="tab-btn">Événements</a>
            </div>
        </div>

        <div class="conversations-list">
            <c:forEach var="conv" items="${conversations}">
                <c:set var="autreUserId" value="${conv.user1Id == sessionScope.user.id ? conv.user2Id : conv.user1Id}" />
                <c:set var="compagnon" value="${membresMap[autreUserId]}" />

                <a class="conv-item ${conv.id == conversationId ? 'active' : ''}"
                   href="${pageContext.request.contextPath}${chatRoot}?action=prive&id=${autreUserId}">
                    
                    <div class="avatar-circle">
                        ${not empty compagnon ? fn:substring(compagnon.prenom, 0, 1) : '👤'}
                    </div>
                    
                    <div class="conv-details">
                        <p class="conv-name">
                            <c:choose>
                                <c:when test="${not empty compagnon}">
                                    <c:out value="${compagnon.prenom} ${compagnon.nom}"/>
                                </c:when>
                                <c:otherwise>
                                    Utilisateur N° ${autreUserId}
                                </c:otherwise>
                            </c:choose>
                        </p>
                        <p class="conv-sub"><c:out value="${not empty compagnon ? compagnon.email : 'Ouvrir la discussion'}"/></p>
                    </div>
                </a>
            </c:forEach>
            
            <c:if test="${empty conversations}">
                <a class="conv-item active" href="#">
                    <div class="avatar-circle">U</div>
                    <div class="conv-details">
                        <p class="conv-name">Utilisateur Destinataire</p>
                        <p class="conv-sub">Lancer la discussion...</p>
                    </div>
                </a>
            </c:if>
        </div>
    </div>

    <!-- ZONE DE CHAT PRINCIPALE -->
    <div class="chat-main">
        <c:choose>
            <c:when test="${not empty messages or not empty destinataireId}">
                <div class="chat-header">
                    <h2>Discussion Privée</h2>
                    <p>Échanges sécurisés au sein du laboratoire LIAS</p>
                </div>

                <div class="chat-body" id="chatBody">
                    <c:forEach var="msg" items="${messages}">
                        <div class="msg-row ${msg.expediteurId == sessionScope.user.id ? 'sent' : 'received'}">
                            <div class="msg-bubble">
                                <c:if test="${msg.expediteurId != sessionScope.user.id}">
                                    <strong style="font-size: 11px; color:#D97706; display:block; margin-bottom: 2px;">
                                        <c:out value="${msg.nomExpediteur}"/>
                                    </strong>
                                </c:if>
                                <c:out value="${msg.contenu}"/>
                                <span class="msg-meta"><c:out value="${msg.dateEnvoi}"/></span>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <div class="chat-footer">
                    <form action="${pageContext.request.contextPath}${chatRoot}" method="POST" class="chat-form">
                        <input type="hidden" name="action" value="envoyerPrive">
                        <input type="hidden" name="conversationId" value="${conversationId}">
                        <input type="hidden" name="destinataireId" value="${destinataireId}">
                        
                        <input type="text" name="contenu" class="chat-input" placeholder="Écrivez votre message ici..." required autocomplete="off">
                        <button type="submit" class="btn-send">Envoyer</button>
                    </form>
                </div>
            </c:when>
            <c:otherwise>
                <div class="empty-chat-state">
                    <span style="font-size: 40px; margin-bottom: 10px;">💬</span>
                    <h3>Sélectionnez un contact pour démarrer</h3>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<script>
    var cb = document.getElementById('chatBody');
    if(cb) { cb.scrollTop = cb.scrollHeight; }
</script>