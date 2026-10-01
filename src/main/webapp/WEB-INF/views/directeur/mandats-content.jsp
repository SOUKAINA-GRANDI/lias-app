<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<style>
    /* ── EN-TÊTE DE LA PAGE ── */
    .mandats-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 32px;
        margin-top: 10px;
    }
    .mandats-header h2 {
        margin: 0;
        font-size: 22px;
        color: #0F172A;
        font-weight: 700;
        letter-spacing: -0.01em;
    }
    .mandats-header .subtitle {
        font-size: 11px;
        font-weight: 700;
        text-transform: uppercase;
        letter-spacing: 0.08em;
        color: #94A3B8;
        display: inline-block;
        margin-top: 6px;
    }

    /* ── BOUTON AJOUTER MANDAT (Style Orange Uniforme) ── */
    .btn-add-mandat {
        background-color: #D97706; /* Orange identique aux conventions */
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
    .btn-add-mandat:hover {
        background-color: #B45309;
    }

    /* ── ACTIONS GLOBALES ── */
    .actions-top-container {
        background-color: #FFFFFF;
        border: 1px dashed #CBD5E1;
        border-radius: 12px;
        padding: 20px;
        margin-bottom: 24px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        gap: 16px;
    }

    .btn-action-cloturer {
        background-color: #EF4444;
        color: white;
        border: none;
        padding: 10px 16px;
        font-size: 13px;
        font-weight: 600;
        border-radius: 8px;
        cursor: pointer;
        transition: background 0.2s;
    }
    .btn-action-cloturer:hover {
        background-color: #DC2626;
    }

    /* ── GRILLE ET CARTES DE MANDATS ── */
    .mandats-grid {
        display: flex;
        flex-direction: column;
        gap: 20px;
    }

    .mandat-card {
        background: #FFFFFF;
        border: 1px solid #E5E7EB;
        border-top: 4px solid #D97706; /* Rappel de la ligne dorée supérieure */
        border-radius: 16px;
        padding: 24px;
        box-shadow: 0 1px 3px rgba(0,0,0,0.01);
    }

    .mandat-internal-box {
        background-color: #F8FAFC;
        border-radius: 12px;
        padding: 20px;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    .badge-status-mandat {
        padding: 4px 10px;
        border-radius: 6px;
        font-weight: 700;
        font-size: 11px;
        text-transform: uppercase;
        letter-spacing: 0.05em;
        display: inline-block;
        margin-bottom: 8px;
    }
    .badge-actif { background-color: #DCFCE7; color: #15803D; }
    .badge-termine { background-color: #F1F5F9; color: #64748B; }

    .mandat-title-text {
        font-size: 16px;
        font-weight: 700;
        color: #0F172A;
        margin: 0;
    }

    .mandat-dates {
        font-size: 12px;
        color: #64748B;
        margin-top: 6px;
    }
</style>

<div class="mandats-header">
    <div>
        <h2>Historique &amp; Gestion des Mandats</h2>
        <span class="subtitle">Suivi des périodes de gouvernance du laboratoire</span>
    </div>
    <a href="${pageContext.request.contextPath}/directeur/mandats?action=nouveau" class="btn-add-mandat">
        + Enregistrer un Nouveau Mandat
    </a>
</div>

<c:if test="${not empty sessionScope.error}">
    <div style="margin-bottom: 20px; padding: 12px 16px; border-radius: 8px; font-size: 13px; background: #fee2e2; color: #991b1b; font-weight: 500;">
        ⚠️ Erreur : ${sessionScope.error}
        <c:remove var="error" scope="session"/>
    </div>
</c:if>

<div class="actions-top-container">
    <div>
        <span style="font-size: 14px; font-weight: 600; color: #1E293B; display: block;">Contrôle de la gouvernance actuelle</span>
        <span style="font-size: 12px; color: #64748B;">Mettre fin de manière définitive au mandat en cours d'exécution.</span>
    </div>
    <form action="${pageContext.request.contextPath}/directeur/mandats" method="POST" style="margin: 0;" onsubmit="return confirm('Êtes-vous sûr de vouloir clôturer le mandat actuel ? Cette action est irréversible.');">
        <input type="hidden" name="action" value="cloturer" />
        <button type="submit" class="btn-action-cloturer">
            🔒 Clôturer le Mandat Actuel
        </button>
    </form>
</div>

<div class="mandats-grid">
    <c:choose>
        <c:when test="${not empty mandats}">
            <c:forEach var="m" items="${mandats}">
                
                <div class="mandat-card">
                    <div class="mandat-internal-box">
                        <div>
                            <c:choose>
                                <c:when test="${empty m.dateFin}">
                                    <span class="badge-status-mandat badge-actif">Mandat Actif En Cours</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge-status-mandat badge-termine">Mandat Terminé</span>
                                </c:otherwise>
                            </c:choose>
                            
                            <h3 class="mandat-title-text">
                                Directeur : <c:out value="${m.directeurPrenom} ${m.directeurNom}"/>
                            </h3>
                            
                            <div class="mandat-dates">
                                📅 Période de direction : <c:out value="${m.dateDebut}"/> 
                                <c:choose>
                                    <c:when test="${not empty m.dateFin}">
                                        au <c:out value="${m.dateFin}"/>
                                    </c:when>
                                    <c:otherwise>
                                        • Présent
                                    </c:otherwise>
                                </c:choose>
                            </div>
                                                        <c:if test="${not empty m.viceDirecteurs}">
                                <div style="margin-top:10px; font-size:13px; color:#334155;">
                                    <strong>Vice-directeur(s) :</strong>
                                    <c:forEach var="vd" items="${m.viceDirecteurs}" varStatus="st">
                                        <c:out value="${vd.membrePrenom} ${vd.membreNom}"/><c:if test="${!st.last}">, </c:if>
                                    </c:forEach>
                                </div>
                            </c:if>

                            <c:if test="${not empty m.chefsEquipe}">
                                <div style="margin-top:4px; font-size:13px; color:#334155;">
                                    <strong>Chef(s) d'équipe :</strong>
                                    <c:forEach var="ce" items="${m.chefsEquipe}" varStatus="st">
                                        <c:out value="${ce.membrePrenom} ${ce.membreNom}"/><c:if test="${!st.last}">, </c:if>
                                    </c:forEach>
                                </div>
                            </c:if>

                            <c:if test="${empty m.viceDirecteurs && empty m.chefsEquipe}">
                                <div style="margin-top:10px; font-size:12px; color:#94A3B8; font-style:italic;">
                                    Aucun vice-directeur ni chef d'équipe désigné pour ce mandat.
                                </div>
                            </c:if>
                        </div>
                    </div>
                </div>

            </c:forEach>
        </c:when>
        <c:otherwise>
            <div style="background: #FFFFFF; border: 1px dashed #CBD5E1; border-radius: 16px; padding: 40px; text-align: center; color: #64748B;">
                Aucun historique de mandat enregistré pour le moment dans la base de données.
            </div>
        </c:otherwise>
    </c:choose>
</div>