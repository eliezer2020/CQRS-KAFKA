package com.techbank.account.query.infraestructure;

import com.techbank.cqrs.core.domain.BaseEntity;
import com.techbank.cqrs.core.infraestructure.QueryDispatcher;
import com.techbank.cqrs.core.queries.BaseQuery;
import com.techbank.cqrs.core.queries.QueryHandler;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class AccountQuerydispatcher implements QueryDispatcher {

  private final Map<Class<? extends BaseQuery>,List<QueryHandler >> routes=new HashMap<>();

  @Override
  public <T extends BaseQuery> void registerHandler(Class<T> type, QueryHandler<T> handler) {
  var handlers= routes.computeIfAbsent(type ,t->new LinkedList<>());
  handlers.add(handler);

  //var handlers= routes.computeIfAbsent(type ,t->new LinkedList<>()).add(handler);
  }

  @Override
  public <U extends BaseEntity> List<U> send(BaseQuery query) {
    var handler= routes.get(query.getClass());
    if(handler==null || handler.size() <=0 || handler.size()>1){
      throw new RuntimeException("HANDLER DOESNT EXIST OR can not have multiple handlers");
    }
    return handler.get(0).handle(query);
  }
}
