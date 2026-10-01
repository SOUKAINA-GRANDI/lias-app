// ✅ Toggle sidebar (mobile)
function toggleSidebar() {
    const sidebar = document.querySelector(".app-sidebar");
    if (sidebar) {
        sidebar.classList.toggle("active");
    }
}

// ✅ Dark mode
function toggleDarkMode() {
    document.body.classList.toggle("dark-mode");

    if (document.body.classList.contains("dark-mode")) {
        localStorage.setItem("theme", "dark");
    } else {
        localStorage.setItem("theme", "light");
    }
}

// ✅ Charger thème sauvegardé
document.addEventListener("DOMContentLoaded", function () {

    const savedTheme = localStorage.getItem("theme");

    if (savedTheme === "dark") {
        document.body.classList.add("dark-mode");
    }

    // ✅ Auto-dismiss alerts
    setTimeout(function () {
        document.querySelectorAll(".auto-dismiss")
            .forEach(function (el) {
                el.style.opacity = "0";
                setTimeout(() => el.remove(), 400);
            });
    }, 4000);

});