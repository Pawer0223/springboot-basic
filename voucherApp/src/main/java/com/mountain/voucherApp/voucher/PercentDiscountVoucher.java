package com.mountain.voucherApp.voucher;

import java.util.UUID;

public class PercentDiscountVoucher implements Voucher {

    private final UUID voucherId;
    private final long percent;

    public PercentDiscountVoucher(UUID voucherId, long percent) {
        this.voucherId = voucherId;
        this.percent = percent;
    }

    public PercentDiscountVoucher(long percent) {
        this.voucherId = UUID.randomUUID();
        this.percent = percent;
    }

    @Override
    public UUID getVoucherId() {
        return voucherId;
    }

    @Override
    public long discount(long beforeDiscount) {
        return beforeDiscount * (percent / 100);
    }

    @Override
    public long getAmount() {
        return percent;
    }

    @Override
    public String toString() {
        return "id: " + voucherId + ", percent: " + percent;
    }
}
