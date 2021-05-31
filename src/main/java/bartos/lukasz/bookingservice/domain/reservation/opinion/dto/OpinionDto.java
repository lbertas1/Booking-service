package bartos.lukasz.bookingservice.domain.reservation.opinion.dto;

import bartos.lukasz.bookingservice.domain.reservation.opinion.Opinion;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
@ApiModel(
        value = "OpinionDto",
        description = """
                Represents object stored in the reservation in the form of a composition.
                It contains basic information about the user's feedback on the booking, such as the numerical rating, message and date."""
)
public class OpinionDto {
    private LocalDate opinionDate;
    private String message;
    private Integer evaluation;

    public Opinion toOpinion() {
        return Opinion
                .builder()
                .opinionDate(opinionDate)
                .evaluation(evaluation)
                .message(message)
                .build();
    }

    public OpinionResponseDto toOpinionResponseDto() {
        return OpinionResponseDto
                .builder()
                .opinionDate(opinionDate)
                .evaluation(evaluation)
                .message(message)
                .build();
    }
}
