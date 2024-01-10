package com.enviro.assessment.grad001.lutendodamuleli.entity;

import com.enviro.assessment.grad001.lutendodamuleli.model.ProductType;
import com.enviro.assessment.grad001.lutendodamuleli.model.View;
import com.enviro.assessment.grad001.lutendodamuleli.model.WithdrawalStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "withdrawal")
public class WithdrawalRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(value = {View.New.class})
    private Long withdrawalId;
    @JsonIgnore
    @Column(name = "withdraw_from")
    @JsonView(value = {View.New.class})
    private ProductType productType;
    @JsonView(value = {View.New.class})
    private Double withdrawalAmount;
    @JsonView(value = {View.New.class})
    private Double closingAmount;
    @JsonView(value = {View.New.class})
    private LocalDate withdrawalDate;
    //banking details paid-to
    @JsonView(value = {View.New.class})
    private String accountNumber;
    @JsonView(value = {View.New.class})
    private String name;
    @JsonView(value = {View.New.class})
    private String bankType;
    @Enumerated(EnumType.ORDINAL)
    @JsonView(value = {View.New.class})
    private WithdrawalStatus status;


    public Long getWithdrawalId() {
        return withdrawalId;
    }

    public void setWithdrawalId(Long withdrawalId) {
        this.withdrawalId = withdrawalId;
    }

    public Double getWithdrawalAmount() {
        return withdrawalAmount;
    }

    public void setWithdrawalAmount(Double withdrawalAmount) {
        this.withdrawalAmount = withdrawalAmount;
    }

    public LocalDate getWithdrawalDate() {
        return withdrawalDate;
    }

    public void setWithdrawalDate(LocalDate withdrawalDate) {
        this.withdrawalDate = withdrawalDate;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public WithdrawalStatus getStatus() {
        return status;
    }

    public void setStatus(WithdrawalStatus status) {
        this.status = status;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public Double getClosingAmount() {
        return closingAmount;
    }

    public void setClosingAmount(Double closingAmount) {
        this.closingAmount = closingAmount;
    }
}
