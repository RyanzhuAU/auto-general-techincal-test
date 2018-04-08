package com.autogeneral.codetest.representation;

import com.autogeneral.codetest.domain.ToDoItem;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Created by ryan.zhu on 7/4/2018.
 */

@Data
public class ToDoItemRep {

    private Integer id;

    private String text;

    private Boolean isCompleted;

    private String createAt;

    public ToDoItemRep() {

    }

    public ToDoItemRep(ToDoItem item) {
        this.id = item.getItemId();
        this.text = item.getText();
        this.isCompleted = item.getIsCompleted();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        this.createAt = format.format(item.getCreateAt()).toString();
    }
}
