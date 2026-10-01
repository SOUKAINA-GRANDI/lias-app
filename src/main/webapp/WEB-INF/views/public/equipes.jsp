<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<style>
    .eq-hero { padding: 40px 0 20px; }
    .eq-hero h1 { font-size: 34px; margin: 8px 0 12px; }
    .eq-hero p { opacity: .8; max-width: 640px; }
    .eq-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
        gap: 20px;
        margin: 30px 0 60px;
    }
    .eq-card {
        border: 1px solid rgba(148,163,184,.25);
        border-radius: 14px;
        padding: 24px;
        background: rgba(148,163,184,.05);
    }
    .eq-card h3 { margin: 0 0 8px; font-size: 19px; }
    .eq-count {
        display: inline-block;
        font-size: 12px;
        font-weight: 700;
        padding: 3px 10px;
        border-radius: 999px;
        background: rgba(124, 58, 237, .15);
        color: #A78BFA;
        margin-bottom: 12px;
    }
    .eq-desc { font-size: 14px; opacity: .8; line-height: 1.6; margin-bottom: 16px; min-height: 40px; }
    .eq-chef { font-size: 13px; opacity: .75; margin-bottom: 16px; }
    .eq-link {
        display: inline-block;
        font-size: 13px;
        font-weight: 600;
        color: #A78BFA;
        text-decoration: none;
        border: 1px solid rgba(167,139,250,.4);
        padding: 8px 14px;
        border-radius: 8px;
    }
    .eq-link:hover { background: rgba(167,139,250,.1); }
</style>

<div class="eq-hero">
    <span style="color:#A78BFA; font-size:12px; letter-spacing:.08em; font-weight:700;">◆ STRUCTURATION DE LA RECHERCHE</span>
    <h1>Nos équipes</h1>
    <p>Le laboratoire LIAS organise ses chercheurs en équipes thématiques, chacune pilotée par un chef d'équipe.</p>
</div>

<div class="eq-grid">
    <c:forEach var="equipe" items="${equipes}">
        <div class="eq-card">
            <span class="eq-count">${effectifs[equipe.id]} membre<c:if test="${effectifs[equipe.id] != 1}">s</c:if></span>
            <h3><c:out value="${equipe.nom}"/></h3>

            <div class="eq-desc">
                <c:out value="${equipe.description}" default="Aucune description disponible."/>
            </div>

            <c:if test="${not empty equipe.chefNom}">
                <div class="eq-chef">👤 Chef d'équipe : <c:out value="${equipe.chefPrenom} ${equipe.chefNom}"/></div>
            </c:if>

            <a class="eq-link" href="${pageContext.request.contextPath}/public/membres?equipe=${equipe.nom}">
                Voir les membres →
            </a>
        </div>
    </c:forEach>

    <c:if test="${empty equipes}">
        <p style="opacity:.7;">Aucune équipe enregistrée pour le moment.</p>
    </c:if>
</div>