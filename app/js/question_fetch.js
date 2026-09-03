// const questionContainerElement = document.getElementById("question_display")
//     || document.getElementById("preview-questions-list");
// let questions = [];
//
// // fetch question
// async function getQuestion() {
//     try {
//
//         let valid = true;
//         let id = 1;
//         while (valid) {
//             // get json
//             const response = await fetch(`/api/question/get?id=${id}`);
//             // catch wrong
//             if (response.status === 404 || response.status === 204 || !response.ok) {
//                 console.log(`[JS] Reached end of questions at ID: ${id}`);
//                 break;
//             }
//
//             // 1. Get raw text response
//             let text = await response.text();
//
//             // 2. Stop the fetch loop cleanly if the server returns an empty body
//             if (!text || text.trim() === "") {
//                 console.log(`[JS] Reached end of question database at ID ${id}.`);
//                 break;
//             }
//
//             // if correct add into local and as element
//             let question = JSON.parse(text);
//
//             questions.push(question);
//
//             // add to local memory
//             localStorage.setItem(`question_${id}`, JSON.stringify(question));
//
//             if (questionContainerElement) {
//                 // create question element
//                 let parent = document.createElement("div");
//                 parent.classList.add("question");
//                 // set data attributes
//                 parent.dataset.questionId = question.questionID;
//                 parent.dataset.categoryId = question.categoryID;
//                 // add heading
//                 let heading = document.createElement("span");
//                 heading.classList.add("question_heading");
//                 heading.innerText = `(${question.questionID}) ${question.assignment}`;
//                 // add to container
//                 parent.appendChild(heading);
//                 questionContainerElement.appendChild(parent);
//             }
//             // increase id
//             id++;
//         }
//     } catch (error) {
//         console.error('Error:', error);
//     }
// }
// getQuestion()
//     .then(() => {
//         window.dispatchEvent(new CustomEvent('questions loaded', {
//             detail: questions
//         }));
//     })

// variables
let Subject = {};
let loadedQuestions = [];
let loadedCategories = [];

// classes
class Question {
    constructor(id, assignment, category, difficulty, answer_correct, answers_wrong) {
        this.id = id;
        this.assignment = assignment;
        this.category = category;
        this.difficulty = difficulty;
        this.answer_correct = answer_correct;
        this.answers_wrong = answers_wrong;
    }

    getElement() {
        const card = document.createElement("div");
        card.className = "question-pool-card";
        card.dataset.questionId = this.id;

        const variantsHtml = "not done";

        // // Check usage across variants A, B, C
        // const variantsHtml = ["A", "B", "C"].map(letter => {
        //     const isUsed = activeVariantsUsingQuestion.includes(letter);
        //     return `<span class="v-dot ${isUsed ? 'active' : ''}">${letter}</span>`;
        // }).join("");

        // Put correct answer first, then incorrect options
        const answersList = [];
        answersList.push({isCorrect: true, text: this.answer_correct});
        for (const wrong in this.answers_wrong) {
            answersList.push({isCorrect: false, text: wrong});
        }

        const answersHtml = answersList.map((item, i) => {
            const letter = String.fromCharCode(65 + i);
            return `
            <div class="answer-row ${item.isCorrect ? 'correct' : ''}">
                <span class="option-label">${letter})</span>
                <span class="option-text">${item.text}</span>
                ${item.isCorrect ? '<i class="fa-solid fa-check check-icon"></i>' : ''}
            </div>
        `;
        }).join("");

        card.innerHTML = `
        <button type="button" class="btn-add-circle" title="Add to variant">
            <i class="fa-solid fa-plus"></i>
        </button>
        <div class="card-body">
            <p class="question-assignment">${this.assignment}</p>
            <div class="card-footer-meta">
                <span class="meta-tag category-tag">${this.category}</span>
                <span class="meta-tag difficulty-tag">Diff: ${this.difficulty || 1}/10 </span>
                <div class="variant-indicators" title="Used in variants">${variantsHtml}</div>
                <button type="button" class="btn-toggle-answers">
                    <span>Options</span>
                    <i class="fa-solid fa-chevron-down"></i>
                </button>
            </div>
            <div class="preview-answers-drawer">${answersHtml}</div>
        </div>
    `;

        // Dropdown toggle logic
        const toggleBtn = card.querySelector(".btn-toggle-answers");
        const drawer = card.querySelector(".preview-answers-drawer");
        toggleBtn.addEventListener("click", () => {
            toggleBtn.classList.toggle("open");
            drawer.classList.toggle("open");
        });

        return card;
    }
}

class Category {
    constructor(id, name, questions) {
        this.id = id;
        this.name = name;
        this.questions = questions;
    }
}

function getTestSubject(sel) {
    Subject.name = sel.options[sel.selectedIndex].text;
    document.getElementById("add-question-error").style.display = "none";
    document.querySelector(".subject").innerText = Subject.name;
    // for now thats good but override prevention needs to be added and saving too
}

async function getQuestions() {
    try {
        // 1) get the subject
        if (Subject.name === undefined) { Subject.name = "Mathematics"; } // in case subject wasnt selected
        let response = await fetch(`/api/subject/by-subject-name?name=${Subject.name}`);

        if (!response.ok) {
            console.log('fuck');
            throw new Error(`Response status: ${response.status}`);
        }

        let result = await response.json();
        Subject.id = result.subjectID;

        console.log(Subject);
        console.log(result);

        // 2) we have subject id now lets get all the categories
        response = await fetch(`/api/category/by-subject-id?id=${Subject.id}`);

        if (!response.ok) {
            console.log('fuck');
            throw new Error(`Response status: ${response.status}`);
        }

        result = await response.json();
        // result is now a array of category objects
        for (const category in result) {
            let questions = [];
            loadedCategories.push(new Category(category.id, category.name, []));
        }
    }
    catch (error) {
        console.error(error.message)
    }

}

async function fetchAllApprovedQuestions() {
    let id = 1;
    while (true) {
        try {
            const response = await fetch(`/api/question/get?id=${id}`);

            if (response.status === 404 || response.status === 204 || !response.ok) {
                console.log(`[JS] Reached end of question pool at ID ${id}.`);
                break;
            }

            const rawText = await response.text();
            if (!rawText || rawText.trim() === "" || rawText === "null") {
                break;
            }

            const question = JSON.parse(rawText);
            loadedQuestions.push(question);
            id++;
        } catch (err) {
            console.warn(`[JS] Question fetch loop finished at ID ${id}`);
            break;
        }
    }

    console.log(`[JS] Loaded ${loadedQuestions.length} questions.`);

    // Dispatch event with loaded questions
    window.dispatchEvent(new CustomEvent('questions-loaded', {
        detail: loadedQuestions
    }));
}

document.addEventListener("DOMContentLoaded", () => {
    //fetchAllApprovedQuestions();
    getQuestions();
});