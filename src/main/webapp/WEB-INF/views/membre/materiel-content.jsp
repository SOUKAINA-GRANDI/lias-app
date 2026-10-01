<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="dashboard-header" style="margin-bottom: 30px;">
    <h2 style="color: #1e293b; font-size: 24px; font-weight: 700; font-family: 'Courier New', monospace; text-transform: uppercase; letter-spacing: 0.5px; margin: 0 0 5px 0;">
        Inventaire du matériel de calcul &amp; Traçabilité
    </h2>
    <p style="color: #64748b; font-size: 14px; margin: 0;">Suivi de vos infrastructures de calcul intensif (HPC) et de vos capteurs connectés.</p>
</div>

<c:if test="${param.success eq 'demande'}">
    <div style="background-color: #ecfdf5; border-left: 4px solid #10b981; color: #065f46; padding: 15px; border-radius: 6px; margin-bottom: 25px; font-size: 14px;">
        <i class="fa-solid fa-circle-check" style="margin-right: 8px;"></i> Demande soumise avec succès au Conseil du laboratoire.
    </div>
</c:if>
<c:if test="${param.error eq 'invalid'}">
    <div style="background-color: #fef2f2; border-left: 4px solid #ef4444; color: #991b1b; padding: 15px; border-radius: 6px; margin-bottom: 25px; font-size: 14px;">
        <i class="fa-solid fa-circle-exclamation" style="margin-right: 8px;"></i> Erreur : Veuillez remplir correctement tous les critères de justification.
    </div>
</c:if>

<div class="materiel-grid" style="display: grid; grid-template-columns: 2fr 1.2fr; gap: 30px; align-items: start;">
    
    <div style="display: flex; flex-direction: column; gap: 20px;">
        <c:choose>
            <c:when test="${not empty materiels}">
                <c:forEach var="mat" items="${materiels}">
                    <div style="background: #ffffff; padding: 25px; border-radius: 12px; box-shadow: 0 4px 20px rgba(0,0,0,0.02); border: 1px solid #f1f5f9; position: relative;">
                        
                        <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 8px;">
                            <h3 style="margin: 0; font-size: 17px; color: #0f172a; font-weight: 700; font-family: system-ui;">
                                <c:out value="${mat.nom}" />
                            </h3>
                            <span style="background: #fef3c7; color: #d97706; font-size: 11px; font-weight: 700; padding: 4px 10px; border-radius: 4px; text-transform: uppercase;">
                                Assigné à vous
                            </span>
                        </div>
                        
                        <div style="font-family: monospace; font-size: 12px; color: #94a3b8; margin-bottom: 15px;">
                           Type : <span style="color: #64748b;"><c:out value="${not empty mat.type ? mat.type : '—'}" /></span>                             <span style="margin: 0 8px;">•</span> Reçu le : <c:out value="${mat.dateAttribution}" />
                        </div>

                        <div style="background: #fafafa; border: 1px solid #e2e8f0; border-radius: 8px; padding: 15px;">
                            <div style="font-family: monospace; font-size: 10px; color: #94a3b8; text-transform: uppercase; letter-spacing: 0.5px; margin-bottom: 8px;">
                                Trace de l'inventaire :
                            </div>
                            
                            <div style="display: flex; flex-direction: column; gap: 8px; font-family: monospace; font-size: 12px;">
                                <div style="display: flex; justify-content: space-between; color: #64748b;">
                                    <span>Réception matériel &amp; enregistrement</span>
                                    <span style="color: #94a3b8;">(Administrateur)</span>
                                </div>
                                <div style="display: flex; justify-content: space-between; color: #1e293b; font-weight: 500; border-top: 1px dashed #e2e8f0; padding-top: 6px;">
                                    <span>Attribution d'accès calcul exclusif</span>
                                    <span style="color: #d97706;">(Pr. Directeur)</span>
                                </div>
                            </div>
                        </div>

                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div style="background: #ffffff; padding: 40px; border-radius: 12px; text-align: center; border: 1px dashed #cbd5e1; color: #94a3b8;">
                    <i class="fa-solid fa-box-open" style="font-size: 32px; color: #cbd5e1; margin-bottom: 12px;"></i>
                    <p style="margin: 0; font-size: 14px; font-style: italic;">Aucun matériel de calcul ne vous est directement affecté à ce jour.</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="request-form-card" style="background: #ffffff; padding: 25px; border-radius: 12px; box-shadow: 0 4px 20px rgba(0,0,0,0.03); border: 1px solid #e2e8f0;">
        <h3 style="margin-top: 0; margin-bottom: 20px; font-size: 14px; color: #1e293b; font-weight: 700; font-family: monospace; text-transform: uppercase; letter-spacing: 0.5px;">
            Déclarer un besoin scientifique ou technique
        </h3>
        
        <form action="${pageContext.request.contextPath}/membre/materiel" method="POST">
            <input type="hidden" name="action" value="demande" />
            
            <div style="margin-bottom: 16px;">
                <label style="display: block; font-family: monospace; font-size: 11px; font-weight: 700; color: #64748b; text-transform: uppercase; margin-bottom: 6px;">Désignation du matériel requis *</label>
                <input type="text" name="designation" placeholder="Ex: Nvidia RTX A6000 Ada Edition" style="width: 100%; padding: 10px; border: 1px solid #cbd5e1; border-radius: 6px; font-size: 14px; color: #1e293b;" required>
            </div>
            
            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 15px; margin-bottom: 16px;">
                <div>
                    <label style="display: block; font-family: monospace; font-size: 11px; font-weight: 700; color: #64748b; text-transform: uppercase; margin-bottom: 6px;">Quantité requis *</label>
                    <input type="number" name="quantite" value="1" min="1" style="width: 100%; padding: 10px; border: 1px solid #cbd5e1; border-radius: 6px; font-size: 14px; color: #1e293b;" required>
                </div>
                <div>
                    <label style="display: block; font-family: monospace; font-size: 11px; font-weight: 700; color: #64748b; text-transform: uppercase; margin-bottom: 6px;">Niveau Urgence *</label>
                    <select name="urgence" style="width: 100%; padding: 10px; border: 1px solid #cbd5e1; border-radius: 6px; font-size: 14px; color: #1e293b; background: white;">
                        <option value="Normale">Normale</option>
                        <option value="Urgente">Urgente</option>
                        <option value="Critique">Critique</option>
                    </select>
                </div>
            </div>

            <div style="margin-bottom: 20px;">
                <label style="display: block; font-family: monospace; font-size: 11px; font-weight: 700; color: #64748b; text-transform: uppercase; margin-bottom: 6px;">Justification Scientifique *</label>
                <textarea name="justification" rows="4" placeholder="Quels types de calculs, modèles IA ou expérimentations motivent cette demande ?" style="width: 100%; padding: 12px; border: 1px solid #cbd5e1; border-radius: 6px; font-size: 13px; font-family: inherit; color: #1e293b; resize: none;" required></textarea>
            </div>
            
            <button type="submit" style="width: 100%; background-color: #0f172a; color: #ffffff; border: none; padding: 12px; border-radius: 6px; font-size: 13px; font-weight: 700; font-family: monospace; text-transform: uppercase; cursor: pointer; transition: background 0.2s;">
                ✓ Soumettre demande au Conseil
            </button>
        </form>
    </div>

</div>