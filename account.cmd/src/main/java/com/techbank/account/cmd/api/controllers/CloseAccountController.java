package com.techbank.account.cmd.api.controllers;

import com.techbank.account.cmd.api.commands.CloseAccountCommand;
import com.techbank.common.dto.BaseResponse;
import com.techbank.cqrs.core.exceptions.AggregateNotFoundException;
import com.techbank.cqrs.core.infraestructure.CommandDispatcher;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/closeaccount")
public class CloseAccountController {

  private final Logger logger= Logger.getLogger(CloseAccountController.class.getName());

  @Autowired
  CommandDispatcher commandDispatcher;

  @DeleteMapping(path = "/{id}")
  public ResponseEntity<BaseResponse> closeAccount(
      @PathVariable String id
  ){
    try{
      commandDispatcher.send(new CloseAccountCommand(id));
      return new ResponseEntity<>(new BaseResponse("account successfully deleted "+id), HttpStatus.OK);

    }catch (IllegalStateException | AggregateNotFoundException e){
      return new ResponseEntity<>(new BaseResponse(e.toString()), HttpStatus.BAD_REQUEST);
    }
    catch (Exception e){
      logger.log(Level.SEVERE, e.toString());
      return new ResponseEntity<>(new BaseResponse(e.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
