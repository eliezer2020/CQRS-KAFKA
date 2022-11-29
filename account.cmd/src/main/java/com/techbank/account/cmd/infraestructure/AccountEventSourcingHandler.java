package com.techbank.account.cmd.infraestructure;

import com.techbank.account.cmd.domain.AccountAggregate;
import com.techbank.cqrs.core.domain.AggregateRoot;
import com.techbank.cqrs.core.handlers.EventSourcingHandler;
import com.techbank.cqrs.core.infraestructure.EventStore;
import com.techbank.cqrs.core.producers.EventProducer;
import java.util.Comparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountEventSourcingHandler implements EventSourcingHandler<AccountAggregate> {
@Autowired
private EventStore eventStore;
@Autowired
  EventProducer eventProducer;
  @Override
  public void save(AggregateRoot aggregate) {
    eventStore.saveEvents(aggregate.getId(), aggregate.getUncommittedChanges(),
        aggregate.getVersion());
  }

  @Override
  public AccountAggregate getById(String id) {
    var aggregate= new AccountAggregate();
    var events = eventStore.getEvents(id);
    if(events!=null && !events.isEmpty()){
      aggregate.replyEvent(events);
      var latestVersion = events.stream().map(event -> event.getVersion()).
          max(Comparator.naturalOrder());

      aggregate.setVersion(latestVersion.get());
    }
    return aggregate;
  }

  @Override
  public void republishEvents() {
    var aggregateIds= eventStore.getAggregatesIds();
    for(var aggregateId : aggregateIds){
      var aggregate= getById(aggregateId);
      if(aggregate==null && !aggregate.isActive()) continue;
      var events= eventStore.getEvents(aggregateId);
      for(var event: events){
        eventProducer.produce(event.getClass().getSimpleName(),event);
      }
    }
  }
}
