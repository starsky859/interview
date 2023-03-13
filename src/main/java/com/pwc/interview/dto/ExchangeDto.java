package com.pwc.interview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Currency;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeDto {

    @NotNull
    private Currency currencyFrom;
    @NotNull
    private Currency currencyTo;
    @NotNull
    private BigDecimal value;
    @NotNull
    private Long accountId;

}
