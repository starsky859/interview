package com.pwc.interview.repository;

import com.pwc.interview.model.AccountModel;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<AccountModel, Long> {
}
