package bartos.lukasz.bookingservice.application.exception;

import org.springframework.http.HttpStatus;

public class LoginAttemptServiceException extends ApplicationServiceException {
    public LoginAttemptServiceException(String message, Integer httpResponseCode, HttpStatus httpStatus) {
        super(message, httpResponseCode, httpStatus);
    }
}
