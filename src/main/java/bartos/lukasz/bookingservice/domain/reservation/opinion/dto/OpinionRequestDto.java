package bartos.lukasz.bookingservice.domain.reservation.opinion.dto;

import bartos.lukasz.bookingservice.domain.reservation.opinion.Opinion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
public class OpinionRequestDto {
    private Long reservationId;
    private LocalDate date;
    private String message;
    private Integer evaluation;

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public Opinion toOpinion() {
        return Opinion
                .builder()
                .date(date)
                .evaluation(evaluation)
                .message(message)
                .build();
    }

    public OpinionDto toOpinionDto() {
        return OpinionDto
                .builder()
                .date(date)
                .evaluation(evaluation)
                .message(message)
                .build();
    }

    public OpinionResponseDto toOpinionResponseDto() {
        return OpinionResponseDto
                .builder()
                .date(date)
                .evaluation(evaluation)
                .message(message)
                .build();
    }
}
