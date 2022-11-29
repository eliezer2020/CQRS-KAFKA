package com.techbank.account.cmd;

import com.techbank.account.cmd.api.commands.CloseAccountCommand;
import com.techbank.account.cmd.api.commands.CommandHandler;
import com.techbank.account.cmd.api.commands.DepositFundsCommand;
import com.techbank.account.cmd.api.commands.OpenAccountCommand;
import com.techbank.account.cmd.api.commands.RestoreDBCommand;
import com.techbank.account.cmd.api.commands.WithdrawFundsCommand;
import com.techbank.cqrs.core.infraestructure.CommandDispatcher;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CommandApplication {

	@Autowired
	private CommandDispatcher commandDispatcher;

	@Autowired
	CommandHandler commandHandler;

	public static void main(String[] args) {
		SpringApplication.run(CommandApplication.class, args);
	}

	@PostConstruct
	public void registerHandlers(){
		commandDispatcher.registerHandler(OpenAccountCommand.class,commandHandler::handle);
		commandDispatcher.registerHandler(DepositFundsCommand.class,commandHandler::handle);
		commandDispatcher.registerHandler(WithdrawFundsCommand.class,commandHandler::handle);
		commandDispatcher.registerHandler(CloseAccountCommand.class,commandHandler::handle);
		commandDispatcher.registerHandler(RestoreDBCommand.class,commandHandler::handle);
	}

}
