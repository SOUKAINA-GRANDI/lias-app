// Injection automatique du token CSRF dans tous les formulaires POST.
// Évite d'avoir à ajouter un <input hidden> dans chaque JSP manuellement.
document.addEventListener('DOMContentLoaded', function () {
    var meta = document.querySelector('meta[name="csrf-token"]');
    if (!meta) return;

    var token = meta.getAttribute('content');
    if (!token) return;

    document.querySelectorAll('form').forEach(function (form) {
        var method = (form.getAttribute('method') || 'GET').toUpperCase();
        if (method !== 'POST') return;
        if (form.querySelector('input[name="csrfToken"]')) return;

        var input = document.createElement('input');
        input.type = 'hidden';
        input.name = 'csrfToken';
        input.value = token;
        form.appendChild(input);
    });
});