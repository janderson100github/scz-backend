package credit.controller;

import credit.core.exception.CreditRuntimeException;
import credit.model.ErrorDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class DefaultErrorController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(CreditRuntimeException.class)
    @ResponseBody
    public ErrorDto handleCreditException(final CreditRuntimeException e, HttpServletResponse response) {
        response.setStatus(e.getHttpStatus()
                                   .value());
        return new ErrorDto(e.getMessage());
    }
}
