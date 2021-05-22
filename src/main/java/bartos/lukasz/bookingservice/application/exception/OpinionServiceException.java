package bartos.lukasz.bookingservice.application.exception;

import org.springframework.http.HttpStatus;

public class OpinionServiceException extends ApplicationServiceException{
    public OpinionServiceException(String message, Integer httpResponseCode, HttpStatus httpStatus) {
        super(message, httpResponseCode, httpStatus);
    }
}
