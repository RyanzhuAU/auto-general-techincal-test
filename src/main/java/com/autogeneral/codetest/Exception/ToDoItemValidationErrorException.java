package com.autogeneral.codetest.Exception;

import com.autogeneral.codetest.representation.ToDoItemValidationErrorRep;
import lombok.Data;

/**
 * Created by ryan.zhu on 7/4/2018.
 */

@Data
public class ToDoItemValidationErrorException extends Exception {

    private ToDoItemValidationErrorRep validationError;

    public ToDoItemValidationErrorException(ToDoItemValidationErrorRep validationError) {
        super();

        this.validationError = validationError;
    }

}
