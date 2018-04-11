package com.autogeneral.codetest.controller;

import com.autogeneral.codetest.exception.ToDoItemValidationErrorException;
import com.autogeneral.codetest.representation.BalanceTestResultRep;
import com.autogeneral.codetest.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ryan.zhu on 7/4/2018.
 */

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * GET /state - returns the current state
     *
     * @param input
     * @return state string and status 200, or status 400
     */
    @RequestMapping(value = "/validateBrackets", method = RequestMethod.GET)
    public ResponseEntity<BalanceTestResultRep> validateBrackets(@RequestParam("input") String input) {
        try {
            Boolean isBalance = taskService.validateBrackets(input);
            BalanceTestResultRep result = new BalanceTestResultRep(input, isBalance);

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (ToDoItemValidationErrorException e) {
            logger.error(e.getMessage());
            return new ResponseEntity(e.getValidationError(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

}
