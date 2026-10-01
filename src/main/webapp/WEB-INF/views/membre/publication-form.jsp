<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<a href="${pageContext.request.contextPath}/membre/publications" class="team-link" style="display:inline-block; margin-bottom:20px;">
    ← Retour aux publications
</a>

<div style="margin-bottom:24px;">
    <span class="pub-eyebrow" style="margin-bottom:8px;">Bibliographie du laboratoire</span>
    <h2 style="font-family:var(--pub-display); margin:0; font-size:22px; font-weight:600; color:var(--pub-ink);">
        ${publication != null ? "Modifier la publication" : "Nouvelle publication"}
    </h2>
</div>

<div class="m-card" style="max-width:640px;">

    <form method="post" action="${pageContext.request.contextPath}/membre/publications">

        <c:if test="${publication != null}">
            <input type="hidden" name="id" value="${publication.id}">
            <input type="hidden" name="action" value="modifier">
        </c:if>
        <c:if test="${publication == null}">
            <input type="hidden" name="action" value="ajouter">
        </c:if>

        <div class="m-field">
            <label>Titre de l'article *</label>
            <input type="text" name="titre" value="${publication.titre}" required>
        </div>

        <div class="m-field">
            <label>Auteurs *</label>
            <input type="text" name="auteurs" value="${publication.auteurs}" placeholder="Ex : Nom1 A., Nom2 B." required>
        </div>

        <div class="m-field-row">
            <div class="m-field">
                <label>Année *</label>
                <input type="number" name="annee" value="${not empty publication.annee ? publication.annee : 2026}" min="1900" max="2100" required>
            </div>
            <div class="m-field">
                <label>Type de support *</label>
                <select name="type" required>
                    <option value="" disabled ${empty publication ? 'selected' : ''}>-- Sélectionner --</option>
                    <option value="JOURNAL"    ${publication.type == 'JOURNAL' ? 'selected' : ''}>Journal</option>
                    <option value="CONFERENCE" ${publication.type == 'CONFERENCE' ? 'selected' : ''}>Conférence</option>
                    <option value="LIVRE"      ${publication.type == 'LIVRE' ? 'selected' : ''}>Livre</option>
                    <option value="CHAPITRE"   ${publication.type == 'CHAPITRE' ? 'selected' : ''}>Chapitre</option>
                </select>
            </div>
        </div>

        <div class="m-field">
            <label>Revue / Éditeur (description)</label>
            <textarea name="description" rows="3" placeholder="Ex : IEEE Access, Springer...">${publication.description}</textarea>
        </div>

        <div style="display:flex; gap:10px; justify-content:flex-end; padding-top:18px; border-top:1px solid var(--pub-line);">
            <a href="${pageContext.request.contextPath}/membre/publications" class="btn-outline">Annuler</a>
            <button type="submit" class="btn-accent">
                <i class="fa-solid fa-floppy-disk"></i> ${publication != null ? "Mettre à jour" : "Enregistrer"}
            </button>
        </div>

    </form>

</div>