package com.techbank.account.query.api.queries;

import com.techbank.cqrs.core.domain.BaseEntity;
import java.util.List;

public interface QueryHandler {
  List<BaseEntity> handle(FIndAllAccounts query);
  List<BaseEntity> handle(FindAccountByHolder query);
  List<BaseEntity> handle(FindAccountById query);
  List<BaseEntity> handle(FindAccountsWithBalance query);

  List<BaseEntity> handle(FindAccountsWithBalanceExactly query);
}
