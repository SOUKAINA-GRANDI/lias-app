<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<style>
    .form-card { background:#fff; border:1px solid #E5E7EB; border-radius:14px; padding:24px; max-width:560px; }
    .form-card h2 { margin:0 0 20px; font-size:18px; color:#0F172A; }
    .form-group { margin-bottom:16px; }
    .form-group label { display:block; font-size:12px; font-weight:700; color:#475569; margin-bottom:6px; text-transform:uppercase; letter-spacing:.03em; }
    .form-group input, .form-group textarea {
        width:100%; padding:10px 12px; border:1px solid #E2E8F0; border-radius:8px; font-size:14px; box-sizing:border-box;
    }
    .form-actions { display:flex; gap:10px; margin-top:20px; }
    .btn-submit { background:#2563EB; color:#fff; border:none; padding:10px 18px; border-radius:8px; font-weight:600; cursor:pointer; }
    .btn-cancel { background:#F1F5F9; color:#334155; border:none; padding:10px 18px; border-radius:8px; font-weight:600; text-decoration:none; }
</style>

<div class="form-card">
    <h2>${not empty equipe ? 'Modifier l\'équipe' : 'Nouvelle équipe'}</h2>

    <form method="POST" action="${pageContext.request.contextPath}/directeur/equipes">
        <input type="hidden" name="action" value="${not empty equipe ? 'modifier' : 'ajouter'}">
        <c:if test="${not empty equipe}">
            <input type="hidden" name="id" value="${equipe.id}">
        </c:if>

        <div class="form-group">
            <label>Nom de l'équipe</label>
            <input type="text" name="nom" value="${equipe.nom}" required maxlength="150">
        </div>

        <div class="form-group">
            <label>Description</label>
            <textarea name="description" rows="4">${equipe.description}</textarea>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn-submit">Enregistrer</button>
            <a class="btn-cancel" href="${pageContext.request.contextPath}/directeur/equipes">Annuler</a>
        </div>
    </form>
</div>
