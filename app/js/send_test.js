async function sendTestAndPreviewPdf() {
    // 1. Build test payload from active test state
    const payload = {
        testName: "Networking - Variant A",
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

        // 2. Read binary response body as a Blob
        const pdfBlob = await response.blob();

        // 3. Create a temporary browser URL pointing to the Blob memory
        const pdfUrl = URL.createObjectURL(pdfBlob);

        // 4. Open in new tab (or set iframe.src = pdfUrl for inline preview)
        window.open(pdfUrl, '_blank');

    } catch (error) {
        console.error("Failed to generate PDF:", error);
        alert("Error creating PDF preview.");
    }
}

await sendTestAndPreviewPdf();