package com.autogeneral.codetest.service;

import com.autogeneral.codetest.Constants;
import com.autogeneral.codetest.exception.ToDoItemNotFoundException;
import com.autogeneral.codetest.Utils;
import com.autogeneral.codetest.domain.ToDoItem;
import com.autogeneral.codetest.repository.ToDoItemRepository;
import com.autogeneral.codetest.representation.ToDoItemAddRequestRep;
import com.autogeneral.codetest.representation.ToDoItemNotFoundErrorDetailRep;
import com.autogeneral.codetest.representation.ToDoItemNotFoundErrorRep;
import com.autogeneral.codetest.representation.ToDoItemUpdateRequestRep;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * Created by ryan.zhu on 7/4/2018.
 */

@Service
public class ToDoItemServiceImpl implements ToDoItemService {
    @Autowired
    private ToDoItemRepository toDoItemRepository;

    private ObjectMapper mapper = new ObjectMapper();

    public ToDoItemServiceImpl(ToDoItemRepository toDoItemRepository) {
        this.toDoItemRepository = toDoItemRepository;
    }

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * @param itemId
     * @return the todo item
     * @throws Exception
     */
    public ToDoItem getToDoItem(long itemId) throws Exception {
        logger.info("Start getting selected todo item");

        ToDoItem item = toDoItemRepository.findByItemId(itemId);
        if (checkItemExist(item, itemId)) {
            String text = item.getText();
            Utils.inputTextValidation(text);
        }

        return item;
    }

    /**
     * @param json
     * @return the new todo item
     * @throws Exception
     */
    public ToDoItem createToDoItem(String json) throws Exception {
        logger.info("Start creating the new todo item.");

        ToDoItemAddRequestRep addRequest = this.mapper.readValue(json, ToDoItemAddRequestRep.class);
        String text = addRequest.getText();
        ToDoItem item = new ToDoItem(text);

        if (Utils.inputTextValidation(text)) {
            item = toDoItemRepository.save(item);
        }

        return item;
    }

    /**
     * @param itemId
     * @param json
     * @return the updated to do item
     * @throws Exception
     */
    public ToDoItem updateToDoItem(long itemId, String json) throws Exception {
        logger.info("Start updating the selected todo item.");

        ToDoItem item = toDoItemRepository.findByItemId(itemId);

        if (checkItemExist(item, itemId)) {
            ToDoItemUpdateRequestRep updateRequest = this.mapper.readValue(json, ToDoItemUpdateRequestRep.class);

            String text = updateRequest.getText();
            Boolean isCompleted = updateRequest.getIsCompleted();

            if (text != null) {
                Utils.inputTextValidation(text);
                item.setText(text);
            }

            if (isCompleted != null) {
                item.setIsCompleted(isCompleted);
            }

            toDoItemRepository.save(item);
        }

        return item;

    }

    /**
     * This function is used to check if the item exist and handle the exception.
     * @param item
     * @param itemId
     * @return true if the item is existed, or throw ToDoItemNotFoundException.
     * @throws ToDoItemNotFoundException
     */
    private Boolean checkItemExist(ToDoItem item, long itemId) throws ToDoItemNotFoundException {
        if (item == null) {
            String errorMsg = String.format(Constants.TODO_ITEM_NOT_FOUND_ERROR_MESSAGE, itemId);
            ToDoItemNotFoundErrorDetailRep errorDetailRep = new ToDoItemNotFoundErrorDetailRep(errorMsg);

            ToDoItemNotFoundErrorRep notFoundErrorRep = new ToDoItemNotFoundErrorRep(Constants.TODO_ITEM_NOT_FOUND_ERROR_NAME, Arrays.asList(errorDetailRep));

            throw new ToDoItemNotFoundException(notFoundErrorRep);
        }

        return true;
    }
}
