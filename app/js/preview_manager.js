// let questions;

window.addEventListener('questions loaded', (event) => {
    // define questions
    questions = event.detail;
    console.log(questions);
});

// generate test (default)
let createdTests = [];
let activeTest;

const testsWrapperElement  = document.querySelector(".tests_wrapper");

class Test {
    questionPool;
    variant = 1;
    rootElement;
    isActive = false;
    constructor(variant) {
        // create element
        this.rootElement  = document.createElement("div");
        this.rootElement.classList.add("test");
        let variantElement = document.createElement("span");
        variantElement.classList.add("test_variant");
        variantElement.innerText = variant;
        this.rootElement.appendChild(variantElement);
        testsWrapperElement.appendChild(this.rootElement);

        this.variant = variant;
        this.setOrder(variant);

        this.rootElement.addEventListener("click", (e) => {
            this.setActive();
        });
        createdTests.push(this);
    }

    setOrder(order) {
        this.rootElement.style.order = order;
    }

    setActive() {
        this.isActive = true;
        let active = createdTests[ createdTests.indexOf(activeTest) ];
        active.setOrder(active.variant);
        active.isActive = false;

        this.setOrder(0);
    }
}

let defaultTest = new Test(1);
activeTest = defaultTest;
defaultTest.setActive();

new Test(2);
new Test(3);