<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="breadcrumb">

    <c:forEach var="item" items="${breadcrumbs}" varStatus="status">

        <span>${item}</span>

        <c:if test="${!status.last}">
            <span class="breadcrumb-separator">/</span>
        </c:if>

    </c:forEach>

</div>