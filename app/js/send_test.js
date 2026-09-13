document.addEventListener("DOMContentLoaded", () => {
    const generateBtn = document.getElementById("btn-generate-pdf") || document.getElementById("btn-export-pdf");

    if (generateBtn) {
        generateBtn.addEventListener("click", async () => {
            // Retrieve data from active TestManager state
            const active = window.TestManager.getActiveVariant();
            const activeLetter = active.letter;
            const questions = window.TestManager ? active.questions : [];

            if (!questions || questions.length === 0) {
                alert("Please add at least one question to the variant before generating the PDF.");
                return;
            }

            const payload = {
                title: document.getElementById("display-test-name")?.value || "Assessment",
                subject: sessionStorage.getItem("subject") || "General",
                variant: activeLetter,
                questions: questions
            };

            const originalHtml = generateBtn.innerHTML;
            try {
                generateBtn.disabled = true;
                generateBtn.innerHTML = `<i class="fa-solid fa-spinner fa-spin"></i> Generating...`;

                const response = await fetch("/api/pdf/generate", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        "Accept": "application/pdf"
                    },
                    body: JSON.stringify(payload)
                });

                if (!response.ok) {
                    const errJson = await response.json().catch(() => ({}));
                    throw new Error(errJson.error || `Server error: ${response.status}`);
                }

                // Download PDF attachment
                const blob = await response.blob();
                const downloadUrl = window.URL.createObjectURL(blob);
                const a = document.createElement("a");
                a.href = downloadUrl;
                a.download = `${payload.title.replace(/\s+/g, "_")}_Var_${payload.variant}.pdf`;
                document.body.appendChild(a);
                a.click();
                a.remove();
                window.URL.revokeObjectURL(downloadUrl);

            } catch (err) {
                console.error("PDF generation failed:", err);
                alert("Failed to create PDF: " + err.message);
            } finally {
                generateBtn.disabled = false;
                generateBtn.innerHTML = originalHtml;
            }
        });
    }
});