package com.autogeneral.codetest.representation;

import lombok.Data;

/**
 * Created by ryan.zhu on 7/4/2018.
 */

@Data
public class BalanceTestResultRep {

    private String input;

    private Boolean isBalance;

    public BalanceTestResultRep(String input, Boolean isBalance) {
        this.input = input;
        this.isBalance = isBalance;
    }

    public BalanceTestResultRep() {}
}
