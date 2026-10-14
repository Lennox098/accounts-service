package com.jcaa.accounts.application.service.mapper;

import com.jcaa.accounts.application.service.dto.command.CreateAccountCommand;
import com.jcaa.accounts.domain.model.AccountModel;
import com.jcaa.accounts.domain.valueobject.AccountEmail;
import com.jcaa.accounts.domain.valueobject.UserId;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AccountApplicationMapper {

  public AccountModel fromCreateCommandToModel(final CreateAccountCommand command) {
    return AccountModel.create(new UserId(command.userId()), new AccountEmail(command.email()));
  }
}
