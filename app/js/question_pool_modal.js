/*
<div id="add-question-error" class="default-error">
            <i class="fa-solid fa-school-circle-exclamation"></i>
            <span>Please select the subject of the test before picking a question</span>
        </div>
 */

const QuestionPoolModal = (function () {
    // Private vars
    let rawQuestions = [];
    let subjectID = null;
    let currentFilter = {
        search: "",
        categoryID: "all",
        toggleAdded: true
    };

    // DOM Elements
    let dialogEl = null;
    let containerEl = null;
    let searchInputEl = null;
    let categorySelectEl = null;

    function init() {
        dialogEl = document.getElementById("question-popup-wrapper");
        containerEl = document.getElementById("add-question-popup-content");
        searchInputEl = document.getElementById("pool-search-input");
        categorySelectEl = document.getElementById("pool-category-filter");

        if (!dialogEl || !containerEl) return;

        // Instant search on typing
        if (searchInputEl) {
            searchInputEl.addEventListener("input", (e) => {
                currentFilter.search = e.target.value.trim().toLowerCase();
                render();
            });
        }

        // Category dropdown/tabs filter
        if (categorySelectEl) {
            categorySelectEl.addEventListener("change", (e) => {
                currentFilter.categoryId = e.target.value;
                render();
            });
        }

        if (dialogEl) {
            dialogEl.addEventListener("click", (e) => {
                if (!document.querySelector('.excluded').contains(e.target)) {
                    dialogEl.close();
                }
            });
        }
    }

    async function loadSubjectQuestions(activeSubjectId) {
        subjectID = activeSubjectId;
        try {
            const response = await fetch(
                `/api/question/by-subject-id?subjectId=${subjectID}`
            );

            console.log("subject id" +  subjectID);

            if (!response.ok) {
                throw new Error("failed to load questions");
            }
            rawQuestions = await response.json();
            console.log(rawQuestions);
            populateCategoryFilter();
        }
        catch (error) {
            console.error(error.message);
            containerEl.innerHTML = `<p class="error-msg">Could not load questions: ${err.message}</p>`;
        }
    }

    function populateCategoryFilter() {
        if (!categorySelectEl) return;

        // Extract unique categories from loaded questions
        const categories = new Map();
        rawQuestions.forEach(q => {
            if (q.categoryID && q.categoryName) {
                categories.set(q.categoryID, q.categoryName);
            }
        });

        categorySelectEl.innerHTML = `<option value="all">All Categories</option>`;
        categories.forEach((name, id) => {
            categorySelectEl.innerHTML += `<option value="${id}">${name}</option>`;
        });
    }

    function getFilteredQuestions() {
        // get questions on current test variant
        const usedIdsOnActiveVariant = window.TestManager
            ? window.TestManager.getActiveVariantQuestionIDs()
            : new Set();

        console.log(usedIdsOnActiveVariant);

        return rawQuestions.filter(q => {
            // 1. Filter out if already in active variant (or kept for dimming)
            if (currentFilter.hideAlreadyAdded && usedIdsOnActiveVariant.has(q.questionID)) {
                return false;
            }

            // 2. Category filter
            if (currentFilter.categoryId !== "all" && q.categoryID !== parseInt(currentFilter.categoryId)) {
                return false;
            }

            // 3. Search filter (matches assignment or answer texts)
            if (currentFilter.search !== "") {
                const matchesAssignment = q.assignment.toLowerCase().includes(currentFilter.search);
                const matchesAnyAnswer = q.answers && q.answers.some(a =>
                    (a.answerText || a).toLowerCase().includes(currentFilter.search)
                );
                if (!matchesAssignment && !matchesAnyAnswer) return false;
            }

            return true;
        });
    }

    function render() {
        const questionsToShow = getFilteredQuestions();
        containerEl.innerHTML = "";

        console.log(getFilteredQuestions());

        if (questionsToShow.length === 0) {
            containerEl.innerHTML = `<div class="empty-pool-state">No questions found matching your criteria.</div>`;
            return;
        }

        // Fetch variants using each question across test (for the A, B, C dots)
        const variantUsageMap = window.TestManager
            ? window.TestManager.getQuestionVariantUsageMap()
            : {};

        questionsToShow.forEach(q => {
            const usedVariants = variantUsageMap[q.questionID] || [];
            const cardEl = createQuestionCardElement(q, usedVariants);
            containerEl.appendChild(cardEl);
        });
    }

    function createQuestionElement(question, usedVariants) {
        const card = document.createElement("div");
        card.className = "question-pool-card";
        card.dataset.questionId = q.questionID;

        // Check if question is used in variants A, B, C
        const variantPills = ["A", "B", "C"].map(v =>
            `<span class="v-dot ${usedVariants.includes(v) ? 'active' : ''}">${v}</span>`
        ).join("");

        // Format answers (correct answer highlighted first)
        const sortedAnswers = [...(q.answers || [])].sort((a, b) => (b.isCorrect ? 1 : 0) - (a.isCorrect ? 1 : 0));
        const answersHtml = sortedAnswers.map((ans, idx) => `
            <div class="answer-row ${ans.isCorrect ? 'correct' : ''}">
                <span class="option-label">${String.fromCharCode(65 + idx)})</span>
                <span class="option-text">${ans.answerText || ans}</span>
                ${ans.isCorrect ? '<i class="fa-solid fa-check check-icon"></i>' : ''}
            </div>
        `).join("");

        card.innerHTML = `
            <button type="button" class="btn-add-circle" title="Add to current variant">
                <i class="fa-solid fa-plus"></i>
            </button>
            <div class="card-body">
                <p class="question-assignment">${q.assignment}</p>
                <div class="card-footer-meta">
                    <span class="meta-tag category-tag">${q.categoryName || 'Category ' + q.categoryID}</span>
                    <span class="meta-tag difficulty-tag">Diff: ${q.difficulty || 1}/5</span>
                    <div class="variant-indicators" title="Used in variants">${variantPills}</div>
                    <button type="button" class="btn-toggle-answers">
                        <span>Options</span>
                        <i class="fa-solid fa-chevron-down"></i>
                    </button>
                </div>
                <div class="preview-answers-drawer">${answersHtml}</div>
            </div>
        `;

        // 1. Accordion preview toggle
        const toggleBtn = card.querySelector(".btn-toggle-answers");
        const drawer = card.querySelector(".preview-answers-drawer");
        toggleBtn.addEventListener("click", () => {
            toggleBtn.classList.toggle("open");
            drawer.classList.toggle("open");
        });

        // 2. Add question click handler
        const addBtn = card.querySelector(".btn-add-circle");
        addBtn.addEventListener("click", () => {
            if (window.TestManager) {
                window.TestManager.addQuestionToActive(q);
                // Re-render modal to immediately hide/dim this question
                render();
            }
        });

        return card;
    }

    return {
        init: init,
        open: function () {
            if (!dialogEl) return;
            // Refresh list when opened so newly added questions disappear
            render();
            dialogEl.showModal();
        },
        close: function () {
            if (dialogEl) dialogEl.close();
        },
        loadQuestions: loadSubjectQuestions
    };
})();


document.addEventListener("DOMContentLoaded", () => {
    QuestionPoolModal.init();

    // Load the questions when test_maker initializes
    const activeSubjectId = sessionStorage.getItem("active_test_subject_id") || 1;
    QuestionPoolModal.loadQuestions(activeSubjectId);
});

document.getElementById('btn-add').addEventListener('click', () => {
    QuestionPoolModal.open();
});
