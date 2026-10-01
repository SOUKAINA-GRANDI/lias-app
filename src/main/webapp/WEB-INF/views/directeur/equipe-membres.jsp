<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<style>
    .equipe-membres-header { margin:10px 0 20px; }
    .equipe-membres-header h2 { margin:0 0 4px; font-size:20px; color:#0F172A; }
    .equipe-membres-header p { margin:0; font-size:13px; color:#64748B; }

    .membres-panel { background:#fff; border:1px solid #E5E7EB; border-radius:14px; padding:20px; margin-bottom:20px; }
    .membres-panel h3 { margin:0 0 14px; font-size:14px; text-transform:uppercase; letter-spacing:.04em; color:#334155; }

    .membre-row { display:flex; justify-content:space-between; align-items:center; padding:10px 0; border-bottom:1px solid #F1F5F9; }
    .membre-row:last-child { border-bottom:none; }
    .membre-row .infos { font-size:13px; color:#1E293B; }
    .membre-row .infos small { display:block; color:#94A3B8; font-weight:400; }
    .chef-badge { background:#FEF3C7; color:#B45309; font-size:11px; font-weight:700; padding:2px 8px; border-radius:999px; margin-left:8px; }

    .membre-row form { display:inline; margin-left:6px; }
    .btn-sm { font-size:12px; padding:6px 10px; border-radius:8px; border:1px solid #E2E8F0; background:#fff; color:#334155; cursor:pointer; }
    .btn-sm:hover { background:#F1F5F9; }
    .btn-sm.danger:hover { background:#FEE2E2; color:#EF4444; border-color:#FECACA; }
    .btn-sm.chef:hover { background:#FEF3C7; color:#B45309; border-color:#FDE68A; }

    .add-form { display:flex; gap:8px; margin-top:6px; }
    .add-form select { flex:1; padding:8px 10px; border:1px solid #E2E8F0; border-radius:8px; font-size:13px; }
    .add-form button { padding:8px 14px; border:none; border-radius:8px; background:#2563EB; color:#fff; font-weight:600; cursor:pointer; }
</style>

<div class="equipe-membres-header">
    <h2>${equipe.nom}</h2>
    <p>${not empty equipe.description ? equipe.description : 'Aucune description.'}</p>
</div>

<%-- ══ MEMBRES ACTUELS ══ --%>
<div class="membres-panel">
    <h3>Membres de l'équipe</h3>

    <c:set var="aUnMembre" value="false" />
    <c:forEach var="m" items="${tousMembres}">
        <c:if test="${m.equipeId == equipe.id}">
            <c:set var="aUnMembre" value="true" />
            <div class="membre-row">
                <div class="infos">
                    ${m.prenom} ${m.nom}
                    <c:if test="${m.id == equipe.chefId}"><span class="chef-badge">Chef d'équipe</span></c:if>
                    <small>${m.email}</small>
                </div>
                <div>
                    <c:if test="${m.id != equipe.chefId}">
                        <form action="${pageContext.request.contextPath}/directeur/equipes" method="POST"
                              onsubmit="return confirm('Désigner ${m.prenom} ${m.nom} comme chef de cette équipe ?')">
                            <input type="hidden" name="action" value="assignerChef">
                            <input type="hidden" name="equipeId" value="${equipe.id}">
                            <input type="hidden" name="membreId" value="${m.id}">
                            <button type="submit" class="btn-sm chef">Nommer chef</button>
                        </form>
                    </c:if>
                    <form action="${pageContext.request.contextPath}/directeur/equipes" method="POST"
                          onsubmit="return confirm('Retirer ${m.prenom} ${m.nom} de cette équipe ?')">
                        <input type="hidden" name="action" value="retirerMembre">
                        <input type="hidden" name="equipeId" value="${equipe.id}">
                        <input type="hidden" name="membreId" value="${m.id}">
                        <button type="submit" class="btn-sm danger">Retirer</button>
                    </form>
                </div>
            </div>
        </c:if>
    </c:forEach>

    <c:if test="${aUnMembre == 'false'}">
        <p style="color:#94A3B8; font-size:13px; margin:10px 0;">Aucun membre dans cette équipe pour le moment.</p>
    </c:if>
</div>

<%-- ══ AJOUTER UN MEMBRE ══ --%>
<div class="membres-panel">
    <h3>Ajouter un membre à l'équipe</h3>
    <form class="add-form" action="${pageContext.request.contextPath}/directeur/equipes" method="POST">
        <input type="hidden" name="action" value="assignerMembre">
        <input type="hidden" name="equipeId" value="${equipe.id}">
        <select name="membreId" required>
            <option value="" disabled selected>-- Choisir un membre --</option>
            <c:forEach var="m" items="${tousMembres}">
                <c:if test="${m.equipeId != equipe.id}">
                    <option value="${m.id}">${m.prenom} ${m.nom} (${m.email})</option>
                </c:if>
            </c:forEach>
        </select>
        <button type="submit">Ajouter</button>
    </form>
</div>

<a href="${pageContext.request.contextPath}/directeur/equipes" style="font-size:13px; color:#64748B; text-decoration:none;">
    &larr; Retour à la liste des équipes
</a>
