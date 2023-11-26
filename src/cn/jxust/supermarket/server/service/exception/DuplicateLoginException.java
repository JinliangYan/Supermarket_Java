package cn.jxust.supermarket.server.service.exception;

public class DuplicateLoginException extends LoginException {
    public DuplicateLoginException() {
        super();
    }

    public DuplicateLoginException(String message) {
        super(message);
    }

    public DuplicateLoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateLoginException(Throwable cause) {
        super(cause);
    }

    protected DuplicateLoginException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
