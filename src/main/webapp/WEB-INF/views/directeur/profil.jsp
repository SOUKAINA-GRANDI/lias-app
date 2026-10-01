<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div class="profile-container" style="max-width: 800px; margin: 2rem auto; padding: 0 1rem;">
    
    <c:if test="${not empty sessionScope.success}">
        <div style="background-color: #d4edda; color: #155724; padding: 1rem; margin-bottom: 1rem; border-radius: 4px;">
            <c:out value="${sessionScope.success}"/>
            <c:remove var="success" scope="session"/>
        </div>
    </c:if>
    <c:if test="${param.error eq 'invalid'}">
        <div style="background-color: #f8d7da; color: #721c24; padding: 1rem; margin-bottom: 1rem; border-radius: 4px;">
            Veuillez remplir correctement les champs obligatoires (Nom et Prénom).
        </div>
    </c:if>

    <div class="profile-main-card" style="background: #fff; border-radius: 8px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); padding: 2rem;">
        
        <div class="profile-header-block" style="display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid #eee; padding-bottom: 1.5rem; margin-bottom: 1.5rem;">
            <div class="profile-identity" style="display: flex; align-items: center; gap: 1.5rem;">
                
                <div class="profile-avatar-container">
                    <c:choose>
                        <c:when test="${not empty directeur.photo}">
                            <%-- 💡 Ajout du timestamp anti-cache navigateur --%>
                            <img src="${pageContext.request.contextPath}/uploads/${directeur.photo}?t=${System.currentTimeMillis()}" alt="Photo de profil" style="width: 70px; height: 70px; border-radius: 50%; object-fit: cover; border: 2px solid #007bff;" />
                        </c:when>
                        <c:otherwise>
                            <%-- 💡 Correction de c:transparent par c:otherwise --%>
                            <div class="profile-avatar-badge" style="width: 70px; height: 70px; background: #007bff; color: white; display: flex; align-items: center; justify-content: center; font-size: 1.5rem; font-weight: bold; border-radius: 50%;">
                                <c:out value="${fn:toUpperCase(fn:substring(sessionScope.user.email, 0, 2))}"/>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
                
                <div class="profile-meta">
                    <h2 style="margin: 0; font-size: 1.6rem;">
                        Pr. <c:out value="${not empty directeur.nom ? directeur.prenom.concat(' ').concat(directeur.nom) : fn:substringBefore(sessionScope.user.email, '@')}"/>
                    </h2>
                    <span class="profile-role-tag" style="background: #e6f7ff; color: #1890ff; padding: 0.25rem 0.75rem; border-radius: 20px; font-size: 0.85rem; font-weight: 500; display: inline-block; margin-top: 0.4rem;">Directeur (Mandat Actif)</span>
                </div>
            </div>
            
            <c:if test="${mode != 'edit'}">
                <a href="${pageContext.request.contextPath}/directeur/profil?mode=edit" class="btn-profile-edit" style="background: #007bff; color: white; text-decoration: none; padding: 0.6rem 1.2rem; border-radius: 4px; font-weight: 500;">
                    Modifier mes informations
                </a>
            </c:if>
        </div>

        <!-- MODE AFFICHAGE SIMPLE -->
        <c:if test="${mode != 'edit'}">
            <div class="profile-details-grid" style="display: grid; grid-template-columns: 1fr 1fr; gap: 1.5rem;">
                <div class="detail-mini-box">
                    <span class="detail-label" style="display: block; color: #666; font-size: 0.85rem; margin-bottom: 0.3rem;">Nom complet</span>
                    <span class="detail-value" style="font-weight: 600;"><c:out value="${directeur.prenom} ${directeur.nom}"/></span>
                </div>
                <div class="detail-mini-box">
                    <span class="detail-label" style="display: block; color: #666; font-size: 0.85rem; margin-bottom: 0.3rem;">Téléphone</span>
                    <span class="detail-value" style="font-weight: 600;"><c:out value="${not empty directeur.telephone ? directeur.telephone : 'Non renseigné'}"/></span>
                </div>
                <div class="detail-mini-box">
                    <span class="detail-label" style="display: block; color: #666; font-size: 0.85rem; margin-bottom: 0.3rem;">Email Professionnel</span>
                    <span class="detail-value" style="font-weight: 600;"><c:out value="${sessionScope.user.email}"/></span>
                </div>
                <div class="detail-mini-box">
                    <span class="detail-label" style="display: block; color: #666; font-size: 0.85rem; margin-bottom: 0.3rem;">Bureau</span>
                    <span class="detail-value" style="font-weight: 600;">Département d'Informatique, FSBM</span>
                </div>
            </div>
            
            <div style="margin-top: 2rem; border-top: 1px solid #eee; padding-top: 1.5rem;">
                <h3 style="font-size: 1.1rem; margin-bottom: 0.5rem;">Biographie</h3>
                <p style="color: #444; line-height: 1.6;"><c:out value="${not empty directeur.biographie ? directeur.biographie : 'Aucune biographie rédigée.'}"/></p>
            </div>
            
            <div style="margin-top: 1.5rem;">
                <h3 style="font-size: 1.1rem; margin-bottom: 0.5rem;">Centres d'intérêt</h3>
                <p style="color: #444; line-height: 1.6;"><c:out value="${not empty directeur.centresInteret ? directeur.centresInteret : 'Non renseignés.'}"/></p>
            </div>
        </c:if>

        <!-- MODE ÉDITION (FORMULAIRE) -->
        <c:if test="${mode == 'edit'}">
            <form action="${pageContext.request.contextPath}/directeur/profil" method="POST" enctype="multipart/form-data" style="display: flex; flex-direction: column; gap: 1.2rem;">
                
                <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                    <div>
                        <label style="display: block; font-weight: 500; margin-bottom: 0.4rem;">Prénom *</label>
                        <input type="text" name="prenom" value="<c:out value='${directeur.prenom}'/>" required style="width: 100%; padding: 0.6rem; border: 1px solid #ccc; border-radius: 4px;" />
                    </div>
                    <div>
                        <label style="display: block; font-weight: 500; margin-bottom: 0.4rem;">Nom *</label>
                        <input type="text" name="nom" value="<c:out value='${directeur.nom}'/>" required style="width: 100%; padding: 0.6rem; border: 1px solid #ccc; border-radius: 4px;" />
                    </div>
                </div>

                <div>
                    <label style="display: block; font-weight: 500; margin-bottom: 0.4rem;">Téléphone</label>
                    <input type="text" name="telephone" value="<c:out value='${directeur.telephone}'/>" style="width: 100%; padding: 0.6rem; border: 1px solid #ccc; border-radius: 4px;" />
                </div>

                <div>
                    <label style="display: block; font-weight: 500; margin-bottom: 0.4rem;">Photo de profil</label>
                    <input type="file" name="photoFile" accept="image/*" style="width: 100%; padding: 0.4rem 0;" />
                </div>

                <div>
                    <label style="display: block; font-weight: 500; margin-bottom: 0.4rem;">Biographie</label>
                    <textarea name="biographie" rows="4" style="width: 100%; padding: 0.6rem; border: 1px solid #ccc; border-radius: 4px; resize: vertical;"><c:out value='${directeur.biographie}'/></textarea>
                </div>

                <div>
                    <label style="display: block; font-weight: 500; margin-bottom: 0.4rem;">Centres d'intérêt</label>
                    <textarea name="centresInteret" rows="3" style="width: 100%; padding: 0.6rem; border: 1px solid #ccc; border-radius: 4px; resize: vertical;"><c:out value='${directeur.centresInteret}'/></textarea>
                </div>

                <div style="display: flex; gap: 1rem; margin-top: 1rem; justify-content: flex-end;">
                    <a href="${pageContext.request.contextPath}/directeur/profil" style="padding: 0.6rem 1.2rem; background: #eee; color: #333; text-decoration: none; border-radius: 4px;">Annuler</a>
                    <button type="submit" style="padding: 0.6rem 1.2rem; background: #28a745; color: white; border: none; border-radius: 4px; font-weight: 500; cursor: pointer;">Enregistrer les modifications</button>
                </div>
            </form>
        </c:if>

    </div>
</div>