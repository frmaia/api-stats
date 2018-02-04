package controllers;

import dtos.StatisticsDTO;
import dtos.TransactionDTO;
import exception.TransactionTimeExceededException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.TransactionService;

import java.util.DoubleSummaryStatistics;

@RestController
@EnableAutoConfiguration
public class StatisticsController {

    @RequestMapping(value = "/statistics", method = RequestMethod.GET)
    public @ResponseBody DoubleSummaryStatistics statistics(){
        //return new StatisticsDTO();

        return TransactionService.getSummarizedStatistics();
    }

    @RequestMapping(value = "/transactions", method = RequestMethod.POST)
    public ResponseEntity<Void> transactions(@RequestBody TransactionDTO transaction){
        //response.
        try {
            TransactionService.processTransaction(transaction);
            return new ResponseEntity<Void>(HttpStatus.CREATED);

        } catch (TransactionTimeExceededException e) {
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        }
        //TransactionService

    }

    //Application Boot
    public static void main(String [] args) throws Exception{
        SpringApplication.run(StatisticsController.class, args);
    }

}
