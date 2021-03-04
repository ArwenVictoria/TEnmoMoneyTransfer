package com.techelevator.tenmo.dao;

import java.util.List;

import com.techelevator.tenmo.model.Account;


public interface AccountDAO {

	Account findById(long user_id);
	
	 List<Account> findAll();

	void updateAccountBalance(Account account);
}
