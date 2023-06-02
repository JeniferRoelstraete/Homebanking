
const {createApp} = Vue;

const app = createApp ({
    data(){
        return{
            jsonObject: {},
            clientsList: [],
            name: '',
            lastname: '',
            email: ''
        }
    },
    created(){
        this.loadData()
    },
    methods: {
        loadData() {
            axios.get("http://localhost:8080/clients")
                .then(response => {
                    this.jsonObject = response.data;
                    this.clientsList = response.data._embedded.clients; 
            }) .catch((error) => {
                console.log(error);
            })
        },
        addClient() {
            if (this.name !== '' && this.lastname !== '' && this.email !== ''){
                this.postClient()
            }
        },
        postClient() {
            const newClient = {
                firstName: this.name,
                lastName: this.lastname,
                email: this.email
            }
            this.name = this.lastname = this.email = ''
            axios.post("http://localhost:8080/clients", newClient)
            .then(response => {
                this.loadData()
            })
            .catch((error) => {
                console.log(error);
            });
        },
        deleteClient(clientId) {
            axios.delete(clientId)
            .then(response => {
                this.loadData()
            }) .catch((error) => {
                console.log(error);
            })
        }
    },
})
.mount ('#app')