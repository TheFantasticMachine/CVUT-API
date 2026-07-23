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

        // add variant span
        let variantElement = document.createElement("span");
        variantElement.classList.add("test_variant");
        variantElement.innerText = variant;

        // add remove button
        let destroyBtn = document.createElement("button");
        destroyBtn.innerText = "remove";
        destroyBtn.addEventListener("click", (e) => this.deleteSelf());

        this.rootElement.appendChild(variantElement);
        this.rootElement.appendChild(destroyBtn);
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

    deleteSelf() {
        createdTests.splice(createdTests.indexOf(this), 1);

        createdTests.filter((el) => {
            el != null || el != undefined
        });

        // add new variants
        let i = 1;
        createdTests.forEach((test) => {
            test.variant = i;
            for (const child of test.rootElement.children) {
                if (child.classList.contains("test_variant")) {
                    child.innerText = i;
                }
            }
            i++;
        });

        testsWrapperElement.removeChild(this.rootElement);
    }
}

let defaultTest = new Test(1);
activeTest = defaultTest;
defaultTest.setActive();

new Test(2);
new Test(3);

document.querySelector("#add_test").addEventListener("click", (e) => {
    new Test(createdTests[createdTests.length-1].variant + 1);
});