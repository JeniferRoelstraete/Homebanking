const { createApp } = Vue

const app = createApp({
    data() {
        return {
            debitCards: [],
            creditCards:[],           
        }
    },
    created() {
        this.loadCards();
    }, methods: {    
        loadCards() {
            axios.get("http://localhost:8080/api/clients/current")
            .then(response => {
            this.debitCards = response.data.cards.filter(card => card.type === "DEBIT");
            this.creditCards = response.data.cards.filter(card => card.type === "CREDIT");
            }) .catch((error) => {
                alertify.alert('Error getting cards', error.message)
            })
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
        canCreate() {
            return this.debitCards.length < 3 || this.creditCards.length < 3
        },
        deleteCardAction(cardNumber) {
            alertify.confirm(
                'Are you sure that you want to delete this card?',    // Title
                `Card Number: ${cardNumber}`,                         // Message
                () => this.deleteCard(cardNumber),                    // Confirm    
                () => {})                                             // Cancel
        },
        deleteCard(cardNumber) {
            axios.delete(`http://localhost:8080/api/cards/${cardNumber}`)
            .then(response => this.loadCards())
            .catch((error) => {
                if (error.response && error.response.data) {
                    alertify.alert('Error deleting card', error.response.data)
                } else {
                    alertify.alert('Error deleting card', error.message)
                }
            })
        },
        isPastDue(thruDate) {
            console.log(thruDate)
            return new Date(thruDate).getTime() < new Date().getTime();
        }
    },
}) .mount('#app')