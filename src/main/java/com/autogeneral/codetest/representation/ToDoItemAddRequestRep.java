package com.autogeneral.codetest.representation;

import lombok.Data;

/**
 * Created by ryan.zhu on 7/4/2018.
 */

@Data
public class ToDoItemAddRequestRep {

    private String text;

    public ToDoItemAddRequestRep(String text) {
        this.text = text;
    }

    public ToDoItemAddRequestRep() {}

}
