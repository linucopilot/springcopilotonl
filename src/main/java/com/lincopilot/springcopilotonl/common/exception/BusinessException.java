package com.lincopilot.springcopilotonl.common.exception;

import lombok.Data;

@Data
public class BusinessException extends RuntimeException {

    private String errorCode;
    private String treatCode;
    private String customMsg;

    public BusinessException(String errorCode, String treatCode) {
        this.errorCode = errorCode;
        this.treatCode = treatCode;
    }

    public BusinessException(String errorCode, String treatCode, String customMsg) {
        this.errorCode = errorCode;
        this.treatCode = treatCode;
        this.customMsg = customMsg;
    }
}
