const { createApp } = Vue;

createApp({
	data() {
		return {
			loans: [],
			data: [],
			client: [],
			firstName: "",
			lastName: "",
			numberAccount: "",
			accounts: [],
			loanTypes: [],
			maxAmount: [],
			loanPayments: [],
			selectedAmount: undefined,
			selectedLoanID: undefined,
			selectedLoan:undefined,
			selectedAccount: undefined,
			selectedPayments: undefined,
			amount: "",
			alertMessage: ""
		};
	},
	created() {
		this.loadLoans();
		this.loadCurrentClient();
	},
	methods: {
		loadLoans() {
			axios.get("/api/loans")
				.then((response) => {
					this.loans = response.data;
					this.selectedLoan = this.loans;
					this.loanTypes = this.loans.map(loan => loan.loanName);
					this.loanPayments = this.loans.map(loan => loan.payments);
					console.log(this.loans);
					console.log(this.loanPayments);
				})
				.catch((error) => console.log(error));
		},
		loadCurrentClient(){
			axios.get("/api/client/current")
			.then((response) => {
				this.client = response.data
				this.accounts = this.client.accounts
				this.numberAccount = this.accounts.map(accounts => accounts.number)
				this.accounts.sort((a, b) => {
					if (a.id > b.id) {
						return -1;
					}
				});
			})
			.catch((error) => {
				console.log(error);
			})
		},
		logOut(){
			axios.post("/api/logout")
			.then(response =>{
				window.location.href = "/web/index.html"
			})
		},
		createLoanApplication(){
			let loanBody = {
				loanId: this.selectedLoan.id,
				amount: this.selectedAmount,
				payments: this.selectedPayments,
				toAccountNumber: this.selectedAccount
			}
			console.log(this.selectedLoan);
			console.log(this.selectedPayments);
			console.log(this.selectedAmount);
			console.log(this.selectedAccount);
			Swal.fire({
				title: "Confirm the loan solicitude",
				text: "This action is irredeemable",
				icon: "warning",
				showCancelButton: true,
				confirmButtonColor: '#3085d6',
				cancelButtonColor: '#d33',
				confirmButtonText: 'Yes, get the loan'
			})
			.then((result) => {
				if(result.isConfirmed){
					axios.post('/api/loans', loanBody)
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
						console.log(this.alertMessage);
						if(this.alertMessage == "Wrong Client"){
							Swal.fire({
								icon: 'error',
								title: 'Wrong Client',
								text: "Missing client data",
							})
						} else if(this.alertMessage == "Missing Amount"){
							Swal.fire({
								icon: 'error',
								title: 'Oops...',
								text: "Missing Amount"
							})
						}else if(this.alertMessage == "Missing Payments"){
							Swal.fire({
								icon: 'error',
								title: 'Oops...',
								text: "Missing Payments"
						})
						}else if(this.alertMessage == "Loan Doesn't Exist"){
							Swal.fire({
							icon: 'error',
							title: 'Oops...',
							text: "Missing loan data"
						})
						} else if(this.alertMessage == "The minimum amount for a loan is $50000"){
							Swal.fire({
							icon: 'error',
							title: 'The minimum amount for a loan is $50000',
							text: ""
							})
						}  else if(this.alertMessage == "Amount Greater than Max Amount"){
							Swal.fire({
							icon: 'error',
							title: 'Amount Greater than Max Amount',
							text: ""
							})
						}  else if(this.alertMessage == "Destiny Account Doesn't Exist"){
							Swal.fire({
							icon: 'error',
							title: 'Oops..',
							text: " Destiny Account Doesn't Exist"
							})
						}  else if(this.alertMessage == "Client already has loan of this type"){
							Swal.fire({
							icon: 'error',
							title: 'Oops..',
							text: "Client already has loan of this type"
							})
						} else if(this.alertMessage == "Client does not contain account selected"){
							Swal.fire({
							icon: 'error',
							title: 'Oops..',
							text: "Client does not contain account selected"
							})
						}  else if(this.alertMessage == "The payments you select exceeds the max amount of payments"){
							Swal.fire({
							icon: 'error',
							title: 'Oops..',
							text: "The payments you select exceeds the max amount of payments"
							})
						} else if(this.alertMessage == "Client has reached the max limit of loans"){
							Swal.fire({
							icon: 'error',
							title: 'Oops..',
							text: "Client has reached the max limit of loans"
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
			console.log(this.selectedLoan);
		},
		filterLoans(){
			this.selectedLoan = this.loans.find( loans => loans.id == this.selectedLoanID)
			if(this.selectedLoan){
				this.loanPayments = this.selectedLoan.payments
				console.log(this.selectedLoan.maxAmount);
				console.log(this.selectedLoan.id)
			}
		}
	}
}).mount("#app");
