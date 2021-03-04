package com.techelevator.tenmo.model;

public class Transfer {

    long transferID;

    String transferType;

    String transferStatus;

    String humanFrom;

    String humanTo;

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

    public String getHumanFrom() {
        return humanFrom;
    }

    public void setHumanFrom(String humanFrom) {
        this.humanFrom = humanFrom;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getHumanTo() {
        return humanTo;
    }

    public void setHumanTo(String humanTo) {
        this.humanTo = humanTo;
    }
}
