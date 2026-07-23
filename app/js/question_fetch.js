const port = 8080; //prompt("insert current port");
const questionContainerElement = document.getElementById("question_display");
let questions = [];

// fetch question
async function getQuestion() {
    try {
        let valid = true;
        let id = 1;
        while (valid) {
            // change url
            let url = `http://localhost:${port}/question?id=${id}`;
            // get json
            let response = await fetch(url);
            // catch wrong
            if (!response.ok) {
                console.log(`[JS] Server returned status ${response.status}. Stopping loop.`);
                break;
            }
            // if correct add into local and as element
            let question = await response.json();

            questions.push(question);

            // add to local memory
            localStorage.setItem(`question_${id}`, JSON.stringify(question));

            // create question element
            let parent = document.createElement("div");
            parent.classList.add("question");
            parent.dataset.questionID = id;
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