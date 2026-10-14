package com.jcaa.accounts.application.service;

import com.jcaa.accounts.application.port.in.GetAllAccountsUseCase;
import com.jcaa.accounts.application.port.out.GetAllAccountsPort;
import com.jcaa.accounts.domain.model.AccountModel;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAllAccountsService implements GetAllAccountsUseCase {

  private final GetAllAccountsPort getAllAccountsPort;

  @Override
  public List<AccountModel> execute() {
    return getAllAccountsPort.getAll();
  }
}
