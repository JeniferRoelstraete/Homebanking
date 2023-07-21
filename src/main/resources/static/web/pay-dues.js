const {createApp} = Vue;

const app = createApp ({
    data(){
        return{
            clientLoans: [],
            accounts: [],
            clientLoanId: "",
            originAccountNumber: "",
            selectedPayment: ""
        }
    },
    created() {
        //obtener cuentas y prestamos
        this.loadAccounts()
        this.loadClientLoans()
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
        loadClientLoans() {
            axios.get("http://localhost:8080/api/clientLoans")
            .then(response => {
                this.clientLoans = response.data
            }).catch((error) => {
                alertify.alert('Error getting client loans', error)
            })
        },
        updatePaymentValue() {
            this.selectedPayment = this.clientLoans.find(clientLoan => clientLoan.clientLoanId === this.clientLoanId).pendingPayments
        },
        performPayment() {
            if (!this.clientLoanId || !this.originAccountNumber || !this.selectedPayment) {
                return
            }

            axios.post("http://localhost:8080/api/clientLoans", `originAccountNumber=${this.originAccountNumber}&clientLoanId=${this.clientLoanId}`, {headers:{'content-type':'application/x-www-form-urlencoded'}})
            .then(response => {
                this.loadAccounts()
                this.loadClientLoans()
                this.originAccountNumber = this.selectedPayment = this.clientLoanId = ''
            }).catch((error) => {
                alertify.alert('Error performing payment', error)
            })
        }
}}).mount('#app')