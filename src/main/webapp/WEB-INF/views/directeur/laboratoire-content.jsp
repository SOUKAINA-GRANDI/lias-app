<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<style>
    .form-card { background:#fff; border:1px solid #E5E7EB; border-radius:14px; padding:24px; max-width:640px; }
    .form-card h2 { margin:0 0 4px; font-size:18px; color:#0F172A; }
    .form-card > p { margin:0 0 20px; font-size:13px; color:#94A3B8; }
    .form-row { display:flex; gap:16px; }
    .form-row .form-group { flex:1; }
    .form-group { margin-bottom:16px; }
    .form-group label { display:block; font-size:12px; font-weight:700; color:#475569; margin-bottom:6px; text-transform:uppercase; letter-spacing:.03em; }
    .form-group input, .form-group textarea {
        width:100%; padding:10px 12px; border:1px solid #E2E8F0; border-radius:8px; font-size:14px; box-sizing:border-box;
    }
    .btn-submit { background:#2563EB; color:#fff; border:none; padding:10px 18px; border-radius:8px; font-weight:600; cursor:pointer; }
</style>

<div class="form-card">
    <h2>Informations du laboratoire</h2>
    <p>Nom, date de création et coordonnées affichées sur le site public et les rapports.</p>

    <form method="POST" action="${pageContext.request.contextPath}/directeur/laboratoire">

        <div class="form-group">
            <label>Nom du laboratoire</label>
            <input type="text" name="nom" value="${laboratoire.nom}" required maxlength="255">
        </div>

        <div class="form-group">
            <label>Description</label>
            <textarea name="description" rows="3">${laboratoire.description}</textarea>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label>Date de création</label>
                <input type="date" name="dateCreation" value="${laboratoire.dateCreation}">
            </div>
            <div class="form-group">
                <label>Téléphone</label>
                <input type="text" name="telephone" value="${laboratoire.telephone}" maxlength="20">
            </div>
        </div>

        <div class="form-group">
            <label>Adresse</label>
            <input type="text" name="adresse" value="${laboratoire.adresse}" maxlength="255">
        </div>

        <div class="form-row">
            <div class="form-group">
                <label>Email de contact</label>
                <input type="email" name="emailContact" value="${laboratoire.emailContact}" maxlength="255">
            </div>
            <div class="form-group">
                <label>Site web</label>
                <input type="text" name="siteWeb" value="${laboratoire.siteWeb}" maxlength="255">
            </div>
        </div>

        <button type="submit" class="btn-submit">Enregistrer</button>
    </form>
</div>
