package bartos.lukasz.bookingservice.domain.reservation.reservationDto;

import bartos.lukasz.bookingservice.application.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SimpleReservationDto {
    private Integer roomNumber;
    private String startOfBooking;
    private String endOfBooking;
    private String priceForNight;
    private String totalAmountForReservation;
    private PaymentStatus paymentStatus;
    private String opinionDate;
    private String opinionMessage;
    private String opinionEvaluation;
}
