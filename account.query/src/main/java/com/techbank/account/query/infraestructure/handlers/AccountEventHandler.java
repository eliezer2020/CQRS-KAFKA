package com.techbank.account.query.infraestructure.handlers;

import com.techbank.account.query.domain.AccountRepository;
import com.techbank.account.query.domain.BankAccount;
import com.techbank.common.events.AccountClosedEvent;
import com.techbank.common.events.AccountOpenedEvent;
import com.techbank.common.events.FundsDepositedEvent;
import com.techbank.common.events.FundsWithdrawEvent;
import com.techbank.cqrs.core.exceptions.AggregateNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountEventHandler implements EventHandler{

  @Autowired
  private AccountRepository accountRepository;

  @Override
  public void on(FundsDepositedEvent event) {
var bankAccount = accountRepository.findById(event.getId());
if(bankAccount.isEmpty()){
  return;
}
var currentBalance= bankAccount.get().getBalance();
bankAccount.get().setBalance(currentBalance+event.getAmount());
accountRepository.save(bankAccount.get());
  }

  @Override
  public void on(AccountOpenedEvent event) {
  var bankAccount= BankAccount.builder()
      .accountHolder(event.getAccountHolder())
      .accountType(event.getAccountType())
      .id(event.getId())
      .balance(event.getOpeningBalance())
      .creationDate(event.getCreatedDate())
      .build();

  try {
    System.out.println("******SAVING EVENT INTO MYSQL "+accountRepository);
    accountRepository.save(bankAccount);
  }catch (Exception e){
    System.out.println("error while saving to sql : "+e.getMessage());
  }


  }

  @Override
  public void on(FundsWithdrawEvent event) {
    var bankAccount = accountRepository.findById(event.getId());
    if(bankAccount.isEmpty()){
      return;
    }
    var currentBalance= bankAccount.get().getBalance();
    bankAccount.get().setBalance(currentBalance-event.getAmount());
    accountRepository.save(bankAccount.get());
  }

  @Override
  public void on(AccountClosedEvent event) {

    try{accountRepository.deleteById(event.getId());}
    catch (Exception e){
      System.out.println("NO  ACCOUNT ID EXIST");
      throw  new AggregateNotFoundException("NO  ACCOUNT ID EXIST");

    }
  }
}
