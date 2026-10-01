<%@ page contentType="text/html;charset=UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<h2>Demandes d’adhésion en attente</h2>

<c:choose>
    <c:when test="${not empty demandes}">

        <table class="table">
            <thead>
                <tr>
                    <th>Nom</th>
                    <th>Email</th>
                    <th>Date</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>

                <c:forEach var="d" items="${demandes}">
                    <tr>
                        <td>${d.prenom} ${d.nom}</td>
                        <td>${d.email}</td>
                        <td>${d.dateDemande}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/directeur/adhesions?action=accept&id=${d.id}"
                               class="btn-success">Accepter</a>

                            <a href="${pageContext.request.contextPath}/directeur/adhesions?action=reject&id=${d.id}"
                               class="btn-danger">Refuser</a>
                        </td>
                    </tr>
                </c:forEach>

            </tbody>
        </table>

    </c:when>

    <c:otherwise>
        <p>Aucune demande en attente.</p>
    </c:otherwise>
</c:choose>