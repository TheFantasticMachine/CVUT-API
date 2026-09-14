document.addEventListener("DOMContentLoaded", async () => {
    const dialog = document.getElementById("new-test-dialog");
    const openButtons = document.querySelectorAll(".open-new-test-btn");
    const closeBtn = document.getElementById("btn-close-dialog");
    const cancelBtn = document.getElementById("btn-cancel-dialog");
    const form = document.getElementById("form-new-test");

    openButtons.forEach(btn => {
        btn.addEventListener("click", () => dialog.showModal());
    });

    const closeDialog = () => dialog.close();
    if (closeBtn) closeBtn.addEventListener("click", closeDialog);
    if (cancelBtn) cancelBtn.addEventListener("click", closeDialog);

    // Handle submission: store chosen title & subject, then navigate to test maker
    if (form) {
        form.addEventListener("submit", (e) => {
            e.preventDefault();
            const titleInput = document.getElementById("new-test-title").value.trim();
            const subjectSelect = document.getElementById("new-test-subject");
            const subjectName = subjectSelect.options[subjectSelect.selectedIndex].text;
            const subjectId = subjectSelect.value;

            // Stash in session storage for test_maker.html to pick up
            sessionStorage.setItem("test-name", titleInput);
            sessionStorage.setItem("subject", subjectName);
            sessionStorage.setItem("subject-id", subjectId);

            // Redirect to test maker with params
            window.location.href = `/test_maker`;
        });
    }

    try {
        const response = await fetch("api/user/current");
        if (!response.ok) {
            throw new Error()
        }
        const result = await response.json();
        console.log(result);
        document.querySelector(".user-name").innerText = result.username;
        document.querySelector(".user-role").innerText = result.role.toLowerCase();
    } catch (error) {
        console.error(error.message);
    }
});

const TestLoader = (function () {
    let loadedTestSummaries = null;

    async function loadSavedTests() {
        try {
            const userResponse = await fetch("api/user/current");
            if (!userResponse.ok) {
                throw new Error("user not loaded")
            }
            const user = await userResponse.json();


            const testSummariesResponse = await fetch(`/api/test_save/summaries-by-user-id?userId=${user.id}`);
            if (!testSummariesResponse.ok) {
                throw new Error("summaries not found");
            }
            loadedTestSummaries = await testSummariesResponse.json();
        }
        catch (error) {
            console.error(error.message);
        }
    }

    function getVariantLetters(summary) {
        let result = "(";
        summary.variants.forEach(letter => {
            result += letter;
            if (summary.variants[summary.variants.length - 1] !== letter) {
                result += ", ";
            }
        });
        result += ")";
        return result;
    }

    function testRedirect(summary) {
        sessionStorage.setItem("current_test_id", summary.testId);
        window.location.href = '/test_maker';
    }

    function crateTestCardElements() {
        const testGrid = document.querySelector(".tests-grid");
        loadedTestSummaries.forEach(summary => {
            const testCard = document.createElement("article");
            testCard.classList.add("test-item-card");
            testCard.innerHTML = `
            <div class="test-card-top">
                    <span class="subject-badge math">${summary.subject}</span>
                    <span class="test-status draft">${summary.status}</span>
                </div>
                <h3 class="test-title">${summary.title}</h3>
                <div class="test-meta-info">
                    <span><i class="fa-solid fa-layer-group"></i> ${summary.variants.length} Variants ${getVariantLetters(summary)}</span>
                    <span><i class="fa-regular fa-clock"></i> Modified 2h ago</span>
                </div>
                <div class="test-card-actions">
                    <a th:href="@{/test_maker}" class="btn-edit-test">Continue Editing</a>
                </div>
            `;
            testGrid.appendChild(testCard);

            testCard.querySelector('a.btn-edit-test').addEventListener("click", (e) => {
                e.preventDefault();
                testRedirect(summary)
            });
        });
    }
});