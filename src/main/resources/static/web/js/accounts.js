const { createApp } = Vue;

createApp({
	data() {
		return {
			json: [],
			data: [],
			allAccounts:[],
			accountsData: [],
			queryString:"",
			loans: [],
			account: [],
			accountTypeInput: null,
			SAVINGS: 'SAVINGS',
			CURRENT: 'CURRENT'
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
					this.allAccounts = this.json.accounts.sort((a,b) => a.id - b.id)
					this.accountsData = this.allAccounts.filter(account => account.enabled === true);
					console.log(this.accountsData);
					console.log(this.allAccounts);
					this.loans = this.json.loans;
					this.accountsData.sort((a, b) => {
						if (a.id < b.id) {
							return -1;
						}
					});
				})
				.catch((error) => console.log(error));
		},
		logOut(){
			axios.post("/api/logout")
			.then(response =>{
				window.location.href = "/web/index.html"
			})
		},
		createAccount(){
			Swal.fire({
				icon: 'question',
				title: 'Confirm new account',
				text: "You're going to create a new account. Are you sure?",
				showCancelButton: true,
				confirmButtonColor: '#3085d6',
				cancelButtonColor: '#d33',
				confirmButtonText: 'Yes, create new account'
			})
			.then((result) => {
				if(result.isConfirmed){
					axios.post('/api/client/current/accounts',`accountType=${this.accountTypeInput}`)
					.then(response => {
						Swal.fire({
							didOpen: (e) => {
								document.querySelector(".swal2-confirm").addEventListener("click", () => {
									window.location.reload();
								})
							},
							icon: 'success',
							title: 'Success',
							text: 'Your account was created successfully'
						})
			})
			.catch((error) => console.log(error));
			}
            })
        },
		disableAccount(account){
			Swal.fire({
				icon: 'question',
				title: 'Do you want to disable this account?',
				text: `Account number: ${this.account.number}. This action is ireedemable`,
				showCancelButton: true,
				confirmButtonColor: '#3085d6',
				cancelButtonColor: '#d33',
				confirmButtonText: 'Yes, disable account'
			}).then((result) => {
				if(result.isConfirmed){
					axios.patch('/api/client/current/accounts/disable', `number=${this.account.number}`)
					Swal.fire('Your account was disabled successfully')
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
