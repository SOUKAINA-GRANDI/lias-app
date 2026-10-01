<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%-- Entête de page --%>
<div class="ph anim">
    <div>
        <div class="ph-eye">Administration</div>
        <h1 class="ph-title">Configuration du Système</h1>
        <p class="ph-sub">Gestion des variables globales, du serveur de messagerie, de la gouvernance et des structures.</p>
    </div>
</div>

<div style="max-width: 800px; margin: 0 auto; display: flex; flex-direction: column; gap: 24px;">

    <%-- C. AFFICHAGE DES MESSAGES FLASH (SUCCÈS / ERREUR) --%>
    <c:if test="${not empty sessionScope.success}">
        <div class="anim-1" style="background: #ecfdf5; color: #065f46; border: 1px solid #a7f3d0; padding: 14px 20px; border-radius: 8px; font-weight: 500; display: flex; align-items: center; gap: 10px;">
            <i class="ti ti-circle-check" style="font-size: 20px;"></i>
            <div>${sessionScope.success}</div>
        </div>
        <% session.removeAttribute("success"); %>
    </c:if>

    <c:if test="${not empty sessionScope.error}">
        <div class="anim-1" style="background: #fef2f2; color: #991b1b; border: 1px solid #fca5a5; padding: 14px 20px; border-radius: 8px; font-weight: 500; display: flex; align-items: center; gap: 10px;">
            <i class="ti ti-alert-circle" style="font-size: 20px;"></i>
            <div>${sessionScope.error}</div>
        </div>
        <% session.removeAttribute("error"); %>
    </c:if>

    <%-- 1. CONFIGURATION TECHNIQUE & SMTP --%>
    <div class="tbl-card anim-1" style="padding: 24px;">
        <form method="post" action="${pageContext.request.contextPath}/admin/parametrage" style="display: flex; flex-direction: column; gap: 16px;">
            <input type="hidden" name="action" value="sauvegarderParams">
            
            <h3 style="margin-bottom: 10px; color: var(--ink); border-bottom: 1px solid var(--slate2); padding-bottom: 8px; font-family: var(--ff-d); font-size: 1.15rem;">
                <i class="ti ti-settings" style="margin-right: 8px; color: var(--gold);"></i>Général
            </h3>
            
            <div style="display: flex; flex-direction: column; gap: 4px;">
                <label class="bold" style="font-size: 13px; color: var(--ink2);">Nom de la plateforme</label>
                <div class="search-box">
                    <input type="text" name="site_name" value="${params['site_name']}" placeholder="Ex: LIAS Plateforme">
                </div>
            </div>

            <h3 style="margin-top: 20px; margin-bottom: 10px; color: var(--ink); border-bottom: 1px solid var(--slate2); padding-bottom: 8px; font-family: var(--ff-d); font-size: 1.15rem;">
                <i class="ti ti-mail" style="margin-right: 8px; color: var(--gold);"></i>Configuration SMTP (Email)
            </h3>

            <div style="display: flex; flex-direction: column; gap: 4px;">
                <label class="bold" style="font-size: 13px; color: var(--ink2);">Hôte SMTP</label>
                <div class="search-box">
                    <input type="text" name="smtp_host" value="${params['smtp_host']}" placeholder="Ex: smtp.gmail.com">
                </div>
            </div>

            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
                <div style="display: flex; flex-direction: column; gap: 4px;">
                    <label class="bold" style="font-size: 13px; color: var(--ink2);">Port SMTP</label>
                    <div class="search-box">
                        <input type="text" name="smtp_port" value="${params['smtp_port']}" placeholder="Ex: 587">
                    </div>
                </div>
                <div style="display: flex; flex-direction: column; gap: 4px;">
                    <label class="bold" style="font-size: 13px; color: var(--ink2);">Adresse email d'expédition</label>
                    <div class="search-box">
                        <input type="email" name="smtp_username" value="${params['smtp_username']}" placeholder="Ex: admin@lias.ma">
                    </div>
                </div>
            </div>

            <div style="display: flex; flex-direction: column; gap: 4px;">
                <label class="bold" style="font-size: 13px; color: var(--ink2);">Mot de passe SMTP / Clé d'application</label>
                <div class="search-box">
                    <input type="password" name="smtp_password" value="${params['smtp_password']}" placeholder="••••••••••••">
                </div>
            </div>

            <div style="margin-top: 16px; text-align: right; border-top: 1px solid var(--slate2); padding-top: 16px;">
                <button type="submit" class="btn btn-ink">
                    <i class="ti ti-device-floppy"></i> Enregistrer les configurations
                </button>
            </div>
        </form>
    </div>

    <%-- 2. GESTION DU MANDAT DE DIRECTION --%>
    <div class="tbl-card anim-2" style="padding: 24px;">
        <h3 style="margin-bottom: 10px; color: var(--ink); border-bottom: 1px solid var(--slate2); padding-bottom: 8px; font-family: var(--ff-d); font-size: 1.15rem;">
            <i class="ti ti-crown" style="margin-right: 8px; color: var(--gold);"></i>Gestion du Mandat de Direction
        </h3>
        <p class="muted" style="margin-bottom: 20px; font-size: 13px;">
            Désignez l'enseignant-chercheur qui assumera les fonctions de <strong>Directeur</strong>. L'ancien directeur sera automatiquement réassigné comme membre standard.
        </p>

        <form method="post" action="${pageContext.request.contextPath}/admin/parametrage">
            <input type="hidden" name="action" value="attribuerMandat">
            
            <div style="display: grid; grid-template-columns: 2fr 1fr; gap: 16px; align-items: end;">
                <div style="display: flex; flex-direction: column; gap: 6px;">
                    <label class="bold" style="font-size: 13px; color: var(--ink2);">Nommer le Directeur du Laboratoire</label>
                    <select name="directeurId" required class="search-box" style="width: 100%; height: 38px; outline: none; font-family: var(--ff-b); color: var(--ink); background: var(--white); cursor: pointer;">
                        <option value="">-- Sélectionner un enseignant permanent --</option>
                        <c:forEach items="${listeMembres}" var="m">
                            <option value="${m.id}" <c:if test="${m.type == 'DIRECTEUR'}">selected</c:if>>
                                <c:choose>
                                    <c:when test="${not empty m.nom}">${m.nom} ${m.prenom} (${m.email})</c:when>
                                    <c:otherwise>${m.email}</c:otherwise>
                                </c:choose>
                                — [<c:out value="${m.type}"/>]
                            </option>
                        </c:forEach>
                    </select>
                </div>
                
                <button type="submit" class="btn btn-gold" style="justify-content: center; height: 38px;">
                    <i class="ti ti-shield-check"></i> Activer le Mandat
                </button>
            </div>
        </form>
    </div>

    <%-- 3. NOUVEAU BLOC : GESTION DES ÉQUIPES DE RECHERCHE --%>
    <div class="tbl-card anim-3" style="padding: 24px;">
        <h3 style="margin-bottom: 10px; color: var(--ink); border-bottom: 1px solid var(--slate2); padding-bottom: 8px; font-family: var(--ff-d); font-size: 1.15rem;">
            <i class="ti ti-sitemap" style="margin-right: 8px; color: var(--gold);"></i>Équipes de Recherche du LIAS
        </h3>
        <p class="muted" style="margin-bottom: 16px; font-size: 13px;">
            Ajoutez ou gérez la nomenclature des équipes de recherche rattachées au laboratoire.
        </p>

        <%-- Formulaire d'ajout d'une équipe --%>
        <form method="post" action="${pageContext.request.contextPath}/admin/parametrage" style="display: flex; gap: 12px; align-items: center; margin-bottom: 20px; background: var(--slate); padding: 12px; border-radius: 8px;">
            <input type="hidden" name="action" value="ajouterEquipe">
            <div style="flex: 1; display: flex; flex-direction: column; gap: 4px;">
                <div class="search-box">
                    <input type="text" name="nomEquipe" required placeholder="Nom ou acronyme de l'équipe (Ex: TI & Systèmes)">
                </div>
            </div>
            <button type="submit" class="btn btn-ink" style="height: 38px;">
                <i class="ti ti-plus"></i> Ajouter l'équipe
            </button>
        </form>

        <%-- Liste des équipes existantes --%>
<div style="border: 1px solid var(--slate2); border-radius: 8px; overflow: hidden;">
    <table style="width: 100%; border-collapse: collapse; text-align: left; font-size: 13px;">
        <thead>
            <tr style="background: var(--slate); color: var(--ink2); border-bottom: 1px solid var(--slate2);">
                <th style="padding: 10px 16px; font-weight: bold;">ID</th>
                <th style="padding: 10px 16px; font-weight: bold;">Nom de l'équipe</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${empty listeEquipes}">
                    <tr>
                        <td colspan="2" style="padding: 16px; text-align: center; color: var(--slate4);">Aucune équipe configurée pour le moment.</td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <%-- CORRECTION ICI : Remplacement de var="eq" par var="equipe" --%>
                    <c:forEach items="${listeEquipes}" var="equipe">
                        <tr style="border-bottom: 1px solid var(--slate2);">
                            <td style="padding: 10px 16px; color: var(--slate4); width: 60px;">#${equipe.id}</td>
                            <td style="padding: 10px 16px; font-family: var(--ff-b); color: var(--ink);">${equipe.nom}</td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>
</div>


    </div>

</div>