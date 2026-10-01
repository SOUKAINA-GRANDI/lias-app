<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%-- Détection de l'URL du navigateur, même après un forward de Servlet --%>
<c:set var="browserURI" value="${not empty requestScope['javax.servlet.forward.request_uri'] ? requestScope['javax.servlet.forward.request_uri'] : pageContext.request.requestURI}" />
<c:set var="baseUrl" value="${fn:contains(browserURI, '/directeur') ? '/directeur/messages' : '/messages'}" />
<style>
    .messaging-container { display: grid; grid-template-columns: 320px 1fr; background-color: #FFFFFF; border: 1px solid #E2E8F0; border-radius: 16px; height: calc(100vh - 140px); max-width: 1250px; margin: 20px auto; box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.02); font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif; overflow: hidden; }
    .msg-sidebar { background-color: #FAFAFA; border-right: 1px solid #E2E8F0; display: flex; flex-direction: column; }
    .sidebar-header { padding: 20px; border-bottom: 1px solid #F1F5F9; }
    .sidebar-header h3 { margin: 0 0 12px 0; font-size: 16px; font-weight: 700; color: #0F172A; }
    .channel-tabs { display: flex; gap: 6px; background: #F1F5F9; padding: 4px; border-radius: 8px; }
    .tab-btn { flex: 1; text-align: center; padding: 6px 4px; font-size: 12px; font-weight: 600; color: #64748B; text-decoration: none; border-radius: 6px; }
    .tab-btn.active { background: #FFFFFF; color: #0F172A; box-shadow: 0 1px 2px rgba(0,0,0,0.05); }
    .conversations-list { flex: 1; overflow-y: auto; padding: 10px; }
    
    .team-item { display: flex; align-items: center; gap: 12px; padding: 12px; border-radius: 10px; background-color: #E2E8F0; color: #0F172A; font-weight: 600; text-decoration: none; font-size: 13.5px; }
    .avatar-team { width: 38px; height: 38px; border-radius: 50%; background-color: #E0F2FE; color: #0369A1; display: flex; align-items: center; justify-content: center; font-weight: 700; }

    .chat-main { display: flex; flex-direction: column; background: #FFFFFF; }
    .chat-header { padding: 18px 24px; border-bottom: 1px solid #F1F5F9; }
    .chat-header h2 { margin: 0; font-size: 15px; color: #0F172A; font-weight: 700; }
    .chat-header p { margin: 2px 0 0 0; font-size: 12px; color: #94A3B8; }
    .chat-body { flex: 1; padding: 24px; overflow-y: auto; background-color: #F8FAFC; display: flex; flex-direction: column; gap: 14px; }
    
    .msg-row { display: flex; width: 100%; }
    .msg-row.received { justify-content: flex-start; }
    .msg-row.sent { justify-content: flex-end; }
    .msg-bubble { max-width: 65%; padding: 10px 14px; border-radius: 12px; font-size: 13.5px; line-height: 1.5; }
    .received .msg-bubble { background-color: #FFFFFF; color: #0F172A; border: 1px solid #E2E8F0; border-top-left-radius: 2px; }
    .sent .msg-bubble { background-color: #0F172A; color: #FFFFFF; border-top-right-radius: 2px; }
    .msg-meta { font-size: 10px; margin-top: 4px; display: block; color: #94A3B8; }
    .sent .msg-meta { color: #CBD5E1; text-align: right; }
    
    .chat-footer { padding: 16px 24px; border-top: 1px solid #F1F5F9; }
    .chat-form { display: flex; gap: 12px; }
    .chat-input { flex: 1; background-color: #F8FAFC; border: 1px solid #E2E8F0; border-radius: 8px; padding: 12px 16px; font-size: 13.5px; outline: none; }
    .chat-input:focus { border-color: #0F172A; }
    .btn-send-team { background-color: #0F172A; color: white; border: none; padding: 0 20px; font-size: 13px; font-weight: 600; border-radius: 8px; cursor: pointer; }
    .btn-send-team:hover { background-color: #1E293B; }
</style>

<c:choose>

    <%-- ✅ Nouveau cas : le membre n'a aucune équipe assignée --%>
    <c:when test="${pasDequipe}">
        <div class="messaging-container" style="grid-template-columns: 1fr;">
            <div class="chat-main">
                <div class="chat-header">
                    <h2>Chat d'équipe</h2>
                    <p>Espace de discussion collaboratif de votre unité</p>
                </div>
                <div class="chat-body" style="align-items:center; justify-content:center;">
                    <div style="text-align:center; color:#94A3B8; max-width:360px;">
                        <div style="font-size:32px; margin-bottom:10px;">👥</div>
                        <p style="font-size:15px; font-weight:600; color:#334155; margin:0 0 6px;">
                            Aucune équipe assignée
                        </p>
                        <p style="font-size:13px; margin:0;">
                            Tu n'es pas encore rattaché(e) à une équipe. Le chat d'équipe sera disponible une fois affecté(e) par le directeur.
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </c:when>

    <%-- Cas normal : le membre a une équipe --%>
    <c:otherwise>

        <div class="messaging-container">
            
            <div class="msg-sidebar">
                <div class="sidebar-header">
                    <h3>Messagerie Interne</h3>
                    <div class="channel-tabs">
                        <%-- URLs de la Sidebar corrigées avec ${baseUrl} --%>
                        <a href="${pageContext.request.contextPath}${baseUrl}?action=prive" class="tab-btn">Privés</a>
                        <a href="${pageContext.request.contextPath}${baseUrl}?action=equipe" class="tab-btn active">Équipe</a>
                        <a href="${pageContext.request.contextPath}${baseUrl}?action=evenement&id=1" class="tab-btn">Événements</a>
                    </div>
                </div>

                <div class="conversations-list">
                    <div class="team-item">
                        <div class="avatar-team">👥</div>
                        <div style="min-width:0; flex:1;">
                            <p style="margin:0; overflow:hidden; text-overflow:ellipsis; white-space:nowrap;">
                                <c:out value="${nomEquipeUtilisateur}" default="Mon Unité de Recherche"/>
                            </p>
                        </div>
                    </div>
                </div>
            </div>

            <div class="chat-main">
                <div class="chat-header">
                    <h2><c:out value="${nomEquipeUtilisateur}" default="Canal d'Équipe"/></h2>
                    <p>Espace de discussion collaboratif de votre unité</p>
                </div>

                <div class="chat-body" id="chatBodyEquipe">
                    <c:forEach var="msg" items="${messages}">
                        <div class="msg-row ${msg.expediteurId == sessionScope.user.id ? 'sent' : 'received'}">
                            <div class="msg-bubble">
                                <c:if test="${msg.expediteurId != sessionScope.user.id}">
                                    <strong style="font-size: 11px; color:#1E3A8A; display:block; margin-bottom: 2px;"><c:out value="${msg.nomExpediteur}"/></strong>
                                </c:if>
                                <c:out value="${msg.contenu}"/>
                                <span class="msg-meta"><c:out value="${msg.dateEnvoi}"/></span>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <div class="chat-footer">
                    <%-- URL d'action du formulaire corrigée avec ${baseUrl} --%>
                    <form action="${pageContext.request.contextPath}${baseUrl}" method="POST" class="chat-form">
                        <input type="hidden" name="action" value="envoyerEquipe">
                        <input type="hidden" name="equipeId" value="${equipeId}">
                        
                        <input type="text" name="contenu" class="chat-input" placeholder="Envoyer un message à l'équipe..." required autocomplete="off">
                        <button type="submit" class="btn-send-team">Diffuser</button>
                    </form>
                </div>
            </div>
        </div>

    </c:otherwise>

</c:choose>

<script>
    var cb = document.getElementById('chatBodyEquipe');
    if(cb) { cb.scrollTop = cb.scrollHeight; }
</script>