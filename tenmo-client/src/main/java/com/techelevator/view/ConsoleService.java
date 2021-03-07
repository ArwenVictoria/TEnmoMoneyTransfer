package com.techelevator.view;


import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.services.BankService;
import com.techelevator.tenmo.services.EmailService;
import com.techelevator.tenmo.services.NotEnoughDoughException;
import com.techelevator.tenmo.services.TransferDoesNotExistException;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Scanner;

public class ConsoleService {

	private PrintWriter out;
	private Scanner in;
	private BankService bankService;

	public ConsoleService(InputStream input, OutputStream output) {
		this.out = new PrintWriter(output, true);
		this.in = new Scanner(input);
		this.bankService = new BankService("http://localhost:8080/");
	}

	public Object getChoiceFromOptions(Object[] options) {
		Object choice = null;
		while (choice == null) {
			displayMenuOptions(options);
			choice = getChoiceFromUserInput(options);
		}
		out.println();
		return choice;
	}

	private Object getChoiceFromUserInput(Object[] options) {
		Object choice = null;
		String userInput = in.nextLine();
		try {
			int selectedOption = Integer.valueOf(userInput);
			if (selectedOption > 0 && selectedOption <= options.length) {
				choice = options[selectedOption - 1];
			}
		} catch (NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will be null
		}
		if (choice == null) {
			out.println(System.lineSeparator() + "*** " + userInput + " is not a valid option ***" + System.lineSeparator());
		}
		return choice;
	}

	private void displayMenuOptions(Object[] options) {
		out.println("********************************************************************");
		out.println("*                                                                  *");
		out.println("                                                                  ");
		out.println("*                 TEnmo Main Menu                                  *");
		out.println("                                                                  ");
		out.println("*                                                                  *");
		for (int i = 0; i < options.length; i++) {
			int optionNum = i + 1;
			out.println("                  "+optionNum + ") " + options[i]+"                                 ");
			out.println("*                                                                  *");
		}

		out.println("                                                                  ");
		out.println("*                 Please select an option to continue              *");
		out.println("                                                                  ");
		out.println("********************************************************************");
	}

	public String getUserInput(String prompt) {
		out.print(prompt+": ");
		out.flush();
		return in.nextLine();
	}

	public Integer getUserInputInteger(String prompt) {
		Integer result = null;
		do {
			out.print(prompt+": ");
			out.flush();
			String userInput = in.nextLine();
			try {
				result = Integer.parseInt(userInput);
			} catch(NumberFormatException e) {
				out.println(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
			}
		} while(result == null);
		return result;
	}

	public void printBalance(AuthenticatedUser currentUser){

		Account account = bankService.getAccountByUserId(currentUser.getUser().getId(), currentUser.getToken());

		System.out.println("********************************************************************");
		System.out.println("                                                                    ");
		System.out.println("*               TEnmo Balance                                      *");
		System.out.println("                                                                    ");
		System.out.println("*                                                                  *");
		System.out.println("                Your current account balance is: $" +account.getBalance());
		System.out.println("*                                                                  *");
		System.out.println("                                                                    ");
		System.out.println("*               Press any key to return to main menu.              *");
		System.out.println("                                                                    ");
		System.out.println("********************************************************************");
		in.nextLine();
	}

	public void transferMenu(AuthenticatedUser user){
		Account fromAccount = bankService.getAccountByUserId(user.getUser().getId(), user.getToken());

		while(true) {
			String currentUserName = user.getUser().getUsername();
			String token = user.getToken();
			User[] users = bankService.listUsers(token);
			User[] allowedUsers = new User[users.length - 1];

			int i = 0;
			for (User u : users) {
				if (!u.getUsername().equals(currentUserName)) {
					allowedUsers[i] = u;
					i++;
				}
			}

			Account toAccount;
			long userId = -1;
			boolean userExists = false;
			double amount = -1;
			String userName = "";
			String input;


			out.println("-------------------------------------------\n" +
					"Users\n" +
					"ID          Name\n" +
					"-------------------------------------------");

			for (User u : allowedUsers) {
				out.println(u.getId() + "          " + u.getUsername());
			}

			while (!userExists) {

				out.println("Enter ID of user you are sending to (0 to cancel):");
				input = in.nextLine();

				if (input.equals("0")) {
					return;
				} else {
					try {
						userId = Long.parseLong(input);


						for (User u : allowedUsers) {
							if (u.getId() == userId) {
								userExists = true;
								userName = u.getUsername();
							}
						}

						if (!userExists) {
							out.println("That user doesn't exist. Please try again.");
						}
					} catch (NumberFormatException e) {
						System.out.println("That's not a number! Please try again.");
					}
				}
			}

			while (amount <= -1) {
				out.println("Enter amount (0 to cancel):");
				input = in.nextLine();
				if (input.equals("0")) {
					return;
				} else {
					try {
						amount = Double.parseDouble(input);


						if (amount < 0) {
							out.println("You can't transfer negative numbers!");
						}

						if (amount > fromAccount.getBalance()) {
							out.println("You do not have enough money to make this transfer.");
							amount = -1;
						}

					} catch (NumberFormatException e) {
						System.out.println("That's not a number! Please try again.");
					}
				}


			}

			toAccount = bankService.getAccountByUserId(userId, token);
			fromAccount.setBalance(fromAccount.getBalance() - amount);
			toAccount.setBalance(toAccount.getBalance() + amount);

			bankService.updateAccount(fromAccount, token);
			bankService.updateAccount(toAccount, token);

			Transfer transfer = new Transfer("Send", "Approved", userName, currentUserName, amount);
			bankService.createTransfer(transfer, token);
			out.println("Transfer successful.\nCurrent balance:" + fromAccount.getBalance());
			out.println("Press any key to send another transfer, or 0 to exit.");
			input = in.nextLine();
			if(input.equals("0")){
				return;
			}
		}

	}
		public void transferHistory(AuthenticatedUser currentUser){
			Transfer[] transfers = bankService.listTransfers(currentUser.getUser().getId(), currentUser.getToken());
			String userName = currentUser.getUser().getUsername();

			while(true) {
				if(transfers.length==0){
					out.println("Nothing to see here! Press any key to return to main menu.");
					in.nextLine();
					return;
				}

				String body = "";
				body += "\n-----------------------------------------------------\n";
				body += "                 Approved Transfers\n";
				body += "-----------------------------------------------------\n";
				boolean printed = false;

				for (Transfer t:transfers){
					if(t.getTransferStatus().equals("Approved")) {
						body += t.printTransfer(userName) + "\n";
						printed = true;
					}
				}

				if(!printed){
					body += ("Nothing to see here!");
				}

				body += "\n-----------------------------------------------------\n";
				body += "                 Pending Transfers\n";
				body += "-----------------------------------------------------\n";
				printed = false;

				for (Transfer t:transfers){
					if(t.getTransferStatus().equals("Pending")) {
						body += t.printTransfer(userName) + "\n";
						printed = true;
					}
				}

				if(!printed){
					body += ("Nothing to see here!");
				}

				body += "\n-----------------------------------------------------\n";
				body += "                 Rejected Transfers\n";
				body += "-----------------------------------------------------\n";
				printed = false;

				for (Transfer t:transfers){
					if(t.getTransferStatus().equals("Rejected")) {
						body += t.printTransfer(userName) + "\n";
						printed = true;
					}
				}

				if(!printed){
					body += ("Nothing to see here!");
				}

				out.println(body);

				out.println("\nPlease enter a transfer id to view details. Press 0 to exit.");
				String input = in.nextLine();
				if ("0".equals(input)) {
					return;
				}
				printed = false;


				try {
					int choice = Integer.parseInt(input);
					for (Transfer t : transfers) {
						if (choice == t.getTransferID()) {
							out.println(t.printTransferDetails());
							out.println("\nPress any key to return to transfer menu.");
							printed = true;
							in.nextLine();
						}
					}
					if (!printed) {
						out.println("That transfer doesn't exist! Please try again.\n");
					}
				} catch (NumberFormatException e) {
					System.out.println("Please enter a number only.");
				}
			}
	}

	public void requestMenu(AuthenticatedUser user){
		while(true) {
			String currentUserName = user.getUser().getUsername();
			String token = user.getToken();
			User[] users = bankService.listUsers(token);
			User[] allowedUsers = new User[users.length - 1];

			int i = 0;
			for (User u : users) {
				if (!u.getUsername().equals(currentUserName)) {
					allowedUsers[i] = u;
					i++;
				}
			}

			long userId;
			boolean userExists = false;
			double amount = -1;
			String userName = "";
			String input;


			out.println("-------------------------------------------\n" +
					"Users\n" +
					"ID          Name\n" +
					"-------------------------------------------");

			for (User u : allowedUsers) {
				out.println(u.getId() + "          " + u.getUsername());
			}

			while (!userExists) {

				out.println("Enter ID of user you are requesting from (0 to cancel):");
				input = in.nextLine();

				if (input.equals("0")) {
					return;
				} else {
					try {
						userId = Long.parseLong(input);


						for (User u : allowedUsers) {
							if (u.getId() == userId) {
								userExists = true;
								userName = u.getUsername();
							}
						}

						if (!userExists) {
							out.println("That user doesn't exist. Please try again.");
						}
					} catch (NumberFormatException e) {
						System.out.println("That's not a number! Please try again.");
					}
				}
			}

			while (amount <= -1) {
				out.println("Enter amount (0 to cancel):");
				input = in.nextLine();
				if (input.equals("0")) {
					return;
				} else {
					try {
						amount = Double.parseDouble(input);


						if (amount < 0) {
							out.println("You can't transfer negative numbers!");
						}

					} catch (NumberFormatException e) {
						System.out.println("That's not a number! Please try again.");
					}
				}


			}

			Transfer transfer = new Transfer("Request", "Pending", currentUserName, userName, amount);
			bankService.createTransfer(transfer, token);
			out.println("Request sent! Press any key to create another request, or 0 to exit.");
			input = in.nextLine();
			if("0".equals(input)){
				return;
			}
		}

	}

	public void pendingRequestMenu(AuthenticatedUser user){
		while(true) {
			Transfer[] pendingTransfers = bankService.getPendingTransfers(user.getUser().getId(), user.getToken());

			out.println("-------------------------------------------\n" +
					"Transfer\n" +
					"ID          From/To                 Amount\n" +
					"-------------------------------------------");

			if(pendingTransfers.length==0){
				out.println("Nothing to see here! Press any key to return to main menu.");
				in.nextLine();
				return;
			}
			for (Transfer t: pendingTransfers) {
				out.println(t.printTransfer(user.getUser().getUsername()));
			}

			out.println("Please choose a request to accept or decline, or 0 to exit.");

			String input = in.nextLine();

			if(input.equals("0")){
				return;
			}

			try{
				long transferId = Long.parseLong(input);
				Transfer transferInQuestion = null;

				for (Transfer t: pendingTransfers){
					if (transferId == t.getTransferID()){
						transferInQuestion = t;
					}
				}

				if(transferInQuestion == null){
					throw new TransferDoesNotExistException();
				}

				out.println("Press 1 to accept, 2 to decline, any other key to return to menu.");

				input = in.nextLine();
				if(input.equals("1")){
					Account a = bankService.getAccountByUserId(user.getUser().getId(), user.getToken());
					if(transferInQuestion.getAmount() >= a.getBalance()){
						throw new NotEnoughDoughException();
					}
					Account b = bankService.getAccountByUserId(transferInQuestion.getUserToId(), user.getToken());

					a.setBalance(a.getBalance()-transferInQuestion.getAmount());
					b.setBalance(b.getBalance()+transferInQuestion.getAmount());
					bankService.updateAccount(a, user.getToken());
					bankService.updateAccount(b, user.getToken());
					transferInQuestion.setTransferStatus("Approved");
					bankService.updateTransferStatus(transferInQuestion, user.getToken());
					out.println("Transfer approved.");
					out.println("Current balance: $" + a.getBalance() + "\nPress any key to return to pending transfers.");
					in.nextLine();
				}
				else if(input.equals("2")){
					transferInQuestion.setTransferStatus("Rejected");
					bankService.updateTransferStatus(transferInQuestion, user.getToken());
					out.println("Transfer declined. Press any key to return to pending transfers.");
					in.next();
				}

			}
			catch (NumberFormatException e){
				out.println("Please enter a number.");
			}
			catch (TransferDoesNotExistException e){
				out.println("That transfer doesn't exist.");
			}
			catch (NotEnoughDoughException e){
				out.println("You do not have enough money to accept this transaction.");
			}
		}
	}

	public void emailStatement(AuthenticatedUser user){
		String subject = "TEnmo Bank Statement: "+ LocalDate.now();

		Account a = bankService.getAccountByUserId(user.getUser().getId(), user.getToken());
		Transfer[] transfers = bankService.listTransfers(user.getUser().getId(), user.getToken());

		String body = "Current Balance: "+a.getBalance();
		body += "\n-----------------------------------------------------\n";
		body += "                 Approved Transfers\n";
		body += "-----------------------------------------------------\n";
		boolean printed = false;

		for (Transfer t:transfers){
			if(t.getTransferStatus().equals("Approved")) {
				body += t.printTransfer(user.getUser().getUsername()) + "\n";
				printed = true;
			}
		}

		if(!printed){
			body += ("Nothing to see here!");
		}

		body += "\n-----------------------------------------------------\n";
		body += "                 Pending Transfers\n";
		body += "-----------------------------------------------------\n";
		printed = false;

		for (Transfer t:transfers){
			if(t.getTransferStatus().equals("Pending")) {
				body += t.printTransfer(user.getUser().getUsername()) + "\n";
				printed = true;
			}
		}

		if(!printed){
			body += ("Nothing to see here!");
		}

		body += "\n-----------------------------------------------------\n";
		body += "                 Rejected Transfers\n";
		body += "-----------------------------------------------------\n";
		printed = false;

		for (Transfer t:transfers){
			if(t.getTransferStatus().equals("Rejected")) {
				body += t.printTransfer(user.getUser().getUsername()) + "\n";
				printed = true;
			}
		}

		if(!printed){
			body += ("Nothing to see here!");
		}

		EmailService emailService = new EmailService();

		out.println("Please enter your email address, or 0 to cancel:");
		String input = in.nextLine();
		if (input.equals("0")){
			return;
		}
		out.println("This will take a couple seconds...");
		out.println("Don't hold your breath");
		out.println(emailService.sendEmail(subject, body, input));
		out.println("Press any key to return to main menu.");
		in.nextLine();
	}


}
