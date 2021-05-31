package bartos.lukasz.bookingservice.domain.reservation.bookingStatus.dto;

import bartos.lukasz.bookingservice.application.enums.PaymentStatus;
import bartos.lukasz.bookingservice.domain.reservation.bookingStatus.BookingStatus;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
@ApiModel(
        value = "BookingStatusDto",
        description = """
                Represents object stored in the reservation in the form of a composition.
                It contains basic information about the reservation status, how the total cost of the reservation 
                and information if it has been paid."""
)
public class BookingStatusDto {
    private PaymentStatus paymentStatus;
    private BigDecimal totalAmountForReservation;

    public BookingStatus toBookingStatus() {
        return BookingStatus.builder()
                .totalAmountForReservation(totalAmountForReservation)
                .paymentStatus(paymentStatus)
                .build();
    }
}
