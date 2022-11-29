package com.techbank.account.query.api.controllers;

import com.techbank.account.query.api.dto.AccountLookupResponse;
import com.techbank.account.query.api.dto.EqualityType;
import com.techbank.account.query.api.queries.FIndAllAccounts;
import com.techbank.account.query.api.queries.FindAccountByHolder;
import com.techbank.account.query.api.queries.FindAccountById;
import com.techbank.account.query.api.queries.FindAccountsWithBalance;
import com.techbank.account.query.api.queries.FindAccountsWithBalanceExactly;
import com.techbank.account.query.domain.BankAccount;
import com.techbank.common.dto.BaseResponse;
import com.techbank.cqrs.core.infraestructure.QueryDispatcher;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/lookup")
public class AccountLookupController {
private final Logger logger = Logger.getLogger(AccountLookupController.class.getName());

@Autowired
  QueryDispatcher queryDispatcher;

@GetMapping(path = "/")
  public ResponseEntity<AccountLookupResponse> getAllAccounts(){
  try{
    List<BankAccount> accounts= queryDispatcher.send(new FIndAllAccounts());
    if(accounts==null || accounts.isEmpty()){
      return new ResponseEntity<>(new AccountLookupResponse("nothing to show "),HttpStatus.NO_CONTENT);
    }
    var response = AccountLookupResponse.builder().accounts(accounts)
        .message("find some bank accounts :: "+accounts.size())
        .build();
    return new ResponseEntity<>(response,HttpStatus.OK);

  }catch (Exception e){
    logger.log(Level.SEVERE, e.toString());
    return new ResponseEntity<>(new AccountLookupResponse("something went wrong : "+e.getMessage()),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }
}

  @GetMapping(path = "/id/{id}")
  public ResponseEntity<AccountLookupResponse> getById(
      @PathVariable String id
  ){
    try{
      List<BankAccount> accounts= queryDispatcher.send(new FindAccountById(id));
      if(accounts==null || accounts.isEmpty()){
        return new ResponseEntity<>(new AccountLookupResponse("nothing to show "),HttpStatus.NO_CONTENT);
      }
      var response = AccountLookupResponse.builder().accounts(accounts)
          .message("find account :: ")
          .build();
      return new ResponseEntity<>(response,HttpStatus.OK);

    }catch (Exception e){
      logger.log(Level.SEVERE, e.toString());
      return new ResponseEntity<>(new AccountLookupResponse("something went wrong : "+e.getMessage()),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping(path = "/holder/{holder}")
  public ResponseEntity<AccountLookupResponse> getByHolder(
      @PathVariable String holder
  ){
    try{
      List<BankAccount> accounts= queryDispatcher.send(new FindAccountByHolder(holder));
      if(accounts==null || accounts.isEmpty()){
        return new ResponseEntity<>(new AccountLookupResponse("nothing to show "),HttpStatus.NO_CONTENT);
      }
      var response = AccountLookupResponse.builder().accounts(accounts)
          .message("find account :: ")
          .build();
      return new ResponseEntity<>(response,HttpStatus.OK);

    }catch (Exception e){
      logger.log(Level.SEVERE, e.toString());
      return new ResponseEntity<>(new AccountLookupResponse("something went wrong : "+e.getMessage()),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping(path = "/balance/{type}/{balance}")
  public ResponseEntity<AccountLookupResponse> getByBalance(
      @PathVariable EqualityType type,
      @PathVariable double balance
  ){
    try{
      List<BankAccount> accounts= queryDispatcher.send(new FindAccountsWithBalance(balance,type));
      if(accounts==null || accounts.isEmpty()){
        return new ResponseEntity<>(new AccountLookupResponse("nothing to show "),HttpStatus.NO_CONTENT);
      }
      var response = AccountLookupResponse.builder().accounts(accounts)
          .message("find some bank accounts :: "+accounts.size())
          .build();
      return new ResponseEntity<>(response,HttpStatus.OK);

    }catch (Exception e){
      logger.log(Level.SEVERE, e.toString());
      return new ResponseEntity<>(new AccountLookupResponse("something went wrong : "+e.getMessage()),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping(path = "/balance/{balance}")
  public ResponseEntity<AccountLookupResponse> getWithBalance(
      @PathVariable double balance
  ){
    try{
      List<BankAccount> accounts= queryDispatcher.send(new FindAccountsWithBalanceExactly(balance));
      if(accounts==null || accounts.isEmpty()){
        return new ResponseEntity<>(new AccountLookupResponse("nothing to show "),HttpStatus.NO_CONTENT);
      }
      var response = AccountLookupResponse.builder().accounts(accounts)
          .message("find account :: ")
          .build();
      return new ResponseEntity<>(response,HttpStatus.OK);

    }catch (Exception e){
      logger.log(Level.SEVERE, e.toString());
      return new ResponseEntity<>(new AccountLookupResponse("something went wrong : "+e.getMessage()),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
