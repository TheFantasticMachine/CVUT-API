const questionContainerElement = document.getElementById("question_display");
let questions = [];

// fetch question
async function getQuestion() {
    try {

        let valid = true;
        let id = 1;
        while (valid) {
            // get json
            const response = await fetch(`/api/question/get?id=${id}`);
            // catch wrong
            if (response.status === 404 || response.status === 204 || !response.ok) {
                console.log(`[JS] Reached end of questions at ID: ${id}`);
                break;
            }

            // 1. Get raw text response
            let text = await response.text();

            // 2. Stop the fetch loop cleanly if the server returns an empty body
            if (!text || text.trim() === "") {
                console.log(`[JS] Reached end of question database at ID ${id}.`);
                break;
            }

            // if correct add into local and as element
            let question = JSON.parse(text);

            questions.push(question);

            // add to local memory
            localStorage.setItem(`question_${id}`, JSON.stringify(question));

            // create question element
            let parent = document.createElement("div");
            parent.classList.add("question");
            // set data attributes
            parent.dataset.questionId = question.questionID;
            parent.dataset.categoryId = question.categoryID;
            // add heading
            let heading = document.createElement("span");
            heading.classList.add("question_heading");
            heading.innerText = `(${question.questionID}) ${question.assignment}`;
            // add to container
            parent.appendChild(heading);
            questionContainerElement.appendChild(parent);

            // increase id
            id++;
        }
    } catch (error) {
        console.error('Error:', error);
    }
}
getQuestion()
    .then(() => {
        window.dispatchEvent(new CustomEvent('questions loaded', {
            detail: questions
        }));
    })