const { createApp } = Vue;

createApp({
	data() {
		return {
			json: {},
			data: [],
			transactions: [],
			accounts: {},
			transferCheck: "",
			inputFromAccount: "",
			accountDestiny: "",
			inputAmount: "",
			inputDescription: "",
			numberAccount: "",
			alertMessage: ""
		};
	},
	created() {
		this.loadClient();
	},
	methods: {
		loadClient() {
			axios.get("/api/client/current")
				.then((response) => {
					this.json = response.data;
					this.accounts = this.json.accounts;
					this.transactions = this.json.transactions;
					this.accounts.sort((a, b) => {
						if (a.id > b.id) {
							return -1;
						}
					});
				})
				.catch((error) => console.log(error));
		},
		logOut(){
			axios.post("/api/logout")
			.then(response =>{
			Swal.fire({
                    icon: 'success',
                    title: 'Success',
                    text: 'You have logged-out successfully',
                    showConfirmButton: false,
                    timer: 2500
				}),
				window.location.href = "/web/index.html"
			})
		},
		createTransfer(){
			console.log(this.inputFromAccount);
			console.log(this.accountDestiny);
			console.log(this.inputAmount);
			console.log(this.inputDescription);
			Swal.fire({
				title: "Confirm the transaction:",
				text: "This action is irredeemable",
				icon: "warning",
				showCancelButton: true,
				confirmButtonColor: '#3085d6',
				cancelButtonColor: '#d33',
				confirmButtonText: 'Yes, do the transactions'
			})
			.then((result) => {
				if(result.isConfirmed){
					axios.post('/api/transactions',`fromAccountNumber=${this.inputFromAccount}&toAccountNumber=${this.accountDestiny}&amount=${this.inputAmount}&description=${this.inputDescription}`)
					.then(response => {
						Swal.fire({
							didOpen: (e) => {
								document.querySelector(".swal2-confirm").addEventListener("click", () => {
									window.location.reload();
								})
							},
							icon: 'success',
							title: 'Success',
							text: 'Your transfer has been sent successfully'
						})
					})
					.catch(response => {
						this.alertMessage = response.response.data
						if(this.alertMessage == "Missing amount"){
							Swal.fire({
								icon: 'error',
								title: 'Oops...',
								text: "Missing amount",
							})
						} else if(this.alertMessage == "Missing description"){
							Swal.fire({
								icon: 'error',
								title: 'Oops...',
								text: "Missing description"
							})
						}else if(this.alertMessage == "Missing origin account"){
							Swal.fire({
								icon: 'error',
								title: 'Oops...',
								text: "Missing origin account"
						})
						}else if(this.alertMessage == "Missing destiny Account"){
							Swal.fire({
							icon: 'error',
							title: 'Oops...',
							text: "Missing destiny Account"
						})
						} else if(this.alertMessage == "Destination account can't be the same as the origin account"){
							Swal.fire({
							icon: 'error',
							title: 'Oops...',
							text: "Destination account can't be the same as the origin account"
							})
						} else if(this.alertMessage == "Your origin account doesn't exist"){
							Swal.fire({
							icon: 'error',
							title: 'Oops...',
							text: "Your origin account doesn't exist"
						})
						}  else if(this.alertMessage == "Your destiny account doesn't exist"){
							Swal.fire({
							icon: 'error',
							title: 'Oops...',
							text: "Your destiny account doesn't exist"
						})
						}  else if(this.alertMessage == "Not enough balance"){
							Swal.fire({
							icon: 'error',
							title: 'Oops...',
							text: "Not enough balance"
						})
						}
					})				
				}
			})

	}
},
	computed: {
		filterAccounts(){
			this.selectedAccounts = this.numberAccount.filter( accounts => accounts !== this.inputFromAccount)
		}
	}
}).mount("#app");
