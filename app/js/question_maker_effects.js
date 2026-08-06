document.addEventListener("DOMContentLoaded", () => {
    const answersList = document.getElementById("answers-list");
    const addAnswerBtn = document.getElementById("add-answer-btn");

    if (!addAnswerBtn || !answersList) return;

    // Convert index to ASCII Letter (0 -> 'A', 1 -> 'B', 2 -> 'C', etc.)
    function getLetter(index) {
        return String.fromCharCode(65 + index);
    }

    // Recalculate letters (A, B, C...) and radio values (0, 1, 2...) sequentially
    function reindexAnswers() {
        const rows = answersList.querySelectorAll(".answer-row");
        rows.forEach((row, index) => {
            // 1. Update letter tag
            const letterSpan = row.querySelector(".answer-letter");
            if (letterSpan) {
                letterSpan.innerText = getLetter(index);
            }

            // 2. Update radio input value to match 0-based array index for Java
            const radio = row.querySelector('input[type="radio"]');
            if (radio) {
                radio.value = index;
            }
        });
    }

    // Add new answer row
    addAnswerBtn.addEventListener("click", () => {
        const currentCount = answersList.querySelectorAll(".answer-row").length;

        if (currentCount >= 10) {
            alert("Maximum 10 answer choices allowed.");
            return;
        }

        const nextLetter = getLetter(currentCount);

        const row = document.createElement("div");
        row.className = "answer-row";
        row.innerHTML = `
            <span class="answer-letter">${nextLetter}</span>
            <input type="text" name="answers[]" placeholder="Enter answer text..." required />
            
            <label class="correct-radio-label" title="Mark as correct answer">
                <input type="radio" name="correctAnswerIndex" value="${currentCount}" required />
                <span class="radio-custom">Correct</span>
            </label>

            <button type="button" class="btn-icon-remove" title="Remove option">&times;</button>
        `;

        answersList.appendChild(row);
        reindexAnswers();
    });

    // Event Delegation: Listens on container for removal clicks (works for present & future rows)
    answersList.addEventListener("click", (e) => {
        if (e.target && e.target.classList.contains("btn-icon-remove")) {
            const rows = answersList.querySelectorAll(".answer-row");

            if (rows.length <= 2) {
                alert("A question must have at least 2 answer choices.");
                return;
            }

            const rowToRemove = e.target.closest(".answer-row");
            if (rowToRemove) {
                rowToRemove.remove();
                reindexAnswers(); // Cleanly re-index remaining A, B, C letters & radio indices
            }
        }
    });
});