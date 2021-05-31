package bartos.lukasz.bookingservice.infrastructure.controllers;

import bartos.lukasz.bookingservice.application.annotations.CoveredControllerAdvice;
import bartos.lukasz.bookingservice.application.enums.EmailContent;
import bartos.lukasz.bookingservice.application.enums.PaymentStatus;
import bartos.lukasz.bookingservice.application.exception.EmailServiceException;
import bartos.lukasz.bookingservice.application.exception.ReservationServiceException;
import bartos.lukasz.bookingservice.application.service.dataServices.ReservationService;
import bartos.lukasz.bookingservice.application.service.email.EmailService;
import bartos.lukasz.bookingservice.domain.reservation.reservationDto.ReservationDto;
import bartos.lukasz.bookingservice.domain.reservation.reservationDto.ReservationRequestDto;
import bartos.lukasz.bookingservice.domain.reservation.reservationDto.ReservationResponseDto;
import bartos.lukasz.bookingservice.domain.room.RoomBookingDatesProjection;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
@CoveredControllerAdvice
@Api(value = "/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final EmailService emailService;

    @ApiOperation(value = """
            Returns list of ReservationResponseDto objects for ending bookings.""")
    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("/endings")
    ResponseEntity<List<ReservationResponseDto>> searchForEndingReservations() {
        return ResponseEntity.ok(reservationService.searchForEndingsReservations());
    }

    @ApiOperation(value = """
            Returns ReservationResponseDto object after saving a new booking.""")
    @PostMapping("/save")
    ResponseEntity<ReservationResponseDto> save(@RequestBody ReservationRequestDto reservationRequestDto) throws ReservationServiceException, EmailServiceException {
        ReservationDto savedReservation = reservationService.save(reservationRequestDto);

//        emailService
//                .send(savedReservation.getUserDto(),
//                        List.of(savedReservation),
//                        savedReservation.getReservationNumber().toString(),
//                        EmailContent.RESERVATION);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedReservation.toReservationResponseDto());
    }

//    @RolesAllowed("ROLE_ADMIN")
//    @PostMapping("/update")
//    ResponseEntity<ReservationResponseDto> update(@RequestBody ReservationRequestDto reservationRequestDto) {
//        return ResponseEntity.ok(reservationService.update(reservationRequestDto));
//    }

    @ApiOperation(value = """
            Returns a list of unpaid reservations in the form of a ReservationResponseDto list.""")
    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("/unpaid-reservation-to-date/{date}")
    ResponseEntity<List<ReservationResponseDto>> getAllUnpaidReservationsTo(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(reservationService.findAllByPaymentStatusToDate(PaymentStatus.UNPAID, date));
    }

    @ApiOperation(value = """
            Returns the set of reserved terms.""")
    @GetMapping("/room-booking-dates/{roomId}")
    ResponseEntity<Set<RoomBookingDatesProjection>> getRoomBookingDates(@PathVariable Long roomId) {
        return ResponseEntity.ok(reservationService.findRoomBookingDates(roomId));
    }
}