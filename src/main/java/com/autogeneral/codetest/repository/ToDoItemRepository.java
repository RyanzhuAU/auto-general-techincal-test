package com.autogeneral.codetest.repository;

import com.autogeneral.codetest.domain.ToDoItem;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by ryan.zhu on 7/4/2018.
 */

public interface ToDoItemRepository extends CrudRepository<ToDoItem, String> {

    ToDoItem findByItemId(Integer itemId);

    ToDoItem save(ToDoItem item);
}
