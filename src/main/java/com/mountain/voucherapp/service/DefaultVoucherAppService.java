package com.mountain.voucherapp.service;

import com.mountain.voucherapp.dao.blackList.BlackListFileFormat;
import com.mountain.voucherapp.dao.blackList.BlackListRepository;
import com.mountain.voucherapp.dao.customer.CustomerRepository;
import com.mountain.voucherapp.dao.voucher.VoucherRepository;
import com.mountain.voucherapp.dto.CustomerDto;
import com.mountain.voucherapp.dto.VoucherCreateDto;
import com.mountain.voucherapp.dto.VoucherIdUpdateDto;
import com.mountain.voucherapp.model.VoucherEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.mountain.voucherapp.common.constants.ProgramMessage.CREATE_NEW_VOUCHER;

@Service
public class DefaultVoucherAppService implements VoucherAppService {

    private static final Logger log = LoggerFactory.getLogger(DefaultVoucherAppService.class);
    private final CustomerRepository customerRepository;
    private final VoucherRepository voucherRepository;
    private final BlackListRepository blackListRepository;

    public DefaultVoucherAppService(CustomerRepository customerRepository,
                                    VoucherRepository voucherRepository,
                                    BlackListRepository blackListRepository) {
        this.customerRepository = customerRepository;
        this.voucherRepository = voucherRepository;
        this.blackListRepository = blackListRepository;
    }

    @Override
    public boolean create(VoucherCreateDto voucherCreateDto) {
        Optional<VoucherEntity> findEntity = voucherRepository.findByDiscountPolicyAndAmount(
                voucherCreateDto.getDiscountPolicy(),
                voucherCreateDto.getDiscountAmount()
        );
        if (findEntity.isEmpty()) {
            VoucherEntity newVoucherEntity = new VoucherEntity(UUID.randomUUID(),
                    voucherCreateDto.getDiscountPolicy(),
                    voucherCreateDto.getDiscountAmount());
            voucherRepository.insert(newVoucherEntity);
            log.info(CREATE_NEW_VOUCHER);
            return true;
        }
        return false;
    }

    @Override
    public List<VoucherEntity> showVoucherList() {
        return voucherRepository.findAll();
    }

    @Override
    public List<CustomerDto> showCustomerVoucherInfo() {
        return customerRepository.findAll();
    }

    @Override
    public void addVoucher(VoucherIdUpdateDto voucherIdUpdateDto) {
        customerRepository.updateVoucherId(voucherIdUpdateDto.getCustomerId(),
                voucherIdUpdateDto.getVoucherId());
    }

    @Override
    public void removeVoucher(UUID customerId) {
        customerRepository.updateVoucherId(customerId, null);
    }

    @Override
    public List<CustomerDto> showByVoucher(UUID voucherId) {
        return customerRepository.findByVoucherId(voucherId);
    }

    @Override
    public List<BlackListFileFormat> getBlackList() throws IOException {
        return blackListRepository.getBlackList();
    }
}
