// variables
let allVariants = [];

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
        this.questions = new Array();

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



        this.render();
    }

    render() {
        const parent = document.getElementById("a4-preview-sheet");
        // clear
        parent.innerText = "";

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
        parent.appendChild(questionListPreview);

        if (this.questions.length === 0) {
            questionListPreview.innerHTML =
                `
                <div class="empty-state-notice">
                       <i class="fa-regular fa-file-lines"></i>
                       <p>No questions added to this variant yet. Use the question pool or auto-generator to add questions.</p>
                   </div>
                `;
        }
        else {

            console.info(this.questions);
            this.questions.forEach(question => {
                console.info(question);
                const q = document.createElement("div");
                q.classList.add("question");
                let html = `
                <div id="flex-row-one">
                   <textarea type="text" name="assignment" class="assignment"> ${question.assignment} </textarea>
                   <button class="remove" title="remove">
                   </button>
               </div>
               <div id="flex-row-two">
                `;
                question.answers.forEach (answer => {
                    console.info(answer);
                    html += `
                    <div class="answer ${answer.correct ? "correct" : ""}">
                       <span class="letter">${String.fromCharCode(65 + question.answers.indexOf(answer))}</span>
                       <input name="answer" type="text" value="${answer.answerText}">
                       <i class="fa-solid fa-grip-lines"></i>
                   </div>`;
                });

                html += `</div>
                    <button class="change">Change</button>`
                q.innerHTML = html;
                q.dataset.instanceId = question.instanceId;
                questionListPreview.appendChild(q);
            });
        }
        questionListPreview.querySelectorAll(".question").forEach(element => {
            element.querySelector('button.change').addEventListener("click", (e) =>  this.updateQuestion(element));
            element.querySelector('button.remove').addEventListener("click", (e) =>  this.removeQuestion(element));
        });

    }

// question handlers

    addQuestion(question) {
        if ( this.questions.find( ({instanceId}) => instanceId === question.instanceId) === undefined ) {
            this.questions.push(question);
            console.info(`new question to variant ${this.letter}`);
            console.info(question);
        }
        this.render();
    }

    removeQuestion(question) {
        let effected = this.questions.find(obj => obj.instanceId === parseInt( question.dataset.instanceId ));
        this.questions = this.questions.filter((obj) => obj !== effected);
        console.warn(`deleted question in variant ${this.letter}`);
        this.render();
    }

    moveQuestion() {}

    updateQuestion(question) {
        let effected = this.questions.find(obj => obj.instanceId === parseInt( question.dataset.instanceId )) || null;
        console.warn (`updated question in variant ${this.letter}`);
        if (effected === null) {
            return null;
        }
        effected.assignment = question.querySelector('textarea[name="assignment"]').value.trim();
        let index = 0;
        question.querySelectorAll('.answer').forEach(element => {
            effected.answers[index].correct = element.classList.contains("correct") ? true : false;
            effected.answers[index].answerText = element.querySelector('input[name = "answer"]').value.trim();
            index++;
        });

        for (let i = 0; i < this.questions.length; i++) {
            if (this.questions[i].questionID === effected.questionID) {
                this.questions[i] = effected;
            }
        }

        console.warn(`updated question in variant ${this.letter} ...` + effected);
        this.render();
    }
}

// define subject, category and question

class Subject {

    constructor() {
    this.id = sessionStorage.getItem("subject-id");
    this.name = sessionStorage.getItem("subject")
}
}

class Category {}

// set event triggers

// create variant (with btn)
document.getElementById("add-variant-btn").addEventListener("click", (e) => {
    try {
        const variant = new TestVariant();
        allVariants.push(variant);
    }
    catch (error) {
        console.error(error.message);
    }
});

// create variant (on load) if new test
// ! for now created by default doesnt check for new test
window.addEventListener("load", async (e) => {
    try {
        const testID = sessionStorage.getItem("current_test_id");
        const response = await fetch(`/api/test_save/test-by-id?testId=${parseInt(testID)}`);
        if (!response.ok) {
            throw new Error("test not found");
        }

        const test = await response.json();
        console.info(test);

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

// share data

window.TestManager = {
    testData: {
        title: sessionStorage.getItem("active_test_title") || "New Exam",
        subjectId: sessionStorage.getItem("active_test_subject_id") || 1
    },

    getActiveVariant: function () {
        let active = null;
        allVariants.forEach( variant => {
            console.log(variant);
            if (variant.isActive) {
                active = variant;
            }
        });

        return active;
    },

    getActiveVariantQuestionIDs: function () {
        const active = this.getActiveVariant();
        console.log(active.questions);
        return new Set( active.questions.map((question) => question.questionID ));
    },

    getQuestionVariantUsageMap: function () {
        let map = {};

        // get all questions used in the whole test
        allVariants.forEach( variant => {
            variant.questions.forEach( question => {
                if (!map[question.questionID]) { map[question.questionID] = []; }
                map[question.questionID].push(variant.letter);
            });
        });

        return map;
    },

    addQuestionToActiveVariant: function (question) {
        this.getActiveVariant().addQuestion(question);
    },

    getAllVariantLetters: function () {
        return allVariants.map((variant) => variant.letter);
    }
}
