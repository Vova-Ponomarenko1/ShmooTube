//опис
document.addEventListener("DOMContentLoaded", function () {
    const fullDescription = document.getElementById("full-description");
    const moreButton = document.getElementById("more-button");

    fullDescription.style.display = "none";

    moreButton.addEventListener("click", function () {
        if (fullDescription.style.display === "none") {
            fullDescription.style.display = "block";
            moreButton.textContent = "Заховати";
        } else {
            fullDescription.style.display = "none";
            moreButton.textContent = "Повний опис";
        }
    });
});

//додавання коментаря

document.addEventListener("DOMContentLoaded", function () {
    const videoPlayer = document.getElementById("videoPlayer");
    const videoUrl = videoPlayer.getAttribute("data-video-url");

    const commentForm = document.getElementById("commentForm");
    const commentText = document.getElementById("commentText");

    commentForm.addEventListener("submit", function (event) {
        event.preventDefault();
        const comment = commentText.value;
        const videoId = extractVideoIdFromUrl(videoUrl);

        if (!videoId) {
            console.error("Помилка отримання ідентифікатора відео");
            return;
        }

        fetch("/ITube/new_comment?comment=" + encodeURIComponent(comment) + "&videoId=" + videoId, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
        })
            .then((response) => {
                if (response.ok) {
                    console.log("Комментарій відправлений успешно");
                    commentText.value = "";
                } else {
                    console.error("Помилка при відправкі комментарія");
                }
            })
            .catch((error) => {
                console.error("Помилка при відправкі комментарія", error);
            });
    });

});

function extractVideoIdFromUrl(url) {
    const regex = /\/ITube\/video\/(\d+)\//;
    const match = url.match(regex);
    if (match && match[1]) {
        return match[1];
    }
    return null;
}


//всі коментарі
const videoPlayer = document.getElementById("videoPlayer");
const videoUrl = videoPlayer.getAttribute("data-video-url");
const videoId = extractVideoIdFromUrl(videoUrl);
const commentsList = document.getElementById("commentsList");
let nextPage = 2;

function loadMoreComments() {
    fetch(`/ITube/video/${videoId}/comments?page=${nextPage}`)
        .then(response => response.json())
        .then(data => {
            data.forEach(comment => {
                const commentContainer = document.createElement("div");
                commentContainer.classList.add("comment");
                commentContainer.innerHTML = `
                    <img src="${comment.commentatorAvatar}" alt="Аватар пользователя">
                    <div class="comment-content">
                        <p class="comment-author">${comment.commentatorName}</p>
                        <p class="comment-text">${comment.commentText}</p>
                    </div>
                `;
                commentsList.appendChild(commentContainer);
            });

            nextPage++;
        })
        .catch(error => {
            console.error("Помилка при завантаженні додаткових коментарів:", error);
        });
}

function likeVideo() {
    const videoLike = {
        videoId: videoId,
    };

    fetch('/ITube/likeVideo', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(videoLike)
    })
        .then(response => {
            if (response.ok) {
                console.log('Лайк отправлен на сервер');
            } else {
                console.error('Ошибка при отправке лайка на сервер');
            }
        })
        .catch(error => {
            console.error('Ошибка:', error);
        });
}

function dislikeVideo() {
    const videoLike = {
        videoId: videoId,
    };

    fetch('/ITube/dislikeVideo', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(videoLike)
    })
        .then(response => {
            if (response.ok) {
                console.log('Лайк отправлен на сервер');
            } else {
                console.error('Ошибка при отправке лайка на сервер');
            }
        })
        .catch(error => {
            console.error('Ошибка:', error);
        });
}

function subscribers(userId) {

    console.log(userId);
    fetch(`/ITube/subscriber/${userId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (response.ok) {
                console.log('Подписка оформлена успешно');
            } else {
                console.error('Ошибка при оформлении подписки');
            }
        })
        .catch(error => {
            console.error('Ошибка:', error);
        });
}


window.addEventListener("scroll", function () {
    if (window.innerHeight + window.scrollY >= document.documentElement.offsetHeight - 100) {
        loadMoreComments();
    }
});

document.getElementById("subscribersButton").addEventListener("click", function() {
    const userId = this.dataset.userId;
    console.log(userId);
    fetch(`/ITube/subscriber/${userId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (response.ok) {
                console.log('Подписка оформлена успешно');
            } else {
                console.error('Ошибка при оформлении подписки');
            }
        })
        .catch(error => {
            console.error('Ошибка:', error);
        });
});

