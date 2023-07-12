package com.cg.service.transfer;

import com.cg.model.Transfer;
import com.cg.service.IGeneralService;

public interface ITransferService extends IGeneralService<Transfer, Long> {
    Boolean existById(Long id);
}
