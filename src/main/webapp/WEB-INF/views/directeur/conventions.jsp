<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<style>
    /* ── EN-TÊTE DE LA PAGE ── */
    .conventions-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 32px;
        margin-top: 10px;
    }
    .conventions-header h2 {
        margin: 0;
        font-size: 22px;
        color: #0F172A;
        font-weight: 700;
        letter-spacing: -0.01em;
    }
    .conventions-header .subtitle {
        font-size: 11px;
        font-weight: 700;
        text-transform: uppercase;
        letter-spacing: 0.08em;
        color: #94A3B8;
        display: inline-block;
        margin-top: 6px;
    }

    /* ── BOUTON ENREGISTRER ── */
    .btn-add-convention {
        background-color: #D97706;
        color: white;
        border: none;
        padding: 10px 20px;
        font-size: 13px;
        font-weight: 600;
        border-radius: 8px;
        cursor: pointer;
        text-decoration: none;
        display: inline-flex;
        align-items: center;
        gap: 8px;
        transition: background 0.2s;
    }
    .btn-add-convention:hover {
        background-color: #B45309;
    }

    /* ── GRILLE ET DESIGN DES CARTES (Style Image d0667f) ── */
    .conventions-grid {
        display: flex;
        flex-direction: column;
        gap: 20px;
    }

    .convention-container-card {
        background: #FFFFFF;
        border: 1px solid #E5E7EB;
        border-top: 4px solid #D97706; /* Rappel de la ligne dorée supérieure */
        border-radius: 16px;
        padding: 24px;
        box-shadow: 0 1px 3px rgba(0,0,0,0.01);
    }

    /* Zone interne grise style d0667f */
    .convention-internal-box {
        background-color: #F8FAFC;
        border-radius: 12px;
        padding: 20px;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    /* Badges Partenaires */
    .badge-partenaire-orange {
        background-color: #FEF3C7;
        color: #B45309;
        padding: 4px 8px;
        border-radius: 6px;
        font-weight: 700;
        font-size: 11px;
        text-transform: uppercase;
        letter-spacing: 0.05em;
        display: inline-block;
        margin-bottom: 8px;
    }

    .convention-title-text {
        font-size: 16px;
        font-weight: 700;
        color: #0F172A;
        margin: 0;
    }

    .convention-dates {
        font-size: 12px;
        color: #64748B;
        margin-top: 6px;
    }

    /* Actions boutons */
    .actions-group {
        display: flex;
        align-items: center;
        gap: 8px;
    }

    .btn-action-edit {
        background-color: #FFFFFF;
        color: #475569;
        border: 1px solid #E2E8F0;
        padding: 8px 14px;
        font-size: 12px;
        font-weight: 600;
        border-radius: 6px;
        cursor: pointer;
        text-decoration: none;
    }
    .btn-action-edit:hover {
        background-color: #F1F5F9;
        color: #0F172A;
    }

    .btn-action-archive {
        background-color: #FFFFFF;
        color: #EF4444;
        border: 1px solid #FEE2E2;
        padding: 8px 14px;
        font-size: 12px;
        font-weight: 600;
        border-radius: 6px;
        cursor: pointer;
    }
    .btn-action-archive:hover {
        background-color: #FEF2F2;
    }
</style>

<div class="conventions-header">
    <div>
        <h2>Conventions et Financements Actifs</h2>
        <span class="subtitle">Partenariats institutionnels ou industriels actifs</span>
    </div>
    <a href="${pageContext.request.contextPath}/directeur/conventions?action=nouveau" class="btn-add-convention">
        + Enregistrer un Nouveau Partenariat
    </a>
</div>

<div class="conventions-grid">
    <c:choose>
        <c:when test="${not empty conventions}">
            <c:forEach var="conv" items="${conventions}">
                
                <div class="convention-container-card">
                    <div class="convention-internal-box">
                        
                        <div>
                            <span class="badge-partenaire-orange">
                                <c:out value="${conv.partenaire}"/>
                            </span>
                            
                            <h3 class="convention-title-text">
                                <c:out value="${conv.titre}"/>
                            </h3>
                            
                            <div class="convention-dates">
                                📅 Période : <c:out value="${conv.dateDebut}"/> 
                                <c:if test="${not empty conv.dateFin}">
                                    au <c:out value="${conv.dateFin}"/>
                                </c:if>
                            </div>
                        </div>

                        <div class="actions-group">
                            <c:if test="${not empty conv.cheminFichier}">
                                <a href="${pageContext.request.contextPath}/${conv.cheminFichier}" target="_blank" class="btn-action-edit">
                                    📄 Fichier
                                </a>
                            </c:if>
                            <a href="${pageContext.request.contextPath}/directeur/conventions?action=voir&id=${conv.id}" class="btn-action-edit">
                                👁 Activités
                            </a>
                            <a href="${pageContext.request.contextPath}/directeur/conventions?action=edit&id=${conv.id}" class="btn-action-edit">
                                Modifier
                            </a>
                            <form action="${pageContext.request.contextPath}/directeur/conventions" method="POST" style="margin: 0;" onsubmit="return confirm('Archiver ce partenariat actif ?');">
                                <input type="hidden" name="action" value="archiver" />
                                <input type="hidden" name="id" value="${conv.id}" />
                                <button type="submit" class="btn-action-archive">Archiver</button>
                            </form>
                        </div>

                    </div>
                </div>

            </c:forEach>
        </c:when>
        <c:otherwise>
            <div style="background: #FFFFFF; border: 1px dashed #CBD5E1; border-radius: 16px; padding: 40px; text-align: center; color: #64748B;">
                Aucun partenariat ou financement actif enregistré pour le moment.
            </div>
        </c:otherwise>
    </c:choose>
</div>