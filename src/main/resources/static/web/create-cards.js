const {createApp} = Vue;

const app = createApp ({
    data(){
        return{
           cardColor: "",
           cardType: "",
        }
    },
    methods: {
        createCard() {
            axios.post("http://localhost:8080/api/clients/current/cards", `type=${this.cardType}&color=${this.cardColor}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
            .then(resp =>  location.href="./cards.html")
            .catch(error => {
                if (error.response && error.response.data) {
                    alertify.alert('Card creation error', error.response.data)
                } else {
                    alertify.alert('Error creating account', error.message)
                }
            })
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
                },
    },
})
.mount('#app')