package com.mountain.voucherApp.dto;


import com.mountain.voucherApp.model.vo.CustomerName;
import com.mountain.voucherApp.model.vo.Email;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.UUID;

public class CustomerDto {
    private final UUID customerId;
    private UUID voucherId;
    private CustomerName customerName;
    private final Email email;
    private LocalDateTime lastLoginAt;
    private final LocalDateTime createdAt;

    public CustomerDto(UUID customerId, UUID voucherId, CustomerName customerName, Email email, LocalDateTime lastLoginAt, LocalDateTime createdAt) {
        this.customerId = customerId;
        this.voucherId = voucherId;
        this.customerName = customerName;
        this.email = email;
        this.lastLoginAt = lastLoginAt;
        this.createdAt = createdAt;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public UUID getVoucherId() {
        return voucherId;
    }

    public String getCustomerName() {
        return customerName.getName();
    }

    public Email getEmail() {
        return email;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return MessageFormat.format("{0},{1},{2},{3}{4}",
                voucherId,
                customerName.getName(),
                email,
                lastLoginAt,
                System.lineSeparator()
        );
    }
}
