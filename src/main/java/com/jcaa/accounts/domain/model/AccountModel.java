package com.jcaa.accounts.domain.model;

import com.jcaa.accounts.domain.enums.AccountStatus;
import com.jcaa.accounts.domain.valueobject.AccountEmail;
import com.jcaa.accounts.domain.valueobject.AccountId;
import com.jcaa.accounts.domain.valueobject.UserId;
import java.time.LocalDateTime;
import lombok.Value;

@Value
public class AccountModel {

  AccountId id;
  UserId userId;
  AccountEmail email;
  AccountStatus status;
  LocalDateTime createdAt;

  /** Toda cuenta nueva nace activa. */
  public static AccountModel create(final UserId userId, final AccountEmail email) {
    return new AccountModel(
        AccountId.generate(), userId, email, AccountStatus.ACTIVE, LocalDateTime.now());
  }
}
