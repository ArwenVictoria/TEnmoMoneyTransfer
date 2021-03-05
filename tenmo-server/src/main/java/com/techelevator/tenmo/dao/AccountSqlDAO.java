package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;

public class AccountSqlDAO implements AccountDAO{
    private JdbcTemplate jdbcTemplate;

    public AccountSqlDAO(){
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/tenmo");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres1");

        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Account findById(long user_id){

        String sqlGetAllAccounts = "SELECT user_id, balance, account_id FROM accounts WHERE user_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sqlGetAllAccounts, user_id);

        if(result.next()){
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

    public void updateAccountBalance(Account account){
        String sqlUpdateBalance = "UPDATE accounts " +
                "SET balance = ? " +
                "WHERE account_id = ?";

        jdbcTemplate.update(sqlUpdateBalance, account.getBalance(), account.getAccount_id());
    }

    @Override
    public long getUserId(long accountId) {
        String sqlGetUser = "SELECT user_id FROM accounts WHERE account_id = ?";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlGetUser, accountId);
        rowSet.next();
        return rowSet.getLong("user_id");
    }

    private Account mapAccountToRow(SqlRowSet row){
        Account a = new Account();
        a.setUser_id(row.getLong("user_id"));
        a.setBalance(row.getDouble("balance"));
        a.setAccount_id(row.getLong("account_id"));
        return a;
    }
}
