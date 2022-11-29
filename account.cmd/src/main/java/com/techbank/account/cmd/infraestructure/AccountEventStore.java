package com.techbank.account.cmd.infraestructure;

import com.techbank.account.cmd.domain.AccountAggregate;
import com.techbank.account.cmd.domain.EventStoreRepository;
import com.techbank.cqrs.core.events.BaseEvent;
import com.techbank.cqrs.core.events.EventModel;
import com.techbank.cqrs.core.exceptions.AggregateNotFoundException;
import com.techbank.cqrs.core.exceptions.ConcurrencyException;
import com.techbank.cqrs.core.infraestructure.EventStore;
import com.techbank.cqrs.core.producers.EventProducer;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountEventStore implements EventStore {

  @Autowired
  private EventStoreRepository eventStoreRepository;

  @Autowired
  EventProducer eventProducer;

  @Override
  public void saveEvents(String aggregateId, Iterable<BaseEvent> events, int expectedVersion) {
    var eventStream=eventStoreRepository.findByAggregateIdentifier(aggregateId);
    if(expectedVersion!=-1 && eventStream.get(eventStream.size()-1).getVersion()!=expectedVersion){
      throw new ConcurrencyException();
    }
    var version= expectedVersion;
    for(var event:events){
      version++;
      event.setVersion(version);
      var eventModel = EventModel.builder().
          timeStamp(new Date())
          .aggregateIdentifier(aggregateId)
          .aggregateType(AccountAggregate.class.getTypeName())
          .version(version)
          .eventType(event.getClass().getTypeName())
          .eventData(event)
          .build();

      var persistedEvent = eventStoreRepository.save(eventModel);
      if(!persistedEvent.getId().isEmpty()){
        //TODO produce to kafka
        eventProducer.produce(event.getClass().getSimpleName(),event);
      }
    }
  }

  @Override
  public List<BaseEvent> getEvents(String aggregateId) {
    var eventStream=eventStoreRepository.findByAggregateIdentifier(aggregateId);
    if(eventStream==null || eventStream.isEmpty()){
      throw new AggregateNotFoundException("incorrect account ID, doesn't exist");
    }

    return eventStream.stream().map(event-> event.getEventData()).collect(Collectors.toList());

  }

  @Override
  public List<String> getAggregatesIds() {
    var eventStream= eventStoreRepository.findAll();
    if(eventStream==null && eventStream.isEmpty()){
      throw new IllegalStateException("could not find any event");
    }
    return eventStream.stream().map(EventModel::getAggregateIdentifier).distinct()
        .collect(Collectors.toList());
  }
}
