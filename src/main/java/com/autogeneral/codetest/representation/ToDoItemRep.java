package com.autogeneral.codetest.representation;

import com.autogeneral.codetest.domain.ToDoItem;
import lombok.Data;

import java.time.format.DateTimeFormatter;

/**
 * Created by ryan.zhu on 7/4/2018.
 */

@Data
public class ToDoItemRep {

    private long id;

    private String text;

    private Boolean isCompleted;

    private String createAt;

    public ToDoItemRep() {

    }

    public ToDoItemRep(ToDoItem item) {
        this.id = item.getItemId();
        this.text = item.getText();
        this.isCompleted = item.getIsCompleted();

        this.createAt = item.getCreateAt().format(DateTimeFormatter.ISO_DATE_TIME);

    }
}
