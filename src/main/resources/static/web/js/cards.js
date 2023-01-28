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
			card: []
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
				})
				.catch((error) => console.log(error));
		},
		logOut(){
			axios.post("/api/logout")
			.then(response =>{
				window.location.href = "/web/index.html"
			})
		},
		disableCard(card){
			Swal.fire({
				icon: 'question',
				title: 'Do you want to disable the card selected?',
				text: `Card ${this.card.cardType} and color ${this.card.cardColor}. This action is ireedemable`,
				showCancelButton: true,
				confirmButtonColor: '#3085d6',
				cancelButtonColor: '#d33',
				confirmButtonText: 'Yes, disable card'
			}) .then((result) => {
				if(result.isConfirmed){
					axios.patch("/api/client/current/cards/disable", `cardNumber=${this.card.cardNumber}`)
					Swal.fire('Your card was disabled successfully')
						.then(result => { window.location.reload()})
					.catch((error) => Swal.fire({
						icon: "error",
						title: "Oops...",
						text: `${error.response.data}`,
					}));
				}else {
					Swal.fire('Operation cancelled')
						.then(result => {
							window.location.reload()
						})
				}
			})
		}

	},

}).mount("#app");
