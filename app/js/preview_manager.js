const ALPHABET = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'];

let all_tests = [];
let selected_test = null;

class Test {

    constructor() {
        this.create();
    }

    getVariant() { return ALPHABET[ all_tests.length ] ; }

    create() {
        if (all_tests.length <= 26){
            this.variant = this.getVariant();
            this.element = document.createElement('button');
            this.element.classList.add("variant-tab");
            this.element.innerText = `variant ${this.variant}`;
            console.log(this.variant);
            console.log(document.getElementById('variant-tab-container'));
            document.getElementById('variant-tab-container').appendChild(this.element);
            all_tests.push(this);
        }
    }

    select() {
        if (selected_test !== null) {
            selected_test.element.classList.toggle('active');
        }
        selected_test = this;
        this.element.classList.toggle('active');
    }
}

let test1 = new Test();
let test2 = new Test();
let test3 = new Test();

test1.select();
test3.select();

// popup manager - since we will handle questions here too
const question_dialog = document.getElementById('question-popup-wrapper');

document.getElementById('btn-add').addEventListener('click', (e) => {
    question_dialog.showModal();
})

question_dialog.addEventListener("click", (e) => {
    if (!document.querySelector('.excluded').contains(e.target)) {
        question_dialog.close();
    }
})