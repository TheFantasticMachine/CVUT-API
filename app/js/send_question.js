// noinspection D

document.addEventListener("DOMContentLoaded", () => {
    const questionForm = document.getElementById("question-form");

    if (!questionForm) return;

    questionForm.addEventListener("submit", async (e) => {
        e.preventDefault(); // Prevent page reload

        // 1. Get input elements with fallbacks
        const assignmentEl = document.getElementById("assignment") || document.querySelector(".assignment");
        const categorySelect = document.getElementById("categoryID");
        const difficultyInput = document.getElementById("difficulty");
        // Safety check to ensure element exists
        if (!assignmentEl) {
            console.error("Assignment input element not found!");
            alert("Error: Question assignment input missing.");
            return;
        }

        // 2. Collect answer texts from all answer input fields
        let answersList = [];
        const answerRows = document.querySelectorAll('.answer-row');
        answerRows.forEach(row => {
            answersList.push({
                isCorrect: row.querySelector('input[name="correctAnswer"]:checked') ? true : false,
                answerText: row.querySelector('input[name="answerText"]').value.trim()
            });
        });


        // 3. Build JSON payload matching QuestionRequest.java
        const payload = {
            assignment: assignmentEl.value.trim(),
            categoryID: parseInt(categorySelect ? categorySelect.value : 1, 10),
            difficulty: parseInt(difficultyInput ? difficultyInput.value : 5, 10),
            answers: answersList
        };

        console.log("Sending payload to server:", payload);

        try {
            // 4. POST to Spring Boot endpoint
            const response = await fetch('/api/question/add', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(payload)
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`Server returned HTTP ${response.status}: ${errorText}`);
            }

            // 5. Handle response from Java backend
            const result = await response.json();
            console.log("Saved Question Response:", result);

            alert("Question created and saved successfully!");

        } catch (error) {
            console.error("[Question Send Error]:", error);
            alert("Failed to save question. Check browser console.");
        }
    });
});