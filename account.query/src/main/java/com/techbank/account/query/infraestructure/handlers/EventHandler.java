package com.techbank.account.query.infraestructure.handlers;

import com.techbank.common.events.AccountClosedEvent;
import com.techbank.common.events.AccountOpenedEvent;
import com.techbank.common.events.FundsDepositedEvent;
import com.techbank.common.events.FundsWithdrawEvent;

public interface EventHandler {

  void on(FundsDepositedEvent event);
  void on(AccountOpenedEvent event);
  void on(FundsWithdrawEvent event);
  void on(AccountClosedEvent event);
}
