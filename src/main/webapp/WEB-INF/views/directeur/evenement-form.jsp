<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<style>
    .form-container {
        font-family: Arial, sans-serif;
        background: #ffffff;
        border: 1px solid #e2e8f0;
        border-radius: 16px;
        padding: 30px;
        max-width: 650px;
        box-shadow: 0 4px 6px -1px rgba(0,0,0,0.03);
    }

    .form-title {
        font-size: 16px;
        font-weight: bold;
        color: #1a202c;
        margin-top: 0;
        margin-bottom: 25px;
        border-bottom: 1px solid #edf2f7;
        padding-bottom: 12px;
    }

    .form-grid-layout {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 20px;
    }

    .full-row {
        grid-column: span 2;
    }

    .form-field-group {
        display: flex;
        flex-direction: column;
        gap: 6px;
    }

    .form-field-group label {
        font-size: 13px;
        font-weight: 600;
        color: #4a5568;
    }

    .form-input-element {
        padding: 10px 14px;
        border: 1px solid #cbd5e1;
        border-radius: 8px;
        font-size: 14px;
        color: #334155;
        background-color: #ffffff;
        transition: border-color 0.15s;
    }
    .form-input-element:focus {
        outline: none;
        border-color: #d4af37;
    }

    textarea.form-input-element {
        resize: vertical;
        font-family: inherit;
    }

    .form-action-footer {
        margin-top: 30px;
        padding-top: 15px;
        border-top: 1px solid #edf2f7;
        display: flex;
        gap: 12px;
        justify-content: flex-start;
    }

    .btn-submit-publish {
        background-color: #d4af37;
        color: white;
        font-size: 14px;
        font-weight: bold;
        padding: 11px 24px;
        border: none;
        border-radius: 8px;
        cursor: pointer;
    }
    .btn-submit-publish:hover { background-color: #bfa030; }

    .btn-submit-cancel {
        background-color: #f1f5f9;
        color: #475569;
        font-size: 14px;
        font-weight: bold;
        padding: 11px 24px;
        border: none;
        border-radius: 8px;
        text-decoration: none;
        text-align: center;
    }
    .btn-submit-cancel:hover { background-color: #e2e8f0; }
</style>

<div class="form-container">
    
    <h2 class="form-title">
        <c:choose>
            <c:when test="${not empty evenement}">Modifier l'événement : <c:out value="${evenement.titre}"/></c:when>
            <c:otherwise>Créer un nouvel événement</c:otherwise>
        </c:choose>
    </h2>

    <form action="${pageContext.request.contextPath}/directeur/evenements" method="POST">
        
        <input type="hidden" name="action" value="${not empty evenement ? 'modifier' : 'ajouter'}">
        
        <c:if test="${not empty evenement}">
            <input type="hidden" name="id" value="${evenement.id}">
        </c:if>
            <div class="form-field-group full-row">
                <label>Organisateurs (membres permanents)</label>
                <div style="max-height:180px; overflow-y:auto; border:1px solid #E2E8F0; border-radius:8px; padding:10px;
                            display:grid; grid-template-columns:repeat(auto-fill,minmax(220px,1fr)); gap:6px 14px;">
                    <c:forEach var="m" items="${candidatsOrganisateurs}">
                        <label style="display:flex; align-items:center; gap:8px; font-weight:500; font-size:14px; cursor:pointer;">
                            <input type="checkbox" name="organisateurs" value="${m.id}"
                                   ${organisateurIds.contains(m.id) ? 'checked' : ''}>
                            <c:out value="${m.prenom} ${m.nom}"/>
                        </label>
                    </c:forEach>
                    <c:if test="${empty candidatsOrganisateurs}">
                        <span style="color:#94A3B8;">Aucun membre permanent actif.</span>
                    </c:if>
                </div>
                <small style="color:#64748B;">Les organisateurs cochés reçoivent une notification.</small>
            </div>
            
        <div class="form-grid-layout">
            
            <div class="form-field-group full-row">
                <label>Titre de l'événement *</label>
                <input type="text" name="titre" class="form-input-element" placeholder="Ex: Séminaire sur l'IA Générative" required value="<c:out value='${evenement.titre}'/>">
            </div>

            <div class="form-field-group">
                <label>Type d'événement *</label>
                <select name="type" class="form-input-element" required>
                    <option value="">-- Sélectionner --</option>
                    <option value="SEMINAIRE" ${evenement.type == 'SEMINAIRE' ? 'selected' : ''}>Séminaire</option>
                    <option value="CONFERENCE" ${evenement.type == 'CONFERENCE' ? 'selected' : ''}>Conférence</option>
                    <option value="WORKSHOP" ${evenement.type == 'WORKSHOP' ? 'selected' : ''}>Workshop</option>
                </select>
            </div>

            <div class="form-field-group">
                <label>Lieu / Salle *</label>
                <input type="text" name="lieu" class="form-input-element" placeholder="Ex: Salle des Thèses, FSBM" required value="<c:out value='${evenement.lieu}'/>">
            </div>

            <div class="form-field-group">
                <label>Date et heure de début *</label>
                <input type="datetime-local" name="dateDebut" class="form-input-element" required value="${evenement.dateDebut}">
            </div>

            <div class="form-field-group">
                <label>Date et heure de fin (Optionnel)</label>
                <input type="datetime-local" name="dateFin" class="form-input-element" value="${evenement.dateFin}">
            </div>

            <div class="form-field-group full-row">
                <label>Description ou résumé de l'événement *</label>
                <textarea name="description" rows="5" class="form-input-element" placeholder="Détaillez le programme ou les intervenants de l'événement..." required><c:out value="${evenement.description}"/></textarea>
            </div>

            <div class="form-field-group full-row" style="flex-direction:row; align-items:center; gap:10px;">
                <input type="checkbox" id="ouvertAuxAssocies" name="ouvertAuxAssocies"
                       <c:if test="${empty evenement || evenement.ouvertAuxAssocies}">checked</c:if>>
                <label for="ouvertAuxAssocies" style="margin:0; font-weight:500;">
                    Ouvert aux membres associés
                </label>
            </div>

        </div>

        <div class="form-action-footer">
            <button type="submit" class="btn-submit-publish">
                <c:choose>
                    <c:when test="${not empty evenement}">💾 Enregistrer les modifications</c:when>
                    <c:otherwise>📋 Publier l'événement</c:otherwise>
                </c:choose>
            </button>
            <a href="${pageContext.request.contextPath}/directeur/evenements" class="btn-submit-cancel">Annuler</a>
        </div>

    </form>
</div>