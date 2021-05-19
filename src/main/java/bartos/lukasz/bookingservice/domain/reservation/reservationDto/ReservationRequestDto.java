package bartos.lukasz.bookingservice.domain.reservation.reservationDto;

import bartos.lukasz.bookingservice.domain.reservation.Reservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class ReservationRequestDto {
    private String startOfBooking;
    private String endOfBooking;
    private Long userId;
    private Long roomId;

    public Reservation toReservation() {
        return Reservation
                .builder()
                .startOfBooking(LocalDate.parse(startOfBooking))
                .endOfBooking(LocalDate.parse(endOfBooking))
                .build();
    }
}
