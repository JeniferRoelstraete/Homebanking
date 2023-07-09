const {createApp} = Vue;

const app = createApp({

    data() {
        return {
            loanTypes: [],
            accounts: [],
            payments: [],
            loanTypeId: "",
            destinationAccountNumber: "",
            amount: "",
            selectedPayment: ""
        }
    },
    created() {
        //obtener cuentas y prestamos
        this.loadAccounts()
        this.loadLoans()
    },
    methods: {
        loadAccounts() {
            axios.get("http://localhost:8080/api/clients/current")
            .then(response => {
                this.accounts = response.data.accounts.sort((account1, account2) => account1.number.localeCompare(account2.number))
            }) .catch((error) => {
                alertify.alert('Error getting accounts', error)
            })
        },
        loadLoans() {
            axios.get("http://localhost:8080/api/loans")
            .then(response => {
                this.loanTypes = response.data
            }).catch((error) => {
                alertify.alert('Error getting accounts', error)
            })
        },
        updatePaymentsList() {
            this.payments = this.loanTypes.find(loan => loan.id === this.loanTypeId).payments
            this.selectedPayment = ""
        },
        createLoanAlert() {
            if (!this.loanTypeId || !this.destinationAccountNumber || +this.amount === 0 || !this.selectedPayment) {
                return
            }

            const message = `<b>Loan application details</b><br/>
            Payment type:        ${this.loanTypes.find(loan => loan.id === this.loanTypeId).name}<br/>
            Destination account: ${this.destinationAccountNumber}<br/>
            Loan amount:         $${this.amount.toLocaleString('es-AR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}<br/>
            Payments:            ${this.selectedPayment}`

            alertify.confirm(
                'Are you sure that you want to apply loan?', // Title
                message,                                     // Message
                () => this.createLoan(),              // Confirm    
                () => {})                                    // Cancel
        },
        createLoan() {
            const loanApplicationDTO = {
                id: this.loanTypeId,
                destinationAccount: this.destinationAccountNumber,
                amount: this.amount,
                payments: this.selectedPayment,
            }

            axios.post("http://localhost:8080/api/loans", loanApplicationDTO, {headers:{'content-type':'application/json'}})
            .then(response => {
                alertify.set('notifier','position', 'bottom-center')
                alertify.notify('Loan applied succesfully!', 'success')
                this.loanTypeId = this.destinationAccountNumber = this.amount = this.selectedPayment = ""
                this.loadAccounts()
            })
            .catch((error) => {
                if (error.response && error.response.data) {
                    alertify.alert('Error creating loan', error.response.data)
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
        applyLoan() {

        } //mensaje qe salio bien y y redicional
    }
}).mount('#app')