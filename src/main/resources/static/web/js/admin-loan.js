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
            payments: [6, 12, 18, 24, 30, 36, 42, 48],
			selectedAmount: undefined,
			selectedLoanName: '',
			selectedLoan:undefined,
			selectedAccount: undefined,
			selectedPayments: [],
			interestPercentaje: undefined,
			amount: "",
			alertMessage: ""
		};
	},
	created() {
		this.loadLoans();
	},
	methods: {
		loadLoans() {
			axios.get("/api/loans")
				.then((response) => {
					this.loans = response.data;
					this.selectedLoan = this.loans;
					this.loans.sort((a, b) => a.id - b.id)

				})
				.catch((error) => console.log(error));
		},
		logOut(){
			axios.post("/api/logout")
			.then(response =>{
				window.location.href = "/web/index.html"
			})
		},
		createLoan(){
/* 			let loanBody = {
				maxAmount: this.selectedAmount,
				payments: this.selectedPayments,
				loanName: this.selectedLoanName,
			} */
			Swal.fire({
				title: "Confirm the loan creation",
				text: "This action is irredeemable",
				icon: "warning",
				showCancelButton: true,
				confirmButtonColor: '#3085d6',
				cancelButtonColor: '#d33',
				confirmButtonText: 'Yes,  the loan'
			})
			.then((result) => {
				if(result.isConfirmed){
					axios.post('/api/loans/create',`maxAmount=${this.selectedAmount}&payments=${this.selectedPayments}&loanName=${this.selectedLoanName}`)
					.then(response => {
						Swal.fire({
							didOpen: (e) => {
								document.querySelector(".swal2-confirm").addEventListener("click", () => {
									window.location.reload();
								})
							},
							icon: 'success',
							title: 'Success',
							text: 'The loan has been created successfully'
						})
					})
					.catch(response => {
						this.alertMessage = response.response.data
						console.log(this.alertMessage);
						if(this.alertMessage == "The loan name input is empty."){
							Swal.fire({
								icon: 'error',
								title: 'Oops...',
								text: "The loan name input is empty.",
							})
						} else if(this.alertMessage == "The amount input is empty"){
							Swal.fire({
								icon: 'error',
								title: 'Oops...',
								text: "The amount input is empty"
							})
						}else if(this.alertMessage == "The amount input is empty."){
							Swal.fire({
								icon: 'error',
								title: 'Oops...',
								text: "The amount input is empty."
						})
						}else if(this.alertMessage == "One or more payments are 0"){
							Swal.fire({
							icon: 'error',
							title: 'Oops...',
							text: "One or more payments are 0"
						})
						}
					})
				}
			})
	}
},
}).mount("#app");
