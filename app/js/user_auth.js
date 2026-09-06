document.addEventListener("DOMContentLoaded", () => {
    // Elements
    const form = document.getElementById("auth-form") || document.querySelector("form");
    const usernameInput = document.getElementById("username") || document.querySelector('input[type="text"]');
    const passwordInput = document.getElementById("password") || document.querySelector('input[type="password"]');
    const errorMessage = document.getElementById("auth-error");

    // Toggle Tab / Mode (Login vs Register)
    const tabLogin = document.getElementById("tab-login");
    const tabRegister = document.getElementById("tab-register");
    const submitBtn = document.getElementById("btn-submit-auth") || form.querySelector('button[type="submit"]');

    let currentMode = "login"; // "login" or "register"

    function setMode(mode) {
        currentMode = mode;
        clearError();

        if (tabLogin && tabRegister) {
            tabLogin.classList.toggle("active", mode === "login");
            tabRegister.classList.toggle("active", mode === "register");
        }

        if (submitBtn) {
            submitBtn.textContent = mode === "login" ? "Sign In" : "Create Account";
        }
    }

    if (tabLogin) tabLogin.addEventListener("click", () => setMode("login"));
    if (tabRegister) tabRegister.addEventListener("click", () => setMode("register"));

    function showError(msg) {
        if (errorMessage) {
            errorMessage.textContent = msg;
            errorMessage.style.display = "block";
        } else {
            alert(msg);
        }
    }

    function clearError() {
        if (errorMessage) {
            errorMessage.textContent = "";
            errorMessage.style.display = "none";
        }
    }

    // Submit handler
    if (form) {
        form.addEventListener("submit", async (e) => {
            e.preventDefault();
            clearError();

            const username = usernameInput.value.trim();
            const password = passwordInput.value.trim();

            if (!username || !password) {
                showError("Please enter both username and password.");
                return;
            }

            const endpoint = currentMode === "login" ? "/api/user/login" : "/api/user/register";

            try {
                const response = await fetch(endpoint, {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        "Accept": "application/json"
                    },
                    body: JSON.stringify({
                        username: username,
                        password: password
                    })
                });

                const data = await response.json();

                if (!response.ok) {
                    throw new Error(data.error || "Authentication failed.");
                }

                // Redirect on success
                window.location.href = "/main";

            } catch (err) {
                showError(err.message);
            }
        });
    }
});