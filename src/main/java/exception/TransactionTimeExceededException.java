package exception;

public class TransactionTimeExceededException extends Exception{
    public TransactionTimeExceededException(String message){
        super(message);
    }
}
