package bartos.lukasz.bookingservice.domain.reservation.opinion.dto;

import bartos.lukasz.bookingservice.domain.reservation.opinion.Opinion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class OpinionDto {
    private LocalDate date;
    private String message;
    private Integer evaluation;

    public Opinion toOpinion() {
        return Opinion
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
