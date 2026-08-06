// get all the input
let allQuestionRows = document.querySelectorAll(".answer-row");
let assignmentIn = document.getElementById("#assignment");
let difficultyIn = document.getElementById("#difficulty");
let categoryIdIn = document.getElementById("#categoryID");

let sendQuestion = new Promise( async (resolve, reject) => {
    const url = "/api/question/new";

    // prepare payload
    let questionsTemp = [];
    let correctQuestionTemp;

    allQuestionRows.forEach((row) => {
        // check if question is correct
        let radioElement = row.querySelector("label").querySelector("input[type='radio']");
        let questionTextElement = row.querySelector(".question_text");

        if (radioElement.checked) {
            correctQuestionTemp = questionTextElement.value;
        }
        else {
            questionsTemp.push(questionTextElement.value);
        }
    });

    const payload = {
        questions : questionsTemp,
        correct : correctQuestionTemp,
        assignment : assignmentIn.value,
        difficulty : difficultyIn.value,
        category : categoryIdIn.value
    };

    try {
        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            throw new Error(`Server returned HTTP ${response.status}`);
        }
        else {
            resolve();
        }
    }
    catch (error) {
        console.error("[PDF generation failed]:", error);
    }
});

document.getElementById("question-form").addEventListener("submit", async (e) => {
    // prevent reload
    e.preventDefault();

    // send question
    await sendQuestion
        .then(() => {
            // at the end refresh
            window.location.reload();
        });
});