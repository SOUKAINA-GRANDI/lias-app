<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="topbar">

    <div class="topbar-title">
        ${pageTitle}
    </div>

    <div class="topbar-actions">
        <jsp:include page="/WEB-INF/views/shared/_search-box.jsp"/>
        <%-- ✅ CORRIGÉ : Au lieu d'aller sur une page 404, on reste sur la page actuelle et on peut déclencher un modal JS ou une action clean --%>
        <a href="${pageContext.request.contextPath}/directeur/notifications"  class="notif-icon" id="topbarNotifBtn">
            <i class="fa-solid fa-bell"></i>
            <c:if test="${globalNbNonLues > 0}">
                <span class="notif-badge">${globalNbNonLues}</span>
            </c:if>
        </a>

       <%-- ✅ CORRECT : Pointe vers l'espace directeur --%>
<a href="${pageContext.request.contextPath}/directeur/messages?action=prive" class="chat-icon">
    <i class="fa-solid fa-comments"></i>
</a>
        <a href="${pageContext.request.contextPath}/directeur/profil" class="user-email">
            ${sessionScope.user.email}
        </a>

        <a href="${pageContext.request.contextPath}/logout" class="logout-btn">
            Déconnexion
        </a>

    </div>

</div>