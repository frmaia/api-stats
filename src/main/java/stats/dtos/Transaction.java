package stats.dtos;

import javax.validation.constraints.NotNull;

public class Transaction {

    //amount is a double specifying the amount
    @NotNull(message = "amount may not be null")
    private Double amount;

    //timestamp is a long specifying unix time format in milliseconds (this is not current timestamp)
    @NotNull(message = "timestamp cannot be null")
    private Long timestamp;

    public Transaction(){ }
    public Transaction(Double amount, Long timestamp){
        this.amount = amount;
        this.timestamp = timestamp;
    }


    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
