
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
        alertify.set('notifier','position', 'top-center')
        alertify.notify('Sign in successful', 'success', 3)
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
                alertify.alert('Error in sign out', error.message)
            })
        },
        createAccount(){
            axios.post('http://localhost:8080/api/clients/current/accounts')
            .then(response => {
                    this.loadAccounts()
            })
            .catch((error) => {
                if (error.response && error.response.data) {
                    alertify.alert('Error creating account', error.response.data)
                } else {
                    alertify.alert('Error creating account', error.message)
                }
            })
        },
        canCreate() {
            return this.accounts.length < 3
        }
    },
})
.mount('#app')