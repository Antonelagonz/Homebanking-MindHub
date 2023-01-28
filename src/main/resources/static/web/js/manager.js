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
      clientes: [],
      json: [],
      idClient: undefined,
      newClient: {},
      inputNewFirstName: "",
      inputNewLastName: "",
      inputNewEmail: "",
    };
  },
  created() {
    this.loadData();
  },
  methods: { 
    loadData(){
        axios.get("/rest/clients")
        .then((data) => {
            this.json = data.data;
            this.clientes = this.json._embedded.clients;
            console.log(this.clientes);
            })
        .catch((error) => console.log(error));
    },
    addClient() {
        if(this.firstName != "" && this.lastName != "" && this.email != "" && this.email.includes("@")){
            let newClient = {
              firstName: this.firstName,
              lastName: this.lastName,
              email: this.email,
            }
            this.postClient(newClient)
            Swal.fire({
              icon: 'success',
              title: 'Your information has been saved',
              showConfirmButton: false,
              timer: 2000
            })
        } else if(this.firstName != "" && this.lastName != "" && this.email != "" && !(this.email.includes("@"))){
          Swal.fire({
            title: 'The information entered is not correct',
            text: "Please make sure the email is correct",
            icon: 'warning',
            showConfirmButton: false,
            timer: 2500
          })
        } else{
          Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'There is one of more blank spaces',
            showConfirmButton: false,
            timer: 2500
          })
        }

    },
    postClient(newClient){
        axios.post("/rest/clients", newClient)
        .then(()=>this.loadData())
        .catch(() => Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'There was an error loading the new client information',
          showConfirmButton: false,
          timer: 2500
        }));
    },
    deleteClient(cliente){
      let idClient = cliente._links.self.href
      axios.delete(idClient)
      .then(()=>this.loadData(),
      Swal.fire({
        icon: 'success',
        title: 'The client information has been deleted succesfully',
        showConfirmButton: false,
        timer: 2500
      }))
      .catch(() => Swal.fire({
        icon: 'error',
        title: 'Error',
        text: 'There was an error loading the new client information',
        showConfirmButton: false,
        timer: 2500
      }))
    },
    changeClient(cliente){
      this.idClient = cliente._links.self.href
      this.inputNewFirstName = cliente.firstName
      this.inputNewLastName = cliente.lastName
      this.inputNewEmail = cliente.email
    },
    saveNewInfo(){
      axios.put(this.idClient, this.newClient = {
        firstName: this.inputNewFirstName,
        lastName: this.inputNewLastName,
        email: this.inputNewEmail,
      })
      .then(()=>this.loadData())
      .catch(() => Swal.fire({
        icon: 'error',
        title: 'Error',
        text: 'There was an error loading the new client information',
        showConfirmButton: false,
        timer: 2500
      }))
    },
    changeFNameClient(cliente){
      this.idClient = cliente._links.self.href
      this.inputNewFirstName = cliente.firstName
    },
    saveNewFNameClient(){
      axios.patch(this.idClient, this.newClient = {
        firstName: this.inputNewFirstName
      })
      .then(()=> this.loadData())
      .catch(() => Swal.fire({
        icon: 'error',
        title: 'Error',
        text: 'There was an error loading the new client information',
        showConfirmButton: false,
        timer: 2500
      }))
    },
    changeLNameClient(cliente){
      this.idClient = cliente._links.self.href
      this.inputNewLastName = cliente.lastName
    },
    saveNewLNameClient(){
      axios.patch(this.idClient, this.newClient = {
        lastName: this.inputNewLastName
      })
      .then(()=> this.loadData())
      .catch(() => Swal.fire({
        icon: 'error',
        title: 'Error',
        text: 'There was an error loading the new client information',
        showConfirmButton: false,
        timer: 2500
      }))
    },
    changeEmailClient(cliente){
      this.idClient = cliente._links.self.href
      this.inputNewEmail = cliente.email
    },
    saveNewEmailClient(){
      axios.patch(this.idClient, this.newClient = {
        email: this.inputNewEmail
      })
      .then(()=> this.loadData())
      .catch(() => Swal.fire({
        icon: 'error',
        title: 'Error',
        text: 'There was an error loading the new client information',
        showConfirmButton: false,
        timer: 2500
      }))
    },
    logOut(){
			axios.post("/api/logout")
			.then(response =>{
				window.location.href = "/web/index.html"
			})
		}
},
}).mount("#app");
//post para crear
//get obtener
//put para cambiar todo el objeto
//con el patch se modifica una propiedad sola x ejemplo lastName
//delete para borrar un nuevo objeto