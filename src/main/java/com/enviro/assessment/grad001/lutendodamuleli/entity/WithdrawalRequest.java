package com.enviro.assessment.grad001.lutendodamuleli.entity;

import com.enviro.assessment.grad001.lutendodamuleli.model.ProductType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @ToString
@Entity
@Table(name = "withdrawal")
public class WithdrawalRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long withdrawalId;
    @JsonIgnore
    @Column(name = "withdraw_from")
    private ProductType productType;
    private Double withdrawalAmount;
    private Double closingAmount;
    private LocalDate withdrawalDate;
    //banking details paid-to
    private String accountNumber;
    private String name;
    private String bankType;
}
