const { createApp } = Vue

const app = createApp({
    data() {
        return {
            debitCards: [],
            creditCards:[],           
        }
    },
    created() {
        axios.get("http://localhost:8080/api/clients/current")
        .then(response => {
         this.debitCards = response.data.cards.filter(card => card.type === "DEBIT");
         this.creditCards = response.data.cards.filter(card => card.type === "CREDIT");
        }) .catch((error) => {
            alertify.alert('Error getting cards', error.message)
        })  
    }, methods: {    
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
        canCreate() {
            return this.debitCards.length < 3 || this.creditCards.length < 3
        }
    },
}) .mount('#app')