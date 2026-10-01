<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<section class="section" style="border-top:none; padding-top:56px; padding-bottom:24px;">
    <div class="container" style="max-width:760px;">
        <span class="pub-eyebrow" style="margin-bottom:14px;">Dossier numérique</span>
        <h1 style="font-family:var(--pub-display); font-size:38px; margin:0 0 16px;">
            Rejoindre le laboratoire LIAS
        </h1>
        <p style="font-size:16.5px; line-height:1.75; color:var(--pub-ink-soft); margin:0;">
            Formulaire de candidature pour doctorants, enseignants-chercheurs et membres associés.
            Votre dossier sera examiné par la direction du laboratoire.
        </p>
    </div>
</section>

<section class="section" style="padding-top:0;">
    <div class="container" style="max-width:760px;">

        <c:if test="${param.success == 'true'}">
            <div class="auth-alert auth-alert-success" style="margin-bottom:22px;">
                Votre demande a été soumise avec succès. Vous recevrez une réponse par email.
            </div>
        </c:if>

        <c:if test="${param.error != null}">
            <div class="auth-alert auth-alert-error" style="margin-bottom:22px;">
                Une erreur est survenue. Vérifiez vos informations et réessayez.
            </div>
        </c:if>

        <div class="pub-detail-card">

            <form method="post"
                  enctype="multipart/form-data"
                  action="${pageContext.request.contextPath}/public/demande-adhesion"
                  class="adhesion-form-grid">

                <div class="auth-field">
                    <label>Prénom *</label>
                    <input type="text" name="prenom" required>
                </div>

                <div class="auth-field">
                    <label>Nom *</label>
                    <input type="text" name="nom" required>
                </div>

                <div class="auth-field adhesion-full">
                    <label>Adresse électronique académique *</label>
                    <input type="email" name="email" required>
                </div>

                <div class="auth-field">
                    <label>Statut visé *</label>
                    <select name="statut" required>
                        <option value="">-- Sélectionner --</option>
                        <option value="DOCTORANT">Doctorant</option>
                        <option value="PERMANENT">Membre permanent</option>
                        <option value="ASSOCIE">Membre associé</option>
                    </select>
                </div>

                <div class="auth-field">
                    <label>Établissement d'origine *</label>
                    <input type="text" name="etablissement" required>
                </div>

                <div class="auth-field adhesion-full">
                    <label>Équipe d'accueil ciblée *</label>
                    <select name="equipeId" required>
                        <option value="">-- Sélectionner une équipe --</option>
                        <c:forEach var="equipe" items="${equipes}">
                            <option value="${equipe.id}">${equipe.nom}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="auth-field adhesion-full">
                    <label>Motivation / sujet de recherche (min. 2 lignes) *</label>
                    <textarea name="motivation" rows="5"
                              placeholder="Décrivez brièvement vos motivations ou axe de recherche..."
                              required></textarea>
                </div>

                <div class="auth-field adhesion-full">
                    <label>Curriculum Vitae (PDF uniquement) *</label>
                    <input type="file" name="cv" accept="application/pdf" required>
                </div>

                <div class="adhesion-full" style="margin-top:8px;">
                    <button type="submit" class="btn-accent auth-submit">
                        Soumettre la demande d'adhésion
                    </button>
                </div>

            </form>

        </div>

    </div>
</section>


