<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<style>
    .cd-page { font-family: system-ui, -apple-system, sans-serif; color:#1E293B; }
    .cd-head { display:flex; justify-content:space-between; align-items:center; margin-bottom:20px; }
    .cd-back { padding:9px 16px; border:1px solid #CBD5E1; border-radius:8px; background:#fff;
               color:#334155; font-size:13px; font-weight:600; text-decoration:none; }
    .cd-card { background:#fff; border:1px solid #E2E8F0; border-radius:12px; padding:24px; margin-bottom:20px; }
    .cd-card h3 { margin:0 0 12px; font-size:16px; }
    .cd-table { width:100%; border-collapse:collapse; }
    .cd-table th { text-align:left; font-size:12px; text-transform:uppercase; color:#64748B; padding:8px; background:#F8FAFC; }
    .cd-table td { padding:8px; border-top:1px solid #F1F5F9; font-size:14px; }
    .cd-add-form { display:flex; gap:8px; margin-top:14px; }
    .cd-add-form select { flex:1; padding:8px; border:1px solid #CBD5E1; border-radius:8px; }
    .cd-add-form button { padding:8px 16px; border:none; border-radius:8px; background:#0F172A; color:#fff; font-weight:600; cursor:pointer; }
    .cd-remove { border:1px solid #FCA5A5; background:#fff; color:#B91C1C; border-radius:6px; padding:4px 8px; font-size:12px; cursor:pointer; }
</style>

<div class="cd-page">

    <div class="cd-head">
        <div>
            <h2 style="margin:0;">📄 <c:out value="${convention.titre}"/></h2>
            <span style="color:#64748B; font-size:13px;">Partenaire : <c:out value="${convention.partenaire}"/></span>
        </div>
        <a class="cd-back" href="${pageContext.request.contextPath}/directeur/conventions">← Retour aux conventions</a>
    </div>

    <!-- Événements associés -->
    <div class="cd-card">
        <h3>🎓 Événements associés</h3>

        <table class="cd-table">
            <thead><tr><th>Titre</th><th>Type</th><th>Date</th><th></th></tr></thead>
            <tbody>
                <c:forEach var="e" items="${evenementsAssocies}">
                    <tr>
                        <td><c:out value="${e.titre}"/></td>
                        <td>${e.type}</td>
                        <td>${e.dateDebut}</td>
                        <td>
                            <form method="POST" action="${pageContext.request.contextPath}/directeur/conventions" style="margin:0;">
                                <input type="hidden" name="action" value="delierEvenement">
                                <input type="hidden" name="conventionId" value="${convention.id}">
                                <input type="hidden" name="evenementId" value="${e.id}">
                                <button type="submit" class="cd-remove">Retirer</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty evenementsAssocies}">
                    <tr><td colspan="4" style="color:#94A3B8; text-align:center; padding:16px;">Aucun événement associé.</td></tr>
                </c:if>
            </tbody>
        </table>

        <form method="POST" action="${pageContext.request.contextPath}/directeur/conventions" class="cd-add-form">
            <input type="hidden" name="action" value="lierEvenement">
            <input type="hidden" name="conventionId" value="${convention.id}">
            <select name="evenementId" required>
                <option value="">-- Choisir un événement --</option>
                <c:forEach var="e" items="${tousEvenements}">
                    <option value="${e.id}"><c:out value="${e.titre}"/></option>
                </c:forEach>
            </select>
            <button type="submit">+ Associer</button>
        </form>
    </div>

    <!-- Documents associés -->
    <div class="cd-card">
        <h3>📁 Documents associés</h3>

        <table class="cd-table">
            <thead><tr><th>Titre</th><th>Type</th><th></th><th></th></tr></thead>
            <tbody>
                <c:forEach var="d" items="${documentsAssocies}">
                    <tr>
                        <td><c:out value="${d.titre}"/></td>
                        <td>${d.type}</td>
                        <td><a href="${pageContext.request.contextPath}/${d.cheminFichier}" target="_blank">📄 Voir</a></td>
                        <td>
                            <form method="POST" action="${pageContext.request.contextPath}/directeur/conventions" style="margin:0;">
                                <input type="hidden" name="action" value="delierDocument">
                                <input type="hidden" name="conventionId" value="${convention.id}">
                                <input type="hidden" name="documentId" value="${d.id}">
                                <button type="submit" class="cd-remove">Retirer</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty documentsAssocies}">
                    <tr><td colspan="4" style="color:#94A3B8; text-align:center; padding:16px;">Aucun document associé.</td></tr>
                </c:if>
            </tbody>
        </table>

        <form method="POST" action="${pageContext.request.contextPath}/directeur/conventions" class="cd-add-form">
            <input type="hidden" name="action" value="lierDocument">
            <input type="hidden" name="conventionId" value="${convention.id}">
            <select name="documentId" required>
                <option value="">-- Choisir un document --</option>
                <c:forEach var="d" items="${tousDocuments}">
                    <option value="${d.id}"><c:out value="${d.titre}"/></option>
                </c:forEach>
            </select>
            <button type="submit">+ Associer</button>
        </form>
    </div>

</div>