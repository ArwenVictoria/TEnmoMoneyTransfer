package com.techelevator.tenmo.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

public class Transfer {

    long transferID;

    long accountToId;

    long accountFromId;

    long userToId;

    long userFromId;

    @NotEmpty
    @NotBlank
    String transferType;

    @NotEmpty
    @NotBlank
    String transferStatus;

    @NotEmpty
    @NotBlank
    String userFromName;

    @NotEmpty
    @NotBlank
    String userToName;

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

    public String getUserFromName() {
        return userFromName;
    }

    public void setUserFromName(String userFromName) {
        this.userFromName = userFromName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getUserToName() {
        return userToName;
    }

    public void setUserToName(String userToName) {
        this.userToName = userToName;
    }

    public long getAccountToId() {
        return accountToId;
    }

    public void setAccountToId(long accountToId) {
        this.accountToId = accountToId;
    }

    public long getAccountFromId() {
        return accountFromId;
    }

    public void setAccountFromId(long accountFromId) {
        this.accountFromId = accountFromId;
    }

    public long getUserToId() {
        return userToId;
    }

    public void setUserToId(long userToId) {
        this.userToId = userToId;
    }

    public long getUserFromId() {
        return userFromId;
    }

    public void setUserFromId(long userFromId) {
        this.userFromId = userFromId;
    }
}
