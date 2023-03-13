package com.pwc.interview.service;

import com.pwc.interview.client.ExchangeRateClient;
import com.pwc.interview.dto.ExchangeDto;
import com.pwc.interview.dto.ExchangeRatesDto;
import com.pwc.interview.dto.RateDto;
import com.pwc.interview.exception.ApiException;
import com.pwc.interview.exception.ValidationException;
import com.pwc.interview.model.AccountModel;
import com.pwc.interview.repository.AccountRepository;
import com.pwc.interview.validator.ExchangeValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

import static com.pwc.interview.client.ExchangeRateClient.DEFAULT_TABLE_TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExchangeServiceTest {

    private static final Long ACCOUNT_ID = 1L;

    @Mock
    private ExchangeValidator exchangeValidator;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private ExchangeRateClient currencyRateClient;
    @InjectMocks
    private ExchangeService exchangeService;

    @Test
    void shouldExchange() {
        // given
        var account = new AccountModel();
        account.getAccountBalance().put(Currency.getInstance("PLN"), BigDecimal.valueOf(55.00));
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(currencyRateClient.getCurrentExchangeRate(DEFAULT_TABLE_TYPE, "USD")).thenReturn(getExchangeRateResponse());
        var request = getBasicRequest();

        // when
        exchangeService.exchange(request);

        // then
        verify(exchangeValidator, times(1)).validate(any());
        assertThat(account.getAccountBalance().get(Currency.getInstance("PLN"))).isEqualTo(BigDecimal.valueOf(53.0));
        assertThat(account.getAccountBalance().get(Currency.getInstance("USD"))).isEqualTo(BigDecimal.valueOf(0.67));
    }

    @Test
    void shouldThrowExceptionOnMissingRateFromExternalApi() {
        // given
        var account = new AccountModel();
        account.getAccountBalance().put(Currency.getInstance("PLN"), BigDecimal.valueOf(55.00));
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(currencyRateClient.getCurrentExchangeRate(DEFAULT_TABLE_TYPE, "USD")).thenReturn(ExchangeRatesDto.builder()
                .rates(List.of())
                .build());
        var request = getBasicRequest();

        // when
        ApiException thrown = Assertions.assertThrows(ApiException.class, () -> exchangeService.exchange(request));

        // then
        assertThat(thrown.getMessage()).isEqualTo("Cannot get rate for currency=USD");
        verify(exchangeValidator, times(1)).validate(any());
    }

    @Test
    void shouldThrowExceptionOnInvalidAccount() {
        // given
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.empty());
        var request = getBasicRequest();

        // when
        ValidationException thrown = Assertions.assertThrows(ValidationException.class, () -> exchangeService.exchange(request));

        // then
        assertThat(thrown.getMessage()).isEqualTo("Cannot find account id=" + ACCOUNT_ID);
        verify(exchangeValidator, times(1)).validate(any());
    }

    private ExchangeDto getBasicRequest() {
        return ExchangeDto.builder()
                .currencyFrom(Currency.getInstance("PLN"))
                .currencyTo(Currency.getInstance("USD"))
                .value(BigDecimal.valueOf(2.00))
                .accountId(ACCOUNT_ID)
                .build();
    }

    private ExchangeRatesDto getExchangeRateResponse() {
        return ExchangeRatesDto.builder()
                .rates(List.of(RateDto.builder()
                        .mid(BigDecimal.valueOf(3.00))
                        .build()))
                .build();
    }

}