package com.pwc.interview.controller;

import com.pwc.interview.InterviewApplication;
import com.pwc.interview.dto.AccountDto;
import com.pwc.interview.dto.CreateAccountDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.math.BigDecimal;
import java.util.Currency;

import static io.restassured.RestAssured.with;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = InterviewApplication.class)
class AccountControllerTest {

    private final static String BASE_URI = "http://localhost";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final BigDecimal ACCOUNT_BALANCE = BigDecimal.valueOf(1.11);

    @LocalServerPort
    private int port;

    @BeforeEach
    void configureRestAssured() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;
    }

    @Test
    void shouldCreateAccountAndGet() {
        // when
        Response response = with()
                .body(CreateAccountDto.builder()
                        .firstname(FIRST_NAME)
                        .lastname(LAST_NAME)
                        .baseAccountBalance(ACCOUNT_BALANCE)
                        .build())
                .contentType(ContentType.JSON)
                .when()
                .request("POST", "/api/v1/account");

        // then
        response.then()
                .log()
                .all()
                .assertThat()
                .statusCode(201);
        AccountDto account = response.as(AccountDto.class);
        assertThat(account.getId()).isNotNull();
        assertThat(account.getFirstname()).isEqualTo(FIRST_NAME);
        assertThat(account.getLastname()).isEqualTo(LAST_NAME);
        assertThat(account.getAccountBalance()).hasSize(1);
        assertThat(account.getAccountBalance().get(Currency.getInstance("PLN"))).isEqualTo(ACCOUNT_BALANCE);

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
        AccountDto account2 = response2.as(AccountDto.class);
        assertThat(account2.getId()).isNotNull();
        assertThat(account2.getFirstname()).isEqualTo(FIRST_NAME);
        assertThat(account2.getLastname()).isEqualTo(LAST_NAME);
        assertThat(account2.getAccountBalance()).hasSize(1);
        assertThat(account2.getAccountBalance().get(Currency.getInstance("PLN"))).isEqualTo(ACCOUNT_BALANCE);
    }

}