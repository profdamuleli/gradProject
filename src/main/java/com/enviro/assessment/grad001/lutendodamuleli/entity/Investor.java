package com.enviro.assessment.grad001.lutendodamuleli.entity;

import com.enviro.assessment.grad001.lutendodamuleli.model.View;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter @ToString
@Entity
@Table(name = "investors")
public class Investor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(value = {View.Base.class})
    private Long id;
    private String firstname;
    private String lastname;
    private LocalDate birth_date;
    private String idNo;
    private Integer acctNo;
    private String physicalAddr;
    private String cellNo;
    private String emailAddr;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Product> products;
}
