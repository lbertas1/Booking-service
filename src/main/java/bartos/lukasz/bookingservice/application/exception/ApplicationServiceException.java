package bartos.lukasz.bookingservice.application.exception;

import org.springframework.http.HttpStatus;

public abstract class ApplicationServiceException extends RuntimeException{

    private final Integer httpResponseCode;
    private final HttpStatus httpStatus;

    public ApplicationServiceException(String message, Integer httpResponseCode, HttpStatus httpStatus) {
        super(message);
        this.httpResponseCode = httpResponseCode;
        this.httpStatus = httpStatus;
    }

    public Integer getHttpResponseCode() {
        return httpResponseCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
