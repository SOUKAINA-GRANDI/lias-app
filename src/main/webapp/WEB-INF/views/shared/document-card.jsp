<div class="card document-card">

    <h4>${document.titre}</h4>

    <p class="text-muted">
        ${document.type}
    </p>

    <a href="${pageContext.request.contextPath}/${document.cheminFichier}"
       target="_blank"
       class="btn-primary">
        📄 Télécharger
    </a>

</div>