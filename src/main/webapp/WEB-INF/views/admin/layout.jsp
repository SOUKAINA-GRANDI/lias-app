<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width,initial-scale=1.0"/>
    <title>LIAS Admin — <c:out value="${pageTitle}"/></title>
    <meta name="csrf-token" content="${csrfToken}">
    <script src="${pageContext.request.contextPath}/assets/js/csrf.js"></script>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@300;400;500;600;700&family=Fraunces:opsz,wght@9..144,400;9..144,600;9..144,700&family=JetBrains+Mono:wght@400;500&display=swap" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/@tabler/icons-webfont@latest/tabler-icons.min.css" rel="stylesheet"/>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css"/>
    <style>
/* ── LIAS ADMIN LAYOUT ── */
:root{
  --ink:#0B1829;--ink2:#2D3F55;--ink3:#64748B;--ink4:#94A3B8;
  --gold:#C9963C;--gold2:#E8B55A;--goldl:#FDF7ED;
  --slate:#F1F5F9;--slate2:#E2E8F0;--white:#fff;
  --green:#047857;--greenl:#ECFDF5;
  --red:#BE123C;--redl:#FFF1F2;--redb:#FECDD3;
  --blue:#1D4ED8;--bluel:#EFF6FF;
  --amber:#B45309;--amberl:#FFFBEB;
  --ff-d:'Fraunces',Georgia,serif;
  --ff-b:'Plus Jakarta Sans',system-ui,sans-serif;
  --ff-m:'JetBrains Mono',monospace;
  --sidebar:258px;--header:62px;
  --shadow-sm:0 1px 3px rgba(11,24,41,.06);
}
*,*::before,*::after{box-sizing:border-box;margin:0;padding:0}
html,body{font-family:var(--ff-b);font-size:15px;color:var(--ink);background:var(--slate);height:100%;-webkit-font-smoothing:antialiased}
a{text-decoration:none;color:inherit}

.lias-app{display:flex;min-height:100vh}
.lias-main{margin-left:var(--sidebar);flex:1;display:flex;flex-direction:column;min-height:100vh}

/* SIDEBAR */
.sidebar{width:var(--sidebar);background:var(--ink);display:flex;flex-direction:column;position:fixed;top:0;left:0;height:100vh;z-index:300;overflow:hidden}
.sidebar::before{content:'';position:absolute;inset:0;background-image:url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='48' height='48'%3E%3Cpath d='M24 2L46 24L24 46L2 24Z' fill='none' stroke='rgba(201,150,60,0.07)' stroke-width='1'/%3E%3Cpath d='M24 12L36 24L24 36L12 24Z' fill='none' stroke='rgba(201,150,60,0.043)' stroke-width='1'/%3E%3C/svg%3E");background-size:48px 48px;pointer-events:none}
.sidebar::after{content:'';position:absolute;top:0;left:0;right:0;height:200px;background:radial-gradient(ellipse 100% 75% at 50% 0%,rgba(201,150,60,.09) 0%,transparent 65%);pointer-events:none}

.sb-head{padding:1.35rem 1.45rem 1.05rem;border-bottom:1px solid rgba(201,150,60,.13);position:relative;z-index:1;flex-shrink:0}
.sb-logo{display:flex;align-items:center;gap:.68rem;margin-bottom:.42rem}
.sb-gem{width:34px;height:34px;background:var(--gold);border-radius:9px;display:flex;align-items:center;justify-content:center;overflow:hidden;flex-shrink:0}
.sb-gem img{width:100%;height:100%;object-fit:cover}
.sb-name{font-family:var(--ff-d);font-size:1.32rem;font-weight:700;color:var(--gold)}
.sb-tag{font-size:.55rem;color:rgba(255,255,255,.2);letter-spacing:1.7px;text-transform:uppercase;font-family:var(--ff-m)}
.sb-admin-badge{display:inline-flex;align-items:center;gap:.3rem;margin-top:.5rem;background:rgba(190,18,60,.15);border:1px solid rgba(190,18,60,.25);color:#FDA4AF;font-size:.6rem;font-family:var(--ff-m);padding:.2rem .65rem;border-radius:20px;letter-spacing:.5px}

.sb-nav{flex:1;overflow-y:auto;padding:.8rem 0;position:relative;z-index:1;scrollbar-width:none}
.sb-nav::-webkit-scrollbar{display:none}
.sb-sec{padding:.68rem 1.45rem .22rem;font-size:.54rem;letter-spacing:2.2px;text-transform:uppercase;color:rgba(255,255,255,.2);font-family:var(--ff-m)}
.sb-item{display:flex;align-items:center;gap:.68rem;padding:.54rem 1.45rem;color:rgba(255,255,255,.48);font-size:.825rem;transition:all 140ms;border-left:2px solid transparent}
.sb-item:hover{color:rgba(255,255,255,.88);background:rgba(255,255,255,.04);border-left-color:rgba(201,150,60,.32)}
.sb-item.active{color:var(--gold);background:rgba(201,150,60,.09);border-left-color:var(--gold);font-weight:500}
.sb-item i{font-size:.88rem;width:15px;text-align:center;flex-shrink:0}
.sb-item.active i{color:var(--gold)}

.sb-foot{padding:.9rem 1.45rem;border-top:1px solid rgba(201,150,60,.11);display:flex;align-items:center;gap:.65rem;flex-shrink:0;position:relative;z-index:1}
.sb-av{width:31px;height:31px;border-radius:50%;background:rgba(190,18,60,.2);border:1.5px solid rgba(190,18,60,.35);color:#FDA4AF;display:flex;align-items:center;justify-content:center;font-size:.66rem;font-weight:600;font-family:var(--ff-m);flex-shrink:0}
.sb-uname{font-size:.79rem;font-weight:500;color:rgba(255,255,255,.8);white-space:nowrap;overflow:hidden;text-overflow:ellipsis;flex:1;min-width:0}
.sb-urole{font-size:.61rem;color:rgba(255,255,255,.3);font-family:var(--ff-m);display:block}
.sb-out{background:none;border:none;color:rgba(255,255,255,.22);cursor:pointer;padding:4px;flex-shrink:0;font-size:.9rem;transition:color 140ms}
.sb-out:hover{color:var(--gold)}

/* TOPBAR */
.topbar{height:var(--header);background:rgba(241,245,249,.94);backdrop-filter:blur(14px);border-bottom:1px solid var(--slate2);display:flex;align-items:center;padding:0 1.85rem;position:sticky;top:0;z-index:200;box-shadow:var(--shadow-sm);gap:.85rem}
.tb-crumb{display:flex;align-items:center;gap:.4rem;font-size:.775rem;color:var(--ink3)}
.tb-crumb .sep{color:var(--slate2)}
.tb-crumb .cur{color:var(--ink);font-weight:500}
.tb-right{margin-left:auto;display:flex;align-items:center;gap:.8rem}
.tb-admin-tag{background:var(--redl);color:var(--red);border:1px solid var(--redb);font-size:.65rem;font-family:var(--ff-m);font-weight:600;padding:.22rem .72rem;border-radius:20px;display:flex;align-items:center;gap:.3rem}
.tb-btn{width:32px;height:32px;border-radius:50%;border:1px solid var(--slate2);background:var(--white);display:flex;align-items:center;justify-content:center;cursor:pointer;color:var(--ink2);font-size:.88rem;transition:all 140ms}
.tb-btn:hover{border-color:var(--gold);color:var(--gold)}
.tb-div{width:1px;height:18px;background:var(--slate2)}
.tb-user{display:flex;align-items:center;gap:.5rem;padding:.22rem .6rem .22rem .22rem;border-radius:22px;border:1px solid var(--slate2);background:var(--white);cursor:pointer}
.tb-uav{width:26px;height:26px;border-radius:50%;background:var(--redl);border:1.5px solid var(--redb);color:var(--red);display:flex;align-items:center;justify-content:center;font-size:.6rem;font-weight:700;font-family:var(--ff-m)}
.tb-un{font-size:.805rem;font-weight:500;color:var(--ink)}

/* CONTENU */
.lias-content{flex:1;padding:2rem 2.25rem}
.ph{display:flex;align-items:flex-start;justify-content:space-between;margin-bottom:1.75rem;flex-wrap:wrap;gap:1rem}
.ph-eye{font-size:.58rem;font-family:var(--ff-m);letter-spacing:2.2px;text-transform:uppercase;color:var(--gold);margin-bottom:.3rem;font-weight:500}
.ph-title{font-family:var(--ff-d);font-size:1.95rem;font-weight:600;color:var(--ink);line-height:1.12}
.ph-sub{font-size:.845rem;color:var(--ink3);margin-top:.3rem}
.ph-actions{display:flex;gap:.6rem;align-items:center}

/* STATS */
.stats{display:grid;grid-template-columns:repeat(auto-fit,minmax(168px,1fr));gap:.95rem;margin-bottom:1.75rem}
.stat{background:var(--white);border:1px solid var(--slate2);border-radius:14px;padding:1.15rem 1.35rem;position:relative;overflow:hidden;transition:transform 200ms}
.stat:hover{transform:translateY(-2px)}
.stat::before{content:'';position:absolute;top:0;left:0;right:0;height:2px;background:linear-gradient(90deg,var(--gold),var(--gold2))}
.stat-ico{position:absolute;right:.9rem;top:50%;transform:translateY(-50%);font-size:2rem;color:var(--slate2);pointer-events:none}
.stat-lbl{font-size:.6rem;font-family:var(--ff-m);text-transform:uppercase;letter-spacing:1.2px;color:var(--ink3);margin-bottom:.28rem}
.stat-val{font-family:var(--ff-d);font-size:2.2rem;font-weight:700;color:var(--ink);line-height:1;margin-bottom:.18rem}
.stat-hint{font-size:.74rem;color:var(--ink3)}

/* CARDS & TABLES */
.card{background:var(--white);border:1px solid var(--slate2);border-radius:14px;padding:1.35rem;box-shadow:var(--shadow-sm)}
.card-hd{display:flex;align-items:center;justify-content:space-between;margin-bottom:1.05rem;padding-bottom:.88rem;border-bottom:1px solid var(--slate)}
.card-t{font-family:var(--ff-d);font-size:1rem;font-weight:600;color:var(--ink);display:flex;align-items:center;gap:.4rem}
.card-t i{color:var(--gold);font-size:.92rem}
.card-lnk{font-size:.76rem;color:var(--gold);font-weight:500;display:flex;align-items:center;gap:.24rem}
.g2{display:grid;grid-template-columns:1fr 1fr;gap:1.2rem}
.tbl-card{background:var(--white);border:1px solid var(--slate2);border-radius:14px;overflow:hidden;box-shadow:var(--shadow-sm)}
.tbl-hd{padding:.95rem 1.35rem;border-bottom:1px solid var(--slate);display:flex;align-items:center;justify-content:space-between;gap:1rem;flex-wrap:wrap}
.tbl-t{font-family:var(--ff-d);font-size:1rem;font-weight:600;color:var(--ink);display:flex;align-items:center;gap:.4rem}
.tbl-t i{color:var(--gold)}
table.lias-tbl{width:100%;border-collapse:collapse;font-size:.845rem}
.lias-tbl thead tr{background:#0E2035}
.lias-tbl thead th{color:rgba(255,255,255,.65);font-size:.6rem;font-weight:500;font-family:var(--ff-m);letter-spacing:1.1px;text-transform:uppercase;padding:.8rem 1.1rem;text-align:left}
.lias-tbl thead th:first-child{padding-left:1.35rem}
.lias-tbl thead th:last-child{padding-right:1.35rem}
.lias-tbl tbody tr{border-bottom:1px solid var(--slate);transition:background 120ms}
.lias-tbl tbody tr:last-child{border-bottom:none}
.lias-tbl tbody tr:hover{background:var(--goldl)}
.lias-tbl td{padding:.8rem 1.1rem;vertical-align:middle}
.lias-tbl td:first-child{padding-left:1.35rem}
.lias-tbl td:last-child{padding-right:1.35rem}

/* BUTTONS & BADGES */
.btn{display:inline-flex;align-items:center;gap:.42rem;padding:.5rem 1.08rem;border-radius:8px;font-size:.845rem;font-weight:500;cursor:pointer;border:1px solid transparent;font-family:var(--ff-b);transition:all 140ms}
.btn-ink{background:var(--ink);color:#fff;border-color:var(--ink)}.btn-ink:hover{background:var(--ink2)}
.btn-gold{background:var(--gold);color:var(--ink);font-weight:600}.btn-gold:hover{background:var(--gold2)}
.btn-danger{background:var(--redl);color:var(--red);border-color:var(--redb)}
.btn-outline{background:transparent;color:var(--ink);border-color:var(--slate2)}.btn-outline:hover{border-color:var(--ink)}
.btn-sm{padding:.3rem .72rem;font-size:.775rem}
.btn-ico{padding:.44rem;border-radius:8px}
.bdg{display:inline-flex;align-items:center;gap:.24rem;padding:.18rem .6rem;border-radius:20px;font-size:.67rem;font-weight:600;font-family:var(--ff-m)}
.bdg-green{background:var(--greenl);color:var(--green)}.bdg-red{background:var(--redl);color:var(--red)}.bdg-amber{background:var(--amberl);color:var(--amber)}.bdg-blue{background:var(--bluel);color:var(--blue)}.bdg-gray{background:var(--slate);color:var(--ink2)}
.tbl-actions{display:flex;gap:.38rem;align-items:center}
.fxc{display:flex;align-items:center}.fxb{display:flex;align-items:center;justify-content:space-between}
.gap-2{gap:.78rem}.bold{font-weight:600}.muted{color:var(--ink3);font-size:.82rem}.mono{font-family:var(--ff-m)}
.av{border-radius:50%;background:var(--ink2);color:var(--gold);display:inline-flex;align-items:center;justify-content:center;font-weight:600;font-family:var(--ff-m);flex-shrink:0}
.av-sm{width:30px;height:30px;font-size:.66rem}
hr.div{border:none;border-top:1px solid var(--slate);margin:1.35rem 0}
.alert{padding:.74rem .98rem;border-radius:8px;font-size:.835rem;display:flex;align-items:flex-start;gap:.58rem;line-height:1.48}
.alert-ok{background:var(--greenl);border-left:3px solid #22C55E;color:var(--green)}
.alert-e{background:var(--redl);border-left:3px solid #EF4444;color:var(--red)}
.alert-i{background:var(--bluel);border-left:3px solid #3B82F6;color:var(--blue)}
.search-box{display:flex;align-items:center;gap:.52rem;background:var(--white);border:1.5px solid var(--slate2);border-radius:8px;padding:.48rem .88rem}
.search-box:focus-within{border-color:var(--gold);box-shadow:0 0 0 3px rgba(201,150,60,.18)}
.search-box i{color:var(--ink3);font-size:.9rem}
.search-box input{border:none;outline:none;font-size:.845rem;font-family:var(--ff-b);color:var(--ink);background:transparent;flex:1}
.fctl{padding:.56rem .84rem;border:1.5px solid var(--slate2);border-radius:8px;font-size:.855rem;font-family:var(--ff-b);color:var(--ink);background:var(--white);width:100%}
.fctl:focus{outline:none;border-color:var(--gold);box-shadow:0 0 0 3px rgba(201,150,60,.18)}
.empty{text-align:center;padding:3rem 1rem;color:var(--ink3)}
.empty-ico{width:58px;height:58px;border-radius:50%;background:var(--slate);display:flex;align-items:center;justify-content:center;margin:0 auto .9rem}
.empty-ico i{font-size:1.6rem;color:var(--slate2)}

/* FOOTER */
.lias-footer{padding:.88rem 2.25rem;border-top:1px solid var(--slate2);background:var(--white);display:flex;justify-content:space-between;align-items:center;font-size:.72rem;color:var(--ink4)}
.lias-footer .ver{font-family:var(--ff-m);font-size:.64rem;background:var(--slate);padding:2px 7px;border-radius:4px}

@keyframes fadeUp{from{opacity:0;transform:translateY(10px)}to{opacity:1;transform:none}}
.anim{animation:fadeUp .3s ease both}
.anim-1{animation-delay:.06s}.anim-2{animation-delay:.12s}.anim-3{animation-delay:.18s}
    </style>
</head>
<body>
<div class="lias-app">

    <!-- SIDEBAR ADMIN -->
    <aside class="sidebar">
        <div class="sb-head">
            <div class="sb-logo">
                <div class="sb-gem">
                    <img src="${pageContext.request.contextPath}/assets/images/logo-lias.png" alt="LIAS" onerror="this.style.display='none';this.parentElement.textContent='L'"/>
                </div>
                <div class="sb-name">LIAS</div>
            </div>
            <div class="sb-tag">Administration système</div>
            <div class="sb-admin-badge"><i class="ti ti-shield-check" style="font-size:.7rem"></i>Espace Admin</div>
        </div>

          <nav class="sb-nav">
            <span class="sb-sec">Administration</span>
            
            <%-- TABLEAU DE BORD --%>
            <a href="${pageContext.request.contextPath}/admin/dashboard"
               class="sb-item <c:if test="${activeMenu == 'dashboard'}">active</c:if>">
                <i class="ti ti-layout-dashboard"></i>Tableau de bord
            </a>
            
            <%-- UTILISATEURS (CORRIGÉ : sb-item & icône valide ti-users) --%>
            <a href="${pageContext.request.contextPath}/admin/utilisateurs" 
               class="sb-item <c:if test="${activeMenu == 'utilisateurs'}">active</c:if>">
                <i class="ti ti-users"></i>Utilisateurs
            </a>
            
            <%-- PARAMÉTRAGE --%>
            <a href="${pageContext.request.contextPath}/admin/parametrage"
               class="sb-item <c:if test="${activeMenu == 'parametrage'}">active</c:if>">
                <i class="ti ti-settings"></i>Paramétrage
            </a>
            
            <%-- MON PROFIL (CORRIGÉ : sb-item & icône valide ti-user-circle) --%>
            <a href="${pageContext.request.contextPath}/admin/profil" 
               class="sb-item <c:if test="${activeMenu == 'profil'}">active</c:if>">
                <i class="ti ti-user-circle"></i>Mon Profil
            </a>
            
            <%-- JOURNAL D'AUDIT --%>
            <a href="${pageContext.request.contextPath}/admin/audit"
               class="sb-item <c:if test="${activeMenu == 'audit'}">active</c:if>">
                <i class="ti ti-clipboard-data"></i>Journal d'audit
            </a>
            
            <%-- NOTIFICATIONS --%>
            <a href="${pageContext.request.contextPath}/admin/notifications"
               class="sb-item <c:if test="${activeMenu == 'notifications'}">active</c:if>">
                <i class="ti ti-bell"></i>Notifications
            </a>
        </nav>
        <div class="sb-foot">
            <div class="sb-av">
                <c:choose>
                    <c:when test="${not empty sessionScope.user.nom}">
                        ${fn:substring(sessionScope.user.prenom, 0, 1)}${fn:substring(sessionScope.user.nom, 0, 1)}
                    </c:when>
                    <c:otherwise>AD</c:otherwise>
                </c:choose>
            </div>
            <div style="flex:1;min-width:0">
                <div class="sb-uname">
                    <c:out value="${not empty sessionScope.user.nomComplet ? sessionScope.user.nomComplet : sessionScope.user.email}"/>
                </div>
                <span class="sb-urole">Administrateur</span>
            </div>
            <a href="${pageContext.request.contextPath}/logout" class="sb-out" title="Déconnexion">
                <i class="ti ti-logout"></i>
            </a>
        </div>
    </aside>

    <!-- MAIN -->
    <div class="lias-main">
        <!-- TOPBAR -->
        <header class="topbar">
            <div class="tb-crumb">
                <i class="ti ti-home" style="font-size:.78rem"></i>
                <span class="sep">›</span>
                <span>Admin</span>
                <c:if test="${not empty pageTitle}">
                    <span class="sep">›</span>
                    <span class="cur"><c:out value="${pageTitle}"/></span>
                </c:if>
            </div>
            <div class="tb-right">
                <jsp:include page="/WEB-INF/views/shared/_search-box.jsp"/>
                <div class="tb-admin-tag"><i class="ti ti-shield" style="font-size:.75rem"></i>Mode Administration</div>
                <div class="tb-div"></div>
                <div class="tb-user">
                    <div class="tb-uav">
                        <c:choose>
                            <c:when test="${not empty sessionScope.user.nom}">
                                ${fn:substring(sessionScope.user.prenom, 0, 1)}${fn:substring(sessionScope.user.nom, 0, 1)}
                            </c:when>
                            <c:otherwise>AD</c:otherwise>
                        </c:choose>
                    </div>
                    <span class="tb-un"><c:out value="${not empty sessionScope.user.email ? sessionScope.user.email : 'Admin'}"/></span>
                </div>
            </div>
        </header>

        <!-- CONTENU DYNAMIQUE -->
        <main class="lias-content anim">
            <!-- Message flash -->
            <c:if test="${not empty sessionScope.success}">
                <div class="alert alert-ok" style="margin-bottom:1.2rem">
                    <i class="ti ti-circle-check"></i><span><c:out value="${sessionScope.success}"/></span>
                </div>
                <c:remove var="success" scope="session"/>
            </c:if>
            <c:if test="${not empty sessionScope.error}">
                <div class="alert alert-e" style="margin-bottom:1.2rem">
                    <i class="ti ti-alert-circle"></i><span><c:out value="${sessionScope.error}"/></span>
                </div>
                <c:remove var="error" scope="session"/>
            </c:if>

            <%-- Inclusion dynamique sécurisée du contenu de la page --%>
            <c:choose>
                <c:when test="${not empty contentPage}">
                    <jsp:include page="${contentPage}"/>
                </c:when>
                <c:otherwise>
                    <div class="alert alert-e">
                        <i class="ti ti-alert-circle"></i>
                        <span>Erreur interne : Aucun contenu (contentPage) n'a été spécifié par le contrôleur.</span>
                    </div>
                </c:otherwise>
            </c:choose>
        </main>

        <!-- FOOTER -->
        <footer class="lias-footer">
            <span>© 2026 Laboratoire LIAS — Faculté des Sciences Ben M'Sik, Université Hassan II Casablanca</span>
            <span class="ver">v1.0.0 · Admin</span>
        </footer>
    </div>
</div>
</body>
</html>