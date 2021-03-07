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

    public List<Transfer> getAllTransfers(long account_id){
        List<Transfer> output = new ArrayList<>();
        String sqlGetAllFromTransfers = "SELECT * FROM transfers JOIN transfer_types USING(transfer_type_id) JOIN transfer_statuses " +
                "USING(transfer_status_id) WHERE account_from = ? OR account_to = ? ORDER BY transfer_id DESC";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sqlGetAllFromTransfers, account_id, account_id);
        while (result.next()){
            output.add(mapRowToTransfer(result));
        }
        return output;
    }

    public Transfer getTransferById(long transfer_id){

        String sqlGetAllTransfers = "SELECT * FROM transfers JOIN transfer_types USING(transfer_type_id) JOIN transfer_statuses " +
                "USING(transfer_status_id) WHERE transfer_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sqlGetAllTransfers, transfer_id);

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
        jdbcTemplate.update(sqlCreateTransfer, transfer.getTransferType(), transfer.getTransferStatus(), transfer.getUserFromName(),
                transfer.getUserToName(), transfer.getAmount());
    }

    public Transfer updateTransferStatus(Transfer transfer){
        String sqlUpdateStatus = "UPDATE transfers SET transfer_status_id = (SELECT transfer_status_id FROM transfer_statuses WHERE transfer_status_desc = ?) WHERE transfer_id = ?";

        jdbcTemplate.update(sqlUpdateStatus, transfer.getTransferStatus(), transfer.getTransferID());

        return getTransferById(transfer.getTransferID());
    }

    public List<Transfer> getPendingTransfers(long account_id){
        List<Transfer> transfers = new ArrayList<>();
        String sqlGetPendingTransfers = "SELECT * FROM transfers JOIN transfer_types USING(transfer_type_id) JOIN transfer_statuses " +
                "USING(transfer_status_id) WHERE account_from = ? AND transfer_status_id = (SELECT transfer_status_id FROM transfer_statuses WHERE transfer_status_desc = 'Pending') ORDER BY transfer_id DESC";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlGetPendingTransfers, account_id);

        while(rowSet.next()){
            transfers.add(mapRowToTransfer(rowSet));
        }

        return transfers;
    }

    private Transfer mapRowToTransfer(SqlRowSet rowSet){
        Transfer transfer = new Transfer();
        transfer.setTransferID(rowSet.getLong("transfer_id"));
        transfer.setTransferType(rowSet.getString("transfer_type_desc"));
        transfer.setAccountFromId(rowSet.getLong("account_from"));
        transfer.setAccountToId(rowSet.getLong("account_to"));
        transfer.setTransferStatus(rowSet.getString("transfer_status_desc"));
        transfer.setAmount(rowSet.getDouble("amount"));
        return transfer;
    }

}
