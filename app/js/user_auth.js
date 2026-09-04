// new user
document.getElementById("new-user-form").addEventListener("submit",  async (e) => {
    e.preventDefault();
    const data = new FormData(e.target);

    try {
        let response = await fetch(`/api/user/by-username?username=${data.get("username")}`);
        if (response.ok) {
            throw new Error('username in use');
        }



    }
    catch (error){
        console.error(error.message);
    }
})