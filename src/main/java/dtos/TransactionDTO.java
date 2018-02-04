package dtos;

public class TransactionDTO {

    //amount is a double specifying the amount
    //TODO: cannot be null
    private double amount;

    //timestamp is a long specifying unix time format in milliseconds (this is not current timestamp)
    //TODO: cannot be null
    private long timestamp;


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
