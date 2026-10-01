<form method="post"
      action="${pageContext.request.contextPath}/membre/profil/modifier"
      class="form-modern">

    <div class="form-group">
        <label>Nom</label>
        <input type="text" name="nom"
               value="${membre.nom}" required>
    </div>

    <div class="form-group">
        <label>Prénom</label>
        <input type="text" name="prenom"
               value="${membre.prenom}" required>
    </div>

    <div class="form-group">
        <label>Téléphone</label>
        <input type="text" name="telephone"
               value="${membre.telephone}">
    </div>

    <div class="form-group">
        <label>Biographie</label>
        <textarea name="biographie">
            ${membre.biographie}
        </textarea>
    </div>

    <div class="form-group">
        <label>Centres d'intérêt</label>
        <textarea name="centresInteret">
            ${membre.centresInteret}
        </textarea>
    </div>

    <button class="btn-primary">
        Enregistrer
    </button>
</form>