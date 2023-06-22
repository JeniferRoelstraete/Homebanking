const { createApp } = Vue

const app = createApp({
    data() {
        return {
            debitCards: [],
            creditCards:[],


            
        }
    },
    created() {
        axios.get("http://localhost:8080/api/clients/1")
        .then(response => {
         this.debitCards = response.data.cards.filter(card => card.type === "DEBIT");
         this.creditCards = response.data.cards.filter(card => card.type === "CREDIT");
        }) .catch((error) => {
        console.log(error);
    })
        
    },
}) .mount('#app')