package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.*;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initData(ClientServices clientServices, AccountServices accountServices, TransactionServices transactionServices, LoanServices loanServices, ClientLoanServices clientLoanServices, CardServices cardServices) {
		return (args) -> {
			Client Melba = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("654321"));
			clientServices.saveClient(Melba);

			// asigna la informacion del cliente en este caso melba
			Client Antonela = new Client("Antonela", "Gonzalez", "antonela@gmail.com", passwordEncoder.encode("123456"));
			clientServices.saveClient(Antonela);

			Client Admin = new Client("Admin", "Admin", "admin@admin.com", passwordEncoder.encode("123456"));
			clientServices.saveClient(Admin);
			Account account1 = new Account("VIN-001", LocalDateTime.now(), 5000, Melba, true, AccountType.CURRENT);
			Account account2 = new Account("VIN-002", LocalDateTime.now().plusDays(1), 7500, Melba, true, AccountType.SAVINGS);
			Account account3 = new Account("VIN-003",  LocalDateTime.now(), 10, Antonela, true, AccountType.SAVINGS);
			Account account4 = new Account("VIN-004", LocalDateTime.now(), 100, Admin, true, AccountType.SAVINGS);
			accountServices.saveAccount(account1);
			accountServices.saveAccount(account2);
			accountServices.saveAccount(account3);
			Transaction transaction1 = new Transaction(TransactionType.DEBIT, 520, "Bills", LocalDateTime.now(), account1);
			transactionServices.saveTransaction(transaction1);
			Transaction transaction2 = new Transaction(TransactionType.CREDIT, 51000, "Rent", LocalDateTime.now().plusDays(2),account1);
			transactionServices.saveTransaction(transaction2);
			Transaction transaction3 = new Transaction(TransactionType.DEBIT, 300, "Farmacity", LocalDateTime.now().plusDays(1), account1);
			transactionServices.saveTransaction(transaction3);
			Transaction transaction4 = new Transaction(TransactionType.DEBIT, 6000, "honorarios", LocalDateTime.now(), account2);
			transactionServices.saveTransaction(transaction4);


			Loan mortgageLoan = new Loan(500000, List.of(12,24,36,48,60), "Mortgage Loan");
			loanServices.saveLoan(mortgageLoan);
			Loan personalLoan = new Loan(100000, List.of(6,12,24), "Personal Loan");
			loanServices.saveLoan(personalLoan);
			Loan carLoan = new Loan(300000, List.of(6,12,24,36), "Car Loan");
			loanServices.saveLoan(carLoan);

			ClientLoan clientLoan1 = new ClientLoan(400000,60, LocalDateTime.now(), Melba, mortgageLoan);
			clientLoanServices.saveClientLoan(clientLoan1);
			ClientLoan clientLoan2 = new ClientLoan(50000, 12, LocalDateTime.now(), Melba, personalLoan);
			clientLoanServices.saveClientLoan(clientLoan2);
			ClientLoan clientLoan3 = new ClientLoan(100000, 24, LocalDateTime.now(), Antonela, personalLoan);
			clientLoanServices.saveClientLoan(clientLoan3);
			ClientLoan clientLoan4 = new ClientLoan(200000, 36, LocalDateTime.now(), Antonela, carLoan);
			clientLoanServices.saveClientLoan(clientLoan4);

			Card card1 = new Card(Melba.getFirstName() + " " + Melba.getLastName(), Melba, CardType.DEBIT, CardColor.GOLD, "0123 4567 8901 6666", 444, LocalDate.now(), LocalDate.now().plusYears(5), true, account1);
			cardServices.saveCard(card1);
			Card card2 = new Card(Melba.getFirstName() + " " + Melba.getLastName(), Melba, CardType.CREDIT, CardColor.TITANIUM, "0123 4567 8901 7777", 888, LocalDate.now(), LocalDate.now().plusYears(5), true, account1);
			cardServices.saveCard(card2);
			Card cardSilver = new Card(Melba.getFirstName() + " " + Melba.getLastName(), Melba, CardType.DEBIT, CardColor.SILVER, "8888 7777 6666 4444", 898, LocalDate.now(), LocalDate.now().plusYears(9), true, account2);
			cardServices.saveCard(cardSilver);
			Card card3 = new Card(Antonela.getFirstName() + " " + Antonela.getLastName(), Antonela, CardType.CREDIT, CardColor.SILVER, "9876 5432 1098 8888", 444, LocalDate.now(), LocalDate.now().plusYears(7),true, account3);
			cardServices.saveCard(card3);
		};
	}
	}

