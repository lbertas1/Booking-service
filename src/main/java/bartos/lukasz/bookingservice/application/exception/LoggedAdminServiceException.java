package bartos.lukasz.bookingservice.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class LoggedAdminServiceException extends ApplicationServiceException {
    public LoggedAdminServiceException(String message, Integer httpResponseCode, HttpStatus httpStatus) {
        super(message, httpResponseCode, httpStatus);
    }
}
