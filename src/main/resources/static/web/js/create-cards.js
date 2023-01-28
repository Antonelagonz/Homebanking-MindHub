const {createApp} = Vue;


createApp({
    data() {
        return{
        json: [],
        data: [],
        cardType:"none",
        cardColor:"none",
        alertMessage: "",
        accountsData: {},
        accountSelected: '',
    };
    },

	created() {
		this.loadClient();
	},
    methods:{
		loadClient() {
			this.queryString = location.search;
			let params = new URLSearchParams(this.queryString);
			let id = params.get("id")
			axios.get("/api/client/current")
				.then((response) => {
					this.json = response.data;
                    this.allAcccounts = this.json.accounts.sort((a,b) => a.id - b.id);
					this.accountsData = this.allAcccounts.filter(account => account.enabled === true);
					this.cards = this.json.cards;
					this.creditCards = this.cards.filter((type) => type.cardType === 'CREDIT');
					this.debitCards = this.cards.filter((type) => type.cardType === 'DEBIT');
				})
				.catch((error) => console.log(error));
		},
        createCard(){
            Swal.fire({
				icon: 'question',
				title: 'Confirm new card',
				text: "You're going to create a new card. Are you sure?",
				showCancelButton: true,
				confirmButtonColor: '#3085d6',
				cancelButtonColor: '#d33',
				confirmButtonText: 'Yes, create new card'
			})
            .then((result) => {
                if(result.isConfirmed){
                    axios.post('/api/client/current/cards',`cardType=${this.cardType}&cardColor=${this.cardColor}&account=${this.accountSelected}`)
                    .then(response => {
                    Swal.fire({
                        didOpen: (e) => {
                            document.querySelector(".swal2-confirm").addEventListener("click", () => {
                            window.location.href = "/web/cards.html"
                            })
                        },
                        icon: 'success',
                        title: 'Success',
                        text: 'Your card was created successfully'
                        })
                    })
                    .catch(response =>{
                        console.log(response.response.data)
                        this.alertMessage = response.response.data
                        console.log(this.alertMessage)
                        if(this.cardColor == "none"){
                            Swal.fire({
                                icon: 'error',
                                title: 'Oops...',
                                text: "The card color field can't be empty"
                            })
                        } else if(this.cardType == "none"){
                            Swal.fire({
                                icon: 'error',
                                title: 'Oops...',
                                text: "The card type field can't be empty"
                            })
                        } else if(this.alertMessage == "You have reached the limit of cards"){
							Swal.fire({
								icon: 'error',
								title: 'Oops...',
								text: "You have reached the limit of cards",
							})
						} else if(this.alertMessage == "You have reached the max amount of Credit Cards"){
                            Swal.fire({
                                icon: 'error',
                                title: 'Oops...',
                                text: "You have reached the max amount of Credit Cards"
                            })
                        } else if(this.alertMessage == "You have reached the max amount of Debit Cards"){
                            Swal.fire({
                                icon: 'error',
                                title: 'Oops...',
                                text: "You have reached the max amount of Debit Cards"
                            })
                        }
                        else if(this.alertMessage == "You already have a card with this properties" || 409 === response.response.status){
                            Swal.fire({
                                icon: 'error',
                                title: 'Oops...',
                                text: "You already have a card with this properties"
                            })
                        }  
                    })
                }
            })
        },
		logOut(){
			axios.post("/api/logout")
			.then(response =>{
				window.location.href = "/web/index.html"
			})
		},
    },
}).mount("#app")
