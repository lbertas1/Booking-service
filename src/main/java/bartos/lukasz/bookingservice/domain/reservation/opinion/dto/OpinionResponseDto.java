package bartos.lukasz.bookingservice.domain.reservation.opinion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class OpinionResponseDto {

    private Long reservationId;
    private LocalDate date;
    private String message;
    private Integer evaluation;
}
