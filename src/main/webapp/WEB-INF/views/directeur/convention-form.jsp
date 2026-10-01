<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<style>
    .form-container { background: #FFFFFF; border: 1px solid #E5E7EB; border-radius: 16px; padding: 32px; max-width: 800px; margin-top: 10px; box-shadow: 0 1px 3px rgba(0,0,0,0.01); }
    .form-title { margin: 0 0 24px 0; font-size: 18px; color: #0F172A; font-weight: 700; }
    
    .form-group { margin-bottom: 20px; display: flex; flex-direction: column; gap: 6px; }
    .form-row { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; }
    
    label { font-size: 13px; font-weight: 600; color: #475569; }
    input[type="text"], input[type="date"] { width: 100%; border: 1px solid #E2E8F0; border-radius: 8px; padding: 10px 14px; font-size: 14px; color: #0F172A; background-color: #F8FAFC; box-sizing: border-box; transition: border-color 0.2s; }
    input:focus { outline: none; border-color: #D97706; background-color: #FFFFFF; }
    
    .actions-row { display: flex; gap: 12px; margin-top: 32px; }
    .btn-save { background-color: #0F172A; color: white; border: none; padding: 10px 20px; font-size: 13px; font-weight: 600; border-radius: 8px; cursor: pointer; display: inline-flex; align-items: center; gap: 6px; }
    .btn-save:hover { background-color: #1E293B; }
    .btn-cancel { background-color: #F1F5F9; color: #475569; border: none; padding: 10px 20px; font-size: 13px; font-weight: 600; border-radius: 8px; cursor: pointer; text-decoration: none; text-align: center; }
    .btn-cancel:hover { background-color: #E2E8F0; color: #0F172A; }
</style>

<div class="form-container">
    <h3 class="form-title">
        <c:choose>
            <c:when test="${not empty convention}">Modifier la convention : <c:out value="${convention.titre}"/></c:when>
            <c:otherwise>Enregistrer une Nouvelle Convention</c:otherwise>
        </c:choose>
    </h3>

    <form action="${pageContext.request.contextPath}/directeur/conventions" method="POST" enctype="multipart/form-data">
        <input type="hidden" name="action" value="${not empty convention ? 'modifier' : 'ajouter'}" />
        <c:if test="${not empty convention}">
            <input type="hidden" name="id" value="${convention.id}" />
        </c:if>

        <div class="form-group">
            <label for="titre">Titre de la convention</label>
            <input type="text" id="titre" name="titre" required placeholder="Ex: Convention de partenariat de recherche IA" value="<c:out value='${convention.titre}'/>" />
        </div>

        <div class="form-group">
            <label for="partenaire">Organisme partenaire</label>
            <input type="text" id="partenaire" name="partenaire" required placeholder="Ex: Université Hassan II / Entreprise X" value="<c:out value='${convention.partenaire}'/>" />
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="dateDebut">Date de début</label>
                <input type="date" id="dateDebut" name="dateDebut" required value="${convention.dateDebut}" />
            </div>
            
            <div class="form-group">
                <label for="dateFin">Date de fin (Optionnel)</label>
                <input type="date" id="dateFin" name="dateFin" value="${convention.dateFin}" />
            </div>
        </div>

        <div class="form-group">
            <label for="description">Description (Optionnel)</label>
            <textarea id="description" name="description" rows="3" style="width:100%; border:1px solid #E2E8F0; border-radius:8px; padding:10px 14px; font-size:14px; box-sizing:border-box; font-family:inherit;">${convention.description}</textarea>
        </div>

        <div class="form-group">
            <label for="fichier">Fichier de la convention (PDF...)</label>
            <input type="file" id="fichier" name="fichier" ${empty convention ? 'required' : ''} />
            <c:if test="${not empty convention.cheminFichier}">
                <span style="font-size:12px; color:#64748B;">
                    Fichier actuel :
                    <a href="${pageContext.request.contextPath}/${convention.cheminFichier}" target="_blank">le consulter</a>
                    — laissez ce champ vide pour le conserver.
                </span>
            </c:if>
        </div>

        <div class="actions-row">
            <button type="submit" class="btn-save">
                💾 Enregistrer les modifications
            </button>
            <a href="${pageContext.request.contextPath}/directeur/conventions" class="btn-cancel">
                Annuler
            </a>
        </div>
    </form>
</div>