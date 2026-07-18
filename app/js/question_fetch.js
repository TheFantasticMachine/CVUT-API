
// lets try fetch
async function getQuestion() {
    try {
        let valid = true;
        let id = 1;
        while (valid) {
            let url = `http://localhost:8080/question?id=${id}`;
            let response = await fetch(url);
            if (response.ok) {
                const data = await response.json();
                console.log(url)
                console.log(data);
                id++;
                localStorage.setItem(`question_${data.questionID}`,JSON.stringify(data));
                let body = document.querySelector("body");
                let questionElement = document.createElement("p");
                questionElement.innerText = `Question assignment: ${data.assignment}`;
                body.appendChild(questionElement);
            } else {
                valid = false;
                throw new Error('Failed to fetch data');
            }
        }
    } catch (error) {
        console.error('Error:', error);
    }
}
getQuestion();
console.log('Hello');
setTimeout(() => { console.log('World!') }, 2000);