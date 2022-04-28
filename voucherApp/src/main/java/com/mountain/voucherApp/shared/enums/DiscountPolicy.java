package com.mountain.voucherApp.shared.enums;

import com.mountain.voucherApp.domain.FixedAmountVoucher;
import com.mountain.voucherApp.domain.PercentDiscountVoucher;
import com.mountain.voucherApp.domain.Voucher;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.mountain.voucherApp.shared.constants.ProgramMessage.*;

public enum DiscountPolicy {
    FIXED(FIXED_DISCOUNT, FIXED_UINT, new FixedAmountVoucher()),
    PERCENT(PERCENT_DISCOUNT, PERCENT_UNIT, new PercentDiscountVoucher());

    private final String description;
    private final String unit;
    private final Voucher voucher;

    DiscountPolicy(String description, String unit, Voucher voucher) {
        this.description = description;
        this.unit = unit;
        this.voucher = voucher;
    }

    public String getUnit() {
        return unit;
    }

    public String getDescription() {
        return description;
    }

    public Voucher getVoucher() {
        return voucher;
    }

    public static final Map<Integer, DiscountPolicy> discountPolicies =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toConcurrentMap(DiscountPolicy::ordinal, Function.identity())));

    public static DiscountPolicy find(int ordinal) {
        return Optional.ofNullable(discountPolicies.get(ordinal)).orElse(null);
    }
}

