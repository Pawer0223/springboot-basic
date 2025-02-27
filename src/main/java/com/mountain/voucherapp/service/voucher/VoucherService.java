package com.mountain.voucherapp.service.voucher;

import com.mountain.voucherapp.dao.customer.CustomerRepository;
import com.mountain.voucherapp.dao.voucher.VoucherRepository;
import com.mountain.voucherapp.model.VoucherEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class VoucherService {

    private final VoucherRepository voucherRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public VoucherService(VoucherRepository voucherRepository, CustomerRepository customerRepository) {
        this.voucherRepository = voucherRepository;
        this.customerRepository = customerRepository;
    }

    public List<VoucherEntity> findAll() {
        return voucherRepository.findAll();
    }

    @Transactional
    public void deleteById(UUID voucherId) {
        customerRepository.removeVoucherId(voucherId);
        voucherRepository.deleteById(voucherId);
    }
}
