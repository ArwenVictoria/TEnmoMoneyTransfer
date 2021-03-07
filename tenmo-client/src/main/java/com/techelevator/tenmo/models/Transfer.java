package com.techelevator.tenmo.models;

public class Transfer {

    long transferID;

    String transferType;

    String transferStatus;

    long accountToId;

    long accountFromId;

    long userToId;

    long userFromId;

    String userToName;

    String userFromName;

    double amount;

    public Transfer(){
    }

    public Transfer(String transferType, String transferStatus, String toUser, String fromUser, double amount) {
        this.transferType = transferType;
        this.transferStatus = transferStatus;
        this.userToName = toUser;
        this.userFromName = fromUser;
        this.amount = amount;
    }

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

    public String getUserToName() {
        return userToName;
    }

    public void setUserToName(String userToName) {
        this.userToName = userToName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getUserFromName() {
        return userFromName;
    }

    public void setUserFromName(String userFromName) {
        this.userFromName = userFromName;
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

    public String printTransfer(String currentUserName){
        String output = "";

        output+= transferID + " ";

        if(currentUserName.equals(userToName)){
            output += "         From: " + userFromName;
        }
        else{
            output += "         To: " + userToName;
        }

        output += "                 $ " + amount;

        return output;
    }

    public String printTransferDetails(){
        String output = "Transfer Details\n" +
                "-------------------------------------------";

        output += "\nTransfer Id: " + transferID;
        output += "\nFrom: " + userFromName;
        output += "\nTo: " + userToName;
        output += "\nType: " + transferType;
        output += "\nStatus: " + transferStatus;
        output += "\nAmount: " + amount + "\n";

        return output;
    }
}
