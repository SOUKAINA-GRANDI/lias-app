<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<section class="section" style="border-top:none; padding-top:56px; padding-bottom:24px;">
    <div class="container">
        <span class="pub-eyebrow" style="margin-bottom:14px;">Annuaire — ${fn:length(membres)} chercheurs</span>
        <h1 style="font-family:var(--pub-display); font-size:38px; margin:0 0 16px;">
            Nos chercheurs
        </h1>
        <p style="font-size:16.5px; line-height:1.75; color:var(--pub-ink-soft); max-width:64ch; margin:0 0 28px;">
            Permanents, associés et doctorants qui font la recherche du laboratoire LIAS au quotidien.
        </p>

        <input type="text" id="memberSearch" class="pub-search"
               placeholder="Rechercher un membre par nom...">

        <!-- ✅ Filtre par équipe -->
        <div class="team-filters" id="teamFilters">
            <button type="button" class="team-filter-chip active" data-team="all">Toutes les équipes</button>
            <c:forEach var="t" items="${equipesDistinctes}">
                <button type="button" class="team-filter-chip" data-team="${fn:toLowerCase(t)}">${t}</button>
            </c:forEach>
        </div>
    </div>
</section>

<section class="section" style="padding-top:0;">
    <div class="container">

        <c:choose>

            <c:when test="${not empty membres}">

                <div class="members-grid" id="membersGrid">

                    <c:forEach var="m" items="${membres}">

                        <div class="member-card"
                             data-name="${fn:toLowerCase(m.prenom)} ${fn:toLowerCase(m.nom)}"
                             data-team="${not empty m.equipeNom ? fn:toLowerCase(m.equipeNom) : 'sans-equipe'}">

                            <div class="member-header">

                                <div class="member-avatar">
                                    <c:choose>
                                        <c:when test="${not empty m.photo}">
                                            <img src="${pageContext.request.contextPath}/${m.photo}" alt="${m.prenom} ${m.nom}">
                                        </c:when>
                                        <c:otherwise>
                                            ${fn:substring(m.prenom, 0, 1)}${fn:substring(m.nom, 0, 1)}
                                        </c:otherwise>
                                    </c:choose>
                                </div>

                                <div class="member-identity">
                                    <h3>${m.prenom} ${m.nom}</h3>
                                    <span class="member-status">${m.statut}</span>
                                </div>

                            </div>

                            <c:if test="${not empty m.equipeNom}">
                                <span class="member-team-badge">${m.equipeNom}</span>
                            </c:if>

                            <p class="member-bio">
                                <c:choose>
                                    <c:when test="${not empty m.biographie}">${m.biographie}</c:when>
                                    <c:otherwise>Aucune biographie disponible.</c:otherwise>
                                </c:choose>
                            </p>

                            <div class="member-footer">
                                <c:if test="${not empty m.email}">
                                    <a href="mailto:${m.email}">${m.email}</a>
                                </c:if>
                            </div>

                        </div>

                    </c:forEach>

                </div>

                <p id="noMemberMatch" style="display:none; text-align:center; color:var(--pub-ink-faint); font-family:var(--pub-mono); font-size:13px; padding:40px 0;">
                    Aucun membre ne correspond à cette recherche.
                </p>

            </c:when>

            <c:otherwise>
                <p style="text-align:center; color:var(--pub-ink-faint); padding:60px 0;">Aucun membre trouvé.</p>
            </c:otherwise>

        </c:choose>

    </div>
</section>

<script>
(function () {
    var input = document.getElementById('memberSearch');
    var grid = document.getElementById('membersGrid');
    var filters = document.getElementById('teamFilters');
    if (!input || !grid) return;

    var cards = grid.querySelectorAll('.member-card');
    var noMatch = document.getElementById('noMemberMatch');
    var activeTeam = 'all';

    function applyFilters() {
        var q = input.value.trim().toLowerCase();
        var visibleCount = 0;

        cards.forEach(function (card) {
            var matchName = card.getAttribute('data-name').indexOf(q) !== -1;
            var matchTeam = activeTeam === 'all' || card.getAttribute('data-team') === activeTeam;
            var visible = matchName && matchTeam;
            card.style.display = visible ? '' : 'none';
            if (visible) visibleCount++;
        });

        if (noMatch) noMatch.style.display = visibleCount === 0 ? 'block' : 'none';
    }

    input.addEventListener('input', applyFilters);

    if (filters) {
        filters.querySelectorAll('.team-filter-chip').forEach(function (chip) {
            chip.addEventListener('click', function () {
                filters.querySelectorAll('.team-filter-chip').forEach(function (c) { c.classList.remove('active'); });
                chip.classList.add('active');
                activeTeam = chip.getAttribute('data-team');
                applyFilters();
            });
        });
    }

    // ✅ Pré-sélection de l'équipe depuis l'URL (venant de la page /public/equipes)
    var params = new URLSearchParams(window.location.search);
    var equipeParam = params.get('equipe');
    if (equipeParam && filters) {
        var cible = equipeParam.toLowerCase();
        var chip = Array.from(filters.querySelectorAll('.team-filter-chip'))
                         .find(function (c) { return c.getAttribute('data-team') === cible; });
        if (chip) chip.click();
    }
})();
</script>