package bartos.lukasz.bookingservice.application.service.dataServices;

import bartos.lukasz.bookingservice.application.enums.Equipments;
import bartos.lukasz.bookingservice.application.exception.RoomServiceException;
import bartos.lukasz.bookingservice.application.exception.UserServiceException;
import bartos.lukasz.bookingservice.domain.reservation.ReservationRepository;
import bartos.lukasz.bookingservice.domain.room.Room;
import bartos.lukasz.bookingservice.domain.room.RoomRepository;
import bartos.lukasz.bookingservice.domain.room.dto.RoomDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class RoomServiceTest {

    @TestConfiguration
    public static class RoomServiceTestConfiguration {
        @MockBean
        private RoomRepository roomRepository;

        @MockBean
        private ReservationRepository reservationRepository;

        @MockBean
        private EntityManager entityManager;

        @Bean
        public RoomService roomsService() {
            return new RoomService(roomRepository, reservationRepository, entityManager);
        }
    }

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private RoomService roomService;

    private RoomDto roomDto1;
    private RoomDto roomDto2;

    private Room room1;
    private Room room2;

    private List<Room> rooms;
    private List<RoomDto> roomsDto;

    @BeforeEach
    private void prepareData() {
        roomDto1 = RoomDto
                .builder()
                .roomNumber(1)
                .priceForNight(new BigDecimal("100"))
                .roomCapacity(1)
                .isBusy(false)
                .description("Description")
                .equipments(Set.of(Equipments.HOT_TUB, Equipments.XBOX_CONSOLE, Equipments.MINI_BAR))
                .build();

        room1 = roomDto1.toRoom();

        roomDto2 = RoomDto
                .builder()
                .id(1L)
                .roomNumber(1)
                .priceForNight(new BigDecimal("100"))
                .roomCapacity(1)
                .isBusy(false)
                .description("Description")
                .equipments(Set.of(Equipments.HOT_TUB, Equipments.XBOX_CONSOLE, Equipments.MINI_BAR))
                .build();

        room2 = roomDto2.toRoom();
    }

    @Test
    public void save() {
        when(roomRepository.save(room1)).thenReturn(room2);

        assertAll(() -> {
            assertNotNull(roomService.save(roomDto1).getId());
        });
    }

    @Test
    public void get() {
        when(roomRepository.findById(room2.getId())).thenReturn(Optional.of(room2));
        assertEquals(roomDto2.getId(), roomService.get(room2.getId()).getId());
    }

    @Test
    public void get_throwRoomExceptionWhenIdIsNull() {
        assertThrows(RoomServiceException.class, () -> roomService.remove(null));
    }

    @Test
    public void remove_throwRoomExceptionWhenIdIsNull() {
      assertThrows(RoomServiceException.class, () -> roomService.remove(null));
    }
}