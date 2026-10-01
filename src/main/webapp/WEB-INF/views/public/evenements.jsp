<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<section class="section" style="border-top:none; padding-top:56px; padding-bottom:24px;">
    <div class="container">
        <span class="pub-eyebrow" style="margin-bottom:14px;">Vie scientifique</span>
        <h1 style="font-family:var(--pub-display); font-size:38px; margin:0 0 16px;">
            Événements du laboratoire
        </h1>
        <p style="font-size:16.5px; line-height:1.75; color:var(--pub-ink-soft); max-width:64ch; margin:0 0 28px;">
            Conférences, séminaires, workshops et manifestations scientifiques organisées par le LIAS.
        </p>

        <div class="event-tabs" id="eventTabs">
    <button type="button" class="event-tab" data-tab="upcoming">À venir</button>
    <button type="button" class="event-tab" data-tab="past">Passés</button>
    <button type="button" class="event-tab active" data-tab="all">Tous</button>
</div>
    </div>
</section>

<section class="section" style="padding-top:0;">
    <div class="container">

        <c:choose>

            <c:when test="${not empty evenements}">

                <div class="events-grid" id="eventsGrid">

                    <c:forEach var="e" items="${evenements}">

                        <div class="event-card" data-status="${e.passe ? 'past' : 'upcoming'}">

                            <div class="event-date-box">
                                <span class="event-day">${e.jour}</span>
                                <span class="event-month">${e.moisAbrege}</span>
                            </div>

                            <div class="event-content">

                                <span class="event-type-tag">${e.type}</span>

                                <h3>${e.titre}</h3>

                                <div class="event-meta">
                                    <c:if test="${not empty e.lieu}">
                                        <span>📍 ${e.lieu}</span>
                                    </c:if>
                                    <span>🗓 ${e.dateLisible}</span>
                                </div>

                                <p class="event-description">
                                    <c:choose>
                                        <c:when test="${not empty e.description}">
                                            <c:choose>
                                                <c:when test="${fn:length(e.description) > 160}">
                                                    ${fn:substring(e.description, 0, 160)}…
                                                </c:when>
                                                <c:otherwise>${e.description}</c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:otherwise>Détails à venir.</c:otherwise>
                                    </c:choose>
                                </p>

                                <a href="${pageContext.request.contextPath}/public/evenement-detail?id=${e.id}"
                                   class="team-link">
                                    Voir détails →
                                </a>

                            </div>

                        </div>

                    </c:forEach>

                </div>

                <p id="noEventMatch" style="display:none; text-align:center; color:var(--pub-ink-faint); font-family:var(--pub-mono); font-size:13px; padding:40px 0;">
                    Aucun événement dans cette catégorie.
                </p>

            </c:when>

            <c:otherwise>
                <div style="text-align:center; padding:60px 20px; color:var(--pub-ink-faint);">
                    <div style="font-size:36px; margin-bottom:12px;">📅</div>
                    <h3 style="margin:0 0 6px;">Aucun événement à venir</h3>
                    <p style="margin:0;">Les prochaines activités scientifiques seront publiées prochainement.</p>
                </div>
            </c:otherwise>

        </c:choose>

    </div>
</section>

<script>
(function () {
    var tabs = document.getElementById('eventTabs');
    var grid = document.getElementById('eventsGrid');
    if (!tabs || !grid) return;

    var cards = grid.querySelectorAll('.event-card');
    var noMatch = document.getElementById('noEventMatch');

    function applyTab(tab) {
        var visibleCount = 0;
        cards.forEach(function (card) {
            var visible = tab === 'all' || card.getAttribute('data-status') === tab;
            card.style.display = visible ? '' : 'none';
            if (visible) visibleCount++;
        });
        if (noMatch) noMatch.style.display = visibleCount === 0 ? 'block' : 'none';
    }

    tabs.querySelectorAll('.event-tab').forEach(function (btn) {
        btn.addEventListener('click', function () {
            tabs.querySelectorAll('.event-tab').forEach(function (b) { b.classList.remove('active'); });
            btn.classList.add('active');
            applyTab(btn.getAttribute('data-tab'));
        });
    });

    applyTab('all');
})();
</script>