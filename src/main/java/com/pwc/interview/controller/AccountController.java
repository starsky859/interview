package com.pwc.interview.controller;

import com.pwc.interview.dto.AccountDto;
import com.pwc.interview.dto.CreateAccountDto;
import com.pwc.interview.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public AccountDto create(@Validated @RequestBody CreateAccountDto request) {
        return accountService.create(request);
    }

    @GetMapping("{accountId}")
    public AccountDto get(@PathVariable("accountId") Long accountId) {
        return accountService.get(accountId);
    }

}