package bartos.lukasz.bookingservice.application.exception;

import org.springframework.http.HttpStatus;

public class UserServiceException extends ApplicationServiceException {

    public UserServiceException(String message, Integer httpResponseCode, HttpStatus httpStatus) {
        super(message, httpResponseCode, httpStatus);
    }
}
