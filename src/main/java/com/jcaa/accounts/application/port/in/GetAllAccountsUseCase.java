package com.jcaa.accounts.application.port.in;

import com.jcaa.accounts.domain.model.AccountModel;
import java.util.List;

public interface GetAllAccountsUseCase {
  List<AccountModel> execute();
}
