package com.cg.service;

import com.cg.model.Transfer;
import com.cg.repository.ICustomerRepository;
import com.cg.repository.ITransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TransferService implements ITransferService{
    @Autowired
    private ITransferRepository transferRepository;

    @Override
    public List<Transfer> findAll() {
        return transferRepository.findAll();
    }

    @Override
    public Transfer findById(Long id) {
        return transferRepository.findById(id);
    }

    @Override
    public List<Transfer> findRecipients(Long id) {
        return null;
    }

    @Override
    public void save(Transfer transfer) {
        transferRepository.save(transfer);
    }

    @Override
    public void remove(Long id) {
        transferRepository.remove(id);
    }
}
