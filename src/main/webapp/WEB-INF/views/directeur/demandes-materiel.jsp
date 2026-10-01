<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<style>
    .demandes-header {
        margin-bottom: 32px;
        margin-top: 10px;
    }
    .demandes-header h2 {
        margin: 0;
        font-size: 22px;
        color: #0F172A;
        font-weight: 700;
    }
    .demandes-header .subtitle {
        font-size: 11px;
        font-weight: 700;
        text-transform: uppercase;
        letter-spacing: 0.08em;
        color: #94A3B8;
        display: inline-block;
        margin-top: 6px;
    }

    .section-indicator {
        font-size: 14px;
        font-weight: 700;
        color: #0F172A;
        margin-bottom: 20px;
        display: flex;
        align-items: center;
        gap: 8px;
        border-bottom: 1px solid #E2E8F0;
        padding-bottom: 12px;
    }
    .orange-dot {
        width: 8px;
        height: 8px;
        background-color: #D97706;
        border-radius: 50%;
    }

    .demandes-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(360px, 1fr));
        gap: 24px;
    }

    .demande-card {
        background: #FFFFFF;
        border: 1px solid #E5E7EB;
        border-radius: 16px;
        padding: 24px;
        box-shadow: 0 1px 3px rgba(0,0,0,0.02);
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        gap: 14px;
    }

    .demande-top {
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
        margin-bottom: 6px;
    }

    .applicant-name {
        font-size: 16px;
        font-weight: 700;
        color: #0F172A;
        margin: 0;
    }

    .motivation-section {
        margin-bottom: 10px;
        margin-top: 10px;
    }

    .motivation-label {
        font-size: 13px;
        font-weight: 600;
        color: #475569;
        margin-bottom: 6px;
        display: block;
    }

    .motivation-text {
        font-size: 13px;
        color: #1E293B;
        background-color: #F8FAFC;
        padding: 12px;
        border-radius: 8px;
        border: 1px solid #F1F5F9;
        line-height: 1.5;
    }

    .demande-actions-row {
        display: flex;
        gap: 10px;
        border-top: 1px solid #F1F5F9;
        padding-top: 16px;
    }

    .btn-validate-green {
        flex-grow: 1;
        background-color: #00A669;
        color: white;
        border: none;
        padding: 10px;
        font-size: 13px;
        font-weight: 600;
        border-radius: 8px;
        cursor: pointer;
        transition: background 0.2s;
    }
    .btn-validate-green:hover {
        background-color: #008754;
    }

    .btn-refuse-gray {
        background-color: #F1F5F9;
        color: #475569;
        border: none;
        padding: 10px 16px;
        font-size: 13px;
        font-weight: 600;
        border-radius: 8px;
        cursor: pointer;
    }
    .btn-refuse-gray:hover {
        background-color: #E2E8F0;
        color: #0F172A;
    }
</style>

<div class="demandes-header">
    <h2>Avis Académiques &amp; Validation des Besoins</h2>
    <span class="subtitle">Dossiers d'adhésion en attente et demandes de subventions logicielles/matérielles</span>
</div>

<div class="section-indicator">
    <div class="orange-dot"></div>
    <span>Demandes de Ressources et Matériels en Attente d'Avis</span>
</div>

<div class="demandes-grid">
    <c:choose>
        <c:when test="${not empty demandes}">
            <c:forEach var="dem" items="${demandes}">
                <div class="demande-card">
                    <div>
                        <div class="demande-top">
                            <div>
                                <h3 class="applicant-name">Demande #<c:out value="${dem.id}"/></h3>
                                <span style="font-size: 12px; color: #94A3B8; font-family: monospace; display: block; margin-top: 2px;">
                                    Chercheur ID: <c:out value="${dem.membreId}"/> • Qté: <c:out value="${dem.quantiteExtraite}"/>
                                </span>
                            </div>
                            
                            <div>
                                <c:choose>
                                    <c:when test="${dem.urgenceExtraite eq 'Critique'}">
                                        <span style="background: #fef2f2; color: #ef4444; padding: 4px 8px; border-radius: 6px; font-size: 11px; font-weight: 700; text-transform: uppercase;">Critique</span>
                                    </c:when>
                                    <c:when test="${dem.urgenceExtraite eq 'Urgente'}">
                                        <span style="background: #fff7ed; color: #f97316; padding: 4px 8px; border-radius: 6px; font-size: 11px; font-weight: 700; text-transform: uppercase;">Urgente</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span style="background: #f0fdf4; color: #16a34a; padding: 4px 8px; border-radius: 6px; font-size: 11px; font-weight: 700; text-transform: uppercase;">Normale</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>

                        <div class="motivation-section">
                            <span class="motivation-label">Matériel demandé :</span>
                            <div class="motivation-text" style="font-weight: 600; color: #0F172A; margin-bottom: 12px;">
                                🔧 <c:out value="${dem.designationExtraite}"/>
                            </div>
                            
                            <span class="motivation-label">Justification scientifique :</span>
                            <div class="motivation-text" style="color: #475569; font-style: italic;">
                                <c:out value="${dem.justificationExtraite}"/>
                            </div>
                        </div>
                    </div>

                    <div class="demande-actions-row">
                        <form action="${pageContext.request.contextPath}/directeur/materiel" method="POST" style="flex-grow: 1; margin: 0;">
                            <input type="hidden" name="action" value="validerDemande" />
                            <input type="hidden" name="demandeId" value="${dem.id}" />
                            <button type="submit" class="btn-validate-green" onclick="return confirm('Accorder ce matériel au chercheur ?');">✓ Valider la demande</button>
                        </form>

                        <form action="${pageContext.request.contextPath}/directeur/materiel" method="POST" style="margin: 0;">
                            <input type="hidden" name="action" value="refuserDemande" />
                            <input type="hidden" name="demandeId" value="${dem.id}" />
                            <button type="submit" class="btn-refuse-gray" onclick="return confirm('Refuser cette demande ?');">Refuser</button>
                        </form>
                    </div>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <div style="grid-column: 1 / -1; background: #FFFFFF; border: 1px dashed #CBD5E1; border-radius: 16px; padding: 40px; text-align: center; color: #64748B;">
                Aucune demande de ressource en attente d'avis pour le moment.
            </div>
        </c:otherwise>
    </c:choose>
</div>