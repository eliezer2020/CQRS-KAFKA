package com.techbank.account.cmd.api.controllers;

import com.techbank.account.cmd.api.commands.DepositFundsCommand;
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
@RequestMapping(path = "/api/v1/depositfunds")
public class DepositFundsController {

  private final Logger logger= Logger.getLogger(DepositFundsController.class.getName());

  @Autowired
  CommandDispatcher commandDispatcher;

  @PutMapping(path = "/{id}")
  public ResponseEntity<BaseResponse> depositFunds(
      @PathVariable String id,
      @RequestBody DepositFundsCommand command
  ){
    try{
      command.setId(id);
      commandDispatcher.send(command);
      return new ResponseEntity<>(new BaseResponse("deposit funds sucessfully proccessed"), HttpStatus.OK);

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
