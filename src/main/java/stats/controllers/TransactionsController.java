package stats.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import stats.dtos.Transaction;
import stats.exceptions.TransactionTimeExceededException;
import stats.exceptions.TransactionTimeInTheFutureException;
import stats.services.TransactionService;

import java.util.DoubleSummaryStatistics;

@RestController
public class TransactionsController {

    @Autowired private TransactionService transactionService;

    @RequestMapping(value = "/transactions", method = RequestMethod.POST)
    public ResponseEntity<Void> transactions(@Validated @RequestBody Transaction transaction){
        try {
            transactionService.addTransaction(transaction);
            return new ResponseEntity<Void>(HttpStatus.CREATED);

        } catch (TransactionTimeExceededException | TransactionTimeInTheFutureException e) {
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(value = "/statistics", method = RequestMethod.GET)
    public @ResponseBody DoubleSummaryStatistics getTransactionsStatistics(){
        return transactionService.getSummarizedStatistics();
    }

}
