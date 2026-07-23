const port = 8080; //prompt("insert current port");
const questionContainerElement = document.getElementById("question_display");


let savedTests = [];
localStorage.setItem("test_variant", 1);

class Test {
    test_variant = 1;
    questions = [];

    constructor() {
        this.test_variant = localStorage.getItem("test_variant");
        localStorage.setItem("test_variant", this.test_variant+1);
    }

    addQuestion(question) {
        if (!this.questions.includes(question)) {
            this.questions.push(question);
        }
    }
}

let defaultTestVariant = new Test();
savedTests.push(defaultTestVariant);

let currentTestVariant = defaultTestVariant;


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

            // add to local memory
            localStorage.setItem(`question_${id}`, JSON.stringify(question));

            // create question element
            let parent = document.createElement("div");
            parent.classList.add("question");
            parent.id = `question_${id}`;
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
        document.querySelectorAll(".question").forEach((element) => {
            element.addEventListener("click", (e) => {
                currentTestVariant.addQuestion(element);
                console.log(currentTestVariant.questions);
            });
        })
    }
);