package com.techbank.account.cmd.api.controllers;

import com.techbank.account.cmd.api.commands.DepositFundsCommand;
import com.techbank.account.cmd.api.commands.WithdrawFundsCommand;
import com.techbank.account.cmd.api.dto.OpenAccountResponse;
import com.techbank.common.dto.BaseResponse;
import com.techbank.cqrs.core.exceptions.AggregateNotFoundException;
import com.techbank.cqrs.core.infraestructure.CommandDispatcher;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/withdrawfunds")
public class WithdrawFundsController {
  private final Logger logger= Logger.getLogger(WithdrawFundsController.class.getName());

  @Autowired
  CommandDispatcher commandDispatcher;

  @PutMapping(path = "/{id}")
  public ResponseEntity<BaseResponse> withDrawfunds(
      @PathVariable String id,
      @RequestBody WithdrawFundsCommand command
  ){
    try{
      command.setId(id);
      commandDispatcher.send(command);
      return new ResponseEntity<>(new BaseResponse("withdraw funds sucessfully proccessed"), HttpStatus.OK);

    }catch (IllegalStateException | AggregateNotFoundException e){
      return new ResponseEntity<>(new OpenAccountResponse(
          "bad request ::  "+e.getMessage(),id), HttpStatus.BAD_REQUEST
      );
    }catch (Exception e){
      logger.log(Level.SEVERE,e.getMessage(),e);
      return new ResponseEntity<>(new OpenAccountResponse(
          "something went wrong ! ",id), HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }
}
