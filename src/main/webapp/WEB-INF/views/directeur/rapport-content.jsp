<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<c:if test="${not empty sessionScope.success}">
    <div class="alert-success-aggregation">✅ <c:out value="${sessionScope.success}"/></div>
    <c:remove var="success" scope="session"/>
</c:if>
<c:if test="${not empty sessionScope.error}">
    <div style="background-color:#FEE2E2; border:1px solid #EF4444; color:#991B1B; border-radius:8px; padding:12px 16px; font-size:13px; font-weight:600; margin-bottom:24px; text-align:center;">
        ⚠️ <c:out value="${sessionScope.error}"/>
    </div>
    <c:remove var="error" scope="session"/>
</c:if>
<style>
    /* ── STYLE ÉCRAN (NAVIGATEUR) ── */
    .rapport-header-main {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 24px;
        margin-top: 10px;
    }
    .rapport-header-main h2 {
        margin: 0;
        font-size: 22px;
        color: #0F172A;
        font-weight: 700;
    }
    .rapport-header-main .subtitle {
        font-size: 11px;
        font-weight: 700;
        text-transform: uppercase;
        letter-spacing: 0.08em;
        color: #94A3B8;
        display: inline-block;
        margin-top: 6px;
    }

    .alert-success-aggregation {
        background-color: #ECFDF5;
        border: 1px solid #10B981;
        color: #065F46;
        border-radius: 8px;
        padding: 12px 16px;
        font-size: 13px;
        font-weight: 600;
        margin-bottom: 24px;
        text-align: center;
    }

    /* Container de la feuille de rapport */
    .report-preview-paper {
        background-color: #FFFFFF;
        border: 1px solid #E2E8F0;
        border-radius: 16px;
        padding: 40px;
        box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05);
        margin-bottom: 24px;
        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    }

    .paper-header {
        text-align: center;
        margin-bottom: 32px;
        border-bottom: 2px solid #0F172A;
        padding-bottom: 16px;
    }
    .paper-institution {
        font-size: 11px;
        font-weight: 700;
        color: #D97706;
        letter-spacing: 0.05em;
        text-transform: uppercase;
        margin-bottom: 4px;
    }
    .paper-title {
        font-size: 18px;
        font-weight: 800;
        color: #0F172A;
        text-transform: uppercase;
        margin: 0 0 6px 0;
    }
    .paper-meta {
        font-size: 11px;
        font-weight: 700;
        color: #64748B;
        text-transform: uppercase;
        letter-spacing: 0.02em;
    }

    .paper-metrics-row {
        display: grid;
        grid-template-columns: repeat(4, 1fr);
        gap: 16px;
        margin-bottom: 32px;
    }
    .metric-paper-box {
        border: 1.5px solid #0F172A;
        border-radius: 12px;
        padding: 12px;
        text-align: center;
        background-color: #FAFAFA;
        -webkit-print-color-adjust: exact;
        print-color-adjust: exact;
    }
    .metric-paper-label {
        font-size: 9px;
        font-weight: 700;
        text-transform: uppercase;
        color: #64748B;
        letter-spacing: 0.02em;
        margin-bottom: 4px;
        display: block;
    }
    .metric-paper-num {
        font-size: 24px;
        font-weight: 800;
        color: #0F172A;
    }

    .paper-section {
        margin-bottom: 24px;
    }
    .paper-section-title {
        font-size: 11px;
        font-weight: 800;
        text-transform: uppercase;
        color: #0F172A;
        border-bottom: 1.5px solid #0F172A;
        padding-bottom: 4px;
        margin-bottom: 10px;
        letter-spacing: 0.02em;
    }
    .paper-section-text {
        font-size: 12px;
        line-height: 1.6;
        color: #334155;
        text-align: justify;
    }

    .paper-signature-block {
        margin-top: 40px;
        display: flex;
        flex-direction: column;
        align-items: flex-end;
        padding-right: 10px;
    }
    .signature-title {
        font-size: 10px;
        font-weight: 800;
        color: #64748B;
        text-transform: uppercase;
        margin-bottom: 4px;
    }
    .signature-name {
        font-size: 13px;
        font-weight: 700;
        color: #0F172A;
        font-style: italic;
    }
    .paper-footer-notice {
        font-size: 10px;
        color: #94A3B8;
        margin-top: 40px;
        border-top: 1px dashed #E2E8F0;
        padding-top: 8px;
    }

    /* ── 🖨️ CORRECTION STRICTE POUR L'IMPRESSION SANS COUPURE ── */
    @media print {
        /* 1. Masquer de manière agressive l'ensemble du layout global externe (Navbar, Sidebar, etc.) */
        body *, html * {
            visibility: hidden;
        }

        /* 2. Réinitialiser et cibler uniquement la zone de la feuille du rapport */
        .report-preview-paper, .report-preview-paper * {
            visibility: visible;
        }

        /* 3. Forcer le positionnement absolu au pixel près de la zone à imprimer pour éviter les décalages */
        .report-preview-paper {
            position: absolute;
            left: 0 !important;
            top: 0 !important;
            width: 100% !important;
            max-width: 210mm !important; /* Largeur standard feuille A4 */
            box-shadow: none !important;
            border: none !important;
            padding: 10mm !important; /* Marges intérieures légères pour éviter les bords */
            margin: 0 !important;
        }

        /* 4. Forcer l'adaptation des couleurs d'arrière-plan */
        * {
            -webkit-print-color-adjust: exact !important;
            print-color-adjust: exact !important;
        }
        
        /* 5. Gérer la mise en page des 4 grilles pour éviter que les boîtes se rentrent dedans */
        .paper-metrics-row {
            display: grid !important;
            grid-template-columns: repeat(4, 1fr) !important;
            gap: 10px !important;
            width: 100% !important;
        }

        /* 6. Le graphique Chart.js est un aperçu écran uniquement, pas dans le PDF imprimé */
        .no-print, .no-print * {
            display: none !important;
            visibility: hidden !important;
        }
    }
</style>

<div class="rapport-header-main">
    <div>
        <h2>Générateur Automatique de Rapport Annuel</h2>
        <span class="subtitle">Conformément aux directives : Agrégation consolidée des données du laboratoire</span>
    </div>
</div>

<div style="background-color: #FFFFFF; border: 1px solid #E5E7EB; border-radius: 16px; padding: 20px 24px; margin-bottom: 32px; display: flex; justify-content: space-between; align-items: center; gap: 20px; box-shadow: 0 1px 3px rgba(0,0,0,0.01);">
    
    <form action="${pageContext.request.contextPath}/directeur/rapport" method="GET" style="display: flex; align-items: center; gap: 10px; margin: 0;">
        <label style="font-size: 13px; font-weight: 600; color: #475569;">Année d'analyse :</label>
        <input type="number" name="annee" style="background-color: #F8FAFC; border: 1px solid #E2E8F0; border-radius: 8px; padding: 8px 16px; font-size: 14px; font-weight: 600; color: #0F172A; outline: none;" value="${rapport.year}" min="2020" max="2030" required />
        <button type="submit" style="background-color: #0F172A; color: white; border: none; padding: 8px 16px; font-size: 13px; font-weight: 600; border-radius: 8px; cursor: pointer;">Analyser</button>
    </form>

    <div style="display: flex; gap: 12px;">
        <form action="${pageContext.request.contextPath}/directeur/rapport" method="POST" style="margin:0;"
              onsubmit="return confirm('Archiver définitivement le rapport ${rapport.year} ? Cette action ne peut être faite qu\'une fois par année.');">
            <input type="hidden" name="annee" value="${rapport.year}">
            <button type="submit" style="background-color:#0369A1; color:white; border:none; padding:10px 18px; font-size:13px; font-weight:600; border-radius:8px; cursor:pointer;">
                🗄️ Archiver ce rapport
            </button>
        </form>
        <button onclick="window.print();" style="background-color: #EF4444; color: white; border: none; padding: 10px 18px; font-size: 13px; font-weight: 600; border-radius: 8px; cursor: pointer; display: inline-flex; align-items: center; gap: 8px; transition: background 0.2s;">
            📄 Exporter en PDF
        </button>
        <a href="${pageContext.request.contextPath}/directeur/rapport?action=excel&annee=${rapport.year}" style="background-color: #059669; color: white; border: none; padding: 10px 18px; font-size: 13px; font-weight: 600; border-radius: 8px; text-decoration: none; display: inline-flex; align-items: center; gap: 8px; transition: background 0.2s;">
            📊 Exporter en Excel
        </a>
    </div>
</div>

<div class="report-preview-paper no-print" style="padding:28px 32px;">
    <div class="paper-section-title" style="margin-bottom:16px;">Comparaison ${rapport.year - 1} → ${rapport.year}</div>
    <canvas id="chartComparaison" height="90"></canvas>
</div>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script>
    new Chart(document.getElementById('chartComparaison'), {
        type: 'bar',
        data: {
            labels: ['Publications', 'Événements', 'Conventions'],
            datasets: [
                {
                    label: '${rapport.year - 1}',
                    data: [${rapport.pubPrev}, ${rapport.eventsPrev}, ${rapport.convPrev}],
                    backgroundColor: '#CBD5E1'
                },
                {
                    label: '${rapport.year}',
                    data: [${rapport.pubCurrent}, ${rapport.eventsCurrent}, ${rapport.convCurrent}],
                    backgroundColor: '#D97706'
                }
            ]
        },
        options: {
            responsive: true,
            plugins: { legend: { position: 'bottom' } },
            scales: { y: { beginAtZero: true, ticks: { precision: 0 } } }
        }
    });
</script>

<div class="report-preview-paper">
    <div class="paper-header">
        <div class="paper-institution">Laboratoire d'Informatique et Aide à la Décision (LIAS)</div>
        <h3 class="paper-title">Rapport Scientifique &amp; Administratif Annuel</h3>
        <div class="paper-meta">Exercice Académique : ${rapport.year - 1}-${rapport.year} • FSBM Casablanca</div>
    </div>

    <div class="paper-metrics-row">
        <div class="metric-paper-box">
            <span class="metric-paper-label">Effectif d'Affiliés</span>
            <div class="metric-paper-num"><c:out value="${rapport.membresActifs}"/></div>
        </div>
        <div class="metric-paper-box">
            <span class="metric-paper-label">Publications Actives</span>
            <div class="metric-paper-num"><c:out value="${rapport.pubCurrent}"/></div>
        </div>
        <div class="metric-paper-box">
            <span class="metric-paper-label">Congrès &amp; Séminaires</span>
            <div class="metric-paper-num"><c:out value="${rapport.eventsCurrent}"/></div>
        </div>
        <div class="metric-paper-box">
            <span class="metric-paper-label">Immobilisation Stock</span>
            <div class="metric-paper-num"><c:out value="${fn:length(rapport.evenements)}"/></div>
        </div>
    </div>

    <div class="paper-section">
        <div class="paper-section-title">1. Constatations relatives aux doctorants &amp; grades</div>
        <div class="paper-section-text">
            Le LIAS compte actuellement <strong><c:out value="${rapport.membresActifs}"/> doctorants et enseignants-chercheurs</strong> inscrits au CED, encadrés par des enseignants-chercheurs permanents. Le ratio d'encadrement s'établit dans les normes préconisées par le CNRST. L'historique des mandats atteste d'une alternance démocratique au sein des directeurs thématiques.
        </div>
    </div>

    <div class="paper-section">
        <div class="paper-section-title">2. Productions scientifiques consolidées par thématique</div>
        <div class="paper-section-text">
            Pour l'exercice ${rapport.year - 1}-${rapport.year}, la compilation des bases DBLP &amp; Scopus recense <strong><c:out value="${rapport.pubCurrent}"/> articles</strong> parus dans des journaux indexés à fort facteur d'impact, et communications de haut rang en conférences internationales rattachées au pôle d'aide à la décision.
        </div>
    </div>

    <div class="paper-section">
        <div class="paper-section-title">3. Actifs physiques &amp; logistique en service</div>
        <div class="paper-section-text">
            L'inventaire consolidé répertorie un volume stable d'équipements de serveurs HPC activement affectés pour les calculs de Deep Learning des doctorants, et des terminaux disponibles en stock prêt-à-l'usage pour les nouveaux arrivants doctorants après validation des demandes de matériels d'affectation.
        </div>
    </div>

    <div class="paper-section">
        <div class="paper-section-title">4. Liste détaillée des manifestations scientifiques rattachées</div>
        <ul style="font-size: 12px; color: #334155; padding-left: 20px; line-height: 1.6; margin-top: 10px;">
            <c:forEach var="ev" items="${rapport.evenements}">
                <li style="margin-bottom: 6px;">
                    <strong><c:out value="${ev.titre}"/></strong> — Organisé à <c:out value="${ev.lieu}"/>
                </li>
            </c:forEach>
        </ul>
    </div>

    <div class="paper-signature-block">
        <div class="signature-title">Le Directeur du LIAS</div>
        <div class="signature-name">Pr. Faouzia Benabbou</div>
        <div style="font-size: 9px; color: #94A3B8; margin-top:2px;">Certifié FSBM LIAS Secure Gate</div>
    </div>

    <div class="paper-footer-notice">
        * Document généré de manière automatique avec signature électronique intégrée.
    </div>
    <div class="report-preview-paper no-print" style="padding:28px 32px; margin-top:24px;">
    <div class="paper-section-title" style="margin-bottom:16px;">Historique des rapports archivés</div>

    <c:choose>
        <c:when test="${not empty historique}">
            <table style="width:100%; border-collapse:collapse;">
                <thead>
                    <tr style="text-align:left; font-size:12px; text-transform:uppercase; color:#64748B;">
                        <th style="padding:8px;">Année</th>
                        <th style="padding:8px;">Généré le</th>
                        <th style="padding:8px;">Publications</th>
                        <th style="padding:8px;">Événements</th>
                        <th style="padding:8px;">Conventions</th>
                        <th style="padding:8px;">Membres actifs</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="h" items="${historique}">
                        <tr style="border-top:1px solid #F1F5F9; font-size:14px;">
                            <td style="padding:8px;"><strong>${h.annee}</strong></td>
                            <td style="padding:8px;">${h.dateGeneration}</td>
                            <td style="padding:8px;">${h.nbPublications}</td>
                            <td style="padding:8px;">${h.nbEvenements}</td>
                            <td style="padding:8px;">${h.nbConventions}</td>
                            <td style="padding:8px;">${h.membresActifs}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <p style="color:#94A3B8; font-size:13px;">Aucun rapport archivé pour l'instant.</p>
        </c:otherwise>
    </c:choose>
</div>
</div>