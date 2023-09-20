document.addEventListener("DOMContentLoaded", function () {
    const toggleButton = document.getElementById("toggleNav");
    const sidebar = document.querySelector(".sidebar");
    const content = document.querySelector(".content");

    toggleButton.addEventListener("click", function () {
        sidebar.classList.toggle("open");
        content.classList.toggle("open");
    });
});

