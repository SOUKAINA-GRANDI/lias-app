<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%-- Entête de page --%>
<div class="ph">
    <div>
        <div class="ph-eye">Administration</div>
        <h1 class="ph-title">Gestion des Utilisateurs</h1>
        <p class="ph-sub">Comptes d'accès et contrôle des privilèges de la plateforme LIAS.</p>
    </div>
</div>

<%-- Conteneur Tableau & Recherche --%>
<div class="tbl-card anim-1">
    <div class="tbl-hd">
        <form method="get" class="search-box">
            <i class="ti ti-search"></i>
            <input type="text" name="q" placeholder="Rechercher un utilisateur…" value="${param.q}">
        </form>
        <span class="muted bold mono">${fn:length(utilisateurs)} compte(s)</span>
    </div>

    <table class="lias-tbl">
        <thead>
            <tr>
                <th style="width: 50px;">#</th>
                <th>Email</th>
                <th>Type</th>
                <th>Créé le</th>
                <th>Statut</th>
                <th style="text-align: right;">Actions</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="u" items="${utilisateurs}" varStatus="s">
            <tr>
                <td class="mono muted">${s.count}</td>
                <td>
                    <div class="fxc gap-2">
                        <div class="av av-sm">
                            ${fn:toUpperCase(fn:substring(u.email, 0, 1))}
                        </div>
                        <span class="bold">${u.email}</span>
                    </div>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${u.type eq 'ADMIN'}">
                            <span class="bdg bdg-red"><i class="ti ti-shield-check"></i> Admin</span>
                        </c:when>
                        <c:otherwise>
                            <span class="bdg bdg-blue"><i class="ti ti-user"></i> Membre</span>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td class="muted">${u.dateCreation}</td>
                <td>
                    <c:choose>
                        <c:when test="${u.actif}">
                            <span class="bdg bdg-green"><i class="ti ti-circle-check"></i> Actif</span>
                        </c:when>
                        <c:otherwise>
                            <span class="bdg bdg-gray"><i class="ti ti-circle-x"></i> Désactivé</span>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <div class="tbl-actions" style="justify-content: flex-end;">
                        <%-- Activer / Désactiver --%>
                        <form method="post" action="${pageContext.request.contextPath}/admin/utilisateurs" style="display:inline"
                              onsubmit="return confirm('Confirmer cette action ?')">
                            <input type="hidden" name="id" value="${u.id}">
                            <c:choose>
                                <c:when test="${u.actif}">
                                    <input type="hidden" name="action" value="desactiver">
                                    <button type="submit" class="btn btn-outline btn-sm btn-danger">
                                        <i class="ti ti-user-off"></i> Désactiver
                                    </button>
                                </c:when>
                                <c:otherwise>
                                    <input type="hidden" name="action" value="activer">
                                    <button type="submit" class="btn btn-outline btn-sm" style="color:var(--green); border-color: #bbf7d0; background: var(--greenl)">
                                        <i class="ti ti-user-check"></i> Activer
                                    </button>
                                </c:otherwise>
                            </c:choose>
                        </form>
                        
                        <%-- Reset mot de passe --%>
                        <form method="post" action="${pageContext.request.contextPath}/admin/utilisateurs" style="display:inline"
                              onsubmit="return confirm('Réinitialiser le mot de passe de cet utilisateur ?')">
                            <input type="hidden" name="id" value="${u.id}">
                            <input type="hidden" name="action" value="resetPassword">
                            <button type="submit" class="btn btn-outline btn-sm">
                                <i class="ti ti-key"></i> Reset MDP
                            </button>
                        </form>
                    </div>
                </td>
            </tr>
        </c:forEach>
        
        <c:if test="${empty utilisateurs}">
            <tr>
                <td colspan="6">
                    <div class="empty">
                        <div class="empty-ico"><i class="ti ti-users-minus"></i></div>
                        <p>Aucun utilisateur trouvé.</p>
                    </div>
                </td>
            </tr>
        </c:if>
        </tbody>
    </table>
</div>