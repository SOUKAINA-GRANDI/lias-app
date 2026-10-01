<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="ph anim">
    <div>
        <div class="ph-eye">Mon Compte</div>
        <h1 class="ph-title">Mon Profil Complet — LIAS</h1>
        <p class="ph-sub">Mettez à jour vos informations de recherche, votre photo de profil ainsi que vos affiliations universitaires.</p>
    </div>
</div>

<div style="max-width: 1100px; margin: 0 auto;">

    <%-- MESSAGES NOTIFICATIONS --%>
    <c:if test="${not empty sessionScope.success}">
        <div class="alert alert-ok" style="margin-bottom:1.2rem">
            <i class="ti ti-circle-check"></i><span><c:out value="${sessionScope.success}"/></span>
        </div>
        <% session.removeAttribute("success"); %>
    </c:if>
    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-e" style="margin-bottom:1.2rem">
            <i class="ti ti-alert-circle"></i><span><c:out value="${sessionScope.error}"/></span>
        </div>
        <% session.removeAttribute("error"); %>
    </c:if>

    <!-- FORMULAIRE GLOBAL AVEC GESTION MULTIPART POUR LA PHOTO -->
    <form method="post" action="${pageContext.request.contextPath}/admin/profil" enctype="multipart/form-data" style="display: grid; grid-template-columns: 280px 1fr; gap: 24px; align-items: start; width: 100%;">
        <input type="hidden" name="action" value="updateInfos">
        <input type="hidden" name="current_photo" value="${membre.photo}">

        <%-- COLONNE GAUCHE : PHOTO & STATUT --%>
        <div class="tbl-card anim-1" style="padding: 24px; text-align: center; display: flex; flex-direction: column; align-items: center; gap: 16px;">
            <div style="width: 140px; height: 140px; border-radius: 50%; background: var(--slate); border: 3px solid var(--gold); display: flex; align-items: center; justify-content: center; overflow: hidden; position: relative;">
                <c:choose>
                    <c:when test="${not empty membre.photo}">
                        <!-- Ajustement de l'adresse URL : On tente l'accès direct via le pattern de l'application -->
                        <img id="profile-avatar" src="${pageContext.request.contextPath}/${membre.photo}" alt="Photo de profil" style="width:100%; height:100%; object-fit:cover;" 
                             onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/photos/${membre.photo}';">
                    </c:when>
                    <c:otherwise>
                        <i class="ti ti-user" style="font-size: 56px; color: var(--ink3);"></i>
                    </c:otherwise>
                </c:choose>
            </div>
            
            <div style="width: 100%;">
                <label class="bold" style="font-size: 11px; color: var(--ink3); display: block; margin-bottom: 5px;">Changer de photo</label>
                <input type="file" name="photoFile" accept="image/*" style="font-size: 12px; max-width: 100%;">
            </div>

            <hr style="width: 100%; border: 0; border-top: 1px solid var(--slate2); margin: 8px 0;">
            
            <div style="text-align: left; width: 100%; display: flex; flex-direction: column; gap: 8px; font-size: 13px;">
                <div><span class="muted">Statut :</span> <span class="bdg bdg-blue">${membre.statut}</span></div>
                <div><span class="muted">Rôle système :</span> <span class="mono">${not empty membre.role ? membre.role : 'MEMBRE'}</span></div>
                <div><span class="muted">Compte actif :</span> ${membre.actif == 1 ? '✅ Oui' : '❌ Non'}</div>
            </div>
        </div>

        <%-- COLONNE DROITE : FORMULAIRES DE DONNÉES --%>
        <div style="display: flex; flex-direction: column; gap: 24px;">
            
            <%-- SECTION 1 : CIVILITÉ ET CONTACT --%>
            <div class="tbl-card" style="padding: 24px;">
                <h3 style="margin-bottom: 16px; color: var(--ink); border-bottom: 1px solid var(--slate2); padding-bottom: 8px; font-family: var(--ff-d);">
                    <i class="ti ti-id" style="margin-right: 8px; color: var(--gold);"></i> Coordonnées Personnelles
                </h3>
                
                <div style="display: flex; flex-direction: column; gap: 16px;">
                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
                        <div style="display: flex; flex-direction: column; gap: 4px;">
                            <label class="bold" style="font-size: 12px;">Nom</label>
                            <div class="search-box"><input type="text" name="nom" value="${membre.nom}" required></div>
                        </div>
                        <div style="display: flex; flex-direction: column; gap: 4px;">
                            <label class="bold" style="font-size: 12px;">Prénom</label>
                            <div class="search-box"><input type="text" name="prenom" value="${membre.prenom}" required></div>
                        </div>
                    </div>

                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
                        <div style="display: flex; flex-direction: column; gap: 4px;">
                            <label class="bold" style="font-size: 12px;">Email Institutionnel</label>
                            <div class="search-box"><input type="email" name="email" value="${membre.email}" required></div>
                        </div>
                        <div style="display: flex; flex-direction: column; gap: 4px;">
                            <label class="bold" style="font-size: 12px;">Téléphone</label>
                            <div class="search-box"><input type="text" name="telephone" value="${membre.telephone}"></div>
                        </div>
                    </div>

                    <div style="display: flex; flex-direction: column; gap: 4px;">
                        <label class="bold" style="font-size: 12px;">Date de naissance</label>
                        <div class="search-box"><input type="date" name="date_naissance" value="${membre.date_naissance}"></div>
                    </div>
                </div>
            </div>

            <%-- SECTION 2 : PARCOURS SCIENTIFIQUE & ORIGINES --%>
            <div class="tbl-card" style="padding: 24px;">
                <h3 style="margin-bottom: 16px; color: var(--ink); border-bottom: 1px solid var(--slate2); padding-bottom: 8px; font-family: var(--ff-d);">
                    <i class="ti ti-school" style="margin-right: 8px; color: var(--gold);"></i> Affiliation & Recherche
                </h3>
                
                <div style="display: flex; flex-direction: column; gap: 16px;">
                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
                        <div style="display: flex; flex-direction: column; gap: 4px;">
                            <label class="bold" style="font-size: 12px;">Établissement d'origine</label>
                            <div class="search-box"><input type="text" name="etablissement_origine" value="${membre.etablissement_origine}" placeholder="Ex: FSBM"></div>
                        </div>
                        <div style="display: flex; flex-direction: column; gap: 4px;">
                            <label class="bold" style="font-size: 12px;">Laboratoire d'origine</label>
                            <div class="search-box"><input type="text" name="laboratoire_origine" value="${membre.laboratoire_origine}" placeholder="Ex: LIAS"></div>
                        </div>
                    </div>

                    <div style="display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 12px;">
                        <div style="display: flex; flex-direction: column; gap: 4px;">
                            <label class="bold" style="font-size: 12px;">Date d'embauche</label>
                            <div class="search-box"><input type="date" name="date_embauche" value="${membre.date_embauche}"></div>
                        </div>
                        <div style="display: flex; flex-direction: column; gap: 4px;">
                            <label class="bold" style="font-size: 12px;">Date d'affiliation</label>
                            <div class="search-box"><input type="date" name="date_affiliation" value="${membre.date_affiliation}"></div>
                        </div>
                        <div style="display: flex; flex-direction: column; gap: 4px;">
                            <label class="bold" style="font-size: 12px;">Date de départ</label>
                            <div class="search-box"><input type="date" name="date_depart" value="${membre.date_depart}"></div>
                        </div>
                    </div>

                    <div style="display: flex; flex-direction: column; gap: 4px;">
                        <label class="bold" style="font-size: 12px;">Centres d'intérêt scientifique</label>
                        <div class="search-box"><input type="text" name="centres_interet" value="${membre.centres_interet}" placeholder="Ex: Data Science, IoT"></div>
                    </div>

                    <div style="display: flex; flex-direction: column; gap: 4px;">
                        <label class="bold" style="font-size: 12px;">Biographie académique</label>
                        <textarea name="biographie" style="width: 100%; min-height: 120px; padding: 12px; border: 1px solid var(--slate2); border-radius: 8px; outline: none; font-size: 13px; resize: vertical;">${membre.biographie}</textarea>
                    </div>

                    <div style="text-align: right; margin-top: 10px;">
                        <button type="submit" class="btn btn-ink"><i class="ti ti-device-floppy"></i> Enregistrer les modifications du compte</button>
                    </div>
                </div>
            </div>
        </div>
    </form>

    <%-- BLOC INDÉPENDANT : MOT DE PASSE (SÉCURITÉ) --%>
    <div class="tbl-card" style="padding: 24px; margin-top: 24px; margin-left: 304px;">
        <h3 style="margin-bottom: 16px; color: var(--ink); border-bottom: 1px solid var(--slate2); padding-bottom: 8px; font-family: var(--ff-d);">
            <i class="ti ti-lock" style="margin-right: 8px; color: var(--gold);"></i> Sécurité
        </h3>
        
        <form method="post" action="${pageContext.request.contextPath}/admin/profil" style="display: flex; flex-direction: column; gap: 16px;">
            <input type="hidden" name="action" value="updatePassword">
            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
                <div style="display: flex; flex-direction: column; gap: 4px;">
                    <label class="bold" style="font-size: 12px;">Nouveau mot de passe</label>
                    <div class="search-box"><input type="password" name="newPassword" placeholder="••••••••" required></div>
                </div>
                <div style="display: flex; flex-direction: column; gap: 4px;">
                    <label class="bold" style="font-size: 12px;">Confirmer le mot de passe</label>
                    <div class="search-box"><input type="password" name="confirmPassword" placeholder="••••••••" required></div>
                </div>
            </div>
            <div style="text-align: right; margin-top: 8px;">
                <button type="submit" class="btn btn-gold"><i class="ti ti-key"></i> Actualiser mon mot de passe</button>
            </div>
        </form>
    </div>

</div>