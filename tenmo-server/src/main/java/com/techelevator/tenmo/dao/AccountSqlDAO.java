package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.ArrayList;
import java.util.List;

public class AccountSqlDAO implements AccountDAO{
    private JdbcTemplate jdbcTemplate;

    public void AccountSqlDAO(){
        this.jdbcTemplate = new JdbcTemplate();
    }

    @Override
    public Account findById(long user_id){

        String sqlGetAllAccounts = "SELECT user_id, balance, account_id FROM accounts WHERE user_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sqlGetAllAccounts, user_id);

        while(result.next()){
            return mapAccountToRow(result);
        }

        return null;
    }

    @Override
    public List<Account> findAll(){
        List<Account> output = new ArrayList<>();
        String sqlGetAllAccounts = "SELECT user_id, balance, account_id FROM accounts";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sqlGetAllAccounts);
        while(result.next()){
            output.add(mapAccountToRow(result));
        }
        return output;
    }

    private Account mapAccountToRow(SqlRowSet row){
        Account a = new Account();
        a.setUser_id(row.getLong("user_id"));
        a.setBalance(row.getDouble("balance"));
        a.setAccount_id(row.getLong("account_id"));
        return a;
    }
}