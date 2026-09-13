document.addEventListener("DOMContentLoaded", () => {
    let activeStatus = "PENDING";
    let loadedQuestions = [];

    const listContainer = document.getElementById("approval-list-container");
    const searchInput = document.getElementById("approval-search-input");
    const feedbackDialog = document.getElementById("feedback-dialog");
    const feedbackForm = document.getElementById("feedback-form");
    const feedbackQuestionIdInput = document.getElementById("feedback-question-id");
    const feedbackMessageInput = document.getElementById("feedback-message");

    // 1. Fetch questions matching current status
    async function fetchQuestions(status) {
        activeStatus = status;
        listContainer.innerHTML = `
            <div class="loading-state">
                <i class="fa-solid fa-circle-notch fa-spin"></i>
                <p>Loading ${status.toLowerCase()} questions...</p>
            </div>
        `;

        try {
            const res = await fetch(`/api/question/status/${status}`);
            if (!res.ok) throw new Error("Could not load questions");
            loadedQuestions = await res.json();
            updateBadges();
            renderList();
        } catch (err) {
            listContainer.innerHTML = `<div class="empty-state"><p class="error-text">${err.message}</p></div>`;
        }
    }

    // 2. Render cards
    function renderList() {
        const query = searchInput ? searchInput.value.trim().toLowerCase() : "";
        const filtered = loadedQuestions.filter(q =>
            !query || q.assignment.toLowerCase().includes(query)
        );

        listContainer.innerHTML = "";

        if (filtered.length === 0) {
            listContainer.innerHTML = `
                <div class="empty-state">
                    <i class="fa-regular fa-folder-open"></i>
                    <p>No ${activeStatus.toLowerCase()} questions found.</p>
                </div>
            `;
            return;
        }

        filtered.forEach(q => {
            const card = document.createElement("div");
            card.className = "approval-card";
            card.dataset.id = q.questionID;

            // Render options
            const answersHtml = (q.answers || []).map((ans, idx) => `
                <div class="answer-choice-row ${ans.isCorrect || ans.correct ? 'is-correct' : ''}">
                    <div class="answer-content">
                        <span class="letter">${String.fromCharCode(65 + idx)})</span>
                        <span class="text">${ans.answerText}</span>
                    </div>
                    ${(ans.isCorrect || ans.correct) ? '<span class="correct-tag"><i class="fa-solid fa-check"></i> Correct</span>' : ''}
                </div>
            `).join("");

            // Review message banner if present
            const feedbackHtml = q.reviewMessage ? `
                <div class="review-feedback-callout">
                    <i class="fa-solid fa-triangle-exclamation"></i>
                    <div class="feedback-body">
                        <strong>Reviewer Note</strong>
                        <p>${q.reviewMessage}</p>
                    </div>
                </div>
            ` : "";

            // Actions (only show Approve/Request edits if still PENDING)
            const actionsHtml = activeStatus === "PENDING" ? `
                <div class="action-button-group">
                    <button type="button" class="btn-action btn-request-edits" data-action="feedback">
                        <i class="fa-solid fa-pencil"></i>
                        <span>Request Edits</span>
                    </button>
                    <button type="button" class="btn-action btn-approve" data-action="approve">
                        <i class="fa-solid fa-check"></i>
                        <span>Approve</span>
                    </button>
                </div>
            ` : `
                <div class="action-button-group">
                    <button type="button" class="btn-action btn-secondary" disabled>
                        <span>${q.status}</span>
                    </button>
                </div>
            `;

            card.innerHTML = `
                <div class="card-top-meta">
                    <div class="meta-pills">
                        <span class="badge badge-category">Category #${q.categoryID}</span>
                        <span class="badge badge-difficulty">Diff: ${q.difficulty}/5</span>
                        ${q.parentID ? '<span class="badge badge-revision"><i class="fa-solid fa-code-compare"></i> Revision of #' + q.parentID + '</span>' : ''}
                    </div>
                    <span class="status-indicator ${q.status.toLowerCase()}">${q.status}</span>
                </div>

                <div class="assignment-content">${q.assignment}</div>
                <div class="answers-review-box">
                    <span class="box-title">Answer Options</span>
                    ${answersHtml}
                </div>

                ${feedbackHtml}

                <footer class="card-footer-controls">
                    <div class="author-stamp">
                        <i class="fa-regular fa-user"></i>
                        <span>Author ID: #${q.userID || 'N/A'}</span>
                    </div>
                    ${actionsHtml}
                </footer>
            `;

            // Button actions
            const approveBtn = card.querySelector('[data-action="approve"]');
            if (approveBtn) {
                approveBtn.addEventListener("click", () => approveQuestion(q.questionID));
            }

            const feedbackBtn = card.querySelector('[data-action="feedback"]');
            if (feedbackBtn) {
                feedbackBtn.addEventListener("click", () => openFeedbackDialog(q.questionID));
            }

            listContainer.appendChild(card);
        });
    }

    // 3. API Actions
    async function approveQuestion(id) {
        if (!confirm("Are you sure you want to approve this question into production?")) return;

        try {
            const res = await fetch(`/api/question/${id}/approve`, { method: "POST" });
            if (!res.ok) throw new Error("Approval failed.");
            // Remove card or refresh list
            fetchQuestions(activeStatus);
        } catch (err) {
            alert(err.message);
        }
    }

    function openFeedbackDialog(id) {
        feedbackQuestionIdInput.value = id;
        feedbackMessageInput.value = "";
        feedbackDialog.showModal();
    }

    document.getElementById("btn-close-dialog")?.addEventListener("click", () => feedbackDialog.close());
    document.getElementById("btn-cancel-feedback")?.addEventListener("click", () => feedbackDialog.close());

    feedbackForm?.addEventListener("submit", async (e) => {
        e.preventDefault();
        const id = feedbackQuestionIdInput.value;
        const msg = feedbackMessageInput.value.trim();

        try {
            const res = await fetch(`/api/question/${id}/feedback`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ message: msg, status: "DRAFT" })
            });

            if (!res.ok) throw new Error("Could not send feedback.");
            feedbackDialog.close();
            fetchQuestions(activeStatus);
        } catch (err) {
            alert(err.message);
        }
    });

    // Tab switching
    document.querySelectorAll(".filter-tab").forEach(tab => {
        tab.addEventListener("click", () => {
            document.querySelectorAll(".filter-tab").forEach(t => t.classList.remove("active"));
            tab.classList.add("active");
            fetchQuestions(tab.dataset.status);
        });
    });

    // Search filter
    searchInput?.addEventListener("input", renderList);

    function updateBadges() {
        const badge = document.getElementById(`badge-${activeStatus.toLowerCase()}`);
        if (badge) badge.textContent = loadedQuestions.length;
    }

    // Initial load
    fetchQuestions("PENDING");
});