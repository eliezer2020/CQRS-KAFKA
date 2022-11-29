package com.techbank.account.query.infraestructure.consumers;

import com.techbank.account.query.infraestructure.handlers.EventHandler;
import com.techbank.common.events.AccountClosedEvent;
import com.techbank.common.events.AccountOpenedEvent;
import com.techbank.common.events.FundsDepositedEvent;
import com.techbank.common.events.FundsWithdrawEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class AccountEventConsumer implements EventConsumer {

  @Autowired
  private EventHandler eventHandler;

  @KafkaListener(topics = "AccountOpenedEvent", groupId = "${spring.kafka.consumer.group-id}")
  @Override
  public void consume(AccountOpenedEvent event, Acknowledgment ack) {
    System.out.println("***** CONSUMING NEW EVENT FROM AccountOpenedEvent: "+event);
    eventHandler.on(event);
    ack.acknowledge();
  }

  @KafkaListener(topics = "FundsDepositedEvent", groupId = "${spring.kafka.consumer.group-id}")
  @Override
  public void consume(FundsDepositedEvent event, Acknowledgment ack) {
    System.out.println("***** CONSUMING NEW EVENT FROM FundsDepositedEvent: "+event);
    eventHandler.on(event);
    ack.acknowledge();
  }

  @KafkaListener(topics = "FundsWithdrawEvent", groupId = "${spring.kafka.consumer.group-id}")
  @Override
  public void consume(FundsWithdrawEvent event, Acknowledgment ack) {
    System.out.println("***** CONSUMING NEW EVENT FROM FundsWithdrawEvent: "+event);
    eventHandler.on(event);
    ack.acknowledge();
  }

  @KafkaListener(topics = "AccountClosedEvent", groupId = "${spring.kafka.consumer.group-id}")
  @Override
  public void consume(AccountClosedEvent event, Acknowledgment ack) {
    System.out.println("***** CONSUMING NEW EVENT FROM AccountClosedEvent: "+event);

    try {
      eventHandler.on(event);
      ack.acknowledge();
    }catch (Exception e)
    {
      System.out.println(e.getMessage());
      //propagate error to controller
    }

  }
}
