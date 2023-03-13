package com.pwc.interview.service;

import com.pwc.interview.client.ExchangeRateClient;
import com.pwc.interview.dto.ExchangeDto;
import com.pwc.interview.dto.RateDto;
import com.pwc.interview.exception.ApiException;
import com.pwc.interview.exception.ValidationException;
import com.pwc.interview.repository.AccountRepository;
import com.pwc.interview.validator.ExchangeValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Optional;

import static com.pwc.interview.client.ExchangeRateClient.DEFAULT_TABLE_TYPE;
import static com.pwc.interview.model.Constants.MAIN_CURRENCY;

@Service
@RequiredArgsConstructor
public class ExchangeService {

    private final ExchangeValidator exchangeValidator;
    private final AccountRepository accountRepository;
    private final ExchangeRateClient currencyRateClient;

    @Transactional
    public void exchange(ExchangeDto request) {
        exchangeValidator.validate(request);
        var account = accountRepository.findById(request.getAccountId()).orElseThrow(() -> new ValidationException("Cannot find account id=" + request.getAccountId()));
        var balanceFrom = Optional.ofNullable(account.getAccountBalance().get(request.getCurrencyFrom())).orElse(BigDecimal.ZERO);
        var balanceTo = Optional.ofNullable(account.getAccountBalance().get(request.getCurrencyTo())).orElse(BigDecimal.ZERO);

        account.getAccountBalance().put(request.getCurrencyFrom(), balanceFrom.subtract(request.getValue()));

        var exchangeRateTo = getRate(request.getCurrencyFrom(), request.getCurrencyTo(), request.getValue());
        account.getAccountBalance().put(request.getCurrencyTo(), balanceTo.add(exchangeRateTo));
    }

    private BigDecimal getRate(Currency currencyFrom, Currency currencyTo, BigDecimal value) {
        if (currencyFrom.getCurrencyCode().equals(MAIN_CURRENCY)) {
            return value.divide(getExchangeRate(currencyTo), 2, RoundingMode.HALF_UP);
        }
        var exchangeRateFrom = getExchangeRate(currencyFrom);
        var valueInMainCurrency = value.multiply(exchangeRateFrom);
        if (currencyTo.getCurrencyCode().equals(MAIN_CURRENCY)) {
            return valueInMainCurrency;
        }
        return valueInMainCurrency.divide(getExchangeRate(currencyTo), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal getExchangeRate(Currency currency) {
        return currencyRateClient.getCurrentExchangeRate(DEFAULT_TABLE_TYPE, currency.getCurrencyCode())
                .getRates().stream()
                .findFirst()
                .map(RateDto::getMid)
                .orElseThrow(() -> new ApiException("Cannot get rate for currency=" + currency));
    }
}
