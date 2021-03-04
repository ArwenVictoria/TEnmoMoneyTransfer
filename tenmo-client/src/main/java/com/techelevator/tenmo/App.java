package com.techelevator.tenmo;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.BankService;
import com.techelevator.view.ConsoleService;

public class App {

private static final String API_BASE_URL = "http://localhost:8080/";
    
    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	
    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;
    private BankService bankService;

    public static void main(String[] args) {
    	App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL), new BankService(API_BASE_URL));
    	app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService, BankService bankService) {
		this.console = console;
		this.authenticationService = authenticationService;
		this.bankService = bankService;
	}

	public void run() {
		System.out.println("*******************************");
		System.out.println("*      Welcome to TEnmo!      *");
		System.out.println("*******************************");
		
		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		
		System.out.println("********************************************************************");
		System.out.println("*                                                                  *");
		System.out.println("*                                                                  *");
		System.out.println("*                 TEnmo Main Menu                                  *");
		System.out.println("*                                                                  *");
		System.out.println("*                                                                  *");
		System.out.println("*                 (1) View Balance                                 *");
		System.out.println("*                                                                  *");
		System.out.println("*                 (2) View Past Transfers                          *");
		System.out.println("*                                                                  *");
		System.out.println("*                 (3) View Pending Requests                        *");
		System.out.println("*                                                                  *");
		System.out.println("*                 (4) Send Bucks                                   *");
		System.out.println("*                                                                  *");
		System.out.println("*                 (5) Request Bucks                                *");
		System.out.println("*                                                                  *");
		System.out.println("*                 (6) Login                                        *");
		System.out.println("*                                                                  *");
		System.out.println("*                 (7) Exit                                         *");
		System.out.println("*                                                                  *");
		System.out.println("*                 Please select an option to continue              *");
		System.out.println("*                                                                  *");
		System.out.println("********************************************************************");
		
		while(true) {
		
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(MAIN_MENU_OPTION_VIEW_BALANCE.equals("1")) {
				viewCurrentBalance();
			} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals("2")) {
				viewTransferHistory();
			} else if(MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals("3")) {
				viewPendingRequests();
			} else if(MAIN_MENU_OPTION_SEND_BUCKS.equals("4")) {
				sendBucks();
			} else if(MAIN_MENU_OPTION_REQUEST_BUCKS.equals("5")) {
				requestBucks();
			} else if(MAIN_MENU_OPTION_LOGIN.equals("6")) {
				login();
			} else if(MENU_OPTION_EXIT.equals("7")) {
				System.exit(0); 
			}
		}
	}

	private void viewCurrentBalance() {
		
		System.out.println("********************************************************************");
		System.out.println("*                                                                  *");
		System.out.println("*                                                                  *");
		System.out.println("*                                                                  *");
		System.out.println("*                View TEnmo Balance                                *");
		System.out.println("*                                                                  *");
		System.out.println("*                                                                  *");
		System.out.println("*                                                                  *");
		System.out.println("*                Your current account balance is: $ " +bankService.getAccountById(currentUser.getUser().getId())+ "           *");
		System.out.println("*                                                                  *");
		System.out.println("*                                                                  *");
		System.out.println("*                                                                  *");
		System.out.println("*                Please select 0 to return to Main Menu            *");
		System.out.println("*                                                                  *");
		System.out.println("*                                                                  *");
		System.out.println("*                                                                  *");
		System.out.println("********************************************************************");
		
		console.printBalance(bankService.getAccountById(currentUser.getUser().getId()));
		
		if (MENU_OPTION_EXIT.equals("0")) {
			System.exit(0); 
			mainMenu(); //returns them to purchase menu
			} 
			mainMenu(); //returns the to purchase menu whether they hit 0 or not
			
	} 
	

	private void viewTransferHistory() {
		// TODO Auto-generated method stub
		// Required  first send list of all transfers then they pick which one
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	}

	private void sendBucks() {
		// TODO Auto-generated method stub
		//Required
	}

	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}
	
	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
            	authenticationService.register(credentials);
            	isRegistered = true;
            	System.out.println("Registration successful. You can now login.");
            } catch(AuthenticationServiceException e) {
            	System.out.println("REGISTRATION ERROR: "+e.getMessage());
				System.out.println("Please attempt to register again.");
            }
        }
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
		    try {
				currentUser = authenticationService.login(credentials);
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: "+e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}
	
	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
}
