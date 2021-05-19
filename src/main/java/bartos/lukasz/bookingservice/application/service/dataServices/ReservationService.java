package bartos.lukasz.bookingservice.application.service.dataServices;

import bartos.lukasz.bookingservice.application.enums.PaymentStatus;
import bartos.lukasz.bookingservice.application.exception.ReservationServiceException;
import bartos.lukasz.bookingservice.domain.reservation.Reservation;
import bartos.lukasz.bookingservice.domain.reservation.ReservationRepository;
import bartos.lukasz.bookingservice.domain.reservation.bookingStatus.dto.BookingStatusDto;
import bartos.lukasz.bookingservice.domain.reservation.reservationDto.ReservationDto;
import bartos.lukasz.bookingservice.domain.reservation.reservationDto.ReservationRequestDto;
import bartos.lukasz.bookingservice.domain.reservation.reservationDto.ReservationResponseDto;
import bartos.lukasz.bookingservice.domain.room.Room;
import bartos.lukasz.bookingservice.domain.room.RoomBookingDatesProjection;
import bartos.lukasz.bookingservice.domain.room.RoomRepository;
import bartos.lukasz.bookingservice.domain.user.User;
import bartos.lukasz.bookingservice.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    private void validateReservationRequest(ReservationRequestDto reservationRequestDto) {
        if (Objects.isNull(reservationRequestDto)) {
            throw new ReservationServiceException("Given reservation object is null", 400, HttpStatus.BAD_REQUEST);
        }

        if (LocalDate.parse(reservationRequestDto.getStartOfBooking()).isBefore(LocalDate.now())) {
            throw new ReservationServiceException("The booking has already started. Beginning cannot be updated now", 400, HttpStatus.BAD_REQUEST);
        }

        if (!LocalDate.parse(reservationRequestDto.getEndOfBooking()).isAfter(LocalDate.parse(reservationRequestDto.getStartOfBooking()))) {
            throw new ReservationServiceException("The booking start date cannot be later than its end date", 400, HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ReservationResponseDto save(ReservationRequestDto reservationRequestDto) throws ReservationServiceException {
        // WALIDACJA, CZY PRZED ZAPISEM DO BAZY, CZY NIE MA TAM JUZ REZERWACJI NA TEN POKÓJ I TERMIN
        validateReservationRequest(reservationRequestDto);

        User user = userRepository
                .findById(reservationRequestDto.getUserId())
                .orElseThrow(() -> new ReservationServiceException("User not found", 404, HttpStatus.NOT_FOUND));


        Room room = roomRepository
                .findById(reservationRequestDto.getRoomId())
                .orElseThrow(() -> new ReservationServiceException("Room not found", 404, HttpStatus.NOT_FOUND));


        Reservation newReservation = reservationRequestDto.toReservation();


        newReservation.setBookingStatus(
                createBookingStatus(
                        LocalDate.parse(reservationRequestDto.getStartOfBooking()),
                        LocalDate.parse(reservationRequestDto.getEndOfBooking()),
                        room.getPriceForNight())
                        .toBookingStatus());

        newReservation.setUser(user);
        newReservation.setRoom(room);

        if (newReservation.getStartOfBooking().equals(LocalDate.now())) {
            room.setIsBusy(true);
            roomRepository.save(room);
        }

        return reservationRepository.save(newReservation).toReservationResponseDto();
    }

//    @Transactional
//    public ReservationResponseDto update(ReservationRequestDto reservationRequestDto) {
//        validateReservationRequest(reservationRequestDto);
//
//        boolean newBookingStatus = false;
//
//        Reservation reservationFromBase = reservationRepository
//                .findById(reservationRequestDto
//                        .getId())
//                .orElseThrow(() -> new ReservationServiceException("Reservation doesn't found", 404, HttpStatus.NOT_FOUND));
//
//
//        if (!reservationFromBase.getUser().getId().equals(reservationRequestDto.getUserId())) {
//            User newUser = userRepository
//                    .findById(reservationRequestDto.getUserId())
//                    .orElseThrow(() -> new ReservationServiceException("User not found", 404, HttpStatus.NOT_FOUND));
//
//            reservationFromBase.setUser(newUser);
//        }
//
//        if (!reservationFromBase.getRoom().getId().equals(reservationRequestDto.getRoomId())) {
//            Room room = roomRepository
//                    .findById(reservationRequestDto.getRoomId())
//                    .orElseThrow(() -> new ReservationServiceException("Room doesn't found", 404, HttpStatus.NOT_FOUND));
//
//            reservationFromBase.setRoom(room);
//            newBookingStatus = true;
//        }
//
//        if (!reservationRequestDto.getStartOfBooking().equals(reservationFromBase.getStartOfBooking())) {
//            reservationFromBase.setStartOfBooking(reservationRequestDto.getStartOfBooking());
//            newBookingStatus = true;
//        }
//
//        if (!reservationRequestDto.getEndOfBooking().equals(reservationFromBase.getEndOfBooking())) {
//            reservationFromBase.setEndOfBooking(reservationRequestDto.getEndOfBooking());
//            newBookingStatus = true;
//        }
//
//        if (newBookingStatus) {
//            reservationFromBase
//                    .setBookingStatus(entityDtoMapper
//                            .toBookingStatus(createBookingStatus(
//                                    reservationFromBase.getStartOfBooking(),
//                                    reservationFromBase.getEndOfBooking(),
//                                    reservationFromBase.getRoom().getPriceForNight())));
//        }
//
//        return entityDtoMapper.fromReservationToReservationResponseDto(reservationRepository.saveAndFlush(reservationFromBase));
//    }

    public ReservationResponseDto getById(Long id) {
        return get(id).toReservationResponseDto();
    }

    private ReservationDto get(Long id) {
        return reservationRepository
                .findById(id)
                .orElseThrow(() -> new ReservationServiceException("Reservation doesn't found", 404, HttpStatus.NOT_FOUND))
                .toReservationDto();
    }

    public List<ReservationResponseDto> searchForEndingsReservations() {
        return reservationRepository
                .findAllByEndOfBookingLessThan(LocalDate.now().plusDays(2))
                .stream()
                .map(Reservation::toReservationResponseDto)
                .collect(Collectors.toList());
    }

    public List<ReservationResponseDto> findAllByPaymentStatusToDate(PaymentStatus paymentStatus, LocalDate date) {
        return reservationRepository
                .findAllByBookingStatusPaymentStatusAndEndOfBookingLessThan(paymentStatus, date)
                .stream()
                .map(Reservation::toReservationResponseDto)
                .collect(Collectors.toList());
    }

    public List<ReservationResponseDto> removeCompletedReservations() {
        List<Reservation> endedReservations = new ArrayList<>(reservationRepository
                .findAllByBookingStatusPaymentStatusAndEndOfBookingLessThan(PaymentStatus.PAID, LocalDate.now()));

        endedReservations
                .stream()
                .map(Reservation::getRoom)
                .peek(roomDto -> roomDto.setIsBusy(false))
                .forEach(roomRepository::save);

        reservationRepository
                .deleteAll(endedReservations);

        return endedReservations
                .stream()
                .map(Reservation::toReservationResponseDto)
                .collect(Collectors.toList());
    }

    public List<ReservationResponseDto> findAllUserReservations(Long userId) {
        return reservationRepository
                .findAllByUserId(userId)
                .stream()
                .map(Reservation::toReservationResponseDto)
                .collect(Collectors.toList());
    }

    private BookingStatusDto createBookingStatus(LocalDate startOfBooking, LocalDate endOfBooking, BigDecimal price) {
        return BookingStatusDto
                .builder()
                .paymentStatus(PaymentStatus.UNPAID)
                .totalAmountForReservation(calculatePriceForReservation(startOfBooking, endOfBooking, price))
                .build();
    }

    public BigDecimal calculatePriceForReservation(LocalDate startOfBooking, LocalDate endOfBooking, BigDecimal priceForNight) {
        Long days = ChronoUnit.DAYS.between(startOfBooking, endOfBooking);
        BigDecimal quantityOfDays = new BigDecimal(String.valueOf(days));
        return quantityOfDays.multiply(priceForNight);
    }

    public Set<RoomBookingDatesProjection> findRoomBookingDates(Long roomId) {
        Set<RoomBookingDatesProjection> roomBookingDateProjections = reservationRepository.findRoomBookingDates(roomId);
        return new HashSet<>(roomBookingDateProjections);
    }
}
