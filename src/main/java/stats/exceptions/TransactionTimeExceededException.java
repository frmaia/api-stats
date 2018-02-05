package stats.exceptions;


public class TransactionTimeExceededException extends RuntimeException {
    public TransactionTimeExceededException(String message){
        super(message);
    }
}
