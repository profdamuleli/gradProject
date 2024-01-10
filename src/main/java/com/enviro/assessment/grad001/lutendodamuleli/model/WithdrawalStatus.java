package com.enviro.assessment.grad001.lutendodamuleli.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;

@Getter
@JsonView(value = {View.New.class})
public enum WithdrawalStatus {
    IN_PROGRESS,
    COMPLETED
}
