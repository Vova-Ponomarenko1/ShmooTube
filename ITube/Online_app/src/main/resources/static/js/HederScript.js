document.addEventListener("DOMContentLoaded", function () {
    const toggleButton = document.getElementById("toggleNav");
    const sidebar = document.querySelector(".sidebar");
    const content = document.querySelector(".content");

    toggleButton.addEventListener("click", function () {
        sidebar.classList.toggle("open");
        content.classList.toggle("open");
    });
});

 //Search
document.addEventListener("DOMContentLoaded", function() {
    const form = document.querySelector('form');

    form.addEventListener('submit', (event) => {
        event.preventDefault();

        const searchInput = form.querySelector('input[type="text"]');
        const searchText = searchInput.value;
        const url = '/ITube/search';
        const data = new FormData();
        data.append('searchText', searchText);
        fetch(url, {
            method: 'POST',
            body: data
         })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                 }
                window.location.href = '/ITube/searchResults?searchText=' + searchText;
            })
            .catch(error => {
                console.error('There was a problem with the fetch operation:', error);
            });
    });
});

