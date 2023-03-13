package com.pwc.interview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ExchangeRatesDto {

    private String table;
    private String currency;
    private String code;
    private List<RateDto> rates = new ArrayList<>();

}
