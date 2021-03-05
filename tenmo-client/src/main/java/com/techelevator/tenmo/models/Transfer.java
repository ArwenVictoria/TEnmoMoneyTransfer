package com.techelevator.tenmo.models;

public class Transfer {

    long transferID;

    String transferType;

    String transferStatus;

    String toUser;

    String fromUser;

    double amount;

    public Transfer(){
    }

    public Transfer(String transferType, String transferStatus, String toUser, String fromUser, double amount) {
        this.transferType = transferType;
        this.transferStatus = transferStatus;
        this.toUser = toUser;
        this.fromUser = fromUser;
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

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String printTransfer(){
        String output = "";

        output+= transferID + " ";

        if("Send".equals(transferType)){
            output += "         To: " + toUser;
        }
        else{
            output += "         From: " + fromUser;
        }

        output += "                 $ " + amount;

        return output;
    }

    public String printTransferDetails(String currentUserName){
        String output = "Transfer Details";

        output += "\nTransfer Id: " + transferID;
        if("Send".equals(transferType) ){
            output += "\nFrom: " + currentUserName;
            output += "\nTo: " + toUser;
        }
        else{
            output += "\nFrom: " + toUser;
            output += "\nTo: " + currentUserName;
        }

        output += "\nType: " + transferType;
        output += "\nStatus: " + transferStatus;
        output += "\nAmount: " + amount;

        return output;
    }
}
