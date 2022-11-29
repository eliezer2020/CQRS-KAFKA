package com.techbank.account.query;

import com.techbank.account.query.api.queries.FIndAllAccounts;
import com.techbank.account.query.api.queries.FindAccountByHolder;
import com.techbank.account.query.api.queries.FindAccountById;
import com.techbank.account.query.api.queries.FindAccountsWithBalance;
import com.techbank.account.query.api.queries.FindAccountsWithBalanceExactly;
import com.techbank.account.query.api.queries.QueryHandler;
import com.techbank.cqrs.core.infraestructure.QueryDispatcher;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.techbank.account.query"
)
public class QueryApplication {
	@Autowired
	QueryDispatcher queryDispatcher;
	@Autowired
	QueryHandler queryHandler;

	public static void main(String[] args) {
		SpringApplication.run(QueryApplication.class, args);
	}

	@PostConstruct
	public void registerHandlers(){
		queryDispatcher.registerHandler(FIndAllAccounts.class,queryHandler::handle);
		queryDispatcher.registerHandler(FindAccountsWithBalance.class,queryHandler::handle);
		queryDispatcher.registerHandler(FindAccountById.class,queryHandler::handle);
		queryDispatcher.registerHandler(FindAccountByHolder.class,queryHandler::handle);
		queryDispatcher.registerHandler(FindAccountsWithBalanceExactly.class,queryHandler::handle);


	}
}
