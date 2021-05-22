package bartos.lukasz.bookingservice.domain.reservation.opinion.dto;

import bartos.lukasz.bookingservice.domain.reservation.opinion.Opinion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class OpinionRequestDto {
    private List<Long>reservationsId;
    private String opinionDate;
    private String message;
    private Integer evaluation;

    public void setReservationsId(List<Long> reservationsId) {
        this.reservationsId = reservationsId;
    }

    public Opinion toOpinion() {
        return Opinion
                .builder()
                .opinionDate(LocalDate.parse(opinionDate))
                .evaluation(evaluation)
                .message(message)
                .build();
    }

    public OpinionDto toOpinionDto() {
        return OpinionDto
                .builder()
                .opinionDate(LocalDate.parse(opinionDate))
                .evaluation(evaluation)
                .message(message)
                .build();
    }

    public OpinionResponseDto toOpinionResponseDto() {
        return OpinionResponseDto
                .builder()
                .opinionDate(LocalDate.parse(opinionDate))
                .evaluation(evaluation)
                .message(message)
                .build();
    }
}
