const {createApp} = Vue;

const app = createApp ({
    data(){
        return{
           accountType: "",
        }
    },
    methods: {
        createAccount() {
            axios.post("http://localhost:8080/api/accounts", `type=${this.accountType}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
            .then(resp => location.href="./accounts.html")
            .catch(error => {
                if (error.response && error.response.data) {
                    alertify.alert('Account creation error', error.response.data)
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
                    alertify.alert('Error signing out', error.response.data)
                } else {
                    alertify.alert('Error signing out', error.message)
                }
            })
        },
    },
})
.mount('#app')