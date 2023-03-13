package com.pwc.interview.client;

import com.pwc.interview.dto.ExchangeRatesDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(value = "nbpExchangeRateClient", url = "${feign.client.config.nbp.url}")
public interface ExchangeRateClient {

    String DEFAULT_TABLE_TYPE = "A";

    @GetMapping(value = "/api/exchangerates/rates/{table}/{currencyCode}", consumes = APPLICATION_JSON_VALUE)
    ExchangeRatesDto getCurrentExchangeRate(@PathVariable("table") String table, @PathVariable("currencyCode") String currencyCode);

}
