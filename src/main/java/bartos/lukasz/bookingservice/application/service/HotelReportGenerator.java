package bartos.lukasz.bookingservice.application.service;

import bartos.lukasz.bookingservice.domain.reservation.Reservation;
import bartos.lukasz.bookingservice.domain.reservation.ReservationRepository;
import bartos.lukasz.bookingservice.domain.reservation.reservationDto.ReservationInfoDto;
import bartos.lukasz.bookingservice.infrastructure.security.dto.AdminIdentity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HotelReportGenerator {

    private final ReservationRepository reservationRepository;

    /**
     * *************** POMYŚLEĆ CZY NIE ZROBIĆ DLA ADMINA W RAMACH ZWYKŁEJ METODKI, ŻEBY MÓGŁ SOBIE POBRAĆ
     * KOŃCZĄCE SIĘ DZIS REZERWACJE, TO WTEDY MOŻE ZROBIĆ TE METODY JAKIMŚ WZROCEM PROJEKTOWYM, UŻYĆ INTERFEJSU,
     * ŻEBY NIE PISAC JEJ DWA RAZY NP.
     */

    public AdminIdentity generateReport(AdminIdentity adminIdentity) {
        adminIdentity.setCommencingReservations(getStartingReservations());
        adminIdentity.setEndingsReservations(getEndingsReservations());
        adminIdentity.setReservationsStartingIn3Days(getReservationByStartDayBetween(LocalDate.now().plusDays(1) ,LocalDate.now().plusDays(3)));
        adminIdentity.setReservationsStartingIn7Days(getReservationByStartDayBetween(LocalDate.now().plusDays(4), LocalDate.now().plusDays(7)));
        return adminIdentity;
    }

    private List<ReservationInfoDto> getEndingsReservations() {
        return this.reservationRepository
                .findAllByEndOfBooking(LocalDate.now())
                .stream()
                .map(Reservation::toReservationInfoDto)
                .collect(Collectors.toList());
    }

    private List<ReservationInfoDto> getStartingReservations() {
        return this.reservationRepository
                .findAllByStartOfBooking(LocalDate.now())
                .stream()
                .map(Reservation::toReservationInfoDto)
                .collect(Collectors.toList());
    }

    private List<ReservationInfoDto> getReservationByStartDayBetween(LocalDate from, LocalDate to) {
        return this.reservationRepository
                .findAllByStartOfBookingBetween(from, to)
                .stream()
                .map(Reservation::toReservationInfoDto)
                .collect(Collectors.toList());
    }
}