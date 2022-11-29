package com.techbank.account.query.domain;

import com.techbank.cqrs.core.domain.BaseEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<BankAccount,String> {

  Optional <BankAccount> findByAccountHolder(String accountHolder);
  List<BaseEntity> findByBalanceGreaterThan(double balance);
  List<BaseEntity> findByBalanceLessThan(double balance);
  List<BaseEntity> findByBalance(double balance);


}
