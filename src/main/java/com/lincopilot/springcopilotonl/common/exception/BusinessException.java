package com.lincopilot.springcopilotonl.common.exception;

public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    protected String errorCode;
    protected String treatCode;
    protected String customMsg;

    public BusinessException(String errorCode, String treatCode) {
        super();
        this.errorCode = errorCode;
        this.treatCode = treatCode;
    }

   public BusinessException(String errorCode, String treatCode, String customMsg) {
        super(customMsg);
        this.errorCode = errorCode;
        this.treatCode = treatCode;
        this.customMsg = customMsg;
    }

    public BusinessException(String errorCode, String treatCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.treatCode = treatCode;
    }

    public BusinessException(String errorCode, String treatCode, String customMsg, Throwable cause) {
        super(customMsg, cause);
        this.errorCode = errorCode;
        this.treatCode = treatCode;
        this.customMsg = customMsg;
    }

}
