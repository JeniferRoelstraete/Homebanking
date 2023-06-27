
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
            axios.get(`http://localhost:8080/api/accounts/${idParam}`)
                .then(response => {
                   this.account = response.data
                   this.transactions = response.data.transactions.sort((transaction1, transaction2) => transaction2.id - transaction1.id)
                   console.log(this.transactions)
            }) .catch((error) => {
                console.log(error);
            })
        },
        formatDate(date) {
            return new Date(date).toLocaleString()
        },
        signOut() {
            axios.post('/api/logout').then(response => location.href="./index.html").catch((error) => {
                console.log(error);
            })
        }
    },
})
.mount('#app')