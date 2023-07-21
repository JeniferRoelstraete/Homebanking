const { createApp } = Vue;

const app = createApp({
  data() {
    return {
      account: {},
      transactions: [],
      redColor: "redText",
      greenColor: "greenText",
      fromDate: "",
      thruDate: "",
    };
  },
  created() {
    this.loadAccount();
  },
  methods: {
    loadAccount() {
      const urlParams = new URLSearchParams(location.search);
      const idParam = urlParams.get("id");
      axios
        .get(`http://localhost:8080/api/clients/current`)
        .then((response) => {
          this.account =
            idParam != null
              ? response.data.accounts.find(
                  (account) => account.id.toString() === idParam
                )
              : response.data.accounts[0];
          this.transactions =
            this.account != undefined
              ? this.account.transactions.sort(
                  (transaction1, transaction2) =>
                    transaction2.id - transaction1.id
                )
              : [];
        })
        .catch((error) => {
          alertify.alert("Error loading transactions", error.message);
        });
    },
    formatDate(date) {
      return new Date(date).toLocaleString("es-AR", {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2,
      });
    },
    signOut() {
      axios
        .post("/api/logout")
        .then((response) => (location.href = "./index.html"))
        .catch((error) => {
          if (error.response && error.response.data) {
            alertify.alert("Error signing out", error.response.data);
          } else {
            alertify.alert("Error signing out", error.message);
          }
        });
    },
    filterAndPrintTransactions(printPDF) {
      if (!this.fromDate || !this.thruDate) {
        return;
      }

      const responseType =  printPDF ? { responseType: 'blob' } : {};
      axios
        .get(
          `http://localhost:8080/api/transactions?accountNumber=${this.account.number}&fromDate=${this.fromDate}&thruDate=${this.thruDate}&printPDF=${printPDF}`,
          responseType
        )
        .then((response) => {
          if (printPDF) {
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement("a");
            link.href = url;
            link.setAttribute(
              "download",
              `Transacciones de cuenta ${this.account.number} del ${this.fromDate} al ${this.thruDate}.pdf`
            );
            document.body.appendChild(link);
            link.click();

            /* <body>
                  <a href="direccion_descarga" download="Transacciones de cuenta VIN-12312 del fecha a fecha.pdf"></a>
               </body> */
          } else {
            this.transactions = response ? response.data.sort((transaction1, transaction2) => transaction2.id - transaction1.id) : [];
          }
        })
        .catch((error) => {
          if (error.response && error.response.data) {
            alertify.alert("Error getting transactions", error.response.data);
          } else {
            alertify.alert("Error getting transactions", error.message);
          }
        });
    },
  },
}).mount("#app");
