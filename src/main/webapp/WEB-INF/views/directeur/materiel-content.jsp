<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<style>
    /* ── DIRECTIVES GRAPHIQUES & POLICES ── */
    .materiel-dashboard {
        font-family: system-ui, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
        color: #1E293B;
    }

    /* ── EN-TÊTE DE LA PAGE ── */
    .materiel-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 32px;
        margin-top: 10px;
    }
    .materiel-header h2 {
        margin: 0;
        font-size: 24px;
        color: #0F172A;
        font-weight: 700;
        letter-spacing: -0.02em;
    }
    .materiel-header .subtitle {
        font-size: 13px;
        font-weight: 500;
        color: #64748B;
        display: inline-block;
        margin-top: 4px;
    }
    .header-actions {
        display: flex;
        gap: 12px;
    }
    .btn-view-demandes {
        background-color: #FFFFFF;
        color: #334155;
        border: 1px solid #CBD5E1;
        padding: 10px 18px;
        font-size: 13px;
        font-weight: 600;
        border-radius: 8px;
        text-decoration: none;
        display: inline-flex;
        align-items: center;
        gap: 8px;
        transition: all 0.2s;
    }
    .btn-view-demandes:hover {
        background-color: #F8FAFC;
        border-color: #94A3B8;
        color: #0F172A;
    }
    .btn-add-materiel {
        background-color: #D97706;
        color: white;
        border: none;
        padding: 10px 20px;
        font-size: 13px;
        font-weight: 600;
        border-radius: 8px;
        text-decoration: none;
        display: inline-flex;
        align-items: center;
        gap: 8px;
        transition: background 0.2s;
    }
    .btn-add-materiel:hover {
        background-color: #B45309;
    }

    /* ── ALERTE ERREUR ── */
    .alert-error {
        margin-bottom: 24px;
        padding: 14px 18px;
        border-radius: 8px;
        font-size: 13px;
        background: #FEE2E2;
        border: 1px solid #FCA5A5;
        color: #991B1B;
        font-weight: 500;
    }

    /* ── GRILLE DE L'INVENTAIRE ── */
    .materiel-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(340px, 1fr));
        gap: 24px;
    }
    .materiel-card {
        background: #FFFFFF;
        border: 1px solid #E2E8F0;
        border-top: 4px solid #D97706;
        border-radius: 12px;
        padding: 24px;
        box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05);
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        position: relative;
    }
    .card-meta {
        font-size: 11px;
        text-transform: uppercase;
        font-weight: 700;
        color: #94A3B8;
        letter-spacing: 0.05em;
        margin-bottom: 6px;
        display: flex;
        justify-content: space-between;
    }
    .materiel-info h3 {
        font-size: 18px;
        font-weight: 700;
        color: #0F172A;
        margin: 0 0 4px 0;
    }
    .card-brand {
        font-size: 13px;
        color: #64748B;
        font-weight: 500;
        margin-bottom: 16px;
    }

    /* ── COMPTEURS STOCK ── */
    .stock-container {
        display: flex;
        gap: 12px;
        margin-bottom: 20px;
    }
    .stock-box {
        flex: 1;
        background-color: #F8FAFC;
        border: 1px solid #E2E8F0;
        border-radius: 8px;
        padding: 10px 12px;
        text-align: center;
    }
    .stock-box.highlight {
        background-color: #FEF3C7;
        border-color: #FDE68A;
    }
    .stock-box.alert {
        background-color: #FEE2E2;
        border-color: #FCA5A5;
    }
    .stock-box .label {
        font-size: 11px;
        color: #64748B;
        font-weight: 600;
        display: block;
        margin-bottom: 4px;
    }
    .stock-box .count {
        font-size: 18px;
        font-weight: 800;
        color: #0F172A;
    }

    /* ── ZONE ATTRIBUTION DIRECTE ── */
    .attribution-form {
        border-top: 1px solid #E2E8F0;
        padding-top: 16px;
        margin-top: auto;
    }
    .attribution-form label {
        font-size: 12px;
        font-weight: 600;
        color: #475569;
        display: block;
        margin-bottom: 8px;
    }
    .attribution-row {
        display: flex;
        flex-direction: column;
        gap: 10px;
    }
    .select-membre {
        width: 100%;
        background-color: #FFFFFF;
        border: 1px solid #CBD5E1;
        border-radius: 6px;
        padding: 8px 10px;
        font-size: 13px;
        color: #1E293B;
        outline: none;
        box-sizing: border-box;
    }
    .select-membre:focus {
        border-color: #D97706;
        box-shadow: 0 0 0 2px rgba(217, 119, 6, 0.1);
    }
    .btn-attribuer {
        background-color: #0F172A;
        color: white;
        border: none;
        border-radius: 6px;
        padding: 10px 12px;
        font-size: 13px;
        font-weight: 600;
        cursor: pointer;
        transition: background 0.2s;
        text-align: center;
    }
    .btn-attribuer:hover {
        background-color: #1E293B;
    }
    .btn-attribuer:disabled {
        background-color: #94A3B8;
        cursor: not-allowed;
    }
</style>

<div class="materiel-dashboard">
    <!-- En-tête -->
    <div class="materiel-header">
        <div>
            <h2>Inventaire &amp; Ressources</h2>
            <span class="subtitle">Suivi et distribution des actifs et équipements du laboratoire</span>
        </div>
        <div class="header-actions">
            <a href="${pageContext.request.contextPath}/directeur/materiel?action=suivi" class="btn-view-demandes">
                ⚖️ Suivi de distribution
            </a>
            <a href="${pageContext.request.contextPath}/directeur/materiel?action=demandes" class="btn-view-demandes">
                📥 Voir les Demandes de Matériel
            </a>
            <a href="${pageContext.request.contextPath}/directeur/materiel?action=nouveau" class="btn-add-materiel">
                + Enregistrer une Ressource
            </a>
        </div>
    </div>

    <!-- Gestion des notifications d'erreurs -->
    <c:if test="${not empty sessionScope.error}">
        <div class="alert-error">
            ⚠️ Erreur système : <c:out value="${sessionScope.error}"/>
            <c:remove var="error" scope="session"/>
        </div>
    </c:if>

    <!-- Grille des Matériels -->
    <div class="materiel-grid">
        <c:choose>
            <c:when test="${not empty materiels}">
                <c:forEach var="mat" items="${materiels}">
                    <div class="materiel-card">
                        
                        <!-- Méta-informations (Type & Date d'achat) -->
                        <div class="card-meta">
                            <span><c:out value="${mat.type}"/></span>
                            <span>
                                <fmt:formatDate value="${mat.dateAchat}" pattern="dd MMM yyyy"/>
                            </span>
                        </div>
                        
                        <!-- Description du produit -->
                        <div class="materiel-info">
                            <h3><c:out value="${mat.nom}"/></h3>
                            <div class="card-brand">Marque : <strong><c:out value="${mat.marque}"/></strong></div>
                            
                            <!-- Blocs de Stock Distincts -->
                            <div class="stock-container">
                                <div class="stock-box">
                                    <span class="label">Total Doté</span>
                                    <span class="count"><c:out value="${mat.quantiteTotale}"/></span>
                                </div>
                                
                                <!-- Changement de style dynamique si le stock disponible est à zéro -->
                                <div class="stock-box ${mat.quantiteDisponible == 0 ? 'alert' : 'highlight'}">
                                    <span class="label">Disponible</span>
                                    <span class="count"><c:out value="${mat.quantiteDisponible}"/></span>
                                </div>
                            </div>
                        </div>

                        <!-- Formulaire d'affectation manuelle -->
                        <form action="${pageContext.request.contextPath}/directeur/materiel" method="POST" class="attribution-form">
                            <input type="hidden" name="action" value="attribuer" />
                            <input type="hidden" name="materielId" value="${mat.id}" />
                            
                            <label>Affectation directe (les moins servis en tête) :</label>                            <div class="attribution-row">
                                <select name="membreId" class="select-membre" required ${mat.quantiteDisponible == 0 ? 'disabled' : ''}>
                                    <option value="" disabled selected>
                                        ${mat.quantiteDisponible == 0 ? 'Aucun stock disponible' : '-- Choisir le membre bénéficiaire --'}
                                    </option>
                                                                        <c:forEach var="s" items="${suiviMembres}">
                                        <option value="${s.id}">
                                            <c:out value="${s.nom} ${s.prenom}"/> — ${s.total} attribution(s)
                                        </option>
                                    </c:forEach>
                                </select>
                                <button type="submit" class="btn-attribuer" ${mat.quantiteDisponible == 0 ? 'disabled' : ''}>
                                    Assigner la ressource
                                </button>
                            </div>
                        </form>
                        
                    </div>
                </c:forEach>
            </c:when>
            
            <c:otherwise>
                <div style="grid-column: 1 / -1; background: #FFFFFF; border: 1px dashed #CBD5E1; border-radius: 12px; padding: 50px; text-align: center; color: #64748B;">
                    <div style="font-size: 24px; margin-bottom: 10px;">📦</div>
                    Aucune ressource ou matériel enregistré dans la base de données actuelle.
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>