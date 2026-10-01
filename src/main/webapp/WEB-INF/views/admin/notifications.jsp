<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%-- 1. Entête de la page --%>
<div class="ph">
    <div>
        <div class="ph-eye">Communication Système</div>
        <h1 class="ph-title">Centre de Notifications</h1>
        <p class="ph-sub">Envoyez des alertes ciblées ou globales aux utilisateurs de la plateforme.</p>
    </div>
</div>

<div style="display: grid; grid-template-columns: 1fr 2fr; gap: 24px; margin-top: 20px;">
    
    <%-- 2. PANNEAU DE GAUCHE : Formulaires d'envoi --%>
    <div style="display: flex; flex-direction: column; gap: 24px;">
        
        <!-- Formulaire Global -->
        <div class="tbl-card anim-1" style="padding: 20px; background: white; border-radius: 12px; box-shadow: 0 1px 3px rgba(0,0,0,0.05);">
            <h3 style="margin-top: 0; color: #1e293b; font-size: 16px; border-bottom: 1px solid #f1f5f9; padding-bottom: 10px;">📢 Notification Globale</h3>
            <p style="font-size: 12px; color: #64748b; margin-bottom: 16px;">Ce message sera envoyé à <strong>tous</strong> les utilisateurs sans exception.</p>
            
            <form method="post" action="${pageContext.request.contextPath}/admin/notifications">
                <input type="hidden" name="action" value="global">
                <div style="display: flex; flex-direction: column; gap: 12px;">
                    <textarea name="message" required style="width: 100%; min-height: 80px; border: 1px solid #e2e8f0; border-radius: 8px; padding: 10px; font-family: inherit; resize: vertical;" placeholder="Votre message global..."></textarea>
                    <button type="submit" class="btn" style="background: #2563eb; color: white; border: none; padding: 10px; border-radius: 8px; font-weight: bold; cursor: pointer; width: 100%;">
                        Diffuser à tout le monde
                    </button>
                </div>
            </form>
        </div>

        <!-- Formulaire Individuel -->
        <div class="tbl-card anim-2" style="padding: 20px; background: white; border-radius: 12px; box-shadow: 0 1px 3px rgba(0,0,0,0.05);">
            <h3 style="margin-top: 0; color: #1e293b; font-size: 16px; border-bottom: 1px solid #f1f5f9; padding-bottom: 10px;">👤 Notification Ciblée</h3>
            <p style="font-size: 12px; color: #64748b; margin-bottom: 16px;">Envoyer un message privé à un utilisateur spécifique.</p>
            
            <form method="post" action="${pageContext.request.contextPath}/admin/notifications">
                <input type="hidden" name="action" value="individuel">
                <div style="display: flex; flex-direction: column; gap: 12px;">
                    
                    <div>
                        <label style="font-size: 12px; font-weight: bold; color: #475569; display: block; margin-bottom: 4px;">Sélectionner le destinataire</label>
                        <select name="userId" required style="width: 100%; border: 1px solid #e2e8f0; border-radius: 8px; padding: 10px; background: white;">
                            <option value="">-- Choisir un utilisateur --</option>
                            <c:forEach items="${listeUtilisateurs}" var="u">
                                <option value="${u.id}">${u.nom} ${u.prenom} (${u.email})</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div>
                        <label style="font-size: 12px; font-weight: bold; color: #475569; display: block; margin-bottom: 4px;">Message</label>
                        <textarea name="message" required style="width: 100%; min-height: 80px; border: 1px solid #e2e8f0; border-radius: 8px; padding: 10px; font-family: inherit; resize: vertical;" placeholder="Votre message ciblé..."></textarea>
                    </div>

                    <button type="submit" class="btn" style="background: #0f172a; color: white; border: none; padding: 10px; border-radius: 8px; font-weight: bold; cursor: pointer; width: 100%;">
                        Envoyer l'alerte
                    </button>
                </div>
            </form>
        </div>
    </div>

    <%-- 3. PANNEAU DE DROITE : Historique des notifications envoyées --%>
    <div class="tbl-card anim-3" style="padding: 20px; background: white; border-radius: 12px; box-shadow: 0 1px 3px rgba(0,0,0,0.05);">
        <h3 style="margin-top: 0; color: #1e293b; font-size: 16px; border-bottom: 1px solid #f1f5f9; padding-bottom: 10px;">📋 Historique des envois</h3>
        
        <div style="overflow-x: auto; margin-top: 15px;">
            <table class="tbl" style="width: 100%; border-collapse: collapse; text-align: left;">
                <thead>
                    <tr style="border-bottom: 2px solid #edf2f7; color: #4a5568; font-size: 13px;">
                        <th style="padding: 12px; width: 60px;">ID</th>
                        <th style="padding: 12px; width: 120px;">ID Destinataire</th>
                        <th style="padding: 12px;">Contenu du Message</th>
                        <th style="padding: 12px; width: 90px; text-align: center;">Statut</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${empty notifications}">
                            <tr>
                                <td colspan="4" style="text-align: center; padding: 40px; color: #64748b; font-size: 14px;">
                                    Aucune notification envoyée pour le moment.
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${notifications}" var="n">
                                <tr style="border-bottom: 1px solid #edf2f7; font-size: 13px;">
                                    <td style="padding: 12px; font-weight: bold; color: #64748b;">#${n.id}</td>
                                    <td style="padding: 12px;">
                                        <span style="font-family: monospace; background: #f1f5f9; padding: 2px 6px; border-radius: 4px;">User ID: ${n.utilisateurId}</span>
                                    </td>
                                    <td style="padding: 12px; color: #1e293b; line-height: 1.4;">${n.message}</td>
                                    <td style="padding: 12px; text-align: center;">
                                        <c:choose>
                                            <c:when test="${n.lu}">
                                                <span style="background: #dcfce7; color: #15803d; padding: 4px 8px; border-radius: 12px; font-size: 11px; font-weight: bold;">Lu</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span style="background: #fef9c3; color: #a16207; padding: 4px 8px; border-radius: 12px; font-size: 11px; font-weight: bold;">Non lu</span>
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
    </div>
</div>