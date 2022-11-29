package com.techbank.account.cmd.domain;

import com.techbank.account.cmd.api.commands.OpenAccountCommand;
import com.techbank.common.events.AccountClosedEvent;
import com.techbank.common.events.AccountOpenedEvent;
import com.techbank.common.events.FundsDepositedEvent;
import com.techbank.common.events.FundsWithdrawEvent;
import com.techbank.cqrs.core.domain.AggregateRoot;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor

public class AccountAggregate extends AggregateRoot {
private boolean active;
private double balance;

  public double getBalance() {
    return balance;
  }
  public boolean isActive(){return active;}

  /*aggregate root constructor that takes openAccountCommand as param*/
public AccountAggregate(OpenAccountCommand command){
  raiseEvent(AccountOpenedEvent.builder()
      .id(command.getId())
      .accountType(command.getAccountType())
      .openingBalance(command.getOpeningBalance()
      ).accountHolder(command.getAccountHolder()).build());


}

public void apply(AccountOpenedEvent event){
  this.id=event.getId();
  this.active= true;
  this.balance=event.getOpeningBalance();
}

public void depositFunds(double amount){
  if(!this.active){
    throw new IllegalStateException("account is not active");
  }else if(amount <=0){
    throw new IllegalStateException("deposited amount should be >0");
  }else{
    raiseEvent(FundsDepositedEvent.builder()
        .amount(amount)
        .id(this.id).build());

  }

}

  public void apply(FundsDepositedEvent event){
    this.id=event.getId();
    this.active= true;
    this.balance+=event.getAmount();
  }

  public void withdrawFunds(double amount){
    if(!this.active){
      throw new IllegalStateException("account is not active");
    }else if(amount > this.balance){
      throw new IllegalStateException("withdraw  amount exceed limits");
    }else{
      raiseEvent(FundsWithdrawEvent.builder().id(this.id).amount(amount).build());

    }

  }

  public void apply(FundsWithdrawEvent event){
    this.id=event.getId();

    this.balance-=event.getAmount();
  }

  public void closeAccount(){
   raiseEvent(AccountClosedEvent.builder()
       .id(this.id)
       .build());
  }

  public void apply(AccountClosedEvent event){
  this.id=event.getId();
  this.active=false;
  }



}
