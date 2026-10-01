<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<style>
    .lias-search {
        display: flex; align-items: center; gap: 6px; margin: 0;
        border: 1px solid rgba(148, 163, 184, .55);
        background: rgba(148, 163, 184, .12);
        border-radius: 8px; padding: 6px 10px;
    }
    .lias-search:focus-within { border-color: #00ACC1; box-shadow: 0 0 0 3px rgba(0, 172, 193, .18); }

    /* On neutralise le style global des <input> du site */
    .lias-search input[type="search"] {
        appearance: none; -webkit-appearance: none;
        background: transparent !important; border: 0 !important;
        box-shadow: none !important; outline: none !important;
        padding: 0 !important; margin: 0 !important;
        height: auto !important; min-height: 0 !important; border-radius: 0 !important;
        color: inherit; font: inherit; font-size: 14px; width: 190px;
    }
    .lias-search input::placeholder { color: inherit; opacity: .55; }
    .lias-search input[type="search"]::-webkit-search-cancel-button { display: none; }
    .lias-search button {
        border: 0; background: transparent; color: inherit;
        cursor: pointer; padding: 0; display: flex; align-items: center;
    }

    /* Variante compacte : icône seule, le champ s'ouvre au clic */
    .lias-search.compact { padding: 6px 8px; }
    .lias-search.compact input[type="search"] { width: 0; transition: width .2s ease; }
    .lias-search.compact:focus-within input[type="search"] { width: 170px; margin-left: 4px !important; }

    @media (max-width: 900px) { .lias-search input[type="search"] { width: 110px; } }

    /* Évite que les boutons de la navbar publique passent sur deux lignes */
    .nav-actions .btn-login, .nav-actions .btn-accent { white-space: nowrap; }
</style>

<form class="lias-search ${param.compact == 'true' ? 'compact' : ''}"
      action="${pageContext.request.contextPath}/search" method="GET" role="search">
    <button type="submit" aria-label="Rechercher">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor"
             stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <circle cx="11" cy="11" r="7"/><path d="M21 21l-4.3-4.3"/>
        </svg>
    </button>
    <input type="search" name="q" placeholder="Rechercher…" maxlength="100"
           autocomplete="off" aria-label="Rechercher">
</form>

<script>
(function () {
    document.querySelectorAll('form.lias-search').forEach(function (form) {
        var input = form.querySelector('input[name="q"]');
        form.addEventListener('submit', function (e) {
            // Champ vide ou 1 seul caractère : on ouvre/active le champ au lieu d'envoyer
            if (input.value.trim().length < 2) {
                e.preventDefault();
                input.focus();
            }
        });
    });
})();
</script>