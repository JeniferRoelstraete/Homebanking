
const {createApp} = Vue;

const app = createApp ({
    data(){
        return{
            account: {},
            transactions: [],
            redColor: 'redText',
            greenColor: 'greenText'
        }
    },
    created(){
        this.loadAccount()
    },
    methods: {
        loadAccount() {
            const urlParams = new URLSearchParams(location.search);
            const idParam = urlParams.get('id');
            axios.get(`http://localhost:8080/api/clients/current`)
                .then(response => {
                   this.account = idParam != null ? response.data.accounts.find(account => account.id.toString() === idParam) : response.data.accounts[0]
                   this.transactions = this.account != undefined ? this.account.transactions.sort((transaction1, transaction2) => transaction2.id - transaction1.id) : []
            }) .catch((error) => {
                alertify.alert('Error loading transactions', error.message)
            })
        },
        formatDate(date) {
            return new Date(date).toLocaleString()
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
        }
    },
})
.mount('#app')