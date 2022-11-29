package com.techbank.account.cmd.infraestructure;

import com.techbank.cqrs.core.commands.BaseCommand;
import com.techbank.cqrs.core.commands.CommandHandlerMethod;
import com.techbank.cqrs.core.infraestructure.CommandDispatcher;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class AccountCommandDispatcher implements CommandDispatcher {

private final Map<Class<? extends BaseCommand>, List<CommandHandlerMethod>>
    routes= new HashMap<>();

  @Override
  public <T extends BaseCommand> void registerHandler(Class<T> type, CommandHandlerMethod<T> handler) {
   var handlers = routes.computeIfAbsent(type, command-> new LinkedList<>());
   handlers.add(handler);
  }

  /*
   dispatch a given command to a register command handler method  */
  @Override
  public void send(BaseCommand command) {
    var handlers= routes.get(command.getClass());
    if(handlers==null || handlers.size()==0) {
      throw new RuntimeException("no command handler was registered");
    } else if (handlers.size()>1){
      throw new RuntimeException("no possible to register mora than one command");
    }else{
      handlers.get(0).handle(command);
    }

  }
}
