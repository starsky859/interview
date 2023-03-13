package com.pwc.interview.validator;

import com.pwc.interview.dto.ExchangeDto;
import com.pwc.interview.exception.ValidationException;
import com.pwc.interview.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Currency;
import java.util.Set;

import static com.pwc.interview.model.Constants.MAIN_CURRENCY;

@Component
@RequiredArgsConstructor
public class ExchangeValidator {

    private static final Set<Currency> ALLOWED_CURRENCIES = Set.of(Currency.getInstance(MAIN_CURRENCY), Currency.getInstance("USD"));

    private final AccountService accountService;

    public void validate(ExchangeDto request) {
        var account = accountService.get(request.getAccountId());
        if (account == null) {
            throw new ValidationException("Account id=" + request.getAccountId() + " does not exist");
        }
        if (account.getAccountBalance().get(request.getCurrencyFrom()) == null) {
            throw new ValidationException("Account does not contain currencyFrom balance");
        }
        if (account.getAccountBalance().get(request.getCurrencyFrom()).compareTo(request.getValue()) < 0) {
            throw new ValidationException("Account currencyFrom balance is less than requested value");
        }
        if (request.getCurrencyTo().equals(request.getCurrencyFrom())) {
            throw new ValidationException("Currency from and to must be different");
        }
        if (!ALLOWED_CURRENCIES.contains(request.getCurrencyTo()) || !ALLOWED_CURRENCIES.contains(request.getCurrencyFrom())) {
            throw new ValidationException("Supported currencies are: " + ALLOWED_CURRENCIES);
        }
    }
}
