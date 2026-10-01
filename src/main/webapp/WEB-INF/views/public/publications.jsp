<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<section class="section" style="border-top:none; padding-top:56px; padding-bottom:24px;">
    <div class="container">
        <span class="pub-eyebrow" style="margin-bottom:14px;">Production scientifique — ${fn:length(publications)} références</span>
        <h1 style="font-family:var(--pub-display); font-size:38px; margin:0 0 16px;">
            Publications
        </h1>
        <p style="font-size:16.5px; line-height:1.75; color:var(--pub-ink-soft); max-width:64ch; margin:0 0 28px;">
            Articles, conférences, ouvrages et chapitres publiés par les chercheurs du LIAS.
        </p>

        <input type="text" id="pubSearch" class="pub-search"
               placeholder="Rechercher par titre ou auteur...">

        <div class="team-filters" id="pubFilters">
            <button type="button" class="team-filter-chip active" data-type="all">Tous les types</button>
            <c:forEach var="t" items="${typesDistincts}">
                <button type="button" class="team-filter-chip" data-type="${fn:toLowerCase(t)}">${t}</button>
            </c:forEach>
        </div>
    </div>
</section>

<section class="section" style="padding-top:0;">
    <div class="container">

        <c:choose>

            <c:when test="${not empty publications}">

                <div class="pub-list" id="pubList">

                    <c:forEach var="p" items="${publications}">

                        <div class="pub-row"
                             data-search="${fn:toLowerCase(p.titre)} ${fn:toLowerCase(p.auteurs)}"
                             data-type="${not empty p.type ? fn:toLowerCase(p.type) : ''}">

                            <span class="pub-year">${p.annee}</span>

                            <div class="pub-info">
                                <span class="pub-type-tag">${p.type}</span>
                                <h3>${p.titre}</h3>
                                <p class="pub-authors">${p.auteurs}</p>
                            </div>

                        </div>

                    </c:forEach>

                </div>

                <p id="noPubMatch" style="display:none; text-align:center; color:var(--pub-ink-faint); font-family:var(--pub-mono); font-size:13px; padding:40px 0;">
                    Aucune publication ne correspond à cette recherche.
                </p>

            </c:when>

            <c:otherwise>
                <div style="text-align:center; padding:60px 20px; color:var(--pub-ink-faint);">
                    <div style="font-size:36px; margin-bottom:12px;">📚</div>
                    <h3 style="margin:0 0 6px;">Aucune publication disponible</h3>
                    <p style="margin:0;">Les publications des chercheurs seront ajoutées prochainement.</p>
                </div>
            </c:otherwise>

        </c:choose>

    </div>
</section>

<script>
(function () {
    var input = document.getElementById('pubSearch');
    var list = document.getElementById('pubList');
    var filters = document.getElementById('pubFilters');
    if (!input || !list) return;

    var rows = list.querySelectorAll('.pub-row');
    var noMatch = document.getElementById('noPubMatch');
    var activeType = 'all';

    function applyFilters() {
        var q = input.value.trim().toLowerCase();
        var visibleCount = 0;

        rows.forEach(function (row) {
            var matchSearch = row.getAttribute('data-search').indexOf(q) !== -1;
            var matchType = activeType === 'all' || row.getAttribute('data-type') === activeType;
            var visible = matchSearch && matchType;
            row.style.display = visible ? '' : 'none';
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
                activeType = chip.getAttribute('data-type');
                applyFilters();
            });
        });
    }
})();
</script>

