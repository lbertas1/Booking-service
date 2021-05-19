package bartos.lukasz.bookingservice.domain.room;

import java.time.LocalDate;

public interface RoomBookingDatesProjection {
    LocalDate getReservationStartDate();
    LocalDate getReservationEndDate();
}
