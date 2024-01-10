package com.enviro.assessment.grad001.lutendodamuleli.model;

import com.fasterxml.jackson.annotation.JsonView;

public enum ProductType {
    RETIREMENT("Retirement"),
    SAVINGS("Savings");

    @JsonView(value = {View.Base.class})
    private String value;


    ProductType(String value) {
        this.value = value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
