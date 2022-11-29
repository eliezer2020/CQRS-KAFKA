package com.techbank.account.cmd.api.controllers;

import com.techbank.account.cmd.api.commands.OpenAccountCommand;
import com.techbank.account.cmd.api.commands.RestoreDBCommand;
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
@RequestMapping(path = "/api/v1/restoredb")
public class RestoreDbController {

  private final Logger logger= Logger.getLogger(RestoreDbController.class.getName());

  @Autowired
  private CommandDispatcher commandDispatcher;

  @PostMapping
  public ResponseEntity<BaseResponse> restoreDb(){

    try{
      commandDispatcher.send(new RestoreDBCommand());
      return new ResponseEntity<>(new OpenAccountResponse(
          "db has been restore with no problems"), HttpStatus.CREATED
      );
    }catch (IllegalStateException e){
      return new ResponseEntity<>(new OpenAccountResponse(
          "error while restoring db"), HttpStatus.BAD_REQUEST
      );

    }catch (Exception e){
      logger.log(Level.SEVERE,e.getMessage(),e);
      return new ResponseEntity<>(new OpenAccountResponse(
          "something went wrong !: ",e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR
      );


    }
  }
}
