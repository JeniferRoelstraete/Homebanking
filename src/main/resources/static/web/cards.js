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
            console.log(error);
        })  
    }, methods: {    
        signOut() {
            axios.post('/api/logout').then(response => location.href="./index.html").catch((error) => {
                console.log(error);
            })
        }
    },
}) .mount('#app')