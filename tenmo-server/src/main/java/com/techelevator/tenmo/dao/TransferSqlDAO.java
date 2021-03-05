package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.ArrayList;
import java.util.List;

public class TransferSqlDAO implements TransferDAO{
    private JdbcTemplate jdbcTemplate;

    public TransferSqlDAO(){
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/tenmo");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres1");

        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Transfer> getAllTransfers(long id){
        List<Transfer> output = new ArrayList<>();
        String sqlGetAllFromTransfers = "SELECT * FROM transfers JOIN transfer_types USING(transfer_type_id) JOIN transfer_statuses " +
                "USING(transfer_status_id) JOIN accounts ON (account_to = account_id) JOIN users USING(user_id) WHERE account_from = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sqlGetAllFromTransfers, id);
        while (result.next()){
            output.add(mapRowToTransfer(result));
        }
        String sqlGetAllToTransfers = "SELECT * FROM transfers JOIN transfer_types USING(transfer_type_id) JOIN transfer_statuses " +
                "USING(transfer_status_id) JOIN accounts ON (account_from = account_id) JOIN users USING(user_id) WHERE account_to = ?";
        result = jdbcTemplate.queryForRowSet(sqlGetAllToTransfers, id);
        while (result.next()){
            output.add(mapRowToTransfer(result));
        }
        return output;
    }

    public Transfer getTransferById(long id){

        String sqlGetAllTransfers = "SELECT * FROM transfers JOIN transfer_types USING(transfer_type_id) JOIN transfer_statuses " +
                "USING(transfer_status_id) JOIN accounts ON (account_to = account_id) JOIN users USING(user_id) WHERE transfer_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sqlGetAllTransfers, id);

        if (result.next()){
            return mapRowToTransfer(result);
        }

        return null;
    }

    public void createTransfer(Transfer transfer){
        String sqlCreateTransfer = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES ((SELECT transfer_type_id FROM transfer_types WHERE transfer_type_desc = ?), " +
                "(SELECT transfer_status_id FROM transfer_statuses WHERE transfer_status_desc = ?), " +
                "(SELECT user_id FROM users WHERE username = ?), " +
                "(SELECT user_id FROM users WHERE username = ?), ?)";
        jdbcTemplate.update(sqlCreateTransfer, transfer.getTransferType(), transfer.getTransferStatus(), transfer.getFromUser(),
                transfer.getToUser(), transfer.getAmount());
    }

    private Transfer mapRowToTransfer(SqlRowSet rowSet){
        Transfer transfer = new Transfer();
        transfer.setTransferID(rowSet.getLong("transfer_id"));
        transfer.setTransferType(rowSet.getString("transfer_type_desc"));

        if(transfer.getTransferType().equals("Send")) {
            transfer.setToUser(rowSet.getString("username"));
            transfer.setFromUser(null);
        }
        else if(transfer.getTransferType().equals("Request")){
            transfer.setFromUser(rowSet.getString("username"));
            transfer.setToUser(null);
        }

        transfer.setTransferStatus(rowSet.getString("transfer_status_desc"));
        transfer.setAmount(rowSet.getDouble("amount"));
        return transfer;
    }
}
