const { createApp } = Vue;

const firstName = document.getElementById("firstName");
const lastName = document.getElementById("lastName");
const email = document.getElementById("email");

createApp({
data() {
    return {
    firstName: "",
    lastName: "",
    email: "",
    authority: "",
    clients: [],
    json: [],
    data: [],
    logInEmail: "",
    logInPassword: "",
    alertMessage: ""
    };
},
methods: {
    logIn(){
        console.log(this.logInEmail);
        console.log(this.logInPassword);
        axios.post('/api/login',`email=${this.logInEmail}&password=${this.logInPassword}`)
        .then(response => {
            axios.get("/api/clients")
            .then(response => location.href = "/web/manager.html")
            .catch(err => {
                Swal.fire({
                    icon: 'success',
                    title: 'Success',
                    text: 'You have logged-in successfully',
                    showConfirmButton: false,
                    timer: 2000
                })
                location.href = "/web/accounts.html"})
        })
        .catch(error => {
                if((this.logInEmail === "")){
                    Swal.fire({
                        icon: 'error',
                        title: 'Missing data',
                        text: "The email field can't be empty.",
                    })
                } else if(this.logInPassword === "" || this.logInPassword.length < 5){
                    Swal.fire({
                        icon: 'error',
                        title: 'Missing data',
                        text: "The password field can't be empty and must have more than 5 digits",
                    })
                } else if (!(this.logInEmail.includes("@")) && !(this.logInEmail.includes(".com"))){
                    Swal.fire({
                        icon: 'error',
                        title: 'Wrong data',
                        text: "You must enter a valid email.",
                    })
                } else if (401 === error.response.status){
                    Swal.fire({
                        icon: 'error',
                        title: 'Wrong mail or password',
                        text: "Make sure you have an account",
                    })
                }
                })
                

/*             this.alertMessage = response.error.map(err => err.error);
            console.log(this.alertMessage);
            console.log(this.alertMessage); */

        }
    },
}).mount("#app");
//post para crear
//get obtener
//put para cambiar todo el objeto
//con el patch se modifica una propiedad sola x ejemplo lastName
//delete para borrar un nuevo objeto