package com.autogeneral.codetest;

import com.autogeneral.codetest.Exception.ToDoItemValidationErrorException;
import com.autogeneral.codetest.representation.ToDoItemValidationErrorDetailRep;
import com.autogeneral.codetest.representation.ToDoItemValidationErrorRep;

import java.util.Arrays;

public class Utils {
    public static Boolean inputTextValidation(String text) throws Exception {
        if (text == null) {
            ToDoItemValidationErrorDetailRep validationErrorDetail = new ToDoItemValidationErrorDetailRep("params", "text", Constants.TEXT_FIELD_NOT_DEFINED_ERROR_MESSAGE, "");
            ToDoItemValidationErrorRep validationError = new ToDoItemValidationErrorRep(Constants.VALIDATION_ERROR_NAME, Arrays.asList(validationErrorDetail));

            throw new ToDoItemValidationErrorException(validationError);
        } else if (!checkTextLength(text)) {
            ToDoItemValidationErrorDetailRep validationErrorDetail = new ToDoItemValidationErrorDetailRep("params", "text", Constants.TEXT_FIELD_LENGTH_ERROR_MESSAGE, text);
            ToDoItemValidationErrorRep validationError = new ToDoItemValidationErrorRep(Constants.VALIDATION_ERROR_NAME, Arrays.asList(validationErrorDetail));

            throw new ToDoItemValidationErrorException(validationError);
        }

        return true;
    }

    public static Boolean checkTextLength(String input) {
        int length = input.length();

        if (length >= 1 && length <= 50) {
            return true;
        } else {
            return false;
        }
    }
}
