const { createApp } = Vue;

const firstName = document.getElementById("signInFirstName");
const lastName = document.getElementById("signInLastName");
const email = document.getElementById("signInEmail");
const password = document.getElementById("signInPassword");

createApp({
data() {
    return {
    firstName: "",
    lastName: "",
    email: "",
    clients: [],
    json: [],
    signInFirstName: "",
    signInLastName: "",
    signInEmail: "",
    signInPassword: "",
    };
},
methods: {
    signIn(){
        axios.post('/api/clients',
        "firstName=" + this.signInFirstName + 
        "&lastName=" + this.signInLastName +
        "&email=" + this.signInEmail +
        "&password=" + this.signInPassword)        
        .then(response =>{
            console.log("registrado rey")
            axios.post('/api/login',`email=${this.signInEmail}&password=${this.signInPassword}`)
            .then(response => {
                window.location.href = "/web/accounts.html"
            })
        })
        .catch(response => {
            this.alertMessage = response.response.data
            console.log(this.alertMessage);
            if(this.alertMessage == "Missing First Name"){
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: "Missing First Name",
                })
            } else if(this.alertMessage == "Missing Last Name"){
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: "Missing Last Name"
                })
            }else if(this.alertMessage == "Missing Email"){
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: "Missing Email"
            })
            } else if(this.alertMessage == "Missing Password"){
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: "Missing Password"
            })
            }else if(this.alertMessage == "Mail is not valid"){
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: "Mail is not valid"
            })
            }else if(this.alertMessage == "Your first name and last name can't be shorter than 3 letters"){
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: "Your first name and last name can't be shorter than 3 letters"
            })
            }else if(this.alertMessage == "Your password must have more than 5 chars"){
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: "Your password must have more than 5 chars"
            })
            } else if(this.alertMessage == "Mail already in use"){
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: "Mail already in use"
            })
            } 
        })
    },
}
}).mount("#app");
//post para crear
//get obtener
//put para cambiar todo el objeto
//con el patch se modifica una propiedad sola x ejemplo lastName
//delete para borrar un nuevo objeto