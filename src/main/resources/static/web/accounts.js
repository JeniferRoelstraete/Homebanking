
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
        alertify.notify('Sign in successful', 'success', 10)
        this.loadClients()
    },
    methods: {
        loadClients() {
            axios.get("http://localhost:8080/api/clients/current")
                .then(response => {
                    this.client = response.data;
                    this.accounts = response.data.accounts.sort((account1, account2) => account1.number.localeCompare(account2.number));
                    this.loans = response.data.loans;
                    console.log(this.client);
                }) .catch((error) => {
                console.log(error);
            })
        },
        formatDate(date) {
            return new Date(date).toLocaleDateString()
        },
        signOut() {
            axios.post('/api/logout')
            .then(response => location.href="./index.html")
            .catch((error) => {
                console.log(error);
            })
        }
    },
})
.mount('#app')