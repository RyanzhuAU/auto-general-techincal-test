package com.autogeneral.codetest.domain;

import com.autogeneral.codetest.converter.LocalDateTimeConverter;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by ryan.zhu on 7/4/2018.
 */

@Data
@Entity
@Table(name = "to_do_item")
public class ToDoItem {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long itemId;

    private String text;

    private Boolean isCompleted;

    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime createAt;

    public ToDoItem() {

    }

    public ToDoItem(String text) {
        this(text, null, null);
    }

    public ToDoItem(String text, Boolean isCompleted, Date createAt) {
        this.text = text;

        if (isCompleted != null) {
            this.isCompleted = isCompleted;
        }
        else {
            this.isCompleted = false;
        }

        if (createAt == null) {
            this.createAt = LocalDateTime.now();
        }
    }
}
