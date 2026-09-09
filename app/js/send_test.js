/**
 * Triggers PDF generation on the backend and prompts file download
 */
async function generateAndDownloadPdf() {
    if (!window.TestManager) {
        alert("TestManager is not initialized.");
        return;
    }

    // 1. Build the variant list
    const test = window.TestManager.getActiveVariant();
    const payload = {
        variant: test.letter,
        subject: sessionStorage.getItem("subject"),
        title: sessionStorage.getItem("test-name"),
        questions: test.questions
    };

    const generateBtn = document.getElementById("btn-generate-pdf") || document.getElementById("btn-export-pdf");
    const originalText = generateBtn ? generateBtn.innerHTML : "";

    try {
        if (generateBtn) {
            generateBtn.disabled = true;
            generateBtn.innerHTML = `<i class="fa-solid fa-spinner fa-spin"></i> Generating PDF...`;
        }

        // 2. Fetch binary stream
        const response = await fetch("/api/pdf/generate", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            throw new Error(`Server returned HTTP ${response.status}`);
        }

        // 3. Receive blob and download
        const blob = await response.blob();
        const downloadUrl = window.URL.createObjectURL(blob);
        const a = document.createElement("a");
        a.href = downloadUrl;
        a.download = `${payload.title.replace(/\s+/g, "_")}.pdf`;
        document.body.appendChild(a);
        a.click();
        a.remove();
        window.URL.revokeObjectURL(downloadUrl);

    } catch (err) {
        console.error("PDF generation failed:", err);
        alert("Failed to create PDF: " + err.message);
    } finally {
        if (generateBtn) {
            generateBtn.disabled = false;
            generateBtn.innerHTML = originalText;
        }
    }
}

// Bind button listener
document.addEventListener("DOMContentLoaded", () => {
    const btn = document.getElementById("btn-generate-pdf") || document.getElementById("btn-export-pdf");
    if (btn) {
        btn.addEventListener("click", generateAndDownloadPdf);
    }
});