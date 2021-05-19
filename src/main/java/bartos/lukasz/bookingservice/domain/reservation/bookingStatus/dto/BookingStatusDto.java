package bartos.lukasz.bookingservice.domain.reservation.bookingStatus.dto;

import bartos.lukasz.bookingservice.application.enums.PaymentStatus;
import bartos.lukasz.bookingservice.domain.reservation.bookingStatus.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
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
