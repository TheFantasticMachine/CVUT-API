// const ALPHABET = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'];
//
// let all_tests = [];
// let selected_test = null;
//
// class Test {
//
//     constructor() {
//         this.create();
//     }
//
//     getVariant() { return ALPHABET[ all_tests.length ] ; }
//
//     create() {
//         if (all_tests.length <= 26){
//             this.variant = this.getVariant();
//             this.element = document.createElement('button');
//             this.element.classList.add("variant-tab");
//             this.element.innerText = `variant ${this.variant}`;
//             console.log(this.variant);
//             console.log(document.getElementById('variant-tab-container'));
//             document.getElementById('variant-tab-container').appendChild(this.element);
//             all_tests.push(this);
//         }
//     }
//
//     select() {
//         if (selected_test !== null) {
//             selected_test.element.classList.toggle('active');
//         }
//         selected_test = this;
//         this.element.classList.toggle('active');
//     }
// }
//
// let test1 = new Test();
// let test2 = new Test();
// let test3 = new Test();
//
// test1.select();
// test3.select();
//
//
// //class Question {}
//
// // manage variants
//
// // display questions
//
// // add questions
//
// // remove questions

// app/js/preview_manager.js
class TestVariant {
    constructor(id, label) {
        this.id = id;
        this.label = label;
        this.questions = [];
    }

    addQuestion(question) {
        this.questions.push(question);
    }

    removeQuestion(questionId) {
        this.questions = this.questions.filter(q => q.questionID !== questionId);
    }
}

const TestManager = {
    variants: [],
    activeVariantIndex: 0,
    allQuestions: [],

    init() {
        const tabContainer = document.getElementById("variant-tab-container");
        const addVariantBtn = document.getElementById("add-variant-btn");

        if (!tabContainer) return;

        // Initialize with Variant A
        this.addVariant("Variant A");
        this.addVariant("Variant B");

        if (addVariantBtn) {
            addVariantBtn.addEventListener("click", () => {
                const nextLetter = String.fromCharCode(65 + this.variants.length); // C, D, E...
                this.addVariant(`Variant ${nextLetter}`);
            });
        }

        // Listen for questions loaded from question_fetch.js
        window.addEventListener("questions-loaded", (e) => {
            this.allQuestions = e.detail;
            console.log("[PreviewManager] Questions received:", this.allQuestions.length);
        });

        // Sync test title input with A4 preview sheet
        const titleInput = document.getElementById("test-name");
        const sheetTitle = document.getElementById("sheet-title-display");
        if (titleInput && sheetTitle) {
            titleInput.addEventListener("input", (e) => {
                sheetTitle.textContent = e.target.value.trim() || "Untitled Test";
            });
        }
    },

    addVariant(label) {
        const id = this.variants.length + 1;
        const variant = new TestVariant(id, label);
        this.variants.push(variant);
        this.renderTabs();
        this.switchVariant(this.variants.length - 1);
    },

    switchVariant(index) {
        if (index < 0 || index >= this.variants.length) return;
        this.activeVariantIndex = index;

        // Update tab buttons
        document.querySelectorAll(".variant-tab").forEach((tab, i) => {
            tab.classList.toggle("active", i === index);
        });

        // Update preview sheet metadata
        const sheetVariant = document.getElementById("sheet-variant-display");
        if (sheetVariant) {
            sheetVariant.textContent = `Variant: ${this.variants[index].label.replace("Variant ", "")}`;
        }

        this.renderSheetQuestions();
    },

    renderTabs() {
        const tabContainer = document.getElementById("variant-tab-container");
        if (!tabContainer) return;

        // Keep the '+' button, rebuild tabs before it
        const addBtn = document.getElementById("add-variant-btn");
        tabContainer.querySelectorAll(".variant-tab").forEach(tab => tab.remove());

        this.variants.forEach((v, idx) => {
            const btn = document.createElement("button");
            btn.className = `variant-tab ${idx === this.activeVariantIndex ? "active" : ""}`;
            btn.textContent = v.label;
            btn.addEventListener("click", () => this.switchVariant(idx));
            tabContainer.insertBefore(btn, addBtn);
        });
    },

    renderSheetQuestions() {
        const container = document.getElementById("preview-questions-list");
        const badge = document.getElementById("question-count-badge");
        if (!container) return;

        const currentVariant = this.variants[this.activeVariantIndex];
        if (badge) badge.textContent = `${currentVariant.questions.length} Questions`;

        if (currentVariant.questions.length === 0) {
            container.innerHTML = `
                <div class="empty-state-notice">
                    <i class="fa-regular fa-file-lines"></i>
                    <p>No questions added to this variant yet. Use the question pool or auto-generator to add questions.</p>
                </div>`;
            return;
        }

        container.innerHTML = "";
        currentVariant.questions.forEach((q, index) => {
            const el = document.createElement("div");
            el.className = "question-entry";
            el.innerHTML = `
                <p><strong>${index + 1}.</strong> ${q.assignment || q.questionText}</p>
                <div class="options-grid">
                    ${(q.answers || []).map((ans, i) => `<div>${String.fromCharCode(65 + i)}) ${ans}</div>`).join("")}
                </div>
            `;
            container.appendChild(el);
        });
    }
};

document.addEventListener("DOMContentLoaded", () => {
    TestManager.init();
});