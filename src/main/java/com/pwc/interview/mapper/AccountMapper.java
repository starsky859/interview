package com.pwc.interview.mapper;

import com.pwc.interview.dto.AccountDto;
import com.pwc.interview.dto.CreateAccountDto;
import com.pwc.interview.model.AccountModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountDto map(AccountModel accountModel);

    AccountModel map(CreateAccountDto createAccountDto);
}
