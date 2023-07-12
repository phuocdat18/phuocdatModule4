package com.cg.service.depossit;

import com.cg.model.Deposit;
import com.cg.service.IGeneralService;


public interface IDepositService extends IGeneralService<Deposit, Long> {
    Boolean existById(Long id);
}
