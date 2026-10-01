<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<nav>
    <a href="${pageContext.request.contextPath}/membre/dashboard" class="menu-item ${pageTitle == 'Tableau de bord' ? 'active' : ''}">
        <span>Tableau de bord</span>
    </a>
    <a href="${pageContext.request.contextPath}/membre/profil" class="menu-item ${pageTitle == 'Mon Profil' ? 'active' : ''}">
        <span>Mon Profil</span>
    </a>
    <a href="${pageContext.request.contextPath}/membre/publications" class="menu-item ${pageTitle == 'Mes Publications' ? 'active' : ''}">
        <span>Mes Publications</span>
    </a>
    

    <%-- CDC §2 : le Doctorant n'a accès qu'à son profil et aux publications --%>
    <c:if test="${statutMembreConnecte != 'DOCTORANT'}">
        <a href="${pageContext.request.contextPath}/membre/documents" class="menu-item ${pageTitle == 'Documents' ? 'active' : ''}">
            <span>Documents</span>
        </a>

        <a href="${pageContext.request.contextPath}/membre/evenements" class="menu-item ${pageTitle == 'Événements' ? 'active' : ''}">
            <span>Événements</span>
        </a>

        <%-- CDC §2 : l'Associé n'a pas accès aux modules internes de gouvernance --%>
        <c:if test="${statutMembreConnecte != 'ASSOCIE'}">

            <a href="${pageContext.request.contextPath}/membre/conventions" class="menu-item ${pageTitle == 'Conventions' ? 'active' : ''}">
                <span>Conventions & Partenariats</span>
            </a>

            <a href="${pageContext.request.contextPath}/membre/reunions" class="menu-item ${pageTitle == 'Réunions' ? 'active' : ''}">
                <span>Réunions & PV</span>
            </a>

            <a href="${pageContext.request.contextPath}/membre/materiel" class="menu-item ${pageTitle == 'Mon Matériel' ? 'active' : ''}">
                <span>Mon Matériel</span>
            </a>

        </c:if>

        <a href="${pageContext.request.contextPath}/messages" class="menu-item ${pageTitle == 'Messages' ? 'active' : ''}">
            <span>Discussion interne</span>
        </a>

    </c:if>
    <a href="${pageContext.request.contextPath}/membre/notifications" class="menu-item ${pageTitle == 'Mes Notifications' ? 'active' : ''}" style="display: flex; align-items: center; justify-content: space-between;">
        <span>Notifications</span>
        <c:if test="${globalNbNonLues > 0}">
            <span style="background-color: #ef4444; color: white; font-size: 11px; font-weight: bold; padding: 2px 7px; border-radius: 10px;">
                ${globalNbNonLues}
            </span>
        </c:if>
    </a>
    <a href="${pageContext.request.contextPath}/logout" class="menu-item" style="margin-top: 50px; color: var(--color-danger); background: rgba(239, 68, 68, 0.05)">
        <span>Déconnexion</span>
    </a>
</nav>