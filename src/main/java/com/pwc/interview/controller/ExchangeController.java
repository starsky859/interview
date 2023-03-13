package com.pwc.interview.controller;

import com.pwc.interview.dto.ExchangeDto;
import com.pwc.interview.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/exchange")
@RequiredArgsConstructor
public class ExchangeController {

    private final ExchangeService exchangeService;

    @PostMapping
    public void exchange(@Validated @RequestBody ExchangeDto request) {
        exchangeService.exchange(request);
    }

}