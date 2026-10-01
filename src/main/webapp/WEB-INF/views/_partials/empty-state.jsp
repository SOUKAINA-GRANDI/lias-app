<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="empty-state">

    <div class="empty-icon">
        ${emptyIcon != null ? emptyIcon : "📭"}
    </div>

    <h3>
        ${emptyTitle != null ? emptyTitle : "Aucune donnée"}
    </h3>

    <p>
        ${emptyMessage != null ? emptyMessage : ""}
    </p>

    <c:if test="${not empty emptyActionLink}">
        <a href="${emptyActionLink}"
           class="btn-primary">
            ${emptyActionLabel}
        </a>
    </c:if>

</div>