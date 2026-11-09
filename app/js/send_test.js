document.addEventListener("DOMContentLoaded", () => {
    const generateBtn = document.getElementById("btn-generate-pdf") || document.getElementById("btn-export-pdf");
    const exportAnswersBtn = document.getElementById("btn-export-answers");

    function buildPayload() {
        const active = window.TestManager ? window.TestManager.getActiveVariant() : null;
        if (!active) return null;

        const activeLetter = active.letter;
        const questions = active.questions || [];

        const titleEl = document.getElementById("display-test-name");
        const subjectEl = document.getElementById("display-subject-tag");

        const title = (titleEl ? (titleEl.value || titleEl.innerText) : "") || "Test";
        const subject = (subjectEl ? subjectEl.innerText : sessionStorage.getItem("subject")) || "Obecné";

        return {
            title: title.trim(),
            subject: subject.trim(),
            variant: activeLetter,
            questions: questions
        };
    }

    if (generateBtn) {
        generateBtn.addEventListener("click", async () => {
            const payload = buildPayload();

            if (!payload || !payload.questions || payload.questions.length === 0) {
                alert("Před generováním PDF přidejte do varianty alespoň jednu otázku.");
                return;
            }

            const originalHtml = generateBtn.innerHTML;
            try {
                generateBtn.disabled = true;
                generateBtn.innerHTML = `<i class="fa-solid fa-spinner fa-spin"></i> Generování...`;

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
                    throw new Error(errJson.error || `Chyba serveru: ${response.status}`);
                }

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
                console.error("Generování PDF selhalo:", err);
                alert("Nepodařilo se vytvořit PDF: " + err.message);
            } finally {
                generateBtn.disabled = false;
                generateBtn.innerHTML = originalHtml;
            }
        });
    }

    if (exportAnswersBtn) {
        exportAnswersBtn.addEventListener("click", async () => {
            const payload = buildPayload();

            if (!payload || !payload.questions || payload.questions.length === 0) {
                alert("Před exportem odpovědí přidejte do varianty alespoň jednu otázku.");
                return;
            }

            const originalHtml = exportAnswersBtn.innerHTML;
            try {
                exportAnswersBtn.disabled = true;
                exportAnswersBtn.innerHTML = `<i class="fa-solid fa-spinner fa-spin"></i><span>Generování...</span>`;

                const response = await fetch("/api/pdf/answers", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        "Accept": "application/pdf"
                    },
                    body: JSON.stringify(payload)
                });

                if (!response.ok) {
                    const errJson = await response.json().catch(() => ({}));
                    throw new Error(errJson.error || `Chyba serveru: ${response.status}`);
                }

                const blob = await response.blob();
                const downloadUrl = window.URL.createObjectURL(blob);
                const a = document.createElement("a");
                a.href = downloadUrl;
                a.download = `${payload.title.replace(/\s+/g, "_")}_Var_${payload.variant}_Odpovedi.pdf`;
                document.body.appendChild(a);
                a.click();
                a.remove();
                window.URL.revokeObjectURL(downloadUrl);

            } catch (err) {
                console.error("Export odpovědí selhal:", err);
                alert("Nepodařilo se exportovat odpovědi: " + err.message);
            } finally {
                exportAnswersBtn.disabled = false;
                exportAnswersBtn.innerHTML = originalHtml;
            }
        });
    }
});