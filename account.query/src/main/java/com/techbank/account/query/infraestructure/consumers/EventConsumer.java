package com.techbank.account.query.infraestructure.consumers;

import com.techbank.common.events.AccountClosedEvent;
import com.techbank.common.events.AccountOpenedEvent;
import com.techbank.common.events.FundsDepositedEvent;
import com.techbank.common.events.FundsWithdrawEvent;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;

public interface EventConsumer {
void consume(@Payload AccountOpenedEvent event, Acknowledgment ack);
  void consume(@Payload FundsDepositedEvent event, Acknowledgment ack);
  void consume(@Payload FundsWithdrawEvent event, Acknowledgment ack);
  void consume(@Payload AccountClosedEvent event, Acknowledgment ack);
}
