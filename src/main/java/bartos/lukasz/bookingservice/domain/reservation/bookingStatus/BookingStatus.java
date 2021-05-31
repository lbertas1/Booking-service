package bartos.lukasz.bookingservice.domain.reservation.bookingStatus;

import bartos.lukasz.bookingservice.application.enums.PaymentStatus;
import bartos.lukasz.bookingservice.domain.reservation.bookingStatus.dto.BookingStatusDto;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Embeddable;
import java.math.BigDecimal;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class BookingStatus {

    private PaymentStatus paymentStatus;
    private BigDecimal totalAmountForReservation;

    public BookingStatusDto toBookingStatusDto() {
        return BookingStatusDto
                .builder()
                .totalAmountForReservation(totalAmountForReservation)
                .paymentStatus(paymentStatus)
                .build();
    }
}
