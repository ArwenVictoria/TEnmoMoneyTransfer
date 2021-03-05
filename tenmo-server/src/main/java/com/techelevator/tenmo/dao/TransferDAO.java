package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDAO {

    List<Transfer> getAllTransfers(long id);

    Transfer getTransferById(long id);

    Transfer updateTransferStatus(Transfer transfer);

    void createTransfer(Transfer transfer);

    List<Transfer> getPendingTransfers(long id);

}
