package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDAO {

    List<Transfer> getAllTransfers(long id);

    Transfer getTransferById(long id);

    void createTransfer(Transfer transfer);

}
