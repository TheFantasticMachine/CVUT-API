// new user
document.getElementById("new-user-form").addEventListener("submit",  async (e) => {
    e.preventDefault();
    const data = new FormData(e.target);

    try {
        const response = await fetch(`/api/user/register`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify( {username: data.get("username"), password: data.get("password")})
        });

        if (!response.ok) {
            throw new Error(`Response status: ${response.status}`);
        }

        const result = await response.json();
        console.log(result);
    }
    catch (error){
        console.error(error.message);
    }
})