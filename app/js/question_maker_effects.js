const alphabet = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'];
let currentIndex  = 0;

/*
<div class="answer-row">
                    <span class="answer-letter">A</span>
                    <input type="text" name="answers[]" placeholder="Enter answer text..." required />

                    <label class="correct-radio-label" title="Mark as correct answer">
                        <input type="radio" name="correctAnswerIndex" value="0" checked />
                        <span class="radio-custom">Correct</span>
                    </label>

                    <button type="button" class="btn-icon-remove" title="Remove option">&times;</button>
                </div>
 */

function createAnswer () {
    // 1. make wrapper
    let wrapper = document.createElement("div");
    wrapper.classList.add("answer-row");

    // <span class="answer-letter">A</span>
    let answerLetter = document.createElement("span");
    answerLetter.innerText = alphabet[currentIndex];
    currentIndex += 1;
    answerLetter.classList.add("answer-letter");
    // add to parent
    wrapper.appendChild(answerLetter);

    // <input type="text" name="answers[]" placeholder="Enter answer text..." required />
    let textIn = document.createElement("input");
    textIn.name = "answers[]";
    textIn.placeholder = "Enter answer text...";
    textIn.type = "text";
    textIn.required = true;
    // add to parent
    wrapper.appendChild(textIn);

    // <label class="correct-radio-label" title="Mark as correct answer">
    let label = document.createElement("label");
    label.classList.add("correct-radio-label");
    label.title = "Mark as correct answer";

    // <input type="radio" name="correctAnswerIndex" value="0" checked />
    let radioIn = document.createElement("input");
    radioIn.type = "radio";
    radioIn.name = "correctAnswerIndex";
    radioIn.value = "0";
    // add to label
    label.appendChild(radioIn)

    // <span class="radio-custom">Correct</span>
    let span = document.createElement("span");
    span.classList.add("radio-custom");
    span.innerText = "Correct";
    // add to label
    label.appendChild(span);

    // add label to wrapper
    wrapper.appendChild(label);

    // <button type="button" class="btn-icon-remove" title="Remove option">&times;</button>
    let button = document.createElement("button");
    button.type = "button";
    button.classList.add("btn-icon-remove");
    button.title = "Remove option";
    button.innerText = "&times;";
    // add to wrapper
    wrapper.appendChild(button);

    return wrapper;
}

document.addEventListener("load", (e) => {
    document.getElementById("answers-list").appendChild(createAnswer());
    document.getElementById("answers-list").appendChild(createAnswer());
    document.getElementById("answers-list").appendChild(createAnswer());
    document.getElementById("answers-list").appendChild(createAnswer());
});
