package com.enviro.assessment.grad001.lutendodamuleli.entity;

import com.enviro.assessment.grad001.lutendodamuleli.model.ProductType;
import com.enviro.assessment.grad001.lutendodamuleli.model.View;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(value = {View.Base.class})
    private Long productId;
    @Enumerated(EnumType.ORDINAL)
    @JsonView(value = {View.Base.class})
    private ProductType type;
    @JsonView(value = {View.Base.class})
    private String name;
    @JsonView(value = {View.Base.class})
    private Double currentBalance;
    @JsonView(value = {View.Base.class})
    private Double previousBalance;
}
