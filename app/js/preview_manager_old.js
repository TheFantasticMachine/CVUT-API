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
let id = 1;

const testsWrapperElement  = document.querySelector(".tests_wrapper");

class Test {
    questionPool;
    variant = 1;
    rootElement;
    isActive = false;
    id;
    constructor(variant) {
        this.questionPool = new Array();
        this.id = id;
        id++;
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
        destroyBtn.onclick = (e) => {
            e.stopPropagation(); // THIS PREVENTS BUBBLING
            this.deleteSelf();
        };

        let idElement = document.createElement("span");
        idElement.classList.add("test_id");
        idElement.innerText = this.id;

        this.rootElement.appendChild(variantElement);
        this.rootElement.appendChild(destroyBtn);
        this.rootElement.appendChild(idElement);
        testsWrapperElement.appendChild(this.rootElement);

        this.variant = variant;
        this.setOrder(variant);

        this.rootElement.onclick = () => {this.setActive()};
        createdTests.push(this);
    }

    setOrder(order) {
        this.rootElement.style.order = order;
    }

    setActive() {
        console.log(`setting active ... ${this.id}`);
        //console.log(activeTest);
        this.isActive = true;
        let active = createdTests[ createdTests.indexOf(activeTest) ];

        if (!active) {
            console.warn(`Cannot set active state: element at index ${createdTests.indexOf(activeTest)} is undefined.`);
            activeTest = this;
            return;
        }

        active.setOrder(active.variant);
        active.isActive = false;
        activeTest = this;

        this.setOrder(0);

        if (this.questionPool !== null) {
            this.displayQuestions();
        }

        this.rootElement.style.backgroundColor = "pink";
        console.log(activeTest);
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
        if (this.questionPool.indexOf(questions[questionID-1]) === -1){
            this.questionPool.push(questions[questionID-1]);
        }
        else {
            this.questionPool.splice(this.questionPool.indexOf(questions[questionID-1]), 1);
            this.questionPool.filter((el) => {
                el != null || el != undefined
            });
        }
        console.log(this.questionPool);
        this.displayQuestions();
    }

    deleteSelf() {
        console.log(`delete ${this.id}`)
        if (this === activeTest) {
            console.log("removing active");
            console.log("old");
            console.log(activeTest);

            if (createdTests.indexOf(this) - 1 > -1) {
                console.log(createdTests[ createdTests.indexOf(this) - 1 ]);

                //activeTest = createdTests[ createdTests.indexOf(this) - 1 ];
                createdTests[ createdTests.indexOf(this) - 1 ].setActive();
                console.log(1);
            }
            else if (createdTests.indexOf(this) + 1 < createdTests.length) {
                console.log(2);
                console.log(createdTests[ createdTests.indexOf(this)  + 1 ]);

                createdTests[ createdTests.indexOf(this) + 1 ].setActive();
            }
            else {
                return;
            }

            console.log("new");
            console.log(activeTest);
        }

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