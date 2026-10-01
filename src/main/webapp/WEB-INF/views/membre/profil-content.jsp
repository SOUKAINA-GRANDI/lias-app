<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div style="margin-bottom:28px;">
    <span class="pub-eyebrow" style="margin-bottom:8px;">Espace chercheur</span>
    <h2 style="font-family:var(--pub-display); margin:0 0 6px; font-size:22px; font-weight:600; color:var(--pub-ink);">
        Mon profil
    </h2>
    <p style="margin:0; color:var(--pub-ink-soft); font-size:13.5px;">
        Gérez vos informations personnelles et votre axe de recherche au sein du laboratoire.
    </p>
</div>

<c:if test="${not empty sessionScope.success}">
    <div class="m-alert m-alert-success">
        <i class="fa-solid fa-circle-check"></i> <c:out value="${sessionScope.success}"/>
    </div>
    <c:remove var="success" scope="session"/>
</c:if>
<c:if test="${not empty sessionScope.error}">
    <div class="m-alert m-alert-error">
        <i class="fa-solid fa-circle-exclamation"></i> <c:out value="${sessionScope.error}"/>
    </div>
    <c:remove var="error" scope="session"/>
</c:if>

<div style="display:grid; grid-template-columns:300px 1fr; gap:22px; align-items:start;">

    <!-- ── COLONNE GAUCHE : IDENTITÉ ── -->
    <div style="display:flex; flex-direction:column; gap:18px;">

        <div class="m-card m-profile-card">

            <div class="m-profile-avatar">
                <c:choose>
                    <c:when test="${not empty membre.photo}">
                        <img src="${pageContext.request.contextPath}/uploads/${membre.photo}" alt="Photo">
                    </c:when>
                    <c:otherwise>
                        ${fn:substring(membre.prenom,0,1)}${fn:substring(membre.nom,0,1)}
                    </c:otherwise>
                </c:choose>
            </div>

            <h3 class="m-profile-name">${membre.prenom} ${membre.nom}</h3>

            <div class="m-profile-badges">
                <span class="m-pill">${membre.statut}</span>
                <c:if test="${not empty membre.role}">
                    <span class="m-pill alt">${membre.role}</span>
                </c:if>
            </div>

            <hr style="border:0; border-top:1px solid var(--pub-line); margin:0 0 18px;">

            <div class="m-profile-info">
                <div><i class="fa-solid fa-envelope"></i> <span style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;">${sessionScope.user.email}</span></div>
                <c:if test="${not empty membre.telephone}">
                    <div><i class="fa-solid fa-phone"></i> <span>${membre.telephone}</span></div>
                </c:if>
                <div><i class="fa-solid fa-building"></i> <span>${not empty membre.etablissementOrigine ? membre.etablissementOrigine : 'FSBM'}</span></div>
                <c:if test="${not empty membre.laboratoireOrigine}">
                    <div><i class="fa-solid fa-flask"></i> <span>${membre.laboratoireOrigine}</span></div>
                </c:if>
                <div><i class="fa-solid fa-calendar-check"></i> <span>Affilié le ${membre.dateAffiliation}</span></div>
            </div>
        </div>

        <c:if test="${not empty membre.centresInteret}">
            <div class="m-card">
                <h4 class="m-card-title"><i class="fa-solid fa-tags"></i> Axes de recherche</h4>
                <p style="font-size:13px; color:var(--pub-ink-soft); line-height:1.7; margin:0;">
                    <c:out value="${membre.centresInteret}"/>
                </p>
            </div>
        </c:if>

    </div>

    <!-- ── COLONNE DROITE : FORMULAIRE ── -->
    <div class="m-card">

        <form action="${pageContext.request.contextPath}/membre/profil/modifier"
              method="POST" enctype="multipart/form-data">

            <h4 class="m-form-section-title"><i class="fa-solid fa-id-card"></i> Informations civiles</h4>

            <div class="m-field-row">
                <div class="m-field">
                    <label>Nom *</label>
                    <input type="text" name="nom" value="${membre.nom}" required>
                </div>
                <div class="m-field">
                    <label>Prénom *</label>
                    <input type="text" name="prenom" value="${membre.prenom}" required>
                </div>
            </div>

            <div class="m-field">
                <label>Téléphone</label>
                <input type="tel" name="telephone" value="${membre.telephone}" placeholder="+212 6xx xxx xxx">
            </div>

            <h4 class="m-form-section-title"><i class="fa-solid fa-camera"></i> Photo de profil</h4>

            <div class="m-photo-row">
                <div class="m-photo-preview">
                    <c:choose>
                        <c:when test="${not empty membre.photo}">
                            <img src="${pageContext.request.contextPath}/uploads/${membre.photo}" alt="Photo">
                        </c:when>
                        <c:otherwise>${fn:substring(membre.prenom,0,1)}</c:otherwise>
                    </c:choose>
                </div>
                <div style="flex:1;">
                    <input type="file" name="photoFile" accept="image/*">
                    <p style="margin:6px 0 0; font-size:11px; color:var(--pub-ink-faint);">Formats : JPG, PNG, GIF — Max : 2 Mo</p>
                </div>
            </div>

            <h4 class="m-form-section-title"><i class="fa-solid fa-microscope"></i> Parcours scientifique</h4>

            <div class="m-field">
                <label>Biographie / Présentation</label>
                <textarea name="biographie" rows="4"
                          placeholder="Décrivez votre parcours, vos travaux de recherche…"><c:out value="${fn:trim(membre.biographie)}"/></textarea>
            </div>

            <div class="m-field">
                <label>Centres d'intérêt &amp; axes de recherche</label>
                <textarea name="centresInteret" rows="3"
                          placeholder="Ex : Intelligence Artificielle, Big Data, Sécurité informatique…"><c:out value="${fn:trim(membre.centresInteret)}"/></textarea>
            </div>

            <div style="display:flex; justify-content:flex-end; padding-top:18px; border-top:1px solid var(--pub-line);">
                <button type="submit" class="btn-accent">
                    <i class="fa-solid fa-floppy-disk"></i> Enregistrer les modifications
                </button>
            </div>

        </form>
    </div>

</div>