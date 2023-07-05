const { createApp } = Vue;

const app = createApp({
    data() {
        return {
            accountSelector: "",
            originAccountNumber: "", 
            destinationAccountNumber: "", 
            transactionAmount: "",
            transactionDescription:"",
            originAccounts: [],
            destinationAccounts: [],
        }
    },
    created() {
        this.loadAccounts()
    },
    methods: {
        loadAccounts() {
            axios.get("http://localhost:8080/api/clients/current")
                .then(response => {
                    this.originAccounts = response.data.accounts.sort((account1, account2) => account1.number.localeCompare(account2.number))
                }) .catch((error) => {
                    alertify.alert('Error getting accounts', error)
            })
        },
        updateDestinationAccounts() {
            this.destinationAccounts = this.originAccounts.filter(account => account.number !== this.originAccountNumber)
        },
        createTransactionAlert() {
            console.log(
                [this.originAccountNumber,
                this.destinationAccountNumber,
                this.transactionAmount,
                this.transactionDescription]
            );
            if (!this.originAccountNumber || !this.destinationAccountNumber || +this.transactionAmount === 0 || !this.transactionDescription) {
                return
            }

            const message = `<b>Transaction details</b><br/>
            Origin account number:      ${this.originAccountNumber}<br/>
            Destination account number: ${this.destinationAccountNumber}<br/>
            Amount to be transfered:    $${this.transactionAmount}<br/>
            Transfer description:       ${this.transactionDescription}`

            alertify.confirm(
                'Are you sure that you want to create the transaction?',    // Title
                message,                                                    // Message
                () => this.createTransaction(),                             // Confirm    
                () => {})                                                   // Cancel
        },
        createTransaction() {
            const transactionDto = {
                originAccountNumber: this.originAccountNumber,
                destinationAccountNumber: this.destinationAccountNumber,
                amount: this.transactionAmount,
                description: this.transactionDescription,
            }

            axios.post("http://localhost:8080/api/client/current/transactions", transactionDto, {headers:{'content-type':'application/json'}})
            .then(response => {
                alertify.set('notifier','position', 'bottom-center')
                alertify.notify('Transaction created succesfully!', 'success')
                this.originAccountNumber = this.destinationAccountNumber = this.transactionDescription = this.accountSelector = ""
                this.transactionAmount = null
                this.loadAccounts()
            })
            .catch((error) => {
                if (error.response && error.response.data) {
                    alertify.alert('Error creating transaction', error.response.data)
                }
            })
        },
        signOut() {
                     axios.post('/api/logout')
                     .then(response => location.href="./index.html")
                     .catch((error) => {
                         if (error.response && error.response.data) {
                             alertify.alert('Error creating account', error.response.data)
                         } else {
                             alertify.alert('Error creating account', error.message)
                         }
                     })
                 },
    },
    
    },).mount('#app')