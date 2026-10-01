<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<style>
    .hist-header { margin:10px 0 20px; }
    .hist-header h2 { margin:0 0 4px; font-size:20px; color:#0F172A; }
    .hist-header p { margin:0; font-size:13px; color:#64748B; }

    table.hist-table { width:100%; border-collapse:collapse; background:#fff;
        border:1px solid #E5E7EB; border-radius:12px; overflow:hidden; }
    table.hist-table th, table.hist-table td {
        padding:11px 12px; text-align:left; font-size:12.5px; border-bottom:1px solid #F1F5F9; white-space:nowrap;
    }
    table.hist-table th { background:#F8FAFC; color:#64748B; font-weight:700;
        text-transform:uppercase; font-size:10.5px; letter-spacing:.03em; }
    table.hist-table tr:last-child td { border-bottom:none; }
    .hist-empty-cell { color:#CBD5E1; }

    .back-link { display:inline-block; margin-bottom:16px; font-size:13px; color:#64748B; text-decoration:none; }
    .back-link:hover { color:#334155; }
</style>

<a class="back-link" href="${pageContext.request.contextPath}/directeur/membres">&larr; Retour aux membres</a>

<div class="hist-header">
    <h2>Historique de ${membre.prenom} ${membre.nom}</h2>
    <p>Chaque ligne est un instantané des valeurs <strong>avant</strong> une modification — aucune donnée n'est jamais écrasée ni supprimée (CDC §3).</p>
</div>

<div style="overflow-x:auto">
<table class="hist-table">
    <thead>
        <tr>
            <th>Date</th>
            <th>Nom</th>
            <th>Prénom</th>
            <th>Téléphone</th>
            <th>Statut</th>
            <th>Rôle</th>
            <th>Équipe</th>
            <th>Photo</th>
            <th>Biographie</th>
            <th>Centres d'intérêt</th>
        </tr>
    </thead>
    <tbody>
    <c:choose>
        <c:when test="${not empty historique}">
            <c:forEach var="h" items="${historique}">
                <tr>
                    <td>${h.dateModification}</td>
                    <td>${h.nom}</td>
                    <td>${h.prenom}</td>
                    <td><c:out value="${h.telephone}" default="—"/></td>
                    <td><c:out value="${h.statut}" default="—"/></td>
                    <td><c:out value="${h.role}" default="—"/></td>
                    <td><c:out value="${h.equipeNom}" default="—"/></td>
                    <td><c:out value="${h.photo}" default="—"/></td>
                    <td style="max-width:200px; white-space:normal;"><c:out value="${h.biographie}" default="—"/></td>
                    <td style="max-width:200px; white-space:normal;"><c:out value="${h.centresInteret}" default="—"/></td>
                </tr>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <tr><td colspan="10" style="text-align:center;color:#94A3B8;padding:30px">
                Aucune modification enregistrée pour ce membre pour le moment.
            </td></tr>
        </c:otherwise>
    </c:choose>
    </tbody>
</table>
</div>
