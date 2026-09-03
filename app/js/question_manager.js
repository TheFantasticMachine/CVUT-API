// variables
let allVariants = [];
let allCategories = [];
let allQuestions = [];

// define a test variant
class TestVariant {

    constructor() {
        // stop if max variant num
        if (allVariants.length > 26 ) { throw new Error("too many variants"); }

        // set the letter
        this.letter = String.fromCharCode(65 + allVariants.length);

        // create the tab in the variant selector
        let tab = document.createElement("button");
        tab.classList.add("variant-tab");
        tab.innerText = `variant ${this.letter}`;
        tab.style.order = allVariants.length.toString();
        this.tabElement = tab;

        document.getElementById("variant-tab-container").appendChild(this.tabElement);
        this.isActive = false;

        return this;
    }

    // methods
    setActive() {
        // find prev active
        for (const variant in allVariants) {
            if (variant.isActive) {
                variant.tabElement.classList.toggle("active");
                variant.isActive = false;
            }
        }

        this.tabElement.classList.toggle("active");
        this.isActive = true;

        // display the test
        let header = document.createElement("header");
        header.innerHTML = `
        <span class="header-test-variant">Variant: ${this.letter}</span>
                    <hr>
                    <div class="header-row-one">
                        <span class="header-test-subject">Subject</span>
                        <span class="header-test-date">Date: </span>
                    </div>
                    <span class="header-name">Name: </span>
                    <hr>
        `
        document.getElementById("a4-preview-sheet")
    }

    // question handlers

    addQuestion() {}

    removeQuestion() {}

    moveQuestion() {}

    updateQuestion() {}
}

// define subject, category and question

class Subject {}

class Category {}

class Question {}

let test = new TestVariant();
console.log(allVariants);

// set event triggers

// create variant (with btn)

// create variant (on load) if new test
// ! for now created by default doesnt check for new test
window.addEventListener("load", (e) => {
    try {



        // only after that create the first variant
        const first = new TestVariant();
        allVariants.push(first);
        first.setActive();
    }
    catch (error) {
        console.error(error.message);
    }
});