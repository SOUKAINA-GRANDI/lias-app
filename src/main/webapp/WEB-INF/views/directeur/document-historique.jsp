<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<style>
    .histo-page { font-family: system-ui, -apple-system, sans-serif; color:#1E293B; }
    .histo-head { display:flex; justify-content:space-between; align-items:center; margin-bottom:20px; }
    .histo-back { padding:9px 16px; border:1px solid #CBD5E1; border-radius:8px; background:#fff;
                  color:#334155; font-size:13px; font-weight:600; text-decoration:none; }
    .histo-table { width:100%; border-collapse:collapse; background:#fff; border:1px solid #E2E8F0; border-radius:12px; overflow:hidden; }
    .histo-table th { text-align:left; font-size:12px; text-transform:uppercase; color:#64748B; padding:12px; background:#F8FAFC; }
    .histo-table td { padding:12px; border-top:1px solid #F1F5F9; font-size:14px; }
    .v-badge { padding:3px 10px; border-radius:999px; font-size:12px; font-weight:700; }
    .v-current { background:#DCFCE7; color:#166534; }
    .v-old { background:#F1F5F9; color:#64748B; }
</style>

<div class="histo-page">

    <div class="histo-head">
        <h2 style="margin:0; font-size:20px;">🕘 Historique des versions</h2>
        <a class="histo-back" href="${pageContext.request.contextPath}/directeur/documents">← Retour aux documents</a>
    </div>

    <table class="histo-table">
        <thead>
            <tr>
                <th>Version</th>
                <th>Titre</th>
                <th>Déposé le</th>
                <th>Statut</th>
                <th>Fichier</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="v" items="${versions}">
                <tr>
                    <td><strong>v${v.version}</strong></td>
                    <td><c:out value="${v.titre}"/></td>
                    <td>${v.dateUpload}</td>
                    <td>
                        <c:choose>
                            <c:when test="${v.versionCourante}"><span class="v-badge v-current">Version actuelle</span></c:when>
                            <c:otherwise><span class="v-badge v-old">Ancienne version</span></c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <a href="${pageContext.request.contextPath}/${v.cheminFichier}" target="_blank">📄 Télécharger</a>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty versions}">
                <tr><td colspan="5" style="text-align:center; color:#94A3B8; padding:30px;">Aucune version trouvée.</td></tr>
            </c:if>
        </tbody>
    </table>

</div>