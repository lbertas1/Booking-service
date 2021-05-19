package bartos.lukasz.bookingservice.application.service.dataServices;

import bartos.lukasz.bookingservice.application.annotations.TimeMeasurement;
import bartos.lukasz.bookingservice.application.dto.FiltersCriteria;
import bartos.lukasz.bookingservice.application.exception.ReservationServiceException;
import bartos.lukasz.bookingservice.application.exception.RoomServiceException;
import bartos.lukasz.bookingservice.domain.reservation.Reservation;
import bartos.lukasz.bookingservice.domain.reservation.ReservationRepository;
import bartos.lukasz.bookingservice.domain.reservation.reservationDto.ReservationDto;
import bartos.lukasz.bookingservice.domain.reservation.reservationDto.ReservationRequestDto;
import bartos.lukasz.bookingservice.domain.room.Room;
import bartos.lukasz.bookingservice.domain.room.RoomRepository;
import bartos.lukasz.bookingservice.domain.room.dto.RoomDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final EntityManager entityManager;

    private void validateRoomRequest(RoomDto roomDto) {
        if (Objects.isNull(roomDto)) {
            throw new RoomServiceException("Given argument is null", 400, HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public RoomDto save(RoomDto roomDto) {
        validateRoomRequest(roomDto);
        return roomRepository.save(roomDto.toRoom()).toRoomDto();
    }

    @Transactional
    public RoomDto update(Long roomId, RoomDto roomDto) throws RoomServiceException {
        validateRoomRequest(roomDto);

        Room room = roomRepository
                .findById(roomId)
                .orElseThrow(() -> new RoomServiceException("Room doesn't found", 404, HttpStatus.NOT_FOUND));

        if (room.getRoomNumber() == null || !room.getRoomNumber().equals(roomDto.getRoomNumber()))
            room.setRoomNumber(roomDto.getRoomNumber());
        if (room.getRoomCapacity() == null || !room.getRoomCapacity().equals(roomDto.getRoomCapacity()))
            room.setRoomCapacity(roomDto.getRoomCapacity());
        if (room.getDescription() == null || !room.getDescription().equals(roomDto.getDescription()))
            room.setDescription(roomDto.getDescription());
        if (room.getPriceForNight() == null || !room.getPriceForNight().equals(roomDto.getPriceForNight()))
            room.setPriceForNight(roomDto.getPriceForNight());
        if (!room.getIsBusy().equals(roomDto.getIsBusy())) room.setIsBusy(roomDto.getIsBusy());

        return roomDto;
    }

    public RoomDto remove(Long id) {
        RoomDto roomDto = get(id);
        roomRepository.deleteById(id);
        return roomDto;
    }

    public List<RoomDto> getAllRooms() {
        return roomRepository
                .findAll()
                .stream()
                .map(Room::toRoomDto)
                .collect(Collectors.toList());
    }

    public RoomDto get(Long id) throws RoomServiceException {
        return roomRepository
                .findById(id)
                .orElseThrow(() -> new RoomServiceException("Room doesn't found", 404, HttpStatus.NOT_FOUND))
                .toRoomDto();
    }

    public List<RoomDto> getRoomsByStatus(boolean isBusy) {
        return roomRepository
                .findAllByIsBusyEquals(isBusy)
                .stream()
                .map(Room::toRoomDto)
                .collect(Collectors.toList());
    }

    public boolean isRoomAvailableInPeriod(Long id, LocalDate from, LocalDate to) {
        Set<Reservation> reservations = reservationRepository.findByRoomId(id);

        if (reservations.isEmpty()) return true;
        else return isAvailable(reservations
                .stream()
                .map(Reservation::toReservationDto)
                .collect(Collectors.toSet()), from, to);
    }

    private boolean isAvailable(Set<ReservationDto> reservationDtos, LocalDate from, LocalDate to) {
        List<Boolean> booleans = reservationDtos
                .stream()
                .map(reservationDto -> {
                    if (reservationDto.getStartOfBooking().isAfter(from) && reservationDto.getStartOfBooking().isBefore(to))
                        return false;
                    if (reservationDto.getEndOfBooking().isAfter(from) && reservationDto.getEndOfBooking().isBefore(to))
                        return false;
                    return !(reservationDto.getStartOfBooking().isBefore(from) && reservationDto.getEndOfBooking().isAfter(to));
                })
                .collect(Collectors.toList());

        return booleans.contains(Boolean.TRUE);
    }

    @Transactional
    public List<RoomDto> setRoomsToBusy() {
        return reservationRepository
                .findAllByStartOfBookingEquals(LocalDate.now())
                .stream()
                .map(Reservation::getRoom)
                .peek(room -> room.setIsBusy(true))
                .map(Room::toRoomDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<RoomDto> setRoomsToEmpty() {
        return reservationRepository
                .findAllByEndOfBookingEquals(LocalDate.now())
                .stream()
                .map(Reservation::getRoom)
                .peek(room -> room.setIsBusy(false))
                .map(Room::toRoomDto)
                .collect(Collectors.toList());
    }

    @TimeMeasurement
    public List<RoomDto> getRoomsByFiltersCriteria(FiltersCriteria filtersCriteria) {
        try {
            return getFilteredRooms(filtersCriteria);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }

        return null;
    }

    private List<RoomDto> getFilteredRooms(FiltersCriteria filtersCriteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Room> criteriaQuery = criteriaBuilder.createQuery(Room.class);
        Root<Room> roomRoot = criteriaQuery.from(Room.class);

        Predicate[] predicates = createPredicates(
                filtersCriteria,
                criteriaBuilder,
                criteriaQuery,
                roomRoot);

        criteriaQuery
                .select(roomRoot)
                .distinct(true)
                .where(predicates);

        return entityManager
                .createQuery(criteriaQuery)
                .getResultList()
                .stream()
                .map(Room::toRoomDto)
                .collect(Collectors.toList());
    }

    private Predicate[] createPredicates(
            FiltersCriteria filtersCriteria,
            CriteriaBuilder criteriaBuilder,
            CriteriaQuery<Room> criteriaQuery,
            Root<Room> roomRoot) {

        List<Predicate> predicatesList = new ArrayList<>();

        if (filtersCriteria.getArrivalDate() != null && filtersCriteria.getDepartureDate() != null &&
                LocalDate.parse(filtersCriteria.getArrivalDate()).isBefore(LocalDate.parse(filtersCriteria.getDepartureDate()))) {

            predicatesList.add(predicateForRoomsAvailableInPeriod(
                    LocalDate.parse(filtersCriteria.getArrivalDate()),
                    LocalDate.parse(filtersCriteria.getDepartureDate()),
                    criteriaBuilder,
                    criteriaQuery,
                    roomRoot
            ));
        }

        if (filtersCriteria.getRoomCapacity() != null && filtersCriteria.getRoomCapacity() > 0) {
            predicatesList.add(predicateForRoomCapacity(filtersCriteria.getRoomCapacity(), criteriaBuilder, roomRoot));
        }

        if (filtersCriteria.getPriceRange() != null) {
            predicatesList.add(predicateForPriceRange(filtersCriteria.getPriceRange(), criteriaBuilder, roomRoot));
        }

        if (filtersCriteria.getSelectedEquipments() != null && filtersCriteria.getSelectedEquipments().size() > 0) {
            predicatesList.addAll(predicateForEquipments(filtersCriteria.getSelectedEquipments(), criteriaBuilder, roomRoot));
        }

        return predicatesList.toArray(new Predicate[0]);
    }

    private Predicate predicateForRoomsAvailableInPeriod(
            LocalDate arrival,
            LocalDate departure,
            CriteriaBuilder criteriaBuilder,
            CriteriaQuery<Room> criteriaQuery,
            Root<Room> roomRoot) {

        Subquery<Room> roomSubquery = criteriaQuery.subquery(Room.class);
        Root<Reservation> subqueryRoomRoot = roomSubquery.from(Reservation.class);

        Predicate startOfBookingBetweenPredicate = criteriaBuilder
                .between(
                        subqueryRoomRoot.get("startOfBooking"),
                        arrival,
                        departure);

        Predicate endOfBookingBetweenPredicate = criteriaBuilder
                .between(
                        subqueryRoomRoot.get("endOfBooking"),
                        arrival,
                        departure);

        Predicate startOrEndOfBookingPredicate = criteriaBuilder
                .or(startOfBookingBetweenPredicate, endOfBookingBetweenPredicate);

        Predicate startOfBookingBeforeArrivalPredicate = criteriaBuilder
                .lessThan(subqueryRoomRoot.get("startOfBooking"),
                        arrival);

        Predicate endOfBookingAfterDeparturePredicate = criteriaBuilder
                .greaterThanOrEqualTo(subqueryRoomRoot.get("endOfBooking"),
                        departure);

        Predicate conjunction = criteriaBuilder
                .and(startOfBookingBeforeArrivalPredicate, endOfBookingAfterDeparturePredicate);

        Predicate roomsInPeriodPredicate = criteriaBuilder
                .or(startOrEndOfBookingPredicate, conjunction);

        roomSubquery
                .select(subqueryRoomRoot.get("room"))
                .distinct(true)
                .where(roomsInPeriodPredicate);

        return roomRoot.in(roomSubquery).not();
    }

    private Predicate predicateForRoomCapacity(int roomCapacity, CriteriaBuilder criteriaBuilder, Root<Room> roomRoot) {
        return criteriaBuilder
                .equal(roomRoot.get("roomCapacity"),
                        roomCapacity);
    }

    private Predicate predicateForPriceRange(String priceRange, CriteriaBuilder criteriaBuilder, Root<Room> roomRoot) {
        return criteriaBuilder
                .between(roomRoot.get("priceForNight"),
                        new BigDecimal(priceRange.split("-")[0]),
                        new BigDecimal(priceRange.split("-")[1]));
    }

    private List<Predicate> predicateForEquipments(List<String> equipments, CriteriaBuilder criteriaBuilder, Root<Room> roomRoot) {
        List<Predicate> predicates = new ArrayList<>();

        equipments
                .forEach(equipment -> predicates
                        .add(criteriaBuilder
                                .isMember(equipment, roomRoot.get("equipment"))));

        return predicates;
    }
}
