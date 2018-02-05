package stats.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import stats.dtos.Transaction;
import stats.exceptions.TransactionTimeExceededException;
import stats.exceptions.TransactionTimeInTheFutureException;

import java.util.Deque;
import java.util.DoubleSummaryStatistics;
import java.util.concurrent.ConcurrentLinkedDeque;

@Service
@EnableAsync
public class TransactionService {

    public TransactionService(){
        super();
    }

    public TransactionService(long maxTTL, boolean updateStatsSchedulerEnabled){
        this.TRANSACTION_MAX_TTL = maxTTL;
        this.updateStatsSchedulerEnabled = updateStatsSchedulerEnabled;
    }

    @Value("${app.constraints.transaction_max_ttl}")
    public long TRANSACTION_MAX_TTL;

    /**
     * object used as a sliding window for transactions.
     * Objects will expire if are older than {@link #TRANSACTION_MAX_TTL}
     */
    private Deque<Transaction> transactions = new ConcurrentLinkedDeque<Transaction>();

    /**
     * DoubleSummaryStatistics {@link DoubleSummaryStatistics} related with the {@link Transaction}'s amounts
     * contained in {@link #transactions}
     */
    private DoubleSummaryStatistics stats = new DoubleSummaryStatistics();

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    /**
     * Return stats. Is expected that updateStats was executed before it.
     * @return DoubleSummaryStatistics of {@link #transactions}
     */
    public DoubleSummaryStatistics getSummarizedStatistics(){
        return stats;
    }


    /**
     * Adds a transaction object specifying a max time to live
     * @param transaction
     * @throws TransactionTimeExceededException if transaction timestamp is expired, according TRANSACTION_MAX_TTL
     * @throws TransactionTimeInTheFutureException if transaction timestamp is greather than {@param transaction}
     */
    public void addTransaction(Transaction transaction) throws TransactionTimeExceededException, TransactionTimeInTheFutureException {

        long currentTime = System.currentTimeMillis();

        // calculate diff between current time and the transaction
        if (currentTime - transaction.getTimestamp() > TRANSACTION_MAX_TTL ) {
            String message = String.format("Transaction timestamp '%s' must not be greater or older than %s (milli) in comparision with currentTime '%s'. " +
                    "In this sense, transaction processing is being unconsidered.", transaction.getTimestamp(), TRANSACTION_MAX_TTL,  currentTime);
            logger.error(message);
            throw new TransactionTimeExceededException(message);
        } else if (transaction.getTimestamp() > currentTime){
            String message = String.format("Transaction timestamp '%s' is in the future, in comparison with current time '%s'. " +
                    "In this sense, transaction processing is being unconsidered.", transaction.getTimestamp(), currentTime);
            logger.error(message);
            throw new TransactionTimeInTheFutureException(message);

        }

        transactions.add(transaction); //(List)

        //updateStatsAsynchronously(currentTime, TRANSACTION_MAX_TTL);
        updateStats(currentTime, TRANSACTION_MAX_TTL);

    }

    /**
     * Clean transactions that are expired (older than maxTTL), and updates statistics
     * @param referenceTime is the maximum time to be considered. When used as sliding window algorithm, it should be
     *                      the current time.
     * @param maxTTL max Time-To-Live behind @referenceTime
     */
    @Async
    public void updateStats(long referenceTime, long maxTTL){

        logger.debug(String.format("Checking for transactions older than '%s' (milli)", maxTTL));

        //clean
        long diff;
        do{
            if (transactions.isEmpty()){
                logger.warn("Stats queue is empty.");
                break;
            }

            Transaction tempTransaction = transactions.getFirst();
            diff = referenceTime - tempTransaction.getTimestamp();

            if (diff > maxTTL){
                transactions.removeFirst();
                logger.debug(String.format("Removed expired transaction from queue " +
                                "[currentTime: '%d', transactionTimeStamp: '%d', diff: '%d', maxTTL: '%d', transactionsQueueSize: '%d'] "
                        , referenceTime, tempTransaction.getTimestamp(), diff, maxTTL, transactions.size()));
            }

        } while (diff > maxTTL);

        //update
        stats = transactions.stream().mapToDouble(Transaction::getAmount).summaryStatistics();
        logger.debug(String.format("stats updated! [%s]", stats));

    }


    @Value("${app.scheduler.update_stats_frequency.enabled}")
    private boolean updateStatsSchedulerEnabled;


    /**
     * Scheduler for running updateStats in a frequency defined by config property 'app.scheduler.update_stats_frequency'
     */
    @Scheduled(fixedRateString = "${app.scheduler.update_stats_frequency}")
    void updateStatsScheduler(){
        logger.debug(String.format("updateStatsScheduler [enabled: '%s', maxTTL: '%s']. ",
                updateStatsSchedulerEnabled, TRANSACTION_MAX_TTL));

        if (updateStatsSchedulerEnabled){
            updateStats(System.currentTimeMillis(), TRANSACTION_MAX_TTL);
        }
    }

}
