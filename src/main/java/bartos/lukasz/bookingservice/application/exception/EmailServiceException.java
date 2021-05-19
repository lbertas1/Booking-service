package bartos.lukasz.bookingservice.application.exception;

import org.springframework.http.HttpStatus;

public class EmailServiceException extends ApplicationServiceException {

    public EmailServiceException(String message, Integer httpResponseCode, HttpStatus httpStatus) {
        super(message, httpResponseCode, httpStatus);
    }
}
