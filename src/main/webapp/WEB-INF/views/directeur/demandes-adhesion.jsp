<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<style>
    .candidature-container {
        font-family: Arial, sans-serif;
        padding: 10px;
        background-color: #fcfbfa;
    }

    .main-title {
        font-size: 16px;
        font-weight: bold;
        color: #2d3748;
        border-bottom: 2px solid #2d3748;
        padding-bottom: 8px;
        margin-bottom: 25px;
        display: flex;
        align-items: center;
        gap: 8px;
    }

    .main-title::before {
        content: "●";
        color: #ff9800;
        font-size: 18px;
    }

    /* 🗂️ GRILLE 2 COLONNES CÔTE À CÔTE */
    .candidatures-grid-layout {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(450px, 1fr));
        gap: 25px;
    }

    .custom-card {
        background: #ffffff;
        border: 1px solid #e2e8f0;
        border-top: 4px solid #d4af37;
        border-radius: 20px;
        padding: 22px;
        box-shadow: 0 4px 6px -1px rgba(0,0,0,0.05);
        display: flex;
        flex-direction: column;
        justify-content: space-between;
    }

    .card-header-row {
        display: flex;
        justify-content: space-between;
        align-items: baseline;
        margin-bottom: 5px;
    }

    .applicant-name {
        font-size: 18px;
        font-weight: bold;
        color: #1a202c;
        margin: 0;
    }

    .statut-request {
        font-size: 11px;
        font-family: monospace;
        color: #d4af37;
        font-weight: bold;
    }

    .statut-value {
        color: #c05621;
    }

    .applicant-email {
        font-size: 13px;
        color: #a0aec0;
        font-style: italic;
        font-family: monospace;
        margin-bottom: 12px;
        display: block;
    }

    .motivation-title {
        font-size: 14px;
        font-weight: bold;
        color: #4a5568;
        margin-bottom: 6px;
    }

    .motivation-content {
        font-size: 14px;
        color: #4a5568;
        line-height: 1.5;
        margin-bottom: 15px;
        min-height: 60px;
    }

    .card-divider {
        border-top: 1px solid #edf2f7;
        margin-bottom: 15px;
        margin-top: auto;
    }

    .action-row {
        display: flex;
        gap: 10px;
        align-items: center;
    }

    .btn-submit-validate {
        background-color: #00965e;
        color: white;
        font-size: 14px;
        font-weight: bold;
        padding: 10px 18px;
        border: none;
        border-radius: 8px;
        cursor: pointer;
        flex-grow: 1;
        text-align: center;
    }
    .btn-submit-validate:hover {
        background-color: #007d4f;
    }

    .btn-submit-refuse {
        background-color: #f1f5f9;
        color: #4a5568;
        font-size: 14px;
        font-weight: bold;
        padding: 10px 18px;
        border: none;
        border-radius: 8px;
        cursor: pointer;
    }
    .btn-submit-refuse:hover {
        background-color: #e2e8f0;
    }

    .btn-submit-cv {
        background-color: #ebf8ff;
        color: #2b6cb0;
        font-size: 14px;
        font-weight: bold;
        padding: 10px 15px;
        border: 1px solid #bee3f8;
        border-radius: 8px;
        text-decoration: none;
        text-align: center;
        display: inline-flex;
        align-items: center;
        justify-content: center;
    }
    .btn-submit-cv:hover {
        background-color: #bee3f8;
    }
</style>

<div class="candidature-container">

   <c:if test="${not empty sessionScope.success}">
        <div class="alert-notification alert-success" style="padding: 14px 20px; border-radius: 10px; font-size: 14px; font-weight: 500; margin-bottom: 20px; display: flex; align-items: center; gap: 10px; background-color: #ecfdf5; border: 1px solid #a7f3d0; color: #065f46; font-family: Arial, sans-serif;">
            <span>✅</span> <strong>Notification :</strong> <c:out value="${sessionScope.success}"/>
        </div>
        <c:remove var="success" scope="session" />
    </c:if>

    <c:if test="${not empty sessionScope.error}">
        <div class="alert-notification alert-danger" style="padding: 14px 20px; border-radius: 10px; font-size: 14px; font-weight: 500; margin-bottom: 20px; display: flex; align-items: center; gap: 10px; background-color: #fef2f2; border: 1px solid #fecaca; color: #991b1b; font-family: Arial, sans-serif;">
            <span>❌</span> <strong>Erreur :</strong> <c:out value="${sessionScope.error}"/>
        </div>
        <c:remove var="error" scope="session" />
    </c:if>
    
    <div class="main-title">
        Candidatures d'Adhésion en Attente d'Avis (CV & Lettre de Motivation)
    </div>

    <c:choose>
        <c:when test="${empty demandes}">
            <div style="padding: 20px; color: #718096;">Aucune candidature disponible.</div>
        </c:when>
        <c:otherwise>
            <div class="candidatures-grid-layout">
                
                <c:forEach var="d" items="${demandes}">
                    
                    <c:set var="parts" value="${fn:split(d.cvPath, '/')}" />
                    <c:set var="filename" value="${parts[fn:length(parts) - 1]}" />

                    <div class="custom-card">
                        
                        <div>
                            <div class="card-header-row">
                                <h2 class="applicant-name">
                                    <c:out value="${d.nom} ${d.prenom}"/>
                                </h2>
                                <span class="statut-request">Demande : <span class="statut-value"><c:out value="${d.statut}"/></span></span>
                            </div>

                            <span class="applicant-email"><c:out value="${d.email}"/></span>
                            <div style="margin:10px 0; font-size:13px; color:#475569; line-height:1.7;">
                                <c:if test="${not empty d.statutVise}">
                                    <div><strong>Statut souhaité :</strong> <c:out value="${d.statutVise}"/></div>
                                </c:if>
                                <c:if test="${not empty d.etablissement}">
                                    <div><strong>Établissement :</strong> <c:out value="${d.etablissement}"/></div>
                                </c:if>
                                <c:if test="${not empty d.equipeNom}">
                                    <div><strong>Équipe souhaitée :</strong> <c:out value="${d.equipeNom}"/></div>
                                </c:if>
                            </div>
                            <div class="motivation-title">Motivation :</div>
                            <div class="motivation-content">
                                <c:out value="${d.motivation}"/>
                            </div>
                        </div>

                        <div>
                            <div class="card-divider"></div>

                            <div class="action-row">
                                <a href="${pageContext.request.contextPath}/uploads/cv/${filename}" target="_blank" class="btn-submit-cv">
                                    📄 CV
                                </a>

                                <form action="${pageContext.request.contextPath}/directeur/demandes" method="POST" style="margin: 0; flex-grow: 1; display:flex; gap:8px;">
                                    <input type="hidden" name="id" value="${d.id}">
                                    <input type="hidden" name="action" value="valider">
                                    <select name="statut" required title="Statut du nouveau membre"
                                            style="padding:8px; border-radius:8px; border:1px solid #CBD5E1;">
                                        <option value="PERMANENT" ${d.statutVise == 'PERMANENT' ? 'selected' : ''}>Permanent</option>
                                        <option value="ASSOCIE"   ${d.statutVise == 'ASSOCIE'   ? 'selected' : ''}>Associé</option>
                                        <option value="DOCTORANT" ${d.statutVise == 'DOCTORANT' ? 'selected' : ''}>Doctorant</option>
                                    </select>
                                    <button type="submit" class="btn-submit-validate" style="flex-grow:1;"
                                            onclick="return confirm('Valider cette adhésion ?');">
                                        ✓ Valider l'Adhésion
                                    </button>
                                </form>

                                <form action="${pageContext.request.contextPath}/directeur/demandes" method="POST" style="margin: 0;">
                                    <input type="hidden" name="id" value="${d.id}">
                                    <input type="hidden" name="action" value="refuser">
                                    <button type="submit" class="btn-submit-refuse" onclick="return confirm('Refuser cette candidature ?');">
                                        Refuser
                                    </button>
                                </form>
                            </div>
                        </div>

                    </div>
                </c:forEach>
                
            </div>
        </c:otherwise>
    </c:choose>
</div>