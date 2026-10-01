<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="app-topbar">

    <div class="topbar-left">

        <div class="mobile-toggle"
             onclick="toggleSidebar()">
            ☰
        </div>

        <h2>${pageTitle}</h2>
    </div>

    <div class="topbar-right">

        <button onclick="toggleDarkMode()"
                class="btn-icon">
            🌙
        </button>

        <a href="${pageContext.request.contextPath}/membre/notifications"
           class="notif-badge" style="position: relative; display: inline-block; text-decoration: none;">

            🔔

            <c:if test="${globalNbNonLues > 0}">
                <span class="badge-number-style">
                    ${globalNbNonLues}
                </span>
            </c:if>

        </a>

        <div class="user-dropdown">
            <div class="avatar">
                ${sessionScope.user.email.substring(0,1).toUpperCase()}
            </div>

            <a href="${pageContext.request.contextPath}/logout">
                Déconnexion
            </a>
        </div>

    </div>

</div>

<style>
.badge-number-style {
    position: absolute;
    top: -5px;
    right: -5px;
    background-color: #ef4444; /* Rouge vif */
    color: white;
    font-size: 10px;
    font-weight: bold;
    border-radius: 50%;
    padding: 1px 5px;
    min-width: 14px;
    height: 14px;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    border: 2px solid #ffffff; /* Détache le badge de la cloche */
    line-height: 1;
}
</style>