package com.techelevator.view;


import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.services.BankService;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
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

	public void printBalance(Account account){

		System.out.println("********************************************************************");
		System.out.println("*                                                                  *");
		System.out.println("*                                                                  *");
		System.out.println("*                                                                  *");
		System.out.println("*                View TEnmo Balance                                *");
		System.out.println("*                                                                  *");
		System.out.println("*                                                                  *");
		System.out.println("*                                                                  *");
		System.out.println("*                Your current account balance is: $" +account.getBalance()+ "           *");
		System.out.println("*                                                                  *");
		System.out.println("*                                                                  *");
		System.out.println("*                                                                  *");
		System.out.println("********************************************************************");
		in.nextLine();
	}

	public void transferMenu(Account fromAccount, String currentUserName){
		User[] users = bankService.listUsers();
		User[] allowedUsers = new User[users.length-1];

		int i = 0;
		for (User u: users) {
			if(!u.getUsername().equals(currentUserName)){
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

		for(User u: allowedUsers){
				out.println(u.getId() + "          " + u.getUsername());
		}

		while(!userExists) {

			out.println("Enter ID of user you are sending to (0 to cancel):");
			input = in.nextLine();

			if (input.equals("0")) {
				return;
			} else {
				try {
					userId = Long.parseLong(input);


					for(User u: allowedUsers){
						if(u.getId()==userId){
							userExists = true;
							userName = u.getUsername();
						}
					}

					if(!userExists){
						out.println("That user doesn't exist. Please try again.");
					}
				} catch (NumberFormatException e) {
					System.out.println("That's not a number! Please try again.");
				}
			}
		}

		while (amount <= -1){
			out.println("Enter amount (0 to cancel):");
			input = in.nextLine();
			if(input.equals("0")){
				return;
			}
			else {
				try {
					amount = Double.parseDouble(input);


					if(amount < 0){
						out.println("You can't transfer negative numbers!");
					}

					if(amount > fromAccount.getBalance()){
						out.println("You do not have enough money to make this transfer.");
						amount = -1;
					}

				} catch (NumberFormatException e) {
					System.out.println("That's not a number! Please try again.");
				}
			}


		}

		toAccount = bankService.getAccountById(userId);
		fromAccount.setBalance(fromAccount.getBalance()-amount);
		toAccount.setBalance(toAccount.getBalance()+amount);

		bankService.updateAccount(fromAccount);
		bankService.updateAccount(toAccount);

		Transfer transfer = new Transfer("Send", "Approved", userName, currentUserName, amount);
		bankService.createTransfer(transfer);

	}
}
