package bartos.lukasz.bookingservice.application.exception;

import org.springframework.http.HttpStatus;

public class ReservationServiceException extends ApplicationServiceException {
    public ReservationServiceException(String message, Integer httpResponseCode, HttpStatus httpStatus) {
        super(message, httpResponseCode, httpStatus);
    }
}
