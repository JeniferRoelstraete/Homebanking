
const {createApp} = Vue;

const app = createApp ({
    data(){
        return{
            client: {},
            accounts: [],
            loans: []
        }
    },
    created(){
        this.loadAccounts()
    },
    methods: {
        loadAccounts() {
            axios.get("http://localhost:8080/api/clients/current")
                .then(response => {
                    this.client = response.data;
                    this.accounts = response.data.accounts.sort((account1, account2) => account1.number.localeCompare(account2.number));
                    this.loans = response.data.loans;
                }) .catch((error) => {
                    alertify.alert('Error getting accounts', error)
            })
        },
        formatDate(date) {
            return new Date(date).toLocaleDateString()
        },
        signOut() {
            axios.post('/api/logout')
            .then(response => location.href="./index.html")
            .catch((error) => {
                if (error.response && error.response.data) {
                    alertify.alert('Error signing out', error.response.data)
                } else {
                    alertify.alert('Error signing out', error.message)
                }
            })
        },
        createAccount(){
            location.href="./create-account.html"
        },
        canCreate() {
            return this.accounts.length < 3
        },
        deleteAccountAction(accountNumber) {
            alertify.confirm(
                'Are you sure that you want to delete this account?',    // Title
                `Account Number: ${accountNumber}`,                         // Message
                () => this.deleteAccount(accountNumber),                    // Confirm    
                () => {})                                             // Cancel
        },
        deleteAccount(accountNumber) {
            axios.delete(`http://localhost:8080/api/accounts/${accountNumber}`)
            .then(response => this.loadAccounts())
            .catch((error) => {
                if (error.response && error.response.data) {
                    alertify.alert('Error deleting account', error.response.data)
                } else {
                    alertify.alert('Error deleting account', error.message)
                }
            })
        },
    },
})
.mount('#app')