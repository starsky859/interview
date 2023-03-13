package com.pwc.interview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountDto {

    @NotBlank
    private String firstname;
    @NotBlank
    private String lastname;
    @NotNull
    private BigDecimal baseAccountBalance;

}
