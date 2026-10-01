<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<section class="hero">

    <div class="container hero-wrapper">

        <!-- LEFT CONTENT -->
        <div class="hero-content">

            <span class="pub-eyebrow">Structure de recherche gouvernementale — FSBM</span>

            <h1 class="hero-title">
                Laboratoire <span>LIAS</span>
            </h1>

            <p class="hero-subtitle">
                INTELLIGENCE ARTIFICIELLE · APPRENTISSAGE · SYSTÈMES COMPLEXES
            </p>

            <p class="hero-description">
                Plateforme institutionnelle dédiée à la gestion scientifique,
                administrative et collaborative du laboratoire LIAS —
                un espace structuré pour valoriser la recherche,
                les publications et les activités académiques.
            </p>

            <div class="hero-actions">

                <a href="${pageContext.request.contextPath}/public/demande-adhesion"
                   class="btn-accent">
                    Adhésion en ligne
                </a>

                <a href="${pageContext.request.contextPath}/public/membres"
                   class="btn-outline">
                    Annuaire des membres
                </a>

                <a href="${pageContext.request.contextPath}/login"
                   class="btn-login">
                    Espace membre →
                </a>

            </div>

        </div>

        <!-- RIGHT STATS CARD -->
        <div style="position:relative;">

            <!-- Graphe signature : réseau de noeuds pulsant, symbolise
                 les Systèmes / la collaboration du laboratoire -->
            <svg class="hero-graph" viewBox="0 0 260 260" fill="none" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
                <g stroke="#7C3AED" stroke-width="1.3">
                    <line class="pulse" x1="30"  y1="40"  x2="120" y2="20"  stroke-dasharray="24"/>
                    <line class="pulse" x1="120" y1="20"  x2="220" y2="60"  stroke-dasharray="24"/>
                    <line class="pulse" x1="30"  y1="40"  x2="60"  y2="140" stroke-dasharray="24"/>
                    <line x1="220" y1="60"  x2="200" y2="150"/>
                    <line x1="60"  y1="140" x2="140" y2="180"/>
                    <line x1="200" y1="150" x2="140" y2="180"/>
                    <line x1="120" y1="20"  x2="140" y2="180"/>
                    <line x1="60"  y1="140" x2="30"  y2="230"/>
                    <line x1="140" y1="180" x2="180" y2="235"/>
                </g>
                <g fill="#7C3AED">
                    <circle cx="30"  cy="40"  r="4"/>
                    <circle cx="120" cy="20"  r="5"/>
                    <circle cx="220" cy="60"  r="4"/>
                    <circle cx="60"  cy="140" r="4.5"/>
                    <circle cx="200" cy="150" r="4"/>
                    <circle cx="140" cy="180" r="6"/>
                    <circle cx="30"  cy="230" r="3.5"/>
                    <circle cx="180" cy="235" r="3.5"/>
                </g>
            </svg>

            <div class="hero-card">

                <h4>Conseil Scientifique — Indicateurs</h4>

                <div class="stats-grid">

                    <div class="stat-item">
                        <h3>${nbMembres}</h3>
                        <span>Membres actifs</span>
                    </div>

                    <div class="stat-item">
                        <h3>${nbPublications}</h3>
                        <span>Publications</span>
                    </div>

                    <div class="stat-item">
                        <h3>${nbEvenements}</h3>
                        <span>Événements</span>
                    </div>

                    <div class="stat-item">
                        <h3>${nbConventions}</h3>
                        <span>Conventions</span>
                    </div>

                </div>

            </div>

        </div>

    </div>

</section>


<!-- ✅ SECTION ÉQUIPES -->
<section class="section container">

    <div class="section-header">
        <span class="pub-eyebrow" style="margin-bottom:12px;">Unités scientifiques</span>
        <h2>Nos équipes de recherche</h2>
        <p>Spécialisées dans l'IA, le Big Data et les systèmes intelligents.</p>
    </div>

    <div class="teams-grid">

        <c:forEach var="equipe" items="${equipes}">
            <div class="team-card">

                <h4>${equipe.nom}</h4>

                <p>
                    <c:choose>
                        <c:when test="${not empty equipe.description}">
                            ${equipe.description}
                        </c:when>
                        <c:otherwise>
                            Recherche scientifique avancée et projets innovants.
                        </c:otherwise>
                    </c:choose>
                </p>

                <a href="${pageContext.request.contextPath}/public/membres"
                   class="team-link">
                    Voir les membres →
                </a>

            </div>
        </c:forEach>

    </div>

</section>
