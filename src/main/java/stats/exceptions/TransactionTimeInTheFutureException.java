package stats.exceptions;

public class TransactionTimeInTheFutureException extends RuntimeException {
    public TransactionTimeInTheFutureException(String message){
        super(message);
    }
}
