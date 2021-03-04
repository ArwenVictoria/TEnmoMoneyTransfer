package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.AccountSqlDAO;
import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.dao.TransferSqlDAO;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BankController {
    private AccountDAO accountDAO;
    private TransferDAO transferDAO;

    public BankController(){
        this.accountDAO = new AccountSqlDAO();
        this.transferDAO = new TransferSqlDAO();
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


}
