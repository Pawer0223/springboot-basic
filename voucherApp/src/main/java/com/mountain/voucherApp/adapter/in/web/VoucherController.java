package com.mountain.voucherApp.adapter.in.web;

import com.mountain.voucherApp.application.port.in.CustomerDto;
import com.mountain.voucherApp.application.port.in.VoucherAppUseCase;
import com.mountain.voucherApp.application.port.in.VoucherCreateDto;
import com.mountain.voucherApp.application.service.VoucherService;
import com.mountain.voucherApp.shared.enums.DiscountPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.UUID;

@Controller
public class VoucherController {

    private static final Logger log = LoggerFactory.getLogger(VoucherController.class);
    private static final String SUCCESS_MESSAGE = "바우처가 생성되었습니다.";
    private static final String DELETE_SUCCESS_MESSAGE = "바우처가 제거되었습니다.";

    private final VoucherService voucherService;
    private final VoucherAppUseCase voucherAppUseCase;

    public VoucherController(VoucherService voucherService, VoucherAppUseCase voucherAppUseCase) {
        this.voucherService = voucherService;
        this.voucherAppUseCase = voucherAppUseCase;
    }

    @GetMapping("/new-voucher")
    public String createVoucherPage(Model model) {
        model.addAttribute("policyMap", DiscountPolicy.discountPolicies);
        return "/voucher/new-voucher";
    }

    @GetMapping("/new-voucher-success")
    public String createVoucherSuccessPage(Model model) {
        model.addAttribute("message", SUCCESS_MESSAGE);
        return "/common/success";
    }

    @GetMapping("/delete-voucher-success")
    public String deleteVoucherSuccessPage(Model model) {
        model.addAttribute("message", DELETE_SUCCESS_MESSAGE);
        return "/common/success";
    }

    @GetMapping("/voucher-exist")
    public String existVoucherPage() {
        return "/voucher/voucher-exist";
    }

    @GetMapping("/vouchers")
    public String vouchersPage(Model model) {
        model.addAttribute("vouchers", voucherService.findAll());
        return "/voucher/voucher-list";
    }

    @GetMapping("select-vouchers")
    public String selectVouchersPage(Model model) {
        model.addAttribute("vouchers", voucherService.findAll());
        return "/voucher/voucher-list-select";
    }

    @GetMapping("/delete-voucher/{voucherId}")
    public String deleteByVoucherId(@PathVariable String voucherId) {
        voucherService.deleteById(UUID.fromString(voucherId));
        return "redirect:/delete-voucher-success";
    }

    @PostMapping("/new-voucher")
    public String createVoucher(VoucherCreateDto voucherCreateDto) {
        log.info("parameters.. {}", voucherCreateDto.toString());
        if (!voucherAppUseCase.create(voucherCreateDto)) {
            return "redirect:voucher-exist";
        }
        return "redirect:new-voucher-success";
    }
}
