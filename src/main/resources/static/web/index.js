const { createApp } = Vue;

const app = createApp({
    data() {
        return {
            firstName: '',
            lastName: '',
            email: '',
            password: ''
        }
    },
    methods: {
        signIn() {
            axios
            .post('/api/login',`email=${this.email}&password=${this.password}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
            .then(response => {
                this.email = this.password = ''
                location.href="./accounts.html"
            })
            .catch(error => {
                if (error.response) {
                    if (error.response.status === 401) {
                        alertify.alert('Sign in error', 'Email or password incorrect')
                    } else {
                        alertify.alert('Sign in error', error.response.message);
                    }
                } else if (error.request) {
                    alertify.alert('Sign in error', 'Server error')
                } else {
                    alertify.alert('Sign in error', error.message)
                }
            })
        },
        register() {
            axios.post('/api/clients',`firstName=${this.firstName}&lastName=${this.lastName}&email=${this.email}&password=${this.password}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
            .then(response => {
                alertify.set('notifier','position', 'top-center')
                alertify.notify('Registration succesful', 'success', 7)
                this.signIn()
                this.firstName = this.lastName = this.email = this.password = ''
            })
            .catch(error => {
                if (error.response && error.response.data) {
                    alertify.alert('Registration error', error.response.data)
                } else {
                    alertify.alert('Error creating account', error.message)
                }
            })
        }
    },
}).mount('#app')