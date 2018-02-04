package service;

import dtos.StatisticsDTO;
import dtos.TransactionDTO;
import exception.TransactionTimeExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;


public class TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    private static List<TransactionDTO> transactions = new ArrayList<TransactionDTO>();
    //private static Deque<TransactionDTO> transactions = new ConcurrentLinkedDeque<TransactionDTO>();

    public static void processTransaction(TransactionDTO transaction) throws TransactionTimeExceededException{

        long currentTime = System.currentTimeMillis() ;

        // calculate diff between current time and the transaction
        if (currentTime - transaction.getTimestamp() > 60000 || transaction.getTimestamp() > currentTime) {
            String message = String.format("Transaction timestamp '%s' must not be greater or older than 60 seconds in comparision with currentTime '%s'. " +
                    "In this sense, transaction processing is being unconsidered.", transaction.getTimestamp(), currentTime);
            logger.error(message);
            throw new TransactionTimeExceededException(message);
        }

        transactions.add(transaction); //(List)
        //transactions.addLast(transaction);
        //transactions.

        //TODO: memoryCache based on DoubleSummaryStatistics
        // check for https://docs.oracle.com/javase/8/docs/api/java/util/DoubleSummaryStatistics.html

    }

    public static DoubleSummaryStatistics getSummarizedStatistics(){
        return transactions.stream().mapToDouble((x) -> x.getAmount()).summaryStatistics();
        //return transactions.stream().red
    }

}
