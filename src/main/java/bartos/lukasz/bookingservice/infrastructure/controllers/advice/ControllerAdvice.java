package bartos.lukasz.bookingservice.infrastructure.controllers.advice;

import bartos.lukasz.bookingservice.application.annotations.CoveredControllerAdvice;
import bartos.lukasz.bookingservice.application.dto.MyHttpResponse;
import bartos.lukasz.bookingservice.application.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice(annotations = CoveredControllerAdvice.class)
public class ControllerAdvice {

    @ExceptionHandler(value = {
            LoggedAdminServiceException.class,
            LoginAttemptServiceException.class,
            ReservationServiceException.class,
            RoomServiceException.class,
            SocketChannelControlServiceException.class,
            UserServiceException.class})
    ResponseEntity<MyHttpResponse> globalExceptionHandler(ApplicationServiceException applicationServiceException) {
        return new ResponseEntity<MyHttpResponse>(MyHttpResponse
                .builder()
                .dateTime(LocalDateTime.now())
                .httpStatus(applicationServiceException.getHttpStatus())
                .httpStatusCode(applicationServiceException.getHttpResponseCode())
                .message(applicationServiceException.getMessage())
                .build(), applicationServiceException.getHttpStatus());
    }
}
