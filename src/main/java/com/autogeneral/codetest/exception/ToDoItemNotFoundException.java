package com.autogeneral.codetest.exception;

import com.autogeneral.codetest.representation.ToDoItemNotFoundErrorRep;
import lombok.Data;

/**
 * Created by ryan.zhu on 7/4/2018.
 */

@Data
public class ToDoItemNotFoundException extends Exception {

    private ToDoItemNotFoundErrorRep notFoundError;

    public ToDoItemNotFoundException(ToDoItemNotFoundErrorRep notFoundError) {
        super();

        this.notFoundError = notFoundError;
    }

}
