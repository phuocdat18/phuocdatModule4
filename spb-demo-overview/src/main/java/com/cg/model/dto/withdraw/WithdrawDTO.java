package com.cg.model.dto.withdraw;

import com.cg.model.dto.customer.CustomerResDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class WithdrawDTO {
    private Long id;
    private CustomerResDTO customerResDTO;
    private BigDecimal transactionAmount;
}
