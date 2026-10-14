package com.jcaa.accounts.application.port.out;

import com.jcaa.accounts.domain.model.AccountModel;
import java.util.List;

public interface GetAllAccountsPort {
  List<AccountModel> getAll();
}
