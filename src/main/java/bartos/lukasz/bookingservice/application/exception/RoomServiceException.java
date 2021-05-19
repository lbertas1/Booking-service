package bartos.lukasz.bookingservice.application.exception;

import org.springframework.http.HttpStatus;

public class RoomServiceException extends ApplicationServiceException {
    public RoomServiceException(String message, Integer httpResponseCode, HttpStatus httpStatus) {
        super(message, httpResponseCode, httpStatus);
    }
}
