package com.enviro.assessment.grad001.lutendodamuleli.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawalNotice {
    private Double currentBalance;
    private Double amountWithdrawn;
    private Double closingBalance;
}
