package com.pwc.interview.controller;

import com.pwc.interview.InterviewApplication;
import com.pwc.interview.client.ExchangeRateClient;
import com.pwc.interview.dto.*;
import com.pwc.interview.service.AccountService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import static io.restassured.RestAssured.with;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = InterviewApplication.class)
class ExchangeControllerTest {

    private final static String BASE_URI = "http://localhost";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final BigDecimal ACCOUNT_BALANCE = BigDecimal.valueOf(10.10);

    @LocalServerPort
    private int port;

    @Autowired
    private AccountService accountService;

    @Mock
    private ExchangeRateClient exchangeRateClient;

    @BeforeEach
    void configureRestAssured() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;
        Mockito.when(exchangeRateClient.getCurrentExchangeRate(any(), any())).thenReturn(ExchangeRatesDto.builder()
                .rates(List.of(RateDto.builder()
                        .mid(BigDecimal.valueOf(2.22))
                        .build()))
                .build());
    }

    @Test
    void shouldExchangeMoney() {
        // given
        var account = accountService.create(CreateAccountDto.builder()
                .firstname(FIRST_NAME)
                .firstname(LAST_NAME)
                .baseAccountBalance(ACCOUNT_BALANCE)
                .build());

        // when
        Response response = with()
                .body(ExchangeDto.builder()
                        .accountId(account.getId())
                        .currencyFrom(Currency.getInstance("PLN"))
                        .currencyTo(Currency.getInstance("USD"))
                        .value(BigDecimal.valueOf(2.11))
                        .build())
                .contentType(ContentType.JSON)
                .when()
                .request("POST", "/api/v1/exchange");

        // then
        response.then()
                .log()
                .all()
                .assertThat()
                .statusCode(200);

        // when
        Response response2 = with()
                .contentType(ContentType.JSON)
                .when()
                .request("GET", "/api/v1/account/" + account.getId());

        // then
        response2.then()
                .log()
                .all()
                .assertThat()
                .statusCode(200);
        AccountDto accountResponse2 = response2.as(AccountDto.class);
        assertThat(accountResponse2.getAccountBalance()).hasSize(2);
        assertThat(accountResponse2.getAccountBalance().get(Currency.getInstance("PLN"))).isEqualTo(BigDecimal.valueOf(7.99));
        assertThat(accountResponse2.getAccountBalance().get(Currency.getInstance("USD"))).isEqualTo(BigDecimal.valueOf(0.48));
    }

}