<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<style>
    .form-wrapper-box {
        display: block !important;
        background-color: #FFFFFF !important;
        border: 1px solid #E2E8F0 !important;
        border-radius: 16px !important;
        padding: 32px !important;
        max-width: 720px !important;
        margin: 30px auto !important;
        box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05) !important;
        font-family: 'Segoe UI', system-ui, sans-serif;
    }
    .form-header-title {
        margin-bottom: 24px;
        border-bottom: 1px solid #F1F5F9;
        padding-bottom: 16px;
    }
    .form-header-title h2 {
        margin: 0;
        font-size: 18px;
        color: #0F172A;
        font-weight: 700;
    }
    .form-header-title p {
        margin: 4px 0 0 0;
        font-size: 13px;
        color: #94A3B8;
    }
    .input-field-group {
        margin-bottom: 20px;
        display: flex;
        flex-direction: column;
    }
    .input-field-group label {
        font-size: 13px;
        font-weight: 600;
        color: #475569;
        margin-bottom: 6px;
        text-align: left;
    }
    .custom-input {
        width: 100%;
        background-color: #F8FAFC;
        border: 1px solid #E2E8F0;
        border-radius: 8px;
        padding: 10px 14px;
        font-size: 14px;
        color: #0F172A;
        outline: none;
        box-sizing: border-box;
        transition: border-color 0.2s;
    }
    .custom-input:focus {
        border-color: #D97706;
    }
    .actions-row-btn {
        display: flex;
        justify-content: flex-end;
        gap: 12px;
        margin-top: 28px;
        border-top: 1px solid #F1F5F9;
        padding-top: 20px;
    }
    .submit-btn-save {
        background-color: #0F172A;
        color: white;
        border: none;
        padding: 10px 20px;
        font-size: 13px;
        font-weight: 600;
        border-radius: 8px;
        cursor: pointer;
    }
    .submit-btn-save:hover { background-color: #1E293B; }
    
    .cancel-btn-link {
        background-color: #FFFFFF;
        color: #64748B;
        border: 1px solid #E2E8F0;
        padding: 10px 20px;
        font-size: 13px;
        font-weight: 600;
        border-radius: 8px;
        text-decoration: none;
        display: inline-block;
        text-align: center;
    }
    .cancel-btn-link:hover { background-color: #F1F5F9; color: #0F172A; }
</style>

<div class="form-wrapper-box">
    <div class="form-header-title">
        <h2>
            <c:choose>
                <c:when test="${reunion != null}">Modifier la Réunion &amp; Éditer le PV</c:when>
                <c:otherwise>Planifier une Nouvelle Réunion</c:otherwise>
            </c:choose>
        </h2>
        <p>Veuillez renseigner les détails logistiques et l'ordre du jour du conseil</p>
    </div>

    <form action="${pageContext.request.contextPath}/directeur/reunions" method="POST" enctype="multipart/form-data">
        <input type="hidden" name="action" value="${reunion != null ? 'modifier' : 'ajouter'}">
        
        <c:if test="${reunion != null}">
            <input type="hidden" name="id" value="${reunion.id}">
        </c:if>

        <div class="input-field-group">
            <label for="titre">Intitulé / Thème de la réunion</label>
            <input type="text" id="titre" name="titre" class="custom-input" 
                   value="<c:out value='${reunion.titre}'/>" placeholder="Ex: Conseil de Laboratoire - Budget" required />
        </div>

        <div class="input-field-group">
            <label for="dateReunion">Date de la séance</label>
            <input type="date" id="dateReunion" name="dateReunion" class="custom-input" 
                   value="${reunion != null ? reunion.dateReunion : ''}" required />
        </div>

        <div class="input-field-group">
            <label for="ordreDuJour">Ordre du jour abrégé</label>
            <input type="text" id="ordreDuJour" name="ordreDuJour" class="custom-input" 
                   value="<c:out value='${reunion.ordreDuJour}'/>" placeholder="Ex: Répartition des dotations" required />
        </div>

        <div class="input-field-group" style="margin-top: 24px; border-top: 1px dashed #E2E8F0; padding-top: 20px;">
            <label for="pv" style="color: #D97706;">Procès-verbal de la séance (fichier)</label>
            <input type="file" id="pv" name="pv" class="custom-input"
                   accept=".pdf,.doc,.docx,.xls,.xlsx,.jpg,.jpeg,.png" />
            <small style="color:#64748B;">PDF, Word, Excel, JPG ou PNG, 10 Mo maximum. Facultatif à la création.</small>

            <c:if test="${reunion != null && fn:startsWith(reunion.pvPath, 'uploads/')}">
                <p style="margin-top:10px; font-size:13px;">
                    PV actuel :
                    <a href="${pageContext.request.contextPath}/${reunion.pvPath}" target="_blank">📄 Télécharger</a>
                    <br><span style="color:#64748B;">Envoyer un nouveau fichier remplace ce PV (l'ancien reste archivé sur le serveur).</span>
                </p>
            </c:if>
        </div>

        <div class="actions-row-btn">
            <a href="${pageContext.request.contextPath}/directeur/reunions" class="cancel-btn-link">Annuler</a>
            <button type="submit" class="submit-btn-save">
                <c:choose>
                    <c:when test="${reunion != null}">Enregistrer les modifications</c:when>
                    <c:otherwise>Planifier la réunion</c:otherwise>
                </c:choose>
            </button>
        </div>
    </form>
</div>