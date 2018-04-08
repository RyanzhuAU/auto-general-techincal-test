package com.autogeneral.codetest.representation;

import lombok.Data;

/**
 * Created by ryan.zhu on 7/4/2018.
 */

@Data
public class ToDoItemNotFoundErrorDetailRep {

    private String message;

    public ToDoItemNotFoundErrorDetailRep(String message) {
        this.message = message;
    }

    public ToDoItemNotFoundErrorDetailRep() {}

}
