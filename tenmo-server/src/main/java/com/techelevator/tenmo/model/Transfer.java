package com.techelevator.tenmo.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

public class Transfer {

    long transferID;

    @NotEmpty
    @NotBlank
    String transferType;

    @NotEmpty
    @NotBlank
    String transferStatus;

    @NotEmpty
    @NotBlank
    String fromUser;

    @NotEmpty
    @NotBlank
    String ToUser;

    @Positive
    double amount;

    public long getTransferID() {
        return transferID;
    }

    public void setTransferID(long transferID) {
        this.transferID = transferID;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getToUser() {
        return ToUser;
    }

    public void setToUser(String toUser) {
        this.ToUser = toUser;
    }
}
