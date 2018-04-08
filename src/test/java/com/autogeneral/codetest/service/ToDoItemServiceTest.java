package com.autogeneral.codetest.service;

import com.autogeneral.codetest.Constants;
import com.autogeneral.codetest.Exception.ToDoItemNotFoundException;
import com.autogeneral.codetest.Exception.ToDoItemValidationErrorException;
import com.autogeneral.codetest.domain.ToDoItem;
import com.autogeneral.codetest.repository.ToDoItemRepository;
import com.autogeneral.codetest.representation.ToDoItemAddRequestRep;
import com.autogeneral.codetest.representation.ToDoItemNotFoundErrorRep;
import com.autogeneral.codetest.representation.ToDoItemUpdateRequestRep;
import com.autogeneral.codetest.representation.ToDoItemValidationErrorRep;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by ryan.zhu on 7/4/2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ToDoItemServiceTest {
    @Autowired
    private ToDoItemRepository toDoItemRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private ToDoItemService toDoItemService;
    private Integer itemId;

    @Before
    public void setup() throws Exception {
        toDoItemService = new ToDoItemServiceImpl(toDoItemRepository);

        ToDoItem toDoItem = new ToDoItem("test1");
        toDoItem = toDoItemRepository.save(toDoItem);
        itemId = toDoItem.getItemId();
    }

    @Test
    public void getToDoItemTest() throws Exception {
        // test normal scenario
        ToDoItem item = toDoItemService.getToDoItem(itemId);

        assertThat(item.getText(), is("test1"));
        assertThat(item.getIsCompleted(), is(false));
        assertThat(item.getItemId(), is(itemId));

        // test not found scenario
        long itemId = this.toDoItemRepository.count() + 100;

        try {
            toDoItemService.getToDoItem((int)itemId);
        } catch (ToDoItemNotFoundException e) {
            ToDoItemNotFoundErrorRep notFoundError = e.getNotFoundError();
            assertThat(notFoundError.getName(), is(Constants.TODO_ITEM_NOT_FOUND_ERROR_NAME));
            assertThat(notFoundError.getDetails().get(0).getMessage(), is(String.format(Constants.TODO_ITEM_NOT_FOUND_ERROR_MESSAGE, itemId)));
        }
    }

    @Test
    public void createToDoItemTest() throws Exception {
        // test normal scenario
        ToDoItemAddRequestRep addRequestRep = new ToDoItemAddRequestRep("test abcd");
        String json = objectMapper.writeValueAsString(addRequestRep);

        ToDoItem item = toDoItemService.createToDoItem(json);

        assertThat(item.getText(), is("test abcd"));
        assertThat(item.getIsCompleted(), is(false));

        // test text validation error scenario - left boundary
        addRequestRep.setText("");
        json = objectMapper.writeValueAsString(addRequestRep);

        try {
            toDoItemService.createToDoItem(json);
        } catch (ToDoItemValidationErrorException e) {
            ToDoItemValidationErrorRep validationErrorRep = e.getValidationError();
            assertThat(validationErrorRep.getName(), is(Constants.VALIDATION_ERROR_NAME));
            assertThat(validationErrorRep.getDetails().get(0).getMsg(), is(Constants.TEXT_FIELD_LENGTH_ERROR_MESSAGE));
        }

        // test text validation error scenario - right boundary
        addRequestRep.setText("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz");
        json = objectMapper.writeValueAsString(addRequestRep);

        try {
            toDoItemService.createToDoItem(json);
        } catch (ToDoItemValidationErrorException e) {
            ToDoItemValidationErrorRep validationErrorRep = e.getValidationError();
            assertThat(validationErrorRep.getName(), is(Constants.VALIDATION_ERROR_NAME));
            assertThat(validationErrorRep.getDetails().get(0).getMsg(), is(Constants.TEXT_FIELD_LENGTH_ERROR_MESSAGE));
        }
    }

    @Test
    public void updateToDoItemTest() throws Exception {
        // test normal scenario
        ToDoItemUpdateRequestRep updateRequestRep = new ToDoItemUpdateRequestRep();
        updateRequestRep.setText("abcd");
        updateRequestRep.setIsCompleted(true);
        String json = objectMapper.writeValueAsString(updateRequestRep);

        ToDoItem item = toDoItemService.updateToDoItem(this.itemId, json);

        assertThat(item.getText(), is("abcd"));
        assertThat(item.getIsCompleted(), is(true));
        assertThat(item.getItemId(), is(this.itemId));

        // test the scenario with updating isCompleted only
        updateRequestRep.setText(null);
        updateRequestRep.setIsCompleted(false);
        json = objectMapper.writeValueAsString(updateRequestRep);

        item = toDoItemService.updateToDoItem(this.itemId, json);

        assertThat(item.getText(), is("abcd"));
        assertThat(item.getIsCompleted(), is(false));
        assertThat(item.getItemId(), is(this.itemId));

        // test the scenario with updating text only
        updateRequestRep.setText("cdef");
        updateRequestRep.setIsCompleted(null);
        json = objectMapper.writeValueAsString(updateRequestRep);

        item = toDoItemService.updateToDoItem(this.itemId, json);

        assertThat(item.getText(), is("cdef"));
        assertThat(item.getIsCompleted(), is(false));
        assertThat(item.getItemId(), is(this.itemId));

        // test not found scenario
        long itemId = this.toDoItemRepository.count() + 100;

        try {
            toDoItemService.updateToDoItem((int)itemId, json);
        } catch (ToDoItemNotFoundException e) {
            ToDoItemNotFoundErrorRep notFoundError = e.getNotFoundError();
            assertThat(notFoundError.getName(), is(Constants.TODO_ITEM_NOT_FOUND_ERROR_NAME));
            assertThat(notFoundError.getDetails().get(0).getMessage(), is(String.format(Constants.TODO_ITEM_NOT_FOUND_ERROR_MESSAGE, itemId)));
        }

        // test text validation error scenario - left boundary
        updateRequestRep.setText("");
        json = objectMapper.writeValueAsString(updateRequestRep);

        try {
            toDoItemService.updateToDoItem(this.itemId, json);
        } catch (ToDoItemValidationErrorException e) {
            ToDoItemValidationErrorRep validationErrorRep = e.getValidationError();
            assertThat(validationErrorRep.getName(), is(Constants.VALIDATION_ERROR_NAME));
            assertThat(validationErrorRep.getDetails().get(0).getMsg(), is(Constants.TEXT_FIELD_LENGTH_ERROR_MESSAGE));
        }

        // test text validation error scenario - right boundary
        updateRequestRep.setText("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz");
        json = objectMapper.writeValueAsString(updateRequestRep);

        try {
            toDoItemService.updateToDoItem(this.itemId, json);
        } catch (ToDoItemValidationErrorException e) {
            ToDoItemValidationErrorRep validationErrorRep = e.getValidationError();
            assertThat(validationErrorRep.getName(), is(Constants.VALIDATION_ERROR_NAME));
            assertThat(validationErrorRep.getDetails().get(0).getMsg(), is(Constants.TEXT_FIELD_LENGTH_ERROR_MESSAGE));
        }
    }
}
