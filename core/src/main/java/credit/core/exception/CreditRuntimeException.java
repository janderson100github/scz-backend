package credit.core.exception;

import org.springframework.http.HttpStatus;

public class CreditRuntimeException extends RuntimeException {

    private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    public CreditRuntimeException(final String msg) {
        super(msg);
    }

    public CreditRuntimeException(final HttpStatus httpStatus) {
        super(httpStatus.toString());
        this.httpStatus = httpStatus;
    }

    public CreditRuntimeException(final HttpStatus httpStatus, final String msg) {
        super(msg);
        this.httpStatus = httpStatus;
    }

    public CreditRuntimeException(String msg, final RuntimeException e) {
        super(msg, e);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
