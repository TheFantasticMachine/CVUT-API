
// lets try fetch
async function getQuestion() {
    try {
        let valid = true;
        let id = 1;
        while (valid) {
            let response = await fetch(`http://localhost:3002/question?id=${id}`);
            if (response.ok) {
                const data = await response.json();
                console.log(data);
                id++;
-
                localStorage.setItem(`question_${data.questionID}`,JSON.stringify(data));
                let body = document.querySelector("body");
                let questionElement = document.createElement("p");
                questionElement.innerText = `Question assignment: ${data.assignment}`;
                body.appendChild(questionElement);
            } else {
                throw new Error('Failed to fetch data');
                valid = false;
            }
        }
    } catch (error) {
        console.error('Error:', error);
    }
}
getQuestion();
console.log('Hello');
setTimeout(() => { console.log('World!') }, 2000);