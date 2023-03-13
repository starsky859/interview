package com.pwc.interview.service;

import com.pwc.interview.dto.AccountDto;
import com.pwc.interview.dto.CreateAccountDto;
import com.pwc.interview.mapper.AccountMapper;
import com.pwc.interview.model.AccountModel;
import com.pwc.interview.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Currency;

import static com.pwc.interview.model.Constants.MAIN_CURRENCY;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Transactional
    public AccountDto create(CreateAccountDto request) {
        AccountModel accountModel = accountMapper.map(request);
        accountModel.getAccountBalance().put(Currency.getInstance(MAIN_CURRENCY), request.getBaseAccountBalance());
        return accountMapper.map(accountRepository.save(accountModel));
    }

    @Transactional(readOnly = true)
    public AccountDto get(Long accountId) {
        return accountRepository.findById(accountId)
                .map(accountMapper::map)
                .orElseThrow(() -> new EntityNotFoundException("Account id=" + accountId + " does not exist"));
    }
}
