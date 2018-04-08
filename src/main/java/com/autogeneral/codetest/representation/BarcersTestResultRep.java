package com.autogeneral.codetest.representation;

import lombok.Data;

/**
 * Created by ryan.zhu on 7/4/2018.
 */

@Data
public class BarcersTestResultRep {

    private String input;

    private Boolean result;

    private Boolean expected;

    private Boolean isCorrect;

}
