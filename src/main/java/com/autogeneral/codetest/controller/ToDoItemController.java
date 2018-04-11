package com.autogeneral.codetest.controller;

import com.autogeneral.codetest.domain.ToDoItem;
import com.autogeneral.codetest.exception.ToDoItemNotFoundException;
import com.autogeneral.codetest.exception.ToDoItemValidationErrorException;
import com.autogeneral.codetest.representation.ToDoItemRep;
import com.autogeneral.codetest.service.ToDoItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by ryan.zhu on 7/4/2018.
 */

@RestController
@RequestMapping("/todo")
public class ToDoItemController {

    @Autowired
    private ToDoItemService toDoItemService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * GET /todo/{id} - returns the todo item
     *
     * @param itemId
     * @return todo item and status 200, or status 400
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getToDoItem(@PathVariable("id") String itemId) {
        try {
            ToDoItem item = toDoItemService.getToDoItem(Long.valueOf(itemId));

            return new ResponseEntity(new ToDoItemRep(item), HttpStatus.OK);
        } catch (ToDoItemNotFoundException e) {
            logger.error(e.getMessage());
            return new ResponseEntity(e.getNotFoundError(), HttpStatus.NOT_FOUND);
        } catch (ToDoItemValidationErrorException e) {
            logger.error(e.getMessage());
            return new ResponseEntity(e.getValidationError(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * POST /todo - create the new todo item
     * e.g. with JSON input {"text": "Uulwi ifis halahs gag erh'ongg w'ssh."}
     * create a todo item with the text "Uulwi ifis halahs gag erh'ongg w'ssh." and set complete to false as default
     *
     * @param json
     * @return JSON - ToDoItem object and status 200, or status 400
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createToDoItem(@RequestBody String json) {
        try {
            ToDoItem item = toDoItemService.createToDoItem(json);

            return new ResponseEntity(new ToDoItemRep(item), HttpStatus.OK);
        } catch (ToDoItemValidationErrorException e) {
            logger.error(e.getMessage());
            return new ResponseEntity(e.getValidationError(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * PATCH /{id} - update the selected todo item with partial parameters
     *
     * @param json
     * @param itemId
     * @return status 200 or status 400
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<ToDoItem> updateToDoItem(@RequestBody String json, @PathVariable("id") String itemId) {
        try {
            ToDoItem item = toDoItemService.updateToDoItem(Long.valueOf(itemId), json);

            return new ResponseEntity(new ToDoItemRep(item), HttpStatus.OK);
        } catch (ToDoItemNotFoundException e) {
            logger.error(e.getMessage());
            return new ResponseEntity(e.getNotFoundError(), HttpStatus.NOT_FOUND);
        } catch (ToDoItemValidationErrorException e) {
            logger.error(e.getMessage());
            return new ResponseEntity(e.getValidationError(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}
