const QuestionPoolModal = (function () {
    let rawQuestions = [];
    let subjectID = null;
    let currentFilter = {
        search: "",
        categoryID: "all",
        hideAlreadyAdded: true
    };

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

        if (searchInputEl) {
            searchInputEl.addEventListener("input", (e) => {
                currentFilter.search = e.target.value.trim().toLowerCase();
                render();
            });
        }

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

            if (!response.ok) {
                throw new Error("Nepodařilo se načíst otázky");
            }
            rawQuestions = await response.json();
            populateCategoryFilter();
        }
        catch (error) {
            console.error(error.message);
            containerEl.innerHTML = `<p class="error-msg">Nepodařilo se načíst otázky: ${error.message}</p>`;
        }
    }

    function populateCategoryFilter() {
        if (!categorySelectEl) return;

        const categoryIds = [...new Set(rawQuestions.map(q => q.categoryID).filter(Boolean))];

        categorySelectEl.innerHTML = `<option value="ALL">Všechny kategorie</option>`;
        categoryIds.forEach(id => {
            categorySelectEl.innerHTML += `<option value="${id}">Kategorie ${id}</option>`;
        });

        currentFilter.categoryId = "ALL";
    }

    function getFilteredQuestions() {
        const usedIdsOnActiveVariant = window.TestManager
            ? window.TestManager.getActiveVariantQuestionIDs()
            : new Set();

        return rawQuestions.filter(q => {
            if (currentFilter.categoryId && currentFilter.categoryId !== "ALL") {
                if (String(q.categoryID) !== String(currentFilter.categoryId)) {
                    return false;
                }
            }

            if (currentFilter.hideAlreadyAdded && usedIdsOnActiveVariant.has(q.questionID)) {
                return false;
            }

            if (currentFilter.search) {
                const assignmentMatch = q.assignment && q.assignment.toLowerCase().includes(currentFilter.search);
                if (!assignmentMatch) {
                    return false;
                }
            }

            return true;
        });
    }

    function render() {
        const questionsToShow = getFilteredQuestions();
        containerEl.innerHTML = "";

        if (questionsToShow.length === 0) {
            containerEl.innerHTML = `<div class="empty-pool-state">Nebyly nalezeny žádné otázky odpovídající zadaným kritériím.</div>`;
            return;
        }

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

        const variantPills = window.TestManager.getAllVariantLetters().map(v =>
            `<span class="v-dot ${usedVariants.includes(v) ? 'active' : ''}">${v}</span>`
        ).join("");

        const sortedAnswers = [...(q.answers || [])].sort((a, b) => (b.correct ? 1 : 0) - (a.correct ? 1 : 0));
        const answersHtml = sortedAnswers.map((ans, idx) => `
            <div class="answer-row ${ans.correct ? 'correct' : ''}">
                <span class="option-label">${String.fromCharCode(65 + idx)})</span>
                <span class="option-text">${ans.answerText || ans}</span>
                ${ans.correct ? '<i class="fa-solid fa-check check-icon"></i>' : ''}
            </div>
        `).join("");

        card.innerHTML = `
            <button type="button" class="btn-add-circle" title="Přidat do aktuální varianty">
                <i class="fa-solid fa-plus"></i>
            </button>
            <div class="card-body">
                <p class="question-assignment">${q.assignment}</p>
                <div class="card-footer-meta">
                    <span class="meta-tag category-tag">${q.categoryName || 'Kategorie ' + q.categoryID}</span>
                    <span class="meta-tag difficulty-tag">Obtížnost: ${q.difficulty || 1}/10</span>
                    <div class="variant-indicators" title="Použito ve variantách">${variantPills}</div>
                    <button type="button" class="btn-toggle-answers">
                        <span>Možnosti</span>
                        <i class="fa-solid fa-chevron-down"></i>
                    </button>
                </div>
                <div class="preview-answers-drawer">${answersHtml}</div>
            </div>
        `;

        const toggleBtn = card.querySelector(".btn-toggle-answers");
        const drawer = card.querySelector(".preview-answers-drawer");
        toggleBtn.addEventListener("click", () => {
            toggleBtn.classList.toggle("open");
            drawer.classList.toggle("open");
        });

        const addBtn = card.querySelector(".btn-add-circle");

        const answerRows = Array.from(card.querySelectorAll('.answer-row'));
        const marked = answerRows.filter((row)=> !row.classList.contains("correct"));

        marked.forEach(row => {
            row.classList.toggle("picked");
        });

        answerRows.forEach(row => {
            if (!row.classList.contains("correct")) {
                row.addEventListener("click", () => {
                    row.classList.toggle("picked");
                    if ( answerRows.filter((row) => row.classList.contains("picked")).length !== 3 ) {
                        addBtn.disable = true;
                    }
                    else {
                        addBtn.disable = false;
                    }
                });
            }
        });

        addBtn.addEventListener("click", () => {
            if (window.TestManager) {
                const questionCopy = structuredClone(q);
                questionCopy.instanceId = crypto.randomUUID();

                let answers = [];
                questionCopy.answers.forEach((anw) => {
                    for (const index in marked) {
                        if (anw.answerText === marked[index].querySelector('.option-text').innerText && answers.length < 3) {
                            answers.push(anw);
                        }
                    }
                });

                answers.push(questionCopy.answers.filter((anw) => anw.correct)[0]);
                answers = shuffleArray(answers);

                questionCopy.answers = answers;
                window.TestManager.addQuestionToActiveVariant(questionCopy);
                render();
            }
        });

        function shuffleArray(array) {
            if (!Array.isArray(array)) return [];
            const copy = [...array];
            for (let i = copy.length - 1; i > 0; i--) {
                const j = Math.floor(Math.random() * (i + 1));
                [copy[i], copy[j]] = [copy[j], copy[i]];
            }
            return copy;
        }

        return card;
    }

    return {
        init: init,
        open: function () {
            if (!dialogEl) return;
            render();
            dialogEl.showModal();
        },
        close: function () {
            if (dialogEl) dialogEl.close();
        },
        loadQuestions: loadSubjectQuestions
    };
})();

document.addEventListener("testSaveLoaded", (e) => {
    QuestionPoolModal.init();
    QuestionPoolModal.loadQuestions(e.detail.subjectId);
}, {once: true});

document.getElementById('btn-add').addEventListener('click', () => {
    QuestionPoolModal.open();
});