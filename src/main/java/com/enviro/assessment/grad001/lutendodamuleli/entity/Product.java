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
    private Long productId;
    @Enumerated(EnumType.ORDINAL)
    private ProductType type;
    private String name;
    private Double currentBalance;
    private Double previousBalance;
}
