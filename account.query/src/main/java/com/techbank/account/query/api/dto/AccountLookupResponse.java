package com.techbank.account.query.api.dto;

import com.techbank.account.query.domain.BankAccount;
import com.techbank.common.dto.BaseResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AccountLookupResponse extends BaseResponse {
  private List<BankAccount> accounts;


  public AccountLookupResponse(String msge){
    super(msge);
  }


}
