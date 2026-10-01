<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<style>
    .suivi-page { color:#1E293B; font-family: system-ui, -apple-system, 'Segoe UI', Roboto, sans-serif; }
    .suivi-head { display:flex; justify-content:space-between; align-items:center; margin:10px 0 24px; }
    .suivi-head h2 { margin:0; font-size:22px; }
    .suivi-sub { color:#64748B; font-size:13px; }
    .suivi-back { padding:9px 16px; border:1px solid #CBD5E1; border-radius:8px; background:#fff;
                  color:#334155; font-size:13px; font-weight:600; text-decoration:none; }
    .suivi-card { background:#fff; border:1px solid #E2E8F0; border-radius:12px; padding:20px; margin-bottom:24px; }
    .suivi-card h3 { margin:0 0 4px; font-size:16px; }
    .suivi-table { width:100%; border-collapse:collapse; margin-top:12px; }
    .suivi-table th { text-align:left; font-size:12px; text-transform:uppercase; color:#64748B; padding:10px; background:#F8FAFC; }
    .suivi-table td { padding:10px; border-top:1px solid #F1F5F9; font-size:14px; }
    .sb { padding:3px 9px; border-radius:999px; font-size:12px; font-weight:600; white-space:nowrap; }
    .sb-red   { background:#FEE2E2; color:#991B1B; }
    .sb-amber { background:#FEF3C7; color:#92400E; }
    .sb-green { background:#DCFCE7; color:#166534; }
    .suivi-cols { display:grid; grid-template-columns:1fr 1fr; gap:20px; margin-top:16px; }
    @media (max-width:800px) { .suivi-cols { grid-template-columns:1fr; } }
    .suivi-list { list-style:none; margin:8px 0 0; padding:0; }
    .suivi-list li { padding:8px 0; border-top:1px solid #F1F5F9; font-size:14px; }
    .suivi-select { padding:9px 12px; border:1px solid #CBD5E1; border-radius:8px; font-size:14px; min-width:260px; }
</style>

<div class="suivi-page">

    <div class="suivi-head">
        <div>
            <h2>⚖️ Suivi de distribution du matériel</h2>
            <span class="suivi-sub">Qui a reçu quoi, et qui n'a pas encore reçu. Membres permanents actifs.</span>
        </div>
        <a class="suivi-back" href="${pageContext.request.contextPath}/directeur/materiel">← Retour à l'inventaire</a>
    </div>

    <!-- 1. Vue par membre -->
    <div class="suivi-card">
        <h3>Répartition par membre</h3>
        <span class="suivi-sub">Les moins servis apparaissent en premier. Moyenne : <strong>${moyenne}</strong> attribution(s) par membre.</span>

        <table class="suivi-table">
            <thead>
                <tr>
                    <th>Membre</th>
                    <th>Total reçu</th>
                    <th>En sa possession</th>
                    <th>Dernière attribution</th>
                    <th>Matériel reçu</th>
                    <th>Équité</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="s" items="${suiviMembres}">
                    <tr>
                        <td><strong><c:out value="${s.nom} ${s.prenom}"/></strong></td>
                        <td>${s.total}</td>
                        <td>${s.enCours}</td>
                        <td><c:out value="${s.derniere}" default="—"/></td>
                        <td><c:out value="${s.materiels}" default="—"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${s.total == 0}"><span class="sb sb-red">Aucune attribution</span></c:when>
                                <c:when test="${s.total < moyenne}"><span class="sb sb-amber">Sous la moyenne</span></c:when>
                                <c:otherwise><span class="sb sb-green">Équilibré</span></c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty suiviMembres}">
                    <tr><td colspan="6" style="text-align:center; color:#94A3B8; padding:30px;">Aucun membre permanent actif.</td></tr>
                </c:if>
            </tbody>
        </table>
    </div>

    <!-- 2. Vue par matériel -->
    <div class="suivi-card">
        <h3>Vue par matériel</h3>
        <span class="suivi-sub">Choisis un matériel pour voir qui l'a reçu et qui ne l'a pas reçu.</span>

        <form method="GET" action="${pageContext.request.contextPath}/directeur/materiel" style="margin-top:12px;">
            <input type="hidden" name="action" value="suivi">
            <select name="materielId" class="suivi-select" onchange="this.form.submit()">
                <option value="">-- Choisir un matériel --</option>
                <c:forEach var="mat" items="${materiels}">
                    <option value="${mat.id}" ${materielChoisi.id == mat.id ? 'selected' : ''}>
                        <c:out value="${mat.nom}"/>
                    </option>
                </c:forEach>
            </select>
        </form>

        <c:if test="${not empty materielChoisi}">
            <div class="suivi-cols">

                <div>
                    <strong>✅ Ont reçu « <c:out value="${materielChoisi.nom}"/> » (${recus.size()})</strong>
                    <ul class="suivi-list">
                        <c:forEach var="r" items="${recus}">
                            <li>
                                <c:out value="${r.nom} ${r.prenom}"/>
                                <span class="suivi-sub"> — reçu le ${r.dateAttribution}</span>
                                <c:choose>
                                   <c:when test="${empty r.dateRetour}">
                                       <span class="sb sb-green">En possession</span>
                                       <form method="POST" action="${pageContext.request.contextPath}/directeur/materiel" style="display:inline; margin-left:6px;"
                                             onsubmit="return confirm('Confirmer le retour de ce matériel ?');">
                                           <input type="hidden" name="action" value="retourner">
                                           <input type="hidden" name="attributionId" value="${r.attributionId}">
                                           <input type="hidden" name="materielId" value="${materielChoisi.id}">
                                           <button type="submit" style="border:1px solid #CBD5E1; background:#fff; border-radius:6px; padding:3px 8px; font-size:11px; cursor:pointer;">
                                               ↩ Marquer retourné
                                           </button>
                                       </form>
                                   </c:when>
                                   <c:otherwise><span class="sb sb-amber">Rendu le ${r.dateRetour}</span></c:otherwise>
                                </c:choose>
                            </li>
                        </c:forEach>
                        <c:if test="${empty recus}"><li class="suivi-sub">Personne pour l'instant.</li></c:if>
                    </ul>
                </div>

                <div>
                    <strong>⏳ N'ont pas reçu (${nonServis.size()})</strong>
                    <ul class="suivi-list">
                        <c:forEach var="n" items="${nonServis}">
                            <li><c:out value="${n.nom} ${n.prenom}"/></li>
                        </c:forEach>
                        <c:if test="${empty nonServis}"><li class="suivi-sub">Tous les membres ont reçu ce matériel.</li></c:if>
                    </ul>
                </div>

            </div>
        </c:if>
    </div>

</div>