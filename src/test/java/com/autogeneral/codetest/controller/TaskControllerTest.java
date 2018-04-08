package com.autogeneral.codetest.controller;

import com.autogeneral.codetest.Constants;
import com.autogeneral.codetest.representation.BalanceTestResultRep;
import com.autogeneral.codetest.representation.ToDoItemValidationErrorRep;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by ryan.zhu on 7/4/2018.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getToDoItemTest() throws Exception {
        // test normal scenario
        MvcResult result = this.mockMvc.perform(get("/tasks/validateBrackets?input=abcd"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        BalanceTestResultRep item = objectMapper.readValue(content, BalanceTestResultRep.class);

        assertThat(item.getInput(), is("abcd"));
        assertThat(item.getIsBalance(), is(true));

        result = this.mockMvc.perform(get("/tasks/validateBrackets?input=a1bcd{}()[]"))
                .andExpect(status().isOk())
                .andReturn();

        content = result.getResponse().getContentAsString();

        item = objectMapper.readValue(content, BalanceTestResultRep.class);

        assertThat(item.getInput(), is("a1bcd{}()[]"));
        assertThat(item.getIsBalance(), is(true));

        result = this.mockMvc.perform(get("/tasks/validateBrackets?input=a1bcd{()[]"))
                .andExpect(status().isOk())
                .andReturn();

        content = result.getResponse().getContentAsString();

        item = objectMapper.readValue(content, BalanceTestResultRep.class);

        assertThat(item.getInput(), is("a1bcd{()[]"));
        assertThat(item.getIsBalance(), is(false));

        result = this.mockMvc.perform(get("/tasks/validateBrackets?input=a1bcd{{)[]"))
                .andExpect(status().isOk())
                .andReturn();

        content = result.getResponse().getContentAsString();

        item = objectMapper.readValue(content, BalanceTestResultRep.class);

        assertThat(item.getInput(), is("a1bcd{{)[]"));
        assertThat(item.getIsBalance(), is(false));

        // test text validation error scenario - left boundary
        result = this.mockMvc.perform(get("/tasks/validateBrackets?input="))
                .andExpect(status().isBadRequest())
                .andReturn();

        content = result.getResponse().getContentAsString();

        ToDoItemValidationErrorRep validationErrorRep = objectMapper.readValue(content, ToDoItemValidationErrorRep.class);

        assertThat(validationErrorRep.getName(), is(Constants.VALIDATION_ERROR_NAME));
        assertThat(validationErrorRep.getDetails().get(0).getMsg(), is(Constants.TEXT_FIELD_LENGTH_ERROR_MESSAGE));

        // test text validation error scenario - right boundary
        result = this.mockMvc.perform(get("/tasks/validateBrackets?input=abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"))
                .andExpect(status().isBadRequest())
                .andReturn();

        content = result.getResponse().getContentAsString();

        validationErrorRep = objectMapper.readValue(content, ToDoItemValidationErrorRep.class);

        assertThat(validationErrorRep.getName(), is(Constants.VALIDATION_ERROR_NAME));
        assertThat(validationErrorRep.getDetails().get(0).getMsg(), is(Constants.TEXT_FIELD_LENGTH_ERROR_MESSAGE));
    }
}
