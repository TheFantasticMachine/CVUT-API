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
        hideAlreadyAdded: true
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

        // Collect distinct category IDs from loaded questions
        const categoryIds = [...new Set(rawQuestions.map(q => q.categoryID).filter(Boolean))];

        categorySelectEl.innerHTML = `<option value="ALL">All Categories</option>`;
        categoryIds.forEach(id => {
            categorySelectEl.innerHTML += `<option value="${id}">Category ${id}</option>`;
        });

        // Reset filter to match the default option
        currentFilter.categoryId = "ALL";
    }

    function getFilteredQuestions() {
        console.log("Current Filter State:", currentFilter);
        console.log("Raw questions count:", rawQuestions.length);

        const usedIdsOnActiveVariant = window.TestManager
            ? window.TestManager.getActiveVariantQuestionIDs()
            : new Set();

        console.log(usedIdsOnActiveVariant);

        return rawQuestions.filter(q => {
            // Test Category
            if (currentFilter.categoryId && currentFilter.categoryId !== "ALL") {
                if (String(q.categoryID) !== String(currentFilter.categoryId)) {
                    console.log(`Q#${q.questionID} rejected by Category. Q has: ${q.categoryID}, Filter wants: ${currentFilter.categoryId}`);
                    return false;
                }
            }

            // Test Used in Active Variant
            if (currentFilter.hideAlreadyAdded && usedIdsOnActiveVariant.has(q.questionID)) {
                console.log(`Q#${q.questionID} rejected: already in active variant`);
                return false;
            }

            // Test Search
            if (currentFilter.search) {
                const assignmentMatch = q.assignment && q.assignment.toLowerCase().includes(currentFilter.search);
                if (!assignmentMatch) {
                    console.log(`Q#${q.questionID} rejected by Search`);
                    return false;
                }
            }

            return true; // Keep question
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
            const cardEl = createQuestionElement(q, usedVariants);
            containerEl.appendChild(cardEl);
        });
    }

    function createQuestionElement(q, usedVariants) {
        const card = document.createElement("div");
        card.className = "question-pool-card";
        card.dataset.questionId = q.questionID;

        // Check if question is used in variants A, B, C
        const variantPills = window.TestManager.getAllVariantLetters().map(v =>
            `<span class="v-dot ${usedVariants.includes(v) ? 'active' : ''}">${v}</span>`
        ).join("");

        // Format answers (correct answer highlighted first)
        const sortedAnswers = [...(q.answers || [])].sort((a, b) => (b.correct ? 1 : 0) - (a.correct ? 1 : 0));
        const answersHtml = sortedAnswers.map((ans, idx) => `
            <div class="answer-row ${ans.correct ? 'correct' : ''}">
                <span class="option-label">${String.fromCharCode(65 + idx)})</span>
                <span class="option-text">${ans.answerText || ans}</span>
                ${ans.correct ? '<i class="fa-solid fa-check check-icon"></i>' : ''}
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
