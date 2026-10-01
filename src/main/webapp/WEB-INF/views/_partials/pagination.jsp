<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="pagination">

    <c:if test="${currentPage > 1}">
        <a class="btn-action"
           href="?page=${currentPage - 1}">
            ← Précédent
        </a>
    </c:if>

    <span>Page ${currentPage}</span>

    <a class="btn-action"
       href="?page=${currentPage + 1}">
        Suivant →
    </a>

</div>

<style>
.pagination {
    display: flex;
    justify-content: space-between;
    margin-top: var(--space-md);
}
</style>