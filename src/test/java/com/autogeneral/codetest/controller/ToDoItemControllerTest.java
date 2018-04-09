package com.autogeneral.codetest.controller;

import com.autogeneral.codetest.Constants;
import com.autogeneral.codetest.repository.ToDoItemRepository;
import com.autogeneral.codetest.representation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by ryan.zhu on 7/4/2018.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ToDoItemControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ToDoItemRepository toDoItemRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Integer testItemId;

    @Before
    public void setup() throws Exception {
        /*
         TODO: Actually we should use H2 db for this kind of test. As the time limitation, I think it should be enough to use this kind of test.
         */
        ToDoItemAddRequestRep addRequestRep = new ToDoItemAddRequestRep("test 1");

        MvcResult result = this.mockMvc.perform(post("/todo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addRequestRep)))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ToDoItemRep item = objectMapper.readValue(content, ToDoItemRep.class);
        this.testItemId = item.getId();

        addRequestRep = new ToDoItemAddRequestRep("test 2");

        this.mockMvc.perform(post("/todo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addRequestRep)));

        addRequestRep = new ToDoItemAddRequestRep("test 3");

        this.mockMvc.perform(post("/todo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addRequestRep)));
    }

    @Test
    public void getToDoItemTest() throws Exception {
        // test normal scenario
        MvcResult result = this.mockMvc.perform(get("/todo/" + this.testItemId))
                            .andExpect(status().isOk())
                            .andReturn();

        String content = result.getResponse().getContentAsString();

        ToDoItemRep item = objectMapper.readValue(content, ToDoItemRep.class);

        assertThat(item.getText(), is("test 1"));
        assertThat(item.getIsCompleted(), is(false));
        assertThat(item.getId(), is(this.testItemId));

        // test not found scenario
        long itemId = this.toDoItemRepository.count() + 100;

        result = this.mockMvc.perform(get("/todo/" + itemId))
                .andExpect(status().isNotFound())
                .andReturn();

        content = result.getResponse().getContentAsString();

        ToDoItemNotFoundErrorRep notFoundErrorRep = objectMapper.readValue(content, ToDoItemNotFoundErrorRep.class);

        assertThat(notFoundErrorRep.getName(), is(Constants.TODO_ITEM_NOT_FOUND_ERROR_NAME));
        assertThat(notFoundErrorRep.getDetails().get(0).getMessage(), is(String.format(Constants.TODO_ITEM_NOT_FOUND_ERROR_MESSAGE, itemId)));

    }

    @Test
    public void createToDoItemTest() throws Exception {
        // test normal scenario
        ToDoItemAddRequestRep addRequestRep = new ToDoItemAddRequestRep("test abcd");

        MvcResult result = this.mockMvc.perform(post("/todo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addRequestRep)))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ToDoItemRep item = objectMapper.readValue(content, ToDoItemRep.class);

        assertThat(item.getText(), is("test abcd"));
        assertThat(item.getIsCompleted(), is(false));

        // test text validation error scenario - left boundary
        addRequestRep.setText("");

        result = this.mockMvc.perform(patch("/todo/" + this.testItemId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addRequestRep)))
                .andExpect(status().isBadRequest())
                .andReturn();

        content = result.getResponse().getContentAsString();

        ToDoItemValidationErrorRep validationErrorRep = objectMapper.readValue(content, ToDoItemValidationErrorRep.class);

        assertThat(validationErrorRep.getName(), is(Constants.VALIDATION_ERROR_NAME));
        assertThat(validationErrorRep.getDetails().get(0).getMsg(), is(Constants.TEXT_FIELD_LENGTH_ERROR_MESSAGE));

        // test text validation error scenario - right boundary
        addRequestRep.setText("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz");

        result = this.mockMvc.perform(patch("/todo/" + this.testItemId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addRequestRep)))
                .andExpect(status().isBadRequest())
                .andReturn();

        content = result.getResponse().getContentAsString();

        validationErrorRep = objectMapper.readValue(content, ToDoItemValidationErrorRep.class);

        assertThat(validationErrorRep.getName(), is(Constants.VALIDATION_ERROR_NAME));
        assertThat(validationErrorRep.getDetails().get(0).getMsg(), is(Constants.TEXT_FIELD_LENGTH_ERROR_MESSAGE));
    }

    @Test
    public void updateToDoItemTest() throws Exception {
        // test normal scenario
        ToDoItemUpdateRequestRep updateRequestRep = new ToDoItemUpdateRequestRep();
        updateRequestRep.setText("abcd");
        updateRequestRep.setIsCompleted(true);

        MvcResult result = this.mockMvc.perform(patch("/todo/" + this.testItemId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequestRep)))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        ToDoItemRep item = objectMapper.readValue(content, ToDoItemRep.class);

        assertThat(item.getText(), is("abcd"));
        assertThat(item.getIsCompleted(), is(true));
        assertThat(item.getId(), is(this.testItemId));

        // test the scenario with updating isCompleted only
        updateRequestRep.setText(null);
        updateRequestRep.setIsCompleted(false);

        result = this.mockMvc.perform(patch("/todo/" + this.testItemId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequestRep)))
                .andExpect(status().isOk())
                .andReturn();

        content = result.getResponse().getContentAsString();

        item = objectMapper.readValue(content, ToDoItemRep.class);

        assertThat(item.getText(), is("abcd"));
        assertThat(item.getIsCompleted(), is(false));
        assertThat(item.getId(), is(this.testItemId));

        // test the scenario with updating text only
        updateRequestRep.setText("cdef");
        updateRequestRep.setIsCompleted(null);

        result = this.mockMvc.perform(patch("/todo/" + this.testItemId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequestRep)))
                .andExpect(status().isOk())
                .andReturn();

        content = result.getResponse().getContentAsString();

        item = objectMapper.readValue(content, ToDoItemRep.class);

        assertThat(item.getText(), is("cdef"));
        assertThat(item.getIsCompleted(), is(false));
        assertThat(item.getId(), is(this.testItemId));

        // test not found scenario
        long itemId = this.toDoItemRepository.count() + 100;

        result = this.mockMvc.perform(patch("/todo/" + itemId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequestRep)))
                .andExpect(status().isNotFound())
                .andReturn();

        content = result.getResponse().getContentAsString();

        ToDoItemNotFoundErrorRep notFoundErrorRep = objectMapper.readValue(content, ToDoItemNotFoundErrorRep.class);

        assertThat(notFoundErrorRep.getName(), is(Constants.TODO_ITEM_NOT_FOUND_ERROR_NAME));
        assertThat(notFoundErrorRep.getDetails().get(0).getMessage(), is(String.format(Constants.TODO_ITEM_NOT_FOUND_ERROR_MESSAGE, itemId)));

        // test text validation error scenario - left boundary
        updateRequestRep.setText("");

        result = this.mockMvc.perform(patch("/todo/" + this.testItemId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequestRep)))
                .andExpect(status().isBadRequest())
                .andReturn();

        content = result.getResponse().getContentAsString();

        ToDoItemValidationErrorRep validationErrorRep = objectMapper.readValue(content, ToDoItemValidationErrorRep.class);

        assertThat(validationErrorRep.getName(), is(Constants.VALIDATION_ERROR_NAME));
        assertThat(validationErrorRep.getDetails().get(0).getMsg(), is(Constants.TEXT_FIELD_LENGTH_ERROR_MESSAGE));

        // test text validation error scenario - right boundary
        updateRequestRep.setText("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz");

        result = this.mockMvc.perform(patch("/todo/" + this.testItemId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequestRep)))
                .andExpect(status().isBadRequest())
                .andReturn();

        content = result.getResponse().getContentAsString();

        validationErrorRep = objectMapper.readValue(content, ToDoItemValidationErrorRep.class);

        assertThat(validationErrorRep.getName(), is(Constants.VALIDATION_ERROR_NAME));
        assertThat(validationErrorRep.getDetails().get(0).getMsg(), is(Constants.TEXT_FIELD_LENGTH_ERROR_MESSAGE));
    }
}
