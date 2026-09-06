// variables
let allVariants = [];
let allCategories = [];
let allQuestions = [];

let testSubject;

// define a test variant
class TestVariant {

    constructor() {
        // stop if max variant num
        if (allVariants.length > 26 ) { throw new Error("too many variants"); }

        // set the letter
        this.letter = String.fromCharCode(65 + allVariants.length);

        // create the tab in the variant selector
        let tab = document.createElement("button");
        tab.classList.add("variant-tab");
        tab.innerText = `variant ${this.letter}`;
        tab.style.order = allVariants.length.toString();
        tab.addEventListener("click", () => {this.setActive()});
        this.tabElement = tab;

        document.getElementById("variant-tab-container").appendChild(this.tabElement);
        this.isActive = false;
        this.questions = [];

        return this;
    }

    // methods
    setActive() {
        // find prev active
        allVariants.forEach( variant => {
            console.log(variant);
            if (variant.isActive) {
                variant.tabElement.classList.toggle("active");
                variant.isActive = false;
            }
        });

        this.tabElement.classList.toggle("active");
        this.isActive = true;

        document.getElementById("a4-preview-sheet").innerText = "";

        this.createPreview();
    }

    createPreview() {
        const parent = document.getElementById("a4-preview-sheet");
        // display the test
        let header = document.createElement("header");
        header.innerHTML = `
        <span class="header-test-variant">Variant: ${this.letter}</span>
        <hr>
        <div class="header-row-one">
            <span class="header-test-subject">Subject: ${testSubject.name}</span>
            <span class="header-test-date">Date: </span>
        </div>
        <span class="header-name">Name: </span>
        <hr>`;
        parent.appendChild(header);
        const divider = document.createElement("hr");
        divider.classList.add("preview-divider");
        parent.appendChild(divider);

        let questionListPreview = document.createElement("div");
        questionListPreview.id = "preview-questions-list";

        if (this.questions.length === 0) {
            questionListPreview.innerHTML =
                `
                <div class="empty-state-notice">
                       <i class="fa-regular fa-file-lines"></i>
                       <p>No questions added to this variant yet. Use the question pool or auto-generator to add questions.</p>
                   </div>
                `;
        }
        else {}

        parent.appendChild(questionListPreview);
    }

// question handlers

addQuestion() {}

removeQuestion() {}

moveQuestion() {}

updateQuestion() {}
}

// define subject, category and question

class Subject {

    constructor() {
    this.id = sessionStorage.getItem("subject-id");
    this.name = sessionStorage.getItem("subject")
}
}

class Category {}

class Question {}

// set event triggers

// create variant (with btn)

// create variant (on load) if new test
// ! for now created by default doesnt check for new test
window.addEventListener("load", (e) => {
    try {
        testSubject = new Subject();
        document.getElementById("display-subject-tag").innerText = testSubject.name;
        document.getElementById("display-test-name").innerText = sessionStorage.getItem("test-name");

        // only after that create the first variant
        const first = new TestVariant();
        allVariants.push(first);
        const second = new TestVariant();
        allVariants.push(second);
        first.setActive();

        console.log(allVariants);
        console.log(first);
    }
    catch (error) {
        console.error(error.message);
    }
});

document.getElementById("add-variant-btn").addEventListener("click", (e) => {
    try {
        const variant = new TestVariant();
        allVariants.push(variant);
    }
    catch (error) {
        console.error(error.message);
    }
});

// systems
// question adding system
// 1) popup - open close
const question_dialog = document.getElementById('question-popup-wrapper');

document.getElementById('btn-add').addEventListener('click', (e) => {
    question_dialog.showModal();

    const parent = document.getElementById("add-question-popup-content");

    // # edge case of no subject
    if (testSubject === null) {
        parent.innerHTML = `
        <div id="add-question-error" class="default-error">
            <i class="fa-solid fa-school-circle-exclamation"></i>
            <span>Please select the subject of the test before picking a question</span>
        </div>
        `;
        return null;
    }

    console.log("test");

})

question_dialog.addEventListener("click", (e) => {
    if (!document.querySelector('.excluded').contains(e.target)) {
        question_dialog.close();
    }
});
