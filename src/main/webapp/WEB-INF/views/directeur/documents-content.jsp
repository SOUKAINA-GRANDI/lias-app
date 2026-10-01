<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<style>
    .docs-top { display:flex; justify-content:space-between; align-items:center; margin-bottom:16px; }
    .docs-top h2 { margin:0; font-size:20px; color:#0F172A; }
    .btn-add { background:#2563EB; color:#fff; padding:10px 16px; border-radius:8px; font-weight:600; text-decoration:none; font-size:14px; }
    .msg-ok  { margin-bottom:16px; padding:12px; background:#DCFCE7; color:#166534; border-radius:6px; font-size:14px; font-weight:500; }
    .msg-err { margin-bottom:16px; padding:12px; background:#FEE2E2; color:#991B1B; border-radius:6px; font-size:14px; font-weight:500; }
    .docs-filters { display:flex; gap:10px; margin-bottom:16px; }
    .docs-filters input, .docs-filters select { padding:9px 12px; border:1px solid #E2E8F0; border-radius:8px; font-size:14px; }
    .docs-filters button { padding:9px 16px; border:none; border-radius:8px; background:#F1F5F9; font-weight:600; cursor:pointer; }
    .docs-table { width:100%; border-collapse:collapse; background:#fff; border:1px solid #E5E7EB; border-radius:12px; overflow:hidden; }
    .docs-table th { text-align:left; font-size:12px; text-transform:uppercase; color:#64748B; padding:12px; background:#F8FAFC; }
    .docs-table td { padding:12px; border-top:1px solid #F1F5F9; font-size:14px; color:#0F172A; }
    .tag { background:#E0F2FE; color:#0369A1; padding:2px 8px; border-radius:999px; font-size:12px; font-weight:600; }
    .tag-arch { background:#F1F5F9; color:#64748B; }
    .row-actions { display:flex; gap:8px; align-items:center; flex-wrap:wrap; }
    .row-actions form { margin:0; display:flex; align-items:center; gap:6px; }
    .row-actions a, .row-actions button { padding:6px 10px; border:1px solid #E2E8F0; border-radius:6px; background:#fff; color:#334155; font-size:13px; text-decoration:none; cursor:pointer; }
    .row-actions input[type="file"] { font-size:12px; max-width:130px; }
</style>

<div class="docs-top">
    <h2>Documents</h2>
    <a class="btn-add" href="${pageContext.request.contextPath}/directeur/documents?action=nouveau">
        <i class="fa-solid fa-plus"></i> Ajouter un document
    </a>
</div>

<c:if test="${not empty sessionScope.success}">
    <div class="msg-ok">✅ <c:out value="${sessionScope.success}"/></div>
    <c:remove var="success" scope="session"/>
</c:if>
<c:if test="${not empty sessionScope.error}">
    <div class="msg-err">⚠️ <c:out value="${sessionScope.error}"/></div>
    <c:remove var="error" scope="session"/>
</c:if>


<div style="display:flex; gap:8px; margin-bottom:16px;">
    <a href="${pageContext.request.contextPath}/directeur/documents?filtre=actifs"
       style="padding:8px 14px; border-radius:8px; text-decoration:none; font-size:14px; font-weight:600;
              ${filtre == 'actifs' ? 'background:#2563EB; color:#fff;' : 'background:#F1F5F9; color:#334155;'}">
        Actifs (${countActifs})
    </a>
    <a href="${pageContext.request.contextPath}/directeur/documents?filtre=archives"
       style="padding:8px 14px; border-radius:8px; text-decoration:none; font-size:14px; font-weight:600;
              ${filtre == 'archives' ? 'background:#2563EB; color:#fff;' : 'background:#F1F5F9; color:#334155;'}">
        Archivés (${countArchives})
    </a>
    <a href="${pageContext.request.contextPath}/directeur/documents?filtre=tous"
       style="padding:8px 14px; border-radius:8px; text-decoration:none; font-size:14px; font-weight:600;
              ${filtre == 'tous' ? 'background:#2563EB; color:#fff;' : 'background:#F1F5F9; color:#334155;'}">
        Tous
    </a>
</div>

<form class="docs-filters" method="GET" action="${pageContext.request.contextPath}/directeur/documents">
    <input type="text" name="q" placeholder="Rechercher par titre..." value="<c:out value='${filtreQ}'/>">
    <select name="type">
        <option value="">Tous les types</option>
        <c:forEach var="t" items="${typesDocument}">
            <option value="${t}" ${filtreType == t ? 'selected' : ''}>${t}</option>
        </c:forEach>
    </select>
    <button type="submit">Filtrer</button>
</form>

<table class="docs-table">
    <thead>
        <tr>
            <th>Titre</th>
            <th>Type</th>
            <th>Événement</th>
            <th>Date</th>
            <th>Statut</th>
            <th>Actions</th>
        </tr>
    </thead>
    <tbody>
        <c:choose>
            <c:when test="${not empty documents}">
                <c:forEach var="d" items="${documents}">
                    <tr>
                        <td><c:out value="${d.titre}"/></td>
                        <td><span class="tag">${d.type}</span></td>
                        <td><c:out value="${d.evenementTitre}" default="—"/></td>
                        <td>${d.dateUpload}</td>
                        <td>
                            <c:choose>
                                <c:when test="${d.archive}"><span class="tag tag-arch">Archivé</span></c:when>
                                <c:otherwise>Actif</c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <div class="row-actions">
                                <a href="${pageContext.request.contextPath}/${d.cheminFichier}" target="_blank">
                                    <i class="fa-solid fa-download"></i> Télécharger
                                </a>

                                <a href="${pageContext.request.contextPath}/directeur/documents?action=historique&id=${d.id}">
                                    🕘 Historique<c:if test="${d.version > 1}"> (v${d.version})</c:if>
                                </a>

                                <form method="POST" action="${pageContext.request.contextPath}/directeur/documents"
                                      enctype="multipart/form-data">
                                    <input type="hidden" name="action" value="remplacer">
                                    <input type="hidden" name="id" value="${d.id}">
                                    <input type="file" name="nouveauFichier" required
                                           accept=".pdf,.doc,.docx,.xls,.xlsx,.jpg,.jpeg,.png">
                                    <button type="submit">🔄 Remplacer</button>
                                </form>

                                <form method="POST" action="${pageContext.request.contextPath}/directeur/documents">
                                    <input type="hidden" name="id" value="${d.id}">
                                    <c:choose>
                                        <c:when test="${d.archive}">
                                            <input type="hidden" name="action" value="desarchiver">
                                            <button type="submit">Désarchiver</button>
                                        </c:when>
                                        <c:otherwise>
                                            <input type="hidden" name="action" value="archiver">
                                            <button type="submit">Archiver</button>
                                        </c:otherwise>
                                    </c:choose>
                                </form>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <tr>
                    <td colspan="6" style="text-align:center; padding:40px; color:#94A3B8;">
                        Aucun document trouvé.
                    </td>
                </tr>
            </c:otherwise>
        </c:choose>
    </tbody>
</table>