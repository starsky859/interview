package com.pwc.interview.validator;

import com.pwc.interview.dto.AccountDto;
import com.pwc.interview.dto.ExchangeDto;
import com.pwc.interview.exception.ValidationException;
import com.pwc.interview.service.AccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeValidatorTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private ExchangeValidator exchangeValidator;
    private static final Long ACCOUNT_ID = 1L;

    @ParameterizedTest
    @MethodSource("validationDataProvider")
    void shouldReturnValidationExceptionOnNotExistingCountry(Currency currencyFrom, Currency currencyTo, AccountDto account, String exceptionMessage) {
        // given
        var request = ExchangeDto.builder()
                .value(BigDecimal.TEN)
                .accountId(ACCOUNT_ID)
                .currencyFrom(currencyFrom)
                .currencyTo(currencyTo)
                .build();
        when(accountService.get(ACCOUNT_ID)).thenReturn(account);

        // when
        ValidationException thrown = Assertions.assertThrows(ValidationException.class, () -> {
            exchangeValidator.validate(request);
        });

        // then
        assertThat(thrown.getMessage()).isEqualTo(exceptionMessage);
    }

    private static Stream<Arguments> validationDataProvider() {
        return Stream.of(
                Arguments.of(Currency.getInstance("PLN"), Currency.getInstance("PLN"), AccountDto.builder()
                        .accountBalance(Map.of())
                        .build(), "Account does not contain currencyFrom balance"),
                Arguments.of(Currency.getInstance("PLN"), Currency.getInstance("USD"), null, "Account id=1 does not exist"),
                Arguments.of(Currency.getInstance("PLN"), Currency.getInstance("USD"), AccountDto.builder()
                        .accountBalance(Map.of(Currency.getInstance("PLN"), BigDecimal.valueOf(1L)))
                        .build(), "Account currencyFrom balance is less than requested value"),
                Arguments.of(Currency.getInstance("PLN"), Currency.getInstance("EUR"), AccountDto.builder()
                        .accountBalance(Map.of(Currency.getInstance("PLN"), BigDecimal.valueOf(20L)))
                        .build(), "Supported currencies are: [USD, PLN]")
        );
    }

}