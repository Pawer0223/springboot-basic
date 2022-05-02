package com.mountain.voucherApp.common.constants;

import java.util.regex.Pattern;

import static com.mountain.voucherApp.common.constants.ErrorMessage.EMAIL_NOT_VALID;

public enum RegExp {
    EMAIL(Expression.EMAIL_EXP, EMAIL_NOT_VALID.getMessage());

    private final String expression;
    private final String errorMsg;


    RegExp(String expression, String errorMsg) {
        this.expression = expression;
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public boolean validate(String source) {
        return Pattern.matches(expression, source);
    }

    public static class Expression {
        public static final String EMAIL_EXP = "\\b[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,4}\\b";
    }
}
