package com.techelevator.tenmo.services;

import java.util.Scanner;

import com.techelevator.tenmo.models.Account;

public class ConsoleService {
	 
	private Scanner scanner;

	public ConsoleService() {
	  scanner = new Scanner(System.in);
	  }

    public int printMainMenu() {
        int menuSelection;
        System.out.println("Welcome to TEnmo! Please make a selection: ");
        System.out.println("1: View account balance");
        System.out.println("2: List details for specific auction");
        System.out.println("3: Find auctions with a specific term in the title");
        System.out.println("4: Find auctions below a specified price");
        System.out.println("5: Create a new auction");
        System.out.println("6: Modify an auction");
        System.out.println("7: Delete an auction");
        System.out.println("0: Exit");
        System.out.print("\nPlease choose an option: ");
        // ensures no InputMisMatchException is thrown
        if (scanner.hasNextInt()) {
            menuSelection = scanner.nextInt();
            scanner.nextLine();
        } else {
            menuSelection = 999;
        }
        return menuSelection;
    }	
	
	
	
	
	public void next() {
	     System.out.println("\nPress Enter to continue...");
	     scanner.nextLine();
	    }

	public void exit() {
	     scanner.close();
	     System.exit(0);
	    }

	public void printError(String errorMessage) {
	     System.err.println(errorMessage);
		
	}

}
