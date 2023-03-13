package com.pwc.interview.service;

import com.pwc.interview.dto.AccountDto;
import com.pwc.interview.dto.CreateAccountDto;
import com.pwc.interview.mapper.AccountMapper;
import com.pwc.interview.model.AccountModel;
import com.pwc.interview.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Currency;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    private static final Long ACCOUNT_ID = 1L;

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountMapper accountMapper;
    @InjectMocks
    private AccountService accountService;

    @Test
    void shouldCreateAccount() {
        // given
        var request = new CreateAccountDto();
        var account = new AccountModel();
        when(accountMapper.map(request)).thenReturn(account);

        // when
        accountService.create(request);

        // then
        assertThat(account.getAccountBalance()).hasSize(1);
        assertThat(account.getAccountBalance().get(Currency.getInstance("PLN"))).isEqualTo(request.getBaseAccountBalance());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void shouldGetAccount() {
        // given
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(new AccountModel()));
        when(accountMapper.map(any(AccountModel.class))).thenReturn(new AccountDto());

        // when
        var result = accountService.get(ACCOUNT_ID);

        // then
        assertThat(result).isNotNull();
    }

}