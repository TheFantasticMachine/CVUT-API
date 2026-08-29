// async function generateActiveTestPdf() {
//     // 1. Safety check
//     if (!activeTest || !activeTest.questionPool || activeTest.questionPool.length === 0) {
//         alert("Please add at least one question to the active test!");
//         return;
//     }
//
//     // 2. Match field names in TestRequest.java
//     const payload = {
//         variant: activeTest.variant,
//         questions: activeTest.questionPool
//     };
//
//     try {
//         const response = await fetch('/api/test/generate-pdf', {
//             method: 'POST',
//             headers: {
//                 'Content-Type': 'application/json'
//             },
//             body: JSON.stringify(payload)
//         });
//
//         if (!response.ok) {
//             throw new Error(`Server returned HTTP ${response.status}`);
//         }
//
//         // 3. Receive binary blob and open PDF tab
//         const pdfBlob = await response.blob();
//         const pdfUrl = URL.createObjectURL(pdfBlob);
//         window.open(pdfUrl, '_blank');
//
//     } catch (error) {
//         console.error("[PDF generation failed]:", error);
//     }
// }
//
// document.getElementById("send").addEventListener("click", (e) => { generateActiveTestPdf(); })
//
// // app/js/send_test.js
// document.addEventListener("DOMContentLoaded", () => {
//     // Check for either possible ID
//     const exportBtn = document.getElementById("btn-export-pdf")
//         || document.getElementById("send-test")
//         || document.getElementById("export");
//
//     if (exportBtn) {
//         exportBtn.addEventListener("click", () => {
//             console.log("[TestMaker] Exporting test configuration to PDF...");
//             // Your export / send logic here
//         });
//     }
// });

// app/js/send_test.js
document.addEventListener("DOMContentLoaded", () => {
    const exportPdfBtn = document.getElementById("btn-export-pdf");
    const saveDraftBtn = document.getElementById("btn-save");

    if (exportPdfBtn) {
        exportPdfBtn.addEventListener("click", async () => {
            const testTitle = document.getElementById("test-name")?.value || "Untitled Test";
            const subjectId = document.getElementById("test-subject")?.value || "1";

            const activeVariant = TestManager.variants[TestManager.activeVariantIndex];

            const payload = {
                title: testTitle,
                subjectID: parseInt(subjectId),
                variantCount: TestManager.variants.length,
                questionPool: activeVariant ? activeVariant.questions : []
            };

            console.log("[SendTest] Sending PDF Generation Request:", payload);

            try {
                const response = await fetch("/api/test/generate", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(payload)
                });

                if (response.ok) {
                    const blob = await response.blob();
                    const downloadUrl = window.URL.createObjectURL(blob);
                    const a = document.createElement("a");
                    a.href = downloadUrl;
                    a.download = `${testTitle.replace(/\s+/g, "_")}.pdf`;
                    document.body.appendChild(a);
                    a.click();
                    a.remove();
                } else {
                    console.error("[SendTest] Server failed to generate PDF:", response.status);
                }
            } catch (err) {
                console.error("[SendTest] Export error:", err);
            }
        });
    }

    if (saveDraftBtn) {
        saveDraftBtn.addEventListener("click", () => {
            console.log("[SendTest] Draft saved locally.");
            localStorage.setItem("test_draft", JSON.stringify(TestManager.variants));
        });
    }
});