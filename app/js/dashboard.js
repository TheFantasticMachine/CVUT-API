

// 1) get user

// 2) get subjects
document.addEventListener("DOMContentLoaded", async (e) => {
    try {
        const response = await fetch("api/subject/all");
        if (!response.ok) {
            throw new Error("no subjects");
        }

        const request = await response.json();
        console.log(request);

        const template = document.getElementById("subject");
        const parent = document.getElementById("variable-content-wrapper");
        for (const subject of request) {
            const clone = document.importNode(template.content, true);
            console.log(subject);
            clone.querySelector("span.name").innerText = subject.subjectName;

            const userByIdRes = await fetch(`api/user/by-id?userId=${subject.adminID}`);
            if (!userByIdRes.ok) {
                console.error("user admin for subject not found");
                continue;
            }
            const userByIdReq = await userByIdRes.json();
            clone.querySelector("span.admin").innerText = userByIdReq.username;

            clone.querySelector("span.categories").innerText = `Categories: ${subject.categories.length}`;

            const questionsByIdRes = await fetch(`api/question/by-subject-id?subjectId=${subject.subjectID}`);
            if (!questionsByIdRes.ok) {
                console.error("questions not found");
                continue;
            }
            const questionsByIdReq = await questionsByIdRes.json();
            console.log(questionsByIdReq);
            clone.querySelector("span.questions").innerText = `Questions: ${questionsByIdReq.length}`;

            const style_data = JSON.parse( subject.styleData );
            console.log(style_data);
            clone.querySelector("span.icon").innerText = style_data.shortcut;

            parent.appendChild(clone);
        }
    }
    catch (error) {
        console.error(error.message);
    }
});

const addBtn = document.querySelector(".menu-btn").addEventListener("click", (e) => {
    const template = document.getElementById("subject");
    const parent = document.getElementById("variable-content-wrapper");

    const clone = document.importNode(template.content, true);
    parent.appendChild(clone);
});