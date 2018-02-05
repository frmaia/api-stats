package stats.services;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import stats.dtos.Transaction;
import stats.exceptions.TransactionTimeExceededException;
import stats.exceptions.TransactionTimeInTheFutureException;

import java.util.DoubleSummaryStatistics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransactionServiceTest {
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionService(60000, false);
    }

    @Test
    @DisplayName("Add valid transaction")
    void addValidTransaction() throws TransactionTimeExceededException {
        Transaction transaction = new Transaction(Double.valueOf(10), System.currentTimeMillis());
        transactionService.addTransaction(transaction);
    }

    @Test
    @DisplayName("Add in the future transaction")
    void addInTheFutureTransaction() {
        Transaction transaction = new Transaction(Double.valueOf(10), System.currentTimeMillis()+10000); //10 seconds forward

        Throwable exception = assertThrows(TransactionTimeInTheFutureException.class, () -> {
            transactionService.addTransaction(transaction);
        });
    }

    @Test
    @DisplayName("Add expired transaction")
    void addExpiredTransaction() {
        Transaction transaction = new Transaction(Double.valueOf(10), 1517782716000l);

        Throwable exception = assertThrows(TransactionTimeExceededException.class, () -> {
            transactionService.addTransaction(transaction);
        });
    }

    @Test
    void updateAndGetStats() throws InterruptedException {
        long currentTime = System.currentTimeMillis();
        transactionService.addTransaction(new Transaction(Double.valueOf(30), currentTime));
        transactionService.addTransaction(new Transaction(Double.valueOf(20), currentTime));
        transactionService.addTransaction(new Transaction(Double.valueOf(10), System.currentTimeMillis()));
        transactionService.addTransaction(new Transaction(Double.valueOf(40.3), currentTime));

        // Test initial summarization
        transactionService.updateStats(currentTime, 60000);
        DoubleSummaryStatistics stats = transactionService.getSummarizedStatistics();

        assertEquals(4, stats.getCount());
        assertEquals(40.3, stats.getMax());
        assertEquals(10, stats.getMin());
        assertEquals(100.3, stats.getSum());
        assertEquals(25.075, stats.getAverage());

        // Add values and check again
        transactionService.addTransaction(new Transaction(Double.valueOf(50.7), System.currentTimeMillis()));
        transactionService.updateStats(currentTime, 60000);

        stats = transactionService.getSummarizedStatistics();
        assertEquals(5, stats.getCount());
        assertEquals(50.7, stats.getMax());
        assertEquals(10, stats.getMin());
        assertEquals(151, stats.getSum());
        assertEquals(30.2, stats.getAverage());

    }


}