package com.techbank.account.query.api.queries;

import com.techbank.account.query.api.dto.EqualityType;
import com.techbank.account.query.domain.AccountRepository;
import com.techbank.cqrs.core.domain.BaseEntity;
import com.techbank.cqrs.core.queries.BaseQuery;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountQueryHandler implements QueryHandler {
  @Autowired
  AccountRepository accountRepository;



  @Override
  public List<BaseEntity> handle(FIndAllAccounts query) {
    var bankAccounts= accountRepository.findAll();
    List<BaseEntity>accounts=new ArrayList<>();
    //forEach bankAccount-> account.add(bankAccount)
    bankAccounts.forEach(accounts::add);
    return accounts;
  }

  @Override
  public List<BaseEntity> handle(FindAccountByHolder query) {
    var bankAccount= accountRepository.findByAccountHolder(query.getAccountHolder());
    if(bankAccount.isEmpty()){
      return null;
    }
    List<BaseEntity> account= new ArrayList<>();
    account.add(bankAccount.get());
    return account;
  }

  @Override
  public List<BaseEntity> handle(FindAccountById query) {
    var bankAccount= accountRepository.findById(query.getId());
    if(bankAccount.isEmpty()){
      return null;
    }
    List<BaseEntity> account= new ArrayList<>();
    account.add(bankAccount.get());
    return account;
  }

  @Override
  public List<BaseEntity> handle(FindAccountsWithBalance query) {

  return query.getEqualityType() == EqualityType.GREATER_THAN ? accountRepository.findByBalanceGreaterThan(
        query.getBalance()
    ) : accountRepository.findByBalanceLessThan(query.getBalance());
  }
@Override
  public List<BaseEntity> handle(FindAccountsWithBalanceExactly query) {
    return accountRepository.findByBalance(query.getBalance());
  }
}
