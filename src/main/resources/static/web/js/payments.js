const { createApp } = Vue;

createApp({
	data() {
		return {
			json: [],
			data: [],
			cards: [],
			allCards: [],
			queryString:"",
			creditCards: [],
			debitCards: [],
			card: [],
			selectedAccountDestiny: '',
			selectedDescription: '',
			selectedCard: [],
			selectedAccount: '',
			selectedAmount: 0,
			selectedCVV: 0,
			accountsData: {}
		};
	},
	created() {
		this.loadClient();
	},
	methods: {
		loadClient() {
			this.queryString = location.search;
			let params = new URLSearchParams(this.queryString);
			let id = params.get("id")
			axios.get("/api/client/current")
				.then((response) => {
					this.json = response.data;
					
					console.log(this.json);
					this.allCards = this.json.cards.sort((a,b) => a.id - b.id);
					this.cards = this.allCards.filter(card => card.enabled === true);
					this.creditCards = this.cards.filter(type => type.cardType === 'CREDIT');
					this.debitCards = this.cards.filter(type => type.cardType === 'DEBIT');
					this.allAcccounts = this.json.accounts.sort((a,b) => a.id - b.id);
					this.accountsData = this.allAcccounts.filter(account => account.enabled === true);
					console.log(this.accountsData);
				})
				.catch((error) => console.log(error));
		},
		logOut(){
			axios.post("/api/logout")
			.then(response =>{
				window.location.href = "/web/index.html"
			})
		},
        confirmPayment() {
            Swal.fire({
                title: '¿Do you want to make the payment?',
                showDenyButton: true,
                // showCancelButton: true,
                confirmButtonText: 'Accept',
                denyButtonText: `Cancel`,
            }).then((result) => {
                if (result.isConfirmed) {
					console.log(this.selectedCard);
					console.log(this.selectedCVV);
					console.log(this.selectedAmount);
					console.log(this.selectedDescription);
                    if (this.selectedAmount > 0 || this.selectedDescription != "" || this.selectedCard != "" || (this.selectedCVV > 0 && this.selectedCVV < 1000) ) {
                        axios.post('/api/payments', `cardNumber=${this.selectedCard}&cvv=${this.selectedCVV}&amount=${this.selectedAmount}&description=${this.selectedDescription}`)
                            .then(response => {
                                Swal.fire('Transaction Success', '', 'success')
                                    .then(result => {
                                        window.location.reload()
                                    })
                            }).catch(error => {
                                this.error = error.response.data
                                Swal.fire('Transaction Failed', this.error, 'error')
                                    .then(result => {
                                        window.location.reload()
                                })
                            })
                    } else {
                        Swal.fire('Transaction Failed', 'Complete all the fields')
                                    .then(result => {
                                        window.location.reload()
                                    })

                    }
                    
                    
                    
                } else if (result.isDenied) {
                    Swal.fire('Cancel transaction', '', 'error')
                }
            })
    
        }

	},

}).mount("#app");
