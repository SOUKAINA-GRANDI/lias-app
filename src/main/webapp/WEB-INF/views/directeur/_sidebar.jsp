<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    String uri = request.getRequestURI();
%>

<div class="sidebar">

    <%-- ── BRAND ── --%>
    <div class="sidebar-brand">
        <div class="brand-logo">L</div>
        <div>
            <div class="brand-name">LIAS</div>
            <div class="brand-role">Direction</div>
        </div>
    </div>

    <%-- ── NAV ── --%>
    <nav class="sidebar-nav">

        <span class="section-title">Espace directeur</span>
        <ul>
            <li class="${uri.contains('/directeur/dashboard') ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/directeur/dashboard">
                    <i class="fa-solid fa-chart-pie"></i>
                    <span>Tableau de bord</span>
                </a>
            </li>
            <li class="${uri.contains('/directeur/profil') ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/directeur/profil">
                    <i class="fa-solid fa-user"></i>
                    <span>Mon profil</span>
                </a>
            </li>
            <li class="${uri.contains('/directeur/membres') ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/directeur/membres">
                    <i class="fa-solid fa-users"></i>
                    <span>Membres du lab</span>
                </a>
            </li>
            <li class="${uri.contains('/directeur/roles') ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/directeur/roles">
                    <i class="fa-solid fa-user-shield"></i>
                    <span>Rôles &amp; responsabilités</span>
                </a>
            </li>
            <li class="${uri.contains('/directeur/equipes') ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/directeur/equipes">
                    <i class="fa-solid fa-people-group"></i>
                    <span>Équipes</span>
                </a>
            </li>
            <li class="${uri.contains('/directeur/laboratoire') ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/directeur/laboratoire">
                    <i class="fa-solid fa-building-columns"></i>
                    <span>Laboratoire</span>
                </a>
            </li>
            <li class="${uri.contains('/directeur/documents') ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/directeur/documents">
                    <i class="fa-solid fa-folder-open"></i>
                    <span>Documents</span>
                </a>
            </li>
        </ul>

        <span class="section-title">Administratif</span>
        <ul>
            <li class="${uri.contains('/directeur/demandes') ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/directeur/demandes">
                    <i class="fa-solid fa-file-circle-check"></i>
                    <span>Candidatures</span>
                    <c:if test="${nbDemandesAttente > 0}">
                        <span class="badge">${nbDemandesAttente}</span>
                    </c:if>
                </a>
            </li>
            <li class="${uri.contains('/directeur/evenements') ? 'active' : ''}">
               <a href="${pageContext.request.contextPath}/directeur/evenements" class="sidebar-link">
                    <i class="fa-solid fa-calendar-days"></i>
                    <span>Événements</span>
                </a>
            </li>
            <li class="${uri.contains('/directeur/reunions') ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/directeur/reunions">
                    <i class="fa-solid fa-clipboard-list"></i>
                    <span>Réunions & PV</span>
                </a>
            </li>
            <li class="${uri.contains('/directeur/conventions') ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/directeur/conventions">
                    <i class="fa-solid fa-handshake"></i>
                    <span>Conventions</span>
                </a>
            </li>
            <li class="${uri.contains('/directeur/mandats') ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/directeur/mandats">
                    <i class="fa-solid fa-id-card"></i>
                    <span>Mandats</span>
                </a>
            </li>
            <li class="${uri.contains('/directeur/materiel') ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/directeur/materiel">
                    <i class="fa-solid fa-box-open"></i>
                    <span>Matériel</span>
                </a>
            </li>
            <li class="${uri.contains('/directeur/rapport') ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/directeur/rapport">
                    <i class="fa-solid fa-chart-bar"></i>
                    <span>Rapport annuel</span>
                </a>
            </li>
        </ul>

        <span class="section-title">Communication</span>
        <ul>
            <%-- ✅ CORRECT : Reste dans l'espace directeur --%>
<li class="${uri.contains('/directeur/messages') ? 'active' : ''}">
    <a href="${pageContext.request.contextPath}/directeur/messages?action=prive">
        <i class="fa-solid fa-comments"></i>
        <span>Chat interne</span>
    </a>
</li>
            <li class="${uri.contains('/directeur/notifications') ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/directeur/notifications" id="sidebarNotifBtn">
                    <i class="fa-solid fa-bell"></i>
                    <span>Notifications</span>
                    <c:if test="${globalNbNonLues > 0}">
                        <span class="badge">${globalNbNonLues}</span>
                    </c:if>
                </a>
            </li>
        </ul>

    </nav>
</div>