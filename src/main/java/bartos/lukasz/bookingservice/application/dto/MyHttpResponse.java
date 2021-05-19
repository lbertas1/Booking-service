package bartos.lukasz.bookingservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyHttpResponse {
    private LocalDateTime dateTime;
    private int httpStatusCode;
    private HttpStatus httpStatus;
    private String message;
}

