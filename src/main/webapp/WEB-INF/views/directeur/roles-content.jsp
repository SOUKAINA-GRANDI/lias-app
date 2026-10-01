<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<style>
    .roles-page-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 24px;
        margin-top: 10px;
    }
    .roles-title-block h2 { margin: 0; font-size: 20px; color: #0F172A; font-weight: 700; }
    .roles-title-block span {
        font-size: 11px; font-weight: 700; text-transform: uppercase;
        letter-spacing: .08em; color: #94A3B8; display: inline-block; margin-top: 4px;
    }

    .roles-section { margin-bottom: 32px; }
    .roles-section h3 {
        font-size: 14px; color: #334155; font-weight: 700; margin: 0 0 12px;
        text-transform: uppercase; letter-spacing: .04em;
    }

    table.roles-table { width: 100%; border-collapse: collapse; background: #fff;
        border: 1px solid #E5E7EB; border-radius: 12px; overflow: hidden; }
    table.roles-table th, table.roles-table td {
        padding: 12px 14px; text-align: left; font-size: 13px; border-bottom: 1px solid #F1F5F9;
    }
    table.roles-table th { background: #F8FAFC; color: #64748B; font-weight: 700;
        text-transform: uppercase; font-size: 11px; letter-spacing: .04em; }

    .role-tag {
        display: inline-block; padding: 3px 10px; border-radius: 999px;
        font-size: 11px; font-weight: 700; text-transform: uppercase;
    }
    .role-DIRECTEUR { background: #FEF3C7; color: #B45309; }
    .role-VICE_DIRECTEUR { background: #E0E7FF; color: #4338CA; }
    .role-CHEF_EQUIPE { background: #DBEAFE; color: #1D4ED8; }
    .role-MEMBRE_EQUIPE { background: #F1F5F9; color: #475569; }

    .role-form { display: flex; gap: 8px; align-items: center; }
    .role-form select {
        padding: 6px 10px; border: 1px solid #E2E8F0; border-radius: 8px; font-size: 13px;
    }
    .role-form button {
        padding: 6px 12px; border: none; border-radius: 8px; background: #2563EB;
        color: #fff; font-size: 12px; font-weight: 600; cursor: pointer;
    }
    .role-form button:hover { background: #1D4ED8; }

    .badge-actif { color: #059669; font-weight: 700; font-size: 12px; }
    .badge-cloture { color: #94A3B8; font-size: 12px; }
</style>

<c:if test="${not empty sessionScope.error}">
    <div style="margin-bottom: 20px; padding: 12px 16px; border-radius: 8px; font-size: 13px; background: #fee2e2; color: #991b1b; font-weight: 500;">
        ⚠️ Erreur : ${sessionScope.error}
        <c:remove var="error" scope="session"/>
    </div>
</c:if>

<div class="roles-page-header">
    <div class="roles-title-block">
        <h2>Rôles &amp; responsabilités</h2>
        <span>Directeur, vice-directeur, chefs d'équipe, membres d'équipe — historique complet</span>
    </div>
</div>

<%-- ══ CHANGEMENT DE RÔLE ══ --%>
<div class="roles-section">
    <h3>Attribuer / changer un rôle</h3>
    <table class="roles-table">
        <thead>
            <tr>
                <th>Membre</th>
                <th>Rôle actuel</th>
                <th>Nouveau rôle</th>
            </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${not empty membres}">
                <c:forEach var="m" items="${membres}">
                    <tr>
                        <td>${m.prenom} ${m.nom} <span style="color:#94A3B8">(${m.email})</span></td>
                        <td>
                            <c:choose>
                                <c:when test="${not empty m.role}">
                                    <span class="role-tag role-${m.role}">${m.role}</span>
                                </c:when>
                                <c:otherwise><span style="color:#CBD5E1">— aucun —</span></c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <form class="role-form" method="POST"
                                  action="${pageContext.request.contextPath}/directeur/roles"
                                  onsubmit="return confirm('Confirmer le changement de rôle pour ${m.prenom} ${m.nom} ?')">
                                <input type="hidden" name="membreId" value="${m.id}">
                                <select name="nouveauRole" required>
                                    <option value="" disabled selected>-- choisir --</option>
                                    <c:forEach var="rt" items="${roleTypes}">
                                        <option value="${rt}" ${m.role eq rt.name() ? 'disabled' : ''}>${rt}</option>
                                    </c:forEach>
                                </select>
                                <button type="submit">Appliquer</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <tr><td colspan="3" style="text-align:center;color:#94A3B8;padding:30px">Aucun membre trouvé.</td></tr>
            </c:otherwise>
        </c:choose>
        </tbody>
    </table>
</div>

<%-- ══ HISTORIQUE COMPLET ══ --%>
<div class="roles-section">
    <h3>Historique complet des rôles</h3>
    <table class="roles-table">
        <thead>
            <tr>
                <th>Membre</th>
                <th>Rôle</th>
                <th>Début</th>
                <th>Fin</th>
                <th>Statut</th>
            </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${not empty historique}">
                <c:forEach var="r" items="${historique}">
                    <tr>
                        <td>${r.membrePrenom} ${r.membreNom}</td>
                        <td><span class="role-tag role-${r.nom}">${r.nom}</span></td>
                        <td>${r.dateDebut}</td>
                        <td>${not empty r.dateFin ? r.dateFin : '—'}</td>
                        <td>
                            <c:choose>
                                <c:when test="${r.actif}"><span class="badge-actif">● Actif</span></c:when>
                                <c:otherwise><span class="badge-cloture">Clôturé</span></c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <tr><td colspan="5" style="text-align:center;color:#94A3B8;padding:30px">Aucun historique pour le moment.</td></tr>
            </c:otherwise>
        </c:choose>
        </tbody>
    </table>
</div>
