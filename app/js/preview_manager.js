// let questions;

window.addEventListener('questions loaded', (event) => {
    // define questions
    questions = event.detail;

    document.querySelectorAll(".question").forEach((question) => {
        question.addEventListener("click", (e) => {
            console.log(question.dataset.questionId);
            activeTest.addQuestion(question.dataset.questionId);
        });
    });
});

console.log(questions);

// generate test (default)
let createdTests = [];
let activeTest;

const testsWrapperElement  = document.querySelector(".tests_wrapper");

class Test {
    questionPool;
    variant = 1;
    rootElement;
    isActive = false;
    constructor(variant) {
        this.questionPool = new Array();
        // create element
        this.rootElement  = document.createElement("div");
        this.rootElement.classList.add("test");

        // add variant span
        let variantElement = document.createElement("span");
        variantElement.classList.add("test_variant");
        variantElement.innerText = variant;

        // add remove button
        let destroyBtn = document.createElement("button");
        destroyBtn.innerText = "remove";
        destroyBtn.addEventListener("click", (e) => this.deleteSelf());

        this.rootElement.appendChild(variantElement);
        this.rootElement.appendChild(destroyBtn);
        testsWrapperElement.appendChild(this.rootElement);

        this.variant = variant;
        this.setOrder(variant);

        this.rootElement.addEventListener("click", (e) => {
            this.setActive();
        });
        createdTests.push(this);
    }

    setOrder(order) {
        this.rootElement.style.order = order;
    }

    setActive() {
        this.isActive = true;
        let active = createdTests[ createdTests.indexOf(activeTest) ];
        active.setOrder(active.variant);
        active.isActive = false;

        activeTest = this;

        this.setOrder(0);

        if (this.questionPool !== null) {
            this.displayQuestions();
        }
    }

    displayQuestions() {
        let previewWrapperElement = document.querySelector(".preview-wrapper");
        for (const child of previewWrapperElement.children)  {
            if (child.classList.contains("content")) {
                for (const subchild of child.children) {
                    if (subchild.classList.contains("display_questions")) {

                        subchild.innerHTML = '';

                        if (activeTest.questionPool !== undefined) {
                            activeTest.questionPool.forEach((question) => {
                                let questionElement = document.createElement("div");
                                let assignment = document.createElement("span");
                                assignment.innerText = question.assignment;
                                questionElement.appendChild(assignment);

                                let list = document.createElement("ul");
                                for (const answer of question.answers) {
                                    let li = document.createElement("li");
                                    li.innerText = answer;
                                    list.appendChild(li);
                                }
                                questionElement.appendChild(list);

                                subchild.appendChild(questionElement);
                            });
                        }
                    }
                }
            }
        }
    }

    addQuestion(questionID) {
        if (!this.questionPool.contains(questions[questionID-1])){
            this.questionPool.push(questions[questionID-1]);
        }
        else {
            this.questionPool.splice(this.questionPool.indexOf(questions[questionID-1]), 1);
            this.questionPool.filter((el) => {
                el != null || el != undefined
            });
        }
        this.displayQuestions();
    }

    deleteSelf() {
        createdTests.splice(createdTests.indexOf(this), 1);

        createdTests.filter((el) => {
            el != null || el != undefined
        });

        // add new variants
        let i = 1;
        createdTests.forEach((test) => {
            test.variant = i;
            for (const child of test.rootElement.children) {
                if (child.classList.contains("test_variant")) {
                    child.innerText = i;
                }
            }
            i++;
        });

        testsWrapperElement.removeChild(this.rootElement);
    }
}

let defaultTest = new Test(1);
activeTest = defaultTest;
defaultTest.setActive();
// test
defaultTest.rootElement.style.backgroundColor = "coral";

new Test(2);
new Test(3);

document.querySelector("#add_test").addEventListener("click", (e) => {
    new Test(createdTests[createdTests.length-1].variant + 1);
});