package com.autogeneral.codetest.representation;

import lombok.Data;

import java.util.List;

/**
 * Created by ryan.zhu on 7/4/2018.
 */

@Data
public class ToDoItemValidationErrorRep {

    private List<ToDoItemValidationErrorDetailRep> details;

    private String name;

    public ToDoItemValidationErrorRep(String name, List<ToDoItemValidationErrorDetailRep> details) {
        this.name = name;
        this.details = details;
    }

    public ToDoItemValidationErrorRep() {}
}
