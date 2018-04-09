package com.autogeneral.codetest.service;

import com.autogeneral.codetest.domain.ToDoItem;

/**
 * Created by ryan.zhu on 7/4/2018.
 */

public interface ToDoItemService {

    ToDoItem getToDoItem(long itemId) throws Exception;

    ToDoItem createToDoItem(String json) throws Exception;

    ToDoItem updateToDoItem(long itemId, String json) throws Exception;

}
