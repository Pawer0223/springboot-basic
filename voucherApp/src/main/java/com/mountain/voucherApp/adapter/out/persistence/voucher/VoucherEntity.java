package com.mountain.voucherApp.adapter.out.persistence.voucher;

import com.mountain.voucherApp.shared.enums.DiscountPolicy;

import java.text.MessageFormat;
import java.util.UUID;

public class VoucherEntity {
    private final UUID voucherId;
    private DiscountPolicy discountPolicy;
    private Long discountAmount;

    public VoucherEntity(UUID voucherId, DiscountPolicy discountPolicy, Long discountAmount) {
        this.voucherId = voucherId;
        this.discountPolicy = discountPolicy;
        this.discountAmount = discountAmount;
    }

    public UUID getVoucherId() {
        return voucherId;
    }

    public Long getDiscountAmount() {
        return discountAmount;
    }

    public DiscountPolicy getDiscountPolicy() {
        return discountPolicy;
    }

    public void changeVoucherInfo(Integer discountPolicyId, DiscountPolicy discountPolicy) {
        this.discountAmount = discountAmount;
        this.discountPolicy = discountPolicy;
    }

    @Override
    public String toString() {
        return MessageFormat.format("{0},{1},{2}{3}",
                voucherId,
                discountPolicy.toString(),
                discountAmount,
                System.lineSeparator()
        );
    }
}
