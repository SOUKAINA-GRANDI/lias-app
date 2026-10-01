<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%-- Entête de la page utilisant tes classes CSS du layout --%>
<div class="ph">
    <div>
        <div class="ph-eye">Indicateurs clés</div>
        <h1 class="ph-title">Vue d'ensemble</h1>
        <p class="ph-sub">Suivi global et statistiques en temps réel des activités du laboratoire LIAS.</p>
    </div>
</div>

<%-- Grille de statistiques utilisant tes classes d'affichage prédéfinies --%>
<div class="stats anim-1">
    
    <!-- Carte Membres -->
    <div class="stat">
        <div class="stat-ico"><i class="ti ti-users"></i></div>
        <div class="stat-lbl">Membres Actifs</div>
        <div class="stat-val"><c:out value="${not empty totalMembres ? totalMembres : 0}"/></div>
        <div class="stat-hint">Chercheurs affiliés</div>
    </div>

    <!-- Carte Publications -->
    <div class="stat">
        <div class="stat-ico"><i class="ti ti-file-text"></i></div>
        <div class="stat-lbl">Publications</div>
        <div class="stat-val"><c:out value="${not empty totalPublications ? totalPublications : 0}"/></div>
        <div class="stat-hint">Articles et brevets</div>
    </div>

    <!-- Carte Événements -->
    <div class="stat">
        <div class="stat-ico"><i class="ti ti-calendar"></i></div>
        <div class="stat-lbl">Événements</div>
        <div class="stat-val"><c:out value="${not empty totalEvenements ? totalEvenements : 0}"/></div>
        <div class="stat-hint">Séminaires et conférences</div>
    </div>

    <!-- Carte Conventions -->
    <div class="stat">
        <div class="stat-ico"><i class="ti ti-hand-shake"></i></div>
        <div class="stat-lbl">Conventions</div>
        <div class="stat-val"><c:out value="${not empty totalConventions ? totalConventions : 0}"/></div>
        <div class="stat-hint">Partenariats actifs</div>
    </div>
    
</div>

<%-- Bloc d'accueil facultatif reprenant le style de tes cartes (.card) --%>
<div class="card anim-2" style="margin-top: 1.5rem;">
    <div class="card-hd">
        <div class="card-t"><i class="ti ti-shield-check"></i> Panneau d'administration système</div>
    </div>
    <p class="muted" style="line-height: 1.5;">
        Bienvenue dans la console centrale de supervision. Utilisez la barre de navigation latérale gauche pour gérer les comptes utilisateurs, configurer les paramètres de la plateforme, auditer l'activité système ou consulter les rapports périodiques.
    </p>
</div>