package bartos.lukasz.bookingservice.domain.reservation;

import bartos.lukasz.bookingservice.application.enums.PaymentStatus;
import bartos.lukasz.bookingservice.domain.room.RoomBookingDatesProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Set;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Set<Reservation> findByRoomId(Long roomId);

    Set<Reservation> findAllByStartOfBookingBetween(LocalDate from, LocalDate to);

    Set<Reservation> findAllByEndOfBookingBetween(LocalDate from, LocalDate to);

    Set<Reservation> findAllByStartOfBookingIsBeforeAndEndOfBookingIsAfter(LocalDate from, LocalDate to);

    Set<Reservation> findAllByEndOfBookingLessThan(LocalDate date);

    Set<Reservation> findAllByBookingStatusPaymentStatusAndEndOfBookingLessThan(PaymentStatus paymentStatus, LocalDate date);

    Set<Reservation> findAllByUserId(Long userId);

    Set<Reservation> findAllByStartOfBookingEquals(LocalDate date);

    Set<Reservation> findAllByEndOfBookingEquals(LocalDate date);

    Set<Reservation> findAllByStartOfBooking(LocalDate date);

    Set<Reservation> findAllByEndOfBooking(LocalDate date);

    @Query("select r.startOfBooking as reservationStartDate, r.endOfBooking as reservationEndDate from Reservation r where r.room.id = :roomId")
    Set<RoomBookingDatesProjection> findRoomBookingDates(Long roomId);

    @Query("select MAX(r.reservationNumber) from Reservation r")
    Long getBiggestReservationNumber();
}
