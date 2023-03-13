package com.pwc.interview.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "ACCOUNT")
@Data
@NoArgsConstructor
public class AccountModel {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "FIRST_NAME")
    private String firstname;
    @Column(name = "LAST_NAME")
    private String lastname;
    @Column(name = "ACCOUNT_BALANCE")
    @ElementCollection
    @CollectionTable(name = "ACCOUNT_CURRENCY_AMOUNT",
            joinColumns = {@JoinColumn(name = "ACCOUNT_ID", referencedColumnName = "id")})
    @MapKeyColumn(name = "CURRENCY")
    private Map<Currency, BigDecimal> accountBalance = new HashMap<>();
}
