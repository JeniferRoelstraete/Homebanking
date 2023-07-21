package com.mindhub.homebanking;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HomebankingApplication {
	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}


	/*@Autowired
	private PasswordEncoder passwordEnconder;*/
	/*@Bean
	public CommandLineRunner initData(ClientRepository clientRepository,
									  AccountRepository accountRepository,
									  TransactionRepository transactionRepository,
									  LoanRepository loanRepository,
									  ClientLoanRepository clientLoanRepository,
									  CardRepository cardRepository) {
		return args -> {
*/
			/*Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEnconder.encode("3333"));
			Client client2 = new Client("Juan", "Bili", "jbili@mindhub.com", passwordEnconder.encode("3675"));
 			Client admin = new Client("admin","admin","admin@gmail.com", passwordEnconder.encode("123"));

			Account account1 = new Account("VIN001", LocalDate.now(), 9000.00);
			Account account2 = new Account("VIN002", LocalDate.now().plusDays(1), 3000.00);
			Account account3 = new Account("VIN003", LocalDate.now().plusDays(2), 1000.00);
			Account account4 = new Account("VIN004", LocalDate.now().plusDays(2), 6000.00);

			Transaction transaction1 = new Transaction(TransactionType.CREDITO, 15000.00, LocalDateTime.now(),"Deposit money into account");
			Transaction transaction2 = new Transaction(TransactionType.DEBITO, -8000.00, LocalDateTime.now(), "Payment to Suppliers");
			Transaction transaction3 = new Transaction(TransactionType.CREDITO, 2000.00, LocalDateTime.now(), "Reimbursement");
			Transaction transaction4 = new Transaction(TransactionType.DEBITO, -2500.00, LocalDateTime.now(), "Car insurance");
			Transaction transaction5 = new Transaction(TransactionType.CREDITO, 7500.00, LocalDateTime.now(), "Deposit money into account");
			Transaction transaction6 = new Transaction(TransactionType.DEBITO, -2000.00, LocalDateTime.now(), "Light tax payment");

            Loan loan1 = new Loan("Mortgage", 500000, List.of(12,24,36,48,60));
            Loan loan2 = new Loan("Personal", 100000, List.of(6,12,24));
            Loan loan3 = new Loan("Automotive", 300000, List.of(6,12,24,36));



			ClientLoan clientLoan1 = new ClientLoan(client1, loan1, 400000, 60);
			ClientLoan clientLoan2 = new ClientLoan(client1, loan2, 50000, 12);
			ClientLoan clientLoan3 = new ClientLoan(client2, loan2, 100000, 24);
			ClientLoan clientLoan4 = new ClientLoan(client2, loan3, 200000, 36);

			Card card1 = new Card(client1.getFirstName() + " " + client1.getLastName(), CardType.DEBIT,CardColor.GOLD,
					"6767-4533-2689-2367", (short) 214,LocalDate.now(), LocalDate.now().plusYears(5));

			Card card2 =new Card(client1.getFirstName() + " " + client1.getLastName(), CardType.CREDIT,CardColor.TITANIUM,
					"1298-0773-2329-2547", (short) 969,LocalDate.now(), LocalDate.now().plusYears(5));

			Card card3 =new Card(client2.getFirstName() + " " + client2.getLastName(), CardType.CREDIT,CardColor.SILVER,
					"8978-2273-2780-4437", (short) 444,LocalDate.now(), LocalDate.now().plusYears(5));


			account1.addTransaction(transaction1);
			account1.addTransaction(transaction2);
			account1.addTransaction(transaction3);
			account2.addTransaction(transaction4);
			account2.addTransaction(transaction5);
			account2.addTransaction(transaction6);

			client1.addAccount(account1);
			client1.addAccount(account2);
 			client2.addAccount(account3);
			client2.addAccount(account4);

			client1.addCard(card1);
			client1.addCard(card2);
			client2.addCard(card3);


			clientRepository.save(client1);
			clientRepository.save(client2);
			clientRepository.save(admin);

			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			accountRepository.save(account4);

			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);
			transactionRepository.save(transaction6);

			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);

			clientLoanRepository.saveAll(List.of(clientLoan1, clientLoan2, clientLoan3, clientLoan4));

			cardRepository.saveAll(List.of(card1,card2,card3));
*/
	/*	};
	}
*/
}
