package com.jcaa.accounts.infrastructure.entrypoint.rest.mapper;

import com.jcaa.accounts.application.service.dto.command.CreateAccountCommand;
import com.jcaa.accounts.domain.model.AccountModel;
import com.jcaa.accounts.infrastructure.entrypoint.rest.dto.request.CreateAccountRestRequest;
import com.jcaa.accounts.infrastructure.entrypoint.rest.dto.response.AccountRestResponse;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AccountRestMapper {

  public CreateAccountCommand toCreateCommand(final CreateAccountRestRequest request) {
    return new CreateAccountCommand(request.userId(), request.email(), request.nombre());
  }

  public AccountRestResponse toResponse(final AccountModel account) {
    return new AccountRestResponse(
        account.getId().value(),
        account.getUserId().value(),
        account.getEmail().value(),
        account.getStatus().name(),
        account.getCreatedAt());
  }

  public List<AccountRestResponse> toResponseList(final List<AccountModel> accounts) {
    return accounts.stream().map(AccountRestMapper::toResponse).toList();
  }
}
