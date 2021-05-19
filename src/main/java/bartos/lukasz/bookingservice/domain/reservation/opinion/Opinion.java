package bartos.lukasz.bookingservice.domain.reservation.opinion;

import bartos.lukasz.bookingservice.domain.reservation.opinion.dto.OpinionDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDate;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class Opinion {
    private LocalDate date;
    @Column(columnDefinition = "VARCHAR(1000)")
    private String message;
    private Integer evaluation;

    public OpinionDto toOpinionDto() {
        return OpinionDto
                .builder()
                .date(date)
                .evaluation(evaluation)
                .message(message)
                .build();
    }
}
