<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<style>
    .form-container-card {
        background: #FFFFFF;
        border: 1px solid #E2E8F0;
        border-radius: 12px;
        padding: 32px;
        max-width: 700px;
        margin: 30px auto 0 auto;
        box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05);
        font-family: system-ui, -apple-system, sans-serif;
    }

    .form-container-card h2 {
        margin-top: 0;
        margin-bottom: 6px;
        font-size: 22px;
        color: #0F172A;
        font-weight: 700;
    }

    .form-desc {
        font-size: 13px;
        color: #64748B;
        margin-bottom: 24px;
        display: block;
    }

    .form-grid {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 20px;
    }

    .form-group { margin-bottom: 5px; }
    .form-group.full-width { grid-column: span 2; }

    .form-group label {
        display: block;
        font-size: 13px;
        font-weight: 600;
        color: #334155;
        margin-bottom: 8px;
    }

    .form-control {
        width: 100%;
        background-color: #F8FAFC;
        border: 1px solid #CBD5E1;
        border-radius: 6px;
        padding: 10px 12px;
        font-size: 14px;
        color: #0F172A;
        box-sizing: border-box;
        outline: none;
        transition: all 0.2s;
    }
    .form-control:focus {
        border-color: #D97706;
        background-color: #FFFFFF;
    }

    .form-actions {
        display: flex;
        justify-content: flex-end;
        gap: 12px;
        margin-top: 32px;
        border-top: 1px solid #E2E8F0;
        padding-top: 20px;
    }

    .btn-submit {
        background-color: #0F172A;
        color: #FFFFFF;
        border: none;
        border-radius: 6px;
        padding: 10px 18px;
        font-size: 13px;
        font-weight: 600;
        cursor: pointer;
        transition: background 0.2s;
    }
    .btn-submit:hover { background-color: #1E293B; }

    .btn-cancel {
        background-color: #F1F5F9;
        color: #475569;
        border: none;
        border-radius: 6px;
        padding: 10px 18px;
        font-size: 13px;
        font-weight: 600;
        text-decoration: none;
        transition: background 0.2s;
    }
    .btn-cancel:hover { background-color: #E2E8F0; }
</style>

<div class="form-container-card">
    <h2>Nouveau mandat de direction</h2>
    <span class="form-desc">Désigne un membre permanent comme directeur pour une nouvelle période de gouvernance.</span>

    <form action="${pageContext.request.contextPath}/directeur/mandats" method="POST">
        <input type="hidden" name="action" value="creer" />

        <div class="form-grid">
            <div class="form-group full-width">
                <label for="directeurId">Directeur désigné</label>
                <select id="directeurId" name="directeurId" class="form-control" required>
                    <option value="">-- Choisir un membre permanent --</option>
                    <c:forEach var="m" items="${membres}">
                        <c:if test="${m.statut == 'PERMANENT' && m.actif}">
                            <option value="${m.id}"><c:out value="${m.prenom} ${m.nom}"/></option>
                        </c:if>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label for="dateDebut">Date de début du mandat</label>
                <input type="date" id="dateDebut" name="dateDebut" class="form-control" required />
            </div>

            <div class="form-group">
                <label for="dateFin">Date de fin (facultatif)</label>
                <input type="date" id="dateFin" name="dateFin" class="form-control" />
                <small style="color:#94A3B8;">Laisse vide si le mandat est en cours.</small>
            </div>
        </div>

        <div class="form-actions">
            <a href="${pageContext.request.contextPath}/directeur/mandats" class="btn-cancel">Annuler</a>
            <button type="submit" class="btn-submit">💾 Enregistrer le mandat</button>
        </div>
    </form>
</div>