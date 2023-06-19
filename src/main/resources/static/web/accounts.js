
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
        this.loadClients()
    },
    methods: {
        loadClients() {
            axios.get("http://localhost:8080/api/clients/1")
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
        }
    },
})
.mount('#app')