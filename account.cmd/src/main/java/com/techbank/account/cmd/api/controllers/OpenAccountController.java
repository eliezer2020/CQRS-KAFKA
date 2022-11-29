package com.techbank.account.cmd.api.controllers;

import com.techbank.account.cmd.api.commands.OpenAccountCommand;
import com.techbank.account.cmd.api.dto.OpenAccountResponse;
import com.techbank.common.dto.BaseResponse;
import com.techbank.cqrs.core.infraestructure.CommandDispatcher;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/openBankAccount")
public class OpenAccountController {

  private final Logger logger= Logger.getLogger(OpenAccountController.class.getName());

  @Autowired
  private CommandDispatcher commandDispatcher;

  @PostMapping
  public ResponseEntity<BaseResponse> openAccount(@RequestBody OpenAccountCommand command){
    var id= UUID.randomUUID().toString();
     command.setId(id);
    try{
      commandDispatcher.send(command);
      return new ResponseEntity<>(new OpenAccountResponse(
          "bank account successfully created",id), HttpStatus.CREATED
      );
    }catch (IllegalStateException e){
      return new ResponseEntity<>(new OpenAccountResponse(
          "bad request",id), HttpStatus.BAD_REQUEST
      );

    }catch (Exception e){
      logger.log(Level.SEVERE,e.getMessage(),e);
      return new ResponseEntity<>(new OpenAccountResponse(
          "something went wrong ! ",id), HttpStatus.INTERNAL_SERVER_ERROR
      );


    }
  }

}
