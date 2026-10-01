<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<style>
    .docs-header { margin:10px 0 20px; }
    .docs-header h2 { margin:0; font-size:20px; color:#0F172A; }

    .docs-filters { display:flex; gap:10px; margin-bottom:18px; flex-wrap:wrap; }
    .docs-filters input, .docs-filters select {
        padding:8px 12px; border:1px solid #E2E8F0; border-radius:8px; font-size:13px;
    }
    .docs-filters button {
        padding:8px 14px; border:none; border-radius:8px; background:#334155; color:#fff; font-size:13px; cursor:pointer;
    }

    .docs-grid { display:grid; grid-template-columns:repeat(auto-fill, minmax(260px, 1fr)); gap:14px; }
    .doc-card { background:#fff; border:1px solid #E5E7EB; border-radius:14px; padding:16px 18px; }
    .doc-card .doc-icon { font-size:26px; margin-bottom:8px; }
    .doc-card h4 { margin:0 0 4px; font-size:14px; color:#1E293B; }
    .doc-type-tag {
        display:inline-block; font-size:10.5px; font-weight:700; text-transform:uppercase;
        padding:2px 8px; border-radius:999px; background:#F1F5F9; color:#475569; margin-bottom:8px;
    }
    .doc-meta { font-size:11.5px; color:#94A3B8; margin-bottom:12px; }
    .doc-card a.dl {
        font-size:12px; padding:6px 10px; border-radius:8px; border:1px solid #E2E8F0;
        background:#fff; color:#334155; text-decoration:none;
    }
    .doc-card a.dl:hover { background:#F1F5F9; }
</style>

<div class="docs-header">
    <h2>Documents</h2>
</div>

<form class="docs-filters" method="GET" action="${pageContext.request.contextPath}/membre/documents">
    <input type="text" name="q" placeholder="Rechercher par titre..." value="${filtreQ}">
    <select name="type">
        <option value="">Tous les types</option>
        <c:forEach var="t" items="${typesDocument}">
            <option value="${t}" ${filtreType == t ? 'selected' : ''}>${t}</option>
        </c:forEach>
    </select>
    <button type="submit">Filtrer</button>
</form>

<div class="docs-grid">
    <c:choose>
        <c:when test="${not empty documents}">
            <c:forEach var="d" items="${documents}">
                <div class="doc-card">
                    <div class="doc-icon">${d.icone}</div>
                    <span class="doc-type-tag">${d.type}</span>
                    <h4>${d.titre}</h4>
                    <div class="doc-meta">
                        ${d.dateUpload}
                        <c:if test="${not empty d.evenementTitre}"> · Événement : ${d.evenementTitre}</c:if>
                    </div>
                    <a class="dl" href="${pageContext.request.contextPath}/${d.cheminFichier}" target="_blank">
                        <i class="fa-solid fa-download"></i> Télécharger
                    </a>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <div style="grid-column:1/-1; text-align:center; padding:60px 20px; color:#94A3B8;">
                Aucun document trouvé.
            </div>
        </c:otherwise>
    </c:choose>
</div>
