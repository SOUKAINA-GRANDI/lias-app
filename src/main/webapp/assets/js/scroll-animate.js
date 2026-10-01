document.addEventListener("DOMContentLoaded", function () {

    const cards = document.querySelectorAll(".member-card");

    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add("visible");
            }
        });
    }, {
        threshold: 0.1
    });

    cards.forEach(card => {
        observer.observe(card);
    });

});

document.addEventListener("DOMContentLoaded", function () {

    const cards = document.querySelectorAll(".event-card");

    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add("visible");
            }
        });
    }, { threshold: 0.1 });

    cards.forEach(card => observer.observe(card));
});
document.addEventListener("DOMContentLoaded", function () {

    const cards = document.querySelectorAll(".publication-card");

    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add("visible");
            }
        });
    }, { threshold: 0.1 });

    cards.forEach(card => observer.observe(card));
});