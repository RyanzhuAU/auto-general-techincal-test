package com.autogeneral.codetest.representation;

import lombok.Data;

/**
 * Created by ryan.zhu on 7/4/2018.
 */

@Data
public class ToDoItemUpdateRequestRep {

    private String text;

    private Boolean isCompleted;

    public ToDoItemUpdateRequestRep() {}
}
