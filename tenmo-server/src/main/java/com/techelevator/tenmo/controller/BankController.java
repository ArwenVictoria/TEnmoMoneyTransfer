package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class BankController {
    private AccountDAO accountDAO;
    private TransferDAO transferDAO;
    private UserDAO userDAO;

    public BankController(){
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/tenmo");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres1");

        this.accountDAO = new AccountSqlDAO();
        this.transferDAO = new TransferSqlDAO();
        this.userDAO = new UserSqlDAO(new JdbcTemplate(dataSource));
    }

    @RequestMapping(path = "/{user_id}/accounts", method = RequestMethod.GET)
    public Account getAccountById(@PathVariable long user_id){
        return accountDAO.findById(user_id);
    }

    @RequestMapping(path = "/{user_id}/transfers", method = RequestMethod.GET)
    public List<Transfer> getAllTransfers(@PathVariable long user_id){
        return transferDAO.getAllTransfers(user_id);
    }

    @RequestMapping(path = "/transfers/{transfer_id}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable long transfer_id){
        return transferDAO.getTransferById(transfer_id);
    }

    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public List<User> getAllUsers(){
        return userDAO.findAll();
    }

    @RequestMapping(path = "/accounts", method = RequestMethod.PUT)
    public void updateBalance(@RequestBody Account account){
        accountDAO.updateAccountBalance(account);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/transfers", method = RequestMethod.POST)
    public Transfer createTransfer(@RequestBody Transfer transfer){
        transferDAO.createTransfer(transfer);
        return transfer;
    }

}
