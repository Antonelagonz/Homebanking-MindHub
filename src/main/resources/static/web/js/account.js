const { createApp } = Vue;

createApp({
	data() {
		return {
			json: [],
			data: [],
			transactionData: [],
			queryString: undefined,
			dateFrom:'',
			dateTo:'',
			numberAccount:'',
			idAccount:undefined,
			accountNumber: [],
			accountsData: {},
			allCards: [],
			cards: [],
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
			axios.get("http://localhost:8080/api/accounts/" + id)
				.then((response) => {
					this.json = response.data;
					this.transactionData = this.json.transactions;
					this.accountsData = this.json.accounts;
					this.numberAccount = this.json.number
					console.log(this.numberAccount);

					//this.numberAccount = this.accounts.map(accounts => accounts.numberAccount)
					/*this.accounts.sort((a, b) => {
						if (a.id > b.id) {
							return -1;
						}
					});*/
					//this.allCards = this.json.cards.sort((a,b) => a.id - b.id);
					this.cards = this.allCards.filter(card => card.enabled === true);
					this.creditCards = this.cards.filter(type => type.cardType === 'CREDIT');
					this.debitCards = this.cards.filter(type => type.cardType === 'DEBIT');					
					this.transactionData.sort((a, b) => {
						if (a.id > b.id) {
							return -1;
						}
					});
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
        downloadPDF() {
            axios({
                method: 'post',
                url: 'http://localhost:8080/api/downloadPDF',
                responseType: 'blob',
                headers: { 'Content-Type': 'application/json' },
                data: JSON.stringify(this.transactionData)
            })
            .then(response => {
            const url = window.URL.createObjectURL(new Blob([response.data]));
            
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', 'accountInfo.pdf');
            document.body.appendChild(link);
            link.click();
            Swal.fire({			
				icon: 'success',
				title: 'Success',
				text: 'Your pdf was downloaded successfully'
			})                       
                .then(result => {
                    window.location.reload()
                })
            })
				.catch(error => {
					this.error = error.response.data
					Swal.fire('Failed download PDF', 'error')
			});         
        },
	},
    /*computed: {
        filterTransactions() {

            let lastxdays;

            switch (this.selectedFilter) {
                case '7':
                    lastxdays = new Date(new Date().setDate(new Date().getDate() - 7))
                    break;
                case '15':
                    lastxdays = new Date(new Date().setDate(new Date().getDate() - 15))
                    break;
                case '30':
                    lastxdays = new Date(new Date().setDate(new Date().getDate() - 30))
                    break;
                case '365':
                    lastxdays = new Date(new Date().setDate(new Date().getDate() - 365))
                    break;
                case 'all':
                    lastxdays = new Date((2022, 11, 01, 0, 0, 0, 0));
                    break;
            }
            lastxdays = lastxdays.toISOString().slice(0, -1);
            lastxdays = lastxdays.replace('T', ' ');
            const urlParams = new URLSearchParams(window.location.search);
            const id = urlParams.get('id');
            if (lastxdays == 0) {
                lastxdays = '';
                this.thru_date = '';
            }
            axios.get(`http://localhost:8080/api/accounts/${id}`, {
                params: {
                    from_date: lastxdays + '000',
                    thru_date: this.today + '000'
                }
            })
                .then(accountData => {
                    console.log(accountData)
                    this.account = accountData.data;
                    this.transactions = this.account.transactions;
                    this.transactions = this.account.transactions.sort((a, b) => a.date - b.date)
                })
                .catch(err => console.log(err))


        },
			filterByExactDate() {
            if (this.date) {
                let from_date = this.date + ' 00:00:00.398000'
                let thru_date = this.date + ' 23:59:59.000000'

                const urlParams = new URLSearchParams(window.location.search);
                const id = urlParams.get('id');

                axios.get(`http://localhost:8080/api/accounts/${id}`, {
                    params: {
                        from_date,
                        thru_date
                    }
                })
                    .then(accountData => {
                        console.log(accountData)
                        this.account = accountData.data;
                        this.transactions = this.account.transactions;
                        this.transactions = this.account.transactions.sort((a, b) => a.date - b.date)
                    })
                    .catch(err => console.log(err))

            }
        }
    }*/

}).mount("#app");
