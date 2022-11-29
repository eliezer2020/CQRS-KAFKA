package com.techbank.common.events;

import com.techbank.common.dto.AccountType;
import com.techbank.cqrs.core.events.BaseEvent;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AccountOpenedEvent extends BaseEvent {
private String accountHolder;
private AccountType accountType;
private Date createdDate;
private Double openingBalance;
}
