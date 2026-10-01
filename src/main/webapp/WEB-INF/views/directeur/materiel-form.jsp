<%@ page contentType="text/html;charset=UTF-8" %>

<style>
    .form-container-card {
        background: #FFFFFF;
        border: 1px solid #E2E8F0;
        border-radius: 12px;
        padding: 32px;
        max-width: 650px;
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

    .form-group {
        margin-bottom: 20px;
    }

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
    .btn-submit:hover {
        background-color: #1E293B;
    }

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
    .btn-cancel:hover {
        background-color: #E2E8F0;
    }
</style>

<div class="form-container-card">
    <h2>Nouvelle ressource d'inventaire</h2>
    <span class="form-desc">Ajouter un actif matériel, équipement de calcul ou package logiciel disponible.</span>
    
    <form action="${pageContext.request.contextPath}/directeur/materiel" method="POST">
        <input type="hidden" name="action" value="ajouter" />

        <div class="form-group">
            <label for="nom">Désignation de la Ressource / Matériel</label>
            <input type="text" id="nom" name="nom" class="form-control" placeholder="Ex: Serveur GPU NVIDIA H100, PC Fixe Dell, etc." required />
        </div>

                <div class="form-group">
            <label for="type">Type</label>
            <input type="text" id="type" name="type" class="form-control" placeholder="Ex: Ordinateur, Serveur, Périphérique" required />
        </div>

        <div class="form-group">
            <label for="marque">Marque</label>
            <input type="text" id="marque" name="marque" class="form-control" placeholder="Ex: Dell, Logitech" />
        </div>

        <div class="form-group">
            <label for="quantite_totale">Quantité de pièces reçues</label>
            <input type="number" id="quantite_totale" name="quantite_totale" class="form-control" placeholder="Ex: 3" min="1" required />
        </div>

        <div class="form-group">
            <label for="date_achat">Date d'arrivage</label>
            <input type="date" id="date_achat" name="date_achat" class="form-control" />
        </div>
        <div class="form-actions">
            <a href="${pageContext.request.contextPath}/directeur/materiel" class="btn-cancel">Annuler</a>
            <button type="submit" class="btn-submit">💾 Enregistrer l'actif</button>
        </div>
    </form>
</div>