<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${not empty sessionScope.success}">
    <div style="margin-bottom:16px; padding:12px; background:#DCFCE7; color:#166534; border-radius:6px; font-size:14px; font-weight:500;">
        ✅ <c:out value="${sessionScope.success}"/>
    </div>
    <c:remove var="success" scope="session"/>
</c:if>
<c:if test="${not empty sessionScope.error}">
    <div style="margin-bottom:16px; padding:12px; background:#FEE2E2; color:#991B1B; border-radius:6px; font-size:14px; font-weight:500;">
        ⚠️ <c:out value="${sessionScope.error}"/>
    </div>
    <c:remove var="error" scope="session"/>
</c:if>
<style>
    .researchers-page-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 24px;
        margin-top: 10px;
    }
    .researchers-title-block h2 {
        margin: 0;
        font-size: 20px;
        color: #0F172A;
        font-weight: 700;
    }
    .researchers-title-block span {
        font-size: 11px;
        font-weight: 700;
        text-transform: uppercase;
        letter-spacing: .08em;
        color: #94A3B8;
        display: inline-block;
        margin-top: 4px;
    }

    /* ── GRILLE ── */
    .researchers-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(420px, 1fr));
        gap: 16px;
    }

    /* ── CARTE ── */
    .researcher-card {
        background: #FFFFFF;
        border: 1px solid #E5E7EB;
        border-radius: 14px;
        padding: 18px 20px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        box-shadow: 0 1px 3px rgba(0,0,0,0.03);
        transition: transform .2s, box-shadow .2s;
    }
    .researcher-card:hover {
        transform: translateY(-2px);
        box-shadow: 0 8px 20px rgba(0,0,0,0.07);
    }

    /* ── GAUCHE : Avatar + infos ── */
    .researcher-left-block {
        display: flex;
        align-items: center;
        gap: 14px;
        flex: 1;
        min-width: 0;
    }
    .researcher-avatar-circle {
        width: 46px;
        height: 46px;
        background: #EFF6FF;
        color: #2563EB;
        border: 1px solid #DBEAFE;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 16px;
        font-weight: 700;
        flex-shrink: 0;
    }
    .researcher-info-details { display: flex; flex-direction: column; gap: 4px; min-width: 0; }
    .researcher-info-details h4 {
        margin: 0;
        font-size: 14px;
        color: #1E293B;
        font-weight: 700;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }
    .researcher-subtext { font-size: 12px; color: #64748B; margin: 0; }
    .researcher-email {
        font-size: 11.5px;
        color: #94A3B8;
        text-decoration: none;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }

    /* ── BADGES ── */
    .status-tag {
        display: inline-block;
        font-size: 10px;
        font-weight: 700;
        padding: 2px 8px;
        border-radius: 4px;
        text-transform: uppercase;
        letter-spacing: .03em;
    }
    .tag-permanent  { background: #FEF3C7; color: #D97706; }
    .tag-associe    { background: #DBEAFE; color: #1D4ED8; }
    .tag-doctorant  { background: #EDE9FE; color: #6D28D9; }
    .tag-inactif    { background: #FEE2E2; color: #991B1B; }
    .tag-retraite   { background: #F1F5F9; color: #475569; }

    /* ── ACTIONS DROITE ── */
    .researcher-actions {
        display: flex;
        align-items: center;
        gap: 8px;
        flex-shrink: 0;
        margin-left: 12px;
    }
    /* Bouton message — NOUVEAU */
    .btn-message {
        display: inline-flex;
        align-items: center;
        gap: 6px;
        padding: 7px 13px;
        background: #EFF6FF;
        color: #2563EB;
        border: 1px solid #BFDBFE;
        border-radius: 8px;
        font-size: 12px;
        font-weight: 600;
        text-decoration: none;
        transition: all .2s;
        white-space: nowrap;
    }
    .btn-message:hover {
        background: #2563EB;
        color: #fff;
        border-color: #2563EB;
    }
    /* Bouton droit de publication */
    .btn-pub-right {
        font-size: 11px;
        padding: 6px 9px;
        border-radius: 8px;
        border: 1px solid #E5E7EB;
        background: #fff;
        cursor: pointer;
        white-space: nowrap;
    }
    .btn-pub-right.granted { color: #059669; }
    .btn-pub-right.revoked { color: #DC2626; }
    /* Bouton désactiver/réactiver */
    .btn-card-action {
        background: none;
        border: 1px solid #E5E7EB;
        color: #94A3B8;
        cursor: pointer;
        padding: 7px 10px;
        border-radius: 8px;
        transition: all .2s;
        font-size: 13px;
    }
    .btn-card-action:hover {
        background: #FEE2E2;
        color: #EF4444;
        border-color: #FECACA;
    }
    .btn-card-action.reactiver:hover {
        background: #D1FAE5;
        color: #059669;
        border-color: #A7F3D0;
    }

    /* ── EMPTY STATE ── */
    .empty-members {
        grid-column: 1 / -1;
        text-align: center;
        padding: 60px 20px;
        color: #94A3B8;
    }
    .empty-members i { font-size: 40px; margin-bottom: 12px; display: block; }

    @media (max-width: 640px) {
        .researchers-grid { grid-template-columns: 1fr; }
        .btn-message span { display: none; }
    }
</style>

<%-- ══ EN-TÊTE ══ --%>
<div class="researchers-page-header">
    <div class="researchers-title-block">
        <h2>Chercheurs du laboratoire LIAS</h2>
        <span>Annuaire d'affiliation — ${not empty membres ? membres.size() : 0} membre(s)</span>
    </div>
</div>


<div style="display:flex; gap:8px; margin-bottom:16px;">
    <a href="${pageContext.request.contextPath}/directeur/membres?filtre=actifs"
       style="padding:8px 14px; border-radius:8px; text-decoration:none; font-size:14px; font-weight:600;
              ${filtre == 'actifs' ? 'background:#1A237E; color:#fff;' : 'background:#F1F5F9; color:#334155;'}">
        Actifs (${countActifs})
    </a>
    <a href="${pageContext.request.contextPath}/directeur/membres?filtre=inactifs"
       style="padding:8px 14px; border-radius:8px; text-decoration:none; font-size:14px; font-weight:600;
              ${filtre == 'inactifs' ? 'background:#1A237E; color:#fff;' : 'background:#F1F5F9; color:#334155;'}">
        Inactifs (${countInactifs})
    </a>
    <a href="${pageContext.request.contextPath}/directeur/membres?filtre=tous"
       style="padding:8px 14px; border-radius:8px; text-decoration:none; font-size:14px; font-weight:600;
              ${filtre == 'tous' ? 'background:#1A237E; color:#fff;' : 'background:#F1F5F9; color:#334155;'}">
        Tous
    </a>
</div>

<%-- ══ GRILLE ══ --%>
<div class="researchers-grid">
    <c:choose>
        <c:when test="${not empty membres}">
            <c:forEach var="m" items="${membres}">

                <div class="researcher-card">

                    <%-- GAUCHE : Infos --%>
                    <div class="researcher-left-block">

                        <%-- Avatar --%>
                        <div class="researcher-avatar-circle">
                            ${not empty m.prenom ? m.prenom.substring(0,1).toUpperCase() : '?'}
                        </div>

                        <%-- Détails --%>
                        <div class="researcher-info-details">
                            <h4>${m.prenom} ${m.nom}</h4>

                            <%-- Badge statut --%>
                            <c:choose>
                                <c:when test="${m.statut eq 'PERMANENT'}">
                                    <span class="status-tag tag-permanent">Permanent</span>
                                </c:when>
                                <c:when test="${m.statut eq 'ASSOCIE'}">
                                    <span class="status-tag tag-associe">Associé</span>
                                </c:when>
                                <c:when test="${m.statut eq 'DOCTORANT'}">
                                    <span class="status-tag tag-doctorant">Doctorant</span>
                                </c:when>
                                <c:when test="${m.statut eq 'RETRAITE'}">
                                    <span class="status-tag tag-retraite">Retraité</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="status-tag tag-inactif">${m.statut}</span>
                                </c:otherwise>
                            </c:choose>
                            <c:if test="${not m.actif}">
                                <span class="status-tag tag-inactif">Désactivé</span>
                            </c:if>
                            <p class="researcher-subtext">
                                ${not empty m.etablissementOrigine ? m.etablissementOrigine : 'FSBM'}
                            </p>
                            <span class="researcher-email">${m.email}</span>
                        </div>
                    </div>

                    <%-- DROITE : Actions --%>
                    <div class="researcher-actions">

                        <%--
                            ✅ BOUTON "ENVOYER UN MESSAGE"
                            URL : /directeur/messages?action=start&with=${m.utilisateurId}
                            Le DirecteurMessagesServlet détecte action=start et crée/trouve
                            la conversation, puis redirige vers le chat.
                            On masque le bouton si c'est le directeur lui-même (utilisateurId en session).
                        --%>
                        <c:if test="${m.utilisateurId != sessionScope.user.id}">
                            <a href="${pageContext.request.contextPath}/directeur/messages?action=start&with=${m.utilisateurId}"
                               class="btn-message"
                               title="Démarrer une conversation privée avec ${m.prenom} ${m.nom}">
                                <i class="fa-solid fa-comment-dots"></i>
                                <span>Message</span>
                            </a>
                        </c:if>

                        <%-- Droit de publication : uniquement pour les membres associés --%>
                        <c:if test="${m.statut eq 'ASSOCIE'}">
                            <form action="${pageContext.request.contextPath}/directeur/membres" method="POST" style="margin:0;"
                                  onsubmit="return confirm('Changer le droit de publication de ${m.prenom} ${m.nom} ?');">
                                <input type="hidden" name="action" value="toggleDroitPublication">
                                <input type="hidden" name="id" value="${m.id}">
                                <button type="submit"
                                        class="btn-pub-right ${m.droitPublication ? 'granted' : 'revoked'}"
                                        title="Basculer le droit d'ajouter des publications">
                                    <c:choose>
                                        <c:when test="${m.droitPublication}">✅ Publication</c:when>
                                        <c:otherwise>🚫 Publication</c:otherwise>
                                    </c:choose>
                                </button>
                            </form>
                        </c:if>

                        <a href="${pageContext.request.contextPath}/directeur/membres?action=historique&id=${m.id}"
                           class="btn-card-action"
                           title="Voir l'historique de ${m.prenom} ${m.nom}">
                            <i class="fa-solid fa-clock-rotate-left"></i>
                        </a>

                        <%-- Désactiver / Réactiver --%>
                        <form action="${pageContext.request.contextPath}/directeur/membres"
                              method="POST"
                              style="margin:0"
                              onsubmit="return confirm('Confirmer cette action sur ${m.prenom} ${m.nom} ?')">
                            <input type="hidden" name="id" value="${m.id}">

                            <c:choose>
                                <c:when test="${m.actif}">
                                    <select name="statutDepart" required title="Motif du départ"
                                            style="padding:4px 6px; border-radius:8px; border:1px solid #CBD5E1; font-size:12px;">
                                        <option value="">-- Motif --</option>
                                        <option value="RETRAITE">Retraité</option>
                                        <option value="ANCIEN">Ancien membre</option>
                                    </select>
                                    <input type="hidden" name="action" value="desactiverAvecMotif">
                                    <button type="submit" class="btn-card-action" title="Désactiver ce membre">
                                        <i class="fa-solid fa-user-slash"></i>
                                    </button>
                                </c:when>
                                <c:otherwise>
                                    <input type="hidden" name="action" value="reactiver">
                                    <select name="statut" required title="Statut à la réactivation"
                                                style="padding:4px 6px; border-radius:8px; border:1px solid #CBD5E1; font-size:12px;">
                                            <option value="PERMANENT">Permanent</option>
                                            <option value="ASSOCIE">Associé</option>
                                            <option value="DOCTORANT">Doctorant</option>
                                    </select>
                                    <button type="submit"
                                            class="btn-card-action reactiver"
                                            title="Réactiver ce membre">
                                        <i class="fa-solid fa-user-check"></i>
                                    </button>
                                </c:otherwise>
                            </c:choose>
                        </form>

                    </div>
                </div>

            </c:forEach>
        </c:when>
        <c:otherwise>
            <div class="empty-members">
                <i class="fa-solid fa-users-slash"></i>
                <p style="font-size:16px;font-weight:600;color:#334155;margin:0 0 6px">
                    Aucun membre trouvé
                </p>
                <p style="font-size:13px;margin:0">
                    La liste des chercheurs affiliés au laboratoire apparaîtra ici.
                </p>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<c:if test="${totalPages > 1}">
    <div style="display: flex; justify-content: space-between; align-items: center; padding-top: 16px; margin-top: 16px; border-top: 1px solid #f1f5f9;">
        <div style="font-size: 14px; color: #64748b;">
            Page <strong>${currentPage}</strong> sur <strong>${totalPages}</strong>
        </div>
        <div style="display: flex; gap: 8px;">
            <c:if test="${currentPage > 1}">
                <a href="${pageContext.request.contextPath}/directeur/membres?filtre=${filtre}&page=${currentPage - 1}"
                   style="background: #f1f5f9; color: #1e293b; text-decoration: none; padding: 8px 16px; border-radius: 6px; font-size: 13px; font-weight: bold;">
                   &larr; Précédent
                </a>
            </c:if>
            <c:if test="${currentPage < totalPages}">
                <a href="${pageContext.request.contextPath}/directeur/membres?filtre=${filtre}&page=${currentPage + 1}"
                   style="background: #f1f5f9; color: #1e293b; text-decoration: none; padding: 8px 16px; border-radius: 6px; font-size: 13px; font-weight: bold;">
                   Suivant &rarr;
                </a>
            </c:if>
        </div>
    </div>
</c:if>