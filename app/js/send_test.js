// send_test.js
async function sendTestAndPreviewPdf() {
    if (!activeTest || !activeTest.questionPool) {
        console.error("No active test or empty question pool!");
        return;
    }

    // 🔑 Keys must match TestRequest.java field names exactly
    const payload = {
        testName: `Test Variant ${activeTest.variant}`,
        questions: activeTest.questionPool
    };

    try {
        const response = await fetch('/api/test/generate-pdf', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            throw new Error(`Server error: ${response.status}`);
        }

        const pdfBlob = await response.blob();
        const pdfUrl = URL.createObjectURL(pdfBlob);
        window.open(pdfUrl, '_blank');

    } catch (error) {
        console.error("Failed to generate PDF:", error);
    }
}
// await sendTestAndPreviewPdf();


async function generateActiveTestPdf() {
    // 1. Safety check
    if (!activeTest || !activeTest.questionPool || activeTest.questionPool.length === 0) {
        alert("Please add at least one question to the active test!");
        return;
    }

    // 2. Match field names in TestRequest.java
    const payload = {
        testName: `Test Variant ${activeTest.variant}`,
        questions: activeTest.questionPool
    };

    try {
        const response = await fetch('/api/test/generate-pdf', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            throw new Error(`Server returned HTTP ${response.status}`);
        }

        // 3. Receive binary blob and open PDF tab
        const pdfBlob = await response.blob();
        const pdfUrl = URL.createObjectURL(pdfBlob);
        window.open(pdfUrl, '_blank');

    } catch (error) {
        console.error("[PDF generation failed]:", error);
    }
}

document.getElementById("send").addEventListener("click", (e) => { generateActiveTestPdf(); })