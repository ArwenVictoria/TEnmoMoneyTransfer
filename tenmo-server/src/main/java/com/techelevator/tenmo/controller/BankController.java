package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AccountDoesNotExistException;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public Account getAccountById(@PathVariable long user_id) throws AccountDoesNotExistException {
        Account a = accountDAO.findByUserId(user_id);
        if(a == null){
            throw new AccountDoesNotExistException();
        }
        else{
            return a;
        }
    }

    @RequestMapping(path = "/{user_id}/transfers", method = RequestMethod.GET)
    public List<Transfer> getAllTransfers(@PathVariable long user_id){
        Account account = accountDAO.findByUserId(user_id);
        List<Transfer> transfers = transferDAO.getAllTransfers(account.getAccount_id());
        for (Transfer t: transfers) {
            t.setUserFromId(accountDAO.getUserIdFromAccountId(t.getAccountFromId()));
            t.setUserToId(accountDAO.getUserIdFromAccountId(t.getAccountToId()));
            t.setUserFromName(userDAO.findUserNameById(t.getUserFromId()));
            t.setUserToName(userDAO.findUserNameById(t.getUserToId()));
        }
        return transfers;
    }

    @RequestMapping(path = "/{user_id}/transfers/pending", method = RequestMethod.GET)
    public List<Transfer> getPendingTransfers(@PathVariable long user_id){
        Account account = accountDAO.findByUserId(user_id);
        List<Transfer> transfers = transferDAO.getPendingTransfers(account.getAccount_id());
        for (Transfer t: transfers) {
            t.setUserFromId(accountDAO.getUserIdFromAccountId(t.getAccountFromId()));
            t.setUserToId(accountDAO.getUserIdFromAccountId(t.getAccountToId()));
            t.setUserFromName(userDAO.findUserNameById(t.getUserFromId()));
            t.setUserToName(userDAO.findUserNameById(t.getUserToId()));
        }
        return transfers;
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
        if(account == null){
            throw new AccountDoesNotExistException();
        }
        accountDAO.updateAccountBalance(account);
    }

    @RequestMapping(path = "/transfers", method = RequestMethod.PUT)
    public Transfer updateTransferStatus(@RequestBody Transfer transfer){
        return transferDAO.updateTransferStatus(transfer);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/transfers", method = RequestMethod.POST)
    public Transfer createTransfer(@Valid @RequestBody Transfer transfer){
        transferDAO.createTransfer(transfer);
        return transfer;
    }

    @RequestMapping(path = "users/{username}", method = RequestMethod.GET)
    public User getUserById(@PathVariable String userName){
        return userDAO.findByUsername(userName);
    }

}
