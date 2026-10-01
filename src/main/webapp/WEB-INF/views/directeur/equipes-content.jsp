<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<style>
    .equipes-page-header { display:flex; justify-content:space-between; align-items:center; margin:10px 0 24px; }
    .equipes-title-block h2 { margin:0; font-size:20px; color:#0F172A; font-weight:700; }
    .equipes-title-block span { font-size:11px; font-weight:700; text-transform:uppercase; letter-spacing:.08em; color:#94A3B8; }
    .btn-nouvelle-equipe { background:#2563EB; color:#fff; padding:9px 16px; border-radius:10px; text-decoration:none; font-size:13px; font-weight:600; }
    .btn-nouvelle-equipe:hover { background:#1D4ED8; }

    .equipes-grid { display:grid; grid-template-columns:repeat(auto-fill, minmax(300px, 1fr)); gap:16px; }
    .equipe-card { background:#fff; border:1px solid #E5E7EB; border-radius:14px; padding:18px 20px; }
    .equipe-card h4 { margin:0 0 6px; font-size:15px; color:#1E293B; font-weight:700; }
    .equipe-card p { margin:0 0 12px; font-size:13px; color:#64748B; }
    .equipe-chef { font-size:12px; color:#B45309; background:#FEF3C7; display:inline-block; padding:3px 10px; border-radius:999px; margin-bottom:10px; }
    .equipe-chef.aucun { color:#94A3B8; background:#F1F5F9; }
    .equipe-actions { display:flex; gap:8px; margin-top:10px; }
    .equipe-actions a, .equipe-actions button {
        font-size:12px; padding:6px 10px; border-radius:8px; text-decoration:none;
        border:1px solid #E2E8F0; background:#fff; color:#334155; cursor:pointer;
    }
    .equipe-actions a:hover, .equipe-actions button:hover { background:#F1F5F9; }
</style>

<div class="equipes-page-header">
    <div class="equipes-title-block">
        <h2>Équipes du laboratoire</h2>
        <span>${not empty equipes ? equipes.size() : 0} équipe(s)</span>
    </div>
    <a class="btn-nouvelle-equipe" href="${pageContext.request.contextPath}/directeur/equipes?action=nouveau">
        <i class="fa-solid fa-plus"></i> Nouvelle équipe
    </a>
</div>

<div class="equipes-grid">
    <c:choose>
        <c:when test="${not empty equipes}">
            <c:forEach var="e" items="${equipes}">
                <div class="equipe-card">
                    <h4>${e.nom}</h4>
                    <p>${not empty e.description ? e.description : 'Pas de description.'}</p>
                    <c:choose>
                        <c:when test="${not empty e.chefNom}">
                            <span class="equipe-chef">Chef : ${e.chefNom}</span>
                        </c:when>
                        <c:otherwise>
                            <span class="equipe-chef aucun">Aucun chef désigné</span>
                        </c:otherwise>
                    </c:choose>
                    <div class="equipe-actions">
                        <a href="${pageContext.request.contextPath}/directeur/equipes?action=membres&id=${e.id}">
                            <i class="fa-solid fa-users"></i> Membres
                        </a>
                        <a href="${pageContext.request.contextPath}/directeur/equipes?action=edit&id=${e.id}">
                            <i class="fa-solid fa-pen"></i> Modifier
                        </a>
                        <form method="POST" action="${pageContext.request.contextPath}/directeur/equipes" style="margin:0"
                              onsubmit="return confirm('Archiver l\'équipe ${e.nom} ?')">
                            <input type="hidden" name="action" value="archiver">
                            <input type="hidden" name="id" value="${e.id}">
                            <button type="submit"><i class="fa-solid fa-box-archive"></i></button>
                        </form>
                    </div>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <div style="grid-column:1/-1; text-align:center; padding:60px 20px; color:#94A3B8;">
                <i class="fa-solid fa-people-group" style="font-size:40px; display:block; margin-bottom:12px;"></i>
                Aucune équipe pour le moment.
            </div>
        </c:otherwise>
    </c:choose>
</div>
