package com.autogeneral.codetest.service;

import com.autogeneral.codetest.Utils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Stack;

/**
 * Created by ryan.zhu on 7/4/2018.
 */

@Service
public class TaskServiceImpl implements TaskService {

    public Boolean validateBrackets(String input) throws Exception {
        Boolean isBalance = false;

        String brackets = "()[]{}";

        if (Utils.inputTextValidation(input)) {
            Stack<Character> stack = new Stack<>();

            /*
             * Compare with the character in the stack
             */
            input.chars()
                    .mapToObj( c -> (char) c)
                    .forEach( c -> {
                            if (brackets.indexOf(c) >= 0) {
                                if (stack.isEmpty()) {
                                    stack.push(c);
                                }
                                else {
                                    Character c1 = stack.peek();
                                    Character c2 = c;

                                    if ((StringUtils.equals(c1.toString(), "(") && StringUtils.equals(c2.toString(), ")"))
                                            || (StringUtils.equals(c1.toString(), "[") && StringUtils.equals(c2.toString(), "]"))
                                            || (StringUtils.equals(c1.toString(), "{") && StringUtils.equals(c2.toString(), "}"))) {
                                        stack.pop();
                                    } else {
                                        stack.push(c2);
                                    }
                                }
                            }
                        });

            isBalance = stack.isEmpty();
        }

        return isBalance;
    }
}
