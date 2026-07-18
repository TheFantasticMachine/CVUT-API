let port = prompt("insert current port");

// lets try fetch
async function getQuestion() {
    try {
        let valid = true;
        let id = 1;
        while (valid) {
            let url = `http://localhost:${port}/question?id=${id}`;
            let response = await fetch(url);
            if (response.ok) {
                const data = await response.json();
                console.log(url)
                console.log(data);
                id++;
                localStorage.setItem(`question_${data.questionID}`,JSON.stringify(data));
            } else {
                valid = false;
                throw new Error('Failed to fetch data');
            }
        }
    } catch (error) {
        console.error('Error:', error);
    }
}
getQuestion().then(() => {
    // lets loop every question just as a test
    // -- 1. get parent
    let parentElement = document.getElementById("question_display");

    // -- 2. loop
    let id = 1;
    while (localStorage.getItem(`question_${id}`)) {
        // get json
        let json = JSON.parse( localStorage.getItem(`question_${id}`) );
        // create parent element
        let questionElement = document.createElement("div");
        // add heading
        let heading = document.createElement("span");
        heading.classList.add("question_heading");
        heading.innerText = `(${json.questionID}) ${json.assignment}`;
        questionElement.appendChild(heading);

        // add answers
        let listOfAnswers = document.createElement("ul");
         // add every one
        for (let i = 0; i < json.answers.length; i++) {
            let item = document.createElement("li");
            item.innerText = json.answers[i];
            if (i === json.correctIndex) {
                item.style.backgroundColor = "limegreen";
            }
            listOfAnswers.appendChild(item);
        }

        questionElement.appendChild(listOfAnswers);
    }
});