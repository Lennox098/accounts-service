package com.jcaa.accounts.infrastructure.adapter.persistence.mapper;

import com.jcaa.accounts.domain.enums.AccountStatus;
import com.jcaa.accounts.domain.model.AccountModel;
import com.jcaa.accounts.domain.valueobject.AccountEmail;
import com.jcaa.accounts.domain.valueobject.AccountId;
import com.jcaa.accounts.domain.valueobject.UserId;
import com.jcaa.accounts.infrastructure.adapter.persistence.exception.PersistenceException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AccountPersistenceMapper {

  private static final String COL_ID = "id";
  private static final String COL_USER_ID = "user_id";
  private static final String COL_EMAIL = "email";
  private static final String COL_STATUS = "status";
  private static final String COL_CREATED_AT = "created_at";

  public AccountModel fromResultSetToModel(final ResultSet resultSet) {
    try {
      return new AccountModel(
          new AccountId(resultSet.getString(COL_ID)),
          new UserId(resultSet.getString(COL_USER_ID)),
          new AccountEmail(resultSet.getString(COL_EMAIL)),
          AccountStatus.fromString(resultSet.getString(COL_STATUS)),
          resultSet.getTimestamp(COL_CREATED_AT).toLocalDateTime());
    } catch (final SQLException exception) {
      throw PersistenceException.becauseRowCouldNotBeRead(exception);
    }
  }

  public List<AccountModel> fromResultSetToModelList(final ResultSet resultSet)
      throws SQLException {
    final List<AccountModel> accounts = new ArrayList<>();
    while (resultSet.next()) {
      accounts.add(fromResultSetToModel(resultSet));
    }
    return accounts;
  }
}
