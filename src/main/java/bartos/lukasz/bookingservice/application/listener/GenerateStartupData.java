package bartos.lukasz.bookingservice.application.listener;

import bartos.lukasz.bookingservice.application.enums.Equipments;
import bartos.lukasz.bookingservice.domain.reservation.Reservation;
import bartos.lukasz.bookingservice.domain.reservation.ReservationRepository;
import bartos.lukasz.bookingservice.domain.reservation.opinion.dto.OpinionRequestDto;
import bartos.lukasz.bookingservice.domain.reservation.reservationDto.ReservationDto;
import bartos.lukasz.bookingservice.domain.reservation.reservationDto.ReservationRequestDto;
import bartos.lukasz.bookingservice.domain.room.Room;
import bartos.lukasz.bookingservice.domain.room.RoomRepository;
import bartos.lukasz.bookingservice.domain.room.dto.RoomDto;
import bartos.lukasz.bookingservice.domain.user.User;
import bartos.lukasz.bookingservice.domain.user.UserRepository;
import bartos.lukasz.bookingservice.domain.user.dto.UserDto;
import bartos.lukasz.bookingservice.domain.user.enums.Role;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@AllArgsConstructor
public class GenerateStartupData implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (userRepository.findAll().size() == 0) {
            prepareBootData();
        }
    }

    private void prepareBootData() {
        List<UserDto> usersDto = new ArrayList<>();
        List<User> users = List.of(
                User.builder().username("admin1").password("password").email("b_lukasz.1994@wp.pl").country("Poland").name("Lukas").surname("Kowalski").address("1200 W Harrison St").birthday(LocalDate.now().minusYears(24)).city("Chicago").phone("XXXXXXXXX").zipCode("XX-XXX").role(Role.ROLE_ADMIN).build(),
                User.builder().username("admin2").password("password").email("oficjalny.1994@wp.pl").country("Poland").name("Lukas").surname("Kowalski").address("1200 W Harrison St").birthday(LocalDate.now().minusYears(24)).city("Manchester").phone("XXXXXXXXX").zipCode("XX-XXX").role(Role.ROLE_ADMIN).build(),
                User.builder().username("user2").password("password").email("b.lukasz.1994@wp.pl").country("Stany Zjednoczone").name("Lukas").surname("Kowalski").address("1200 W Harrison St").birthday(LocalDate.now().minusYears(24)).city("Chicago").phone("XXXXXXXXX").zipCode("XX-XXX").role(Role.ROLE_USER).build(),
                User.builder().username("user3").password("password").email("lbertas1@wp.pl").country("Kanada").name("Lukas").surname("Kowalski").address("1200 W Harrison St").birthday(LocalDate.now().minusYears(24)).city("Chicago").phone("XXXXXXXXX").zipCode("XX-XXX").role(Role.ROLE_USER).build()
        );
        users.forEach(user -> usersDto.add(userRepository.save(user).toUserDto()));

        List<RoomDto> roomsDto = new ArrayList<>();
        List<Room> rooms = List.of(
                Room.builder().roomNumber(1).roomCapacity(1).description("""
                Lorem ipsum dolor sit amet, consectetur adipisicing elit. Doloremque et nostrum quasi ratione,
                reprehenderit sequi soluta sunt. Accusantium, aspernatur at commodi ex iste laudantium libero nam
                necessitatibus omnis rerum sequi tenetur velit voluptatem! Assumenda, atque dolore est in maiores necessitatibus
                nisi rerum! Ad architecto ipsum maxime. At autem, ipsa laboriosam laudantium nostrum perferendis repellat voluptas
                Deleniti dolorum eaque eos hic omnis repudiandae rerum sint voluptas! Eum odit perferendis unde ut! Alias atque """)
                        .priceForNight(new BigDecimal("100"))
                        .isBusy(false)
                        .equipment(Set.of(Equipments.MINI_FRIDGE.name(), Equipments.MINI_BAR.name(), Equipments.OCEAN_VIEW_ROOM.name()))
                        .build(),
                Room.builder().roomNumber(2).roomCapacity(1).description("""
                Lorem ipsum dolor sit amet, consectetur adipisicing elit. Doloremque et nostrum quasi ratione,
                reprehenderit sequi soluta sunt. Accusantium, aspernatur at commodi ex iste laudantium libero nam
                necessitatibus omnis rerum sequi tenetur velit voluptatem! Assumenda, atque dolore est in maiores necessitatibus
                nisi rerum! Ad architecto ipsum maxime. At autem, ipsa laboriosam laudantium nostrum perferendis repellat voluptas
                Deleniti dolorum eaque eos hic omnis repudiandae rerum sint voluptas! Eum odit perferendis unde ut! Alias atque """)
                        .priceForNight(new BigDecimal("200"))
                        .isBusy(false)
                        .equipment(Set.of(Equipments.XBOX_CONSOLE.name(), Equipments.PROJECTOR.name(), Equipments.OCEAN_VIEW_ROOM.name(),
                                Equipments.MINI_FRIDGE.name(), Equipments.HOT_TUB.name(), Equipments.WATER_BED.name()))
                        .build(),

                Room.builder().roomNumber(3).roomCapacity(1).description("""
                Lorem ipsum dolor sit amet, consectetur adipisicing elit. Doloremque et nostrum quasi ratione,
                reprehenderit sequi soluta sunt. Accusantium, aspernatur at commodi ex iste laudantium libero nam
                necessitatibus omnis rerum sequi tenetur velit voluptatem! Assumenda, atque dolore est in maiores necessitatibus
                nisi rerum! Ad architecto ipsum maxime. At autem, ipsa laboriosam laudantium nostrum perferendis repellat voluptas
                Deleniti dolorum eaque eos hic omnis repudiandae rerum sint voluptas! Eum odit perferendis unde ut! Alias atque """)
                        .priceForNight(new BigDecimal("350"))
                        .isBusy(true)
                        .equipment(Set
                                .of(Equipments.WATER_BED.name(), Equipments.SECOND_BATHROOM.name(), Equipments.MINI_BAR.name(),
                                        Equipments.PROJECTOR.name(), Equipments.HOT_TUB.name(), Equipments.XBOX_CONSOLE.name()))
                        .build(),
                Room.builder().roomNumber(4).roomCapacity(2).description("""
                Lorem ipsum dolor sit amet, consectetur adipisicing elit. Doloremque et nostrum quasi ratione,
                reprehenderit sequi soluta sunt. Accusantium, aspernatur at commodi ex iste laudantium libero nam
                necessitatibus omnis rerum sequi tenetur velit voluptatem! Assumenda, atque dolore est in maiores necessitatibus
                nisi rerum! Ad architecto ipsum maxime. At autem, ipsa laboriosam laudantium nostrum perferendis repellat voluptas
                Deleniti dolorum eaque eos hic omnis repudiandae rerum sint voluptas! Eum odit perferendis unde ut! Alias atque """)
                        .priceForNight(new BigDecimal("400"))
                        .isBusy(true)
                        .equipment(Set
                                .of(Equipments.MINI_BAR.name(), Equipments.MINI_FRIDGE.name(), Equipments.PROJECTOR.name(),
                                        Equipments.WATER_BED.name(), Equipments.OCEAN_VIEW_ROOM.name(), Equipments.SECOND_BATHROOM.name()))
                        .build(),
                Room.builder().roomNumber(5).roomCapacity(2).description("""
                Lorem ipsum dolor sit amet, consectetur adipisicing elit. Doloremque et nostrum quasi ratione,
                reprehenderit sequi soluta sunt. Accusantium, aspernatur at commodi ex iste laudantium libero nam
                necessitatibus omnis rerum sequi tenetur velit voluptatem! Assumenda, atque dolore est in maiores necessitatibus
                nisi rerum! Ad architecto ipsum maxime. At autem, ipsa laboriosam laudantium nostrum perferendis repellat voluptas
                Deleniti dolorum eaque eos hic omnis repudiandae rerum sint voluptas! Eum odit perferendis unde ut! Alias atque """)
                        .priceForNight(new BigDecimal("300"))
                        .isBusy(false)
                        .equipment(Set
                                .of(Equipments.SECOND_BATHROOM.name(), Equipments.MINI_FRIDGE.name(), Equipments.MINI_BAR.name(),
                                        Equipments.OCEAN_VIEW_ROOM.name(), Equipments.WATER_BED.name(), Equipments.HOT_TUB.name(),
                                        Equipments.PROJECTOR.name(), Equipments.XBOX_CONSOLE.name()))
                        .build(),
                Room.builder().roomNumber(6).roomCapacity(2).description("""
                Lorem ipsum dolor sit amet, consectetur adipisicing elit. Doloremque et nostrum quasi ratione,
                reprehenderit sequi soluta sunt. Accusantium, aspernatur at commodi ex iste laudantium libero nam
                necessitatibus omnis rerum sequi tenetur velit voluptatem! Assumenda, atque dolore est in maiores necessitatibus
                nisi rerum! Ad architecto ipsum maxime. At autem, ipsa laboriosam laudantium nostrum perferendis repellat voluptas
                Deleniti dolorum eaque eos hic omnis repudiandae rerum sint voluptas! Eum odit perferendis unde ut! Alias atque """)
                        .priceForNight(new BigDecimal("200"))
                        .isBusy(false)
                        .equipment(Set
                                .of(Equipments.SECOND_BATHROOM.name(), Equipments.MINI_FRIDGE.name(), Equipments.MINI_BAR.name(),
                                        Equipments.OCEAN_VIEW_ROOM.name(), Equipments.WATER_BED.name(), Equipments.HOT_TUB.name(),
                                        Equipments.PROJECTOR.name(), Equipments.XBOX_CONSOLE.name()))
                        .build(),
                Room.builder().roomNumber(7).roomCapacity(2).description("""
                Lorem ipsum dolor sit amet, consectetur adipisicing elit. Doloremque et nostrum quasi ratione,
                reprehenderit sequi soluta sunt. Accusantium, aspernatur at commodi ex iste laudantium libero nam
                necessitatibus omnis rerum sequi tenetur velit voluptatem! Assumenda, atque dolore est in maiores necessitatibus
                nisi rerum! Ad architecto ipsum maxime. At autem, ipsa laboriosam laudantium nostrum perferendis repellat voluptas
                Deleniti dolorum eaque eos hic omnis repudiandae rerum sint voluptas! Eum odit perferendis unde ut! Alias atque """)
                        .priceForNight(new BigDecimal("1000"))
                        .isBusy(false)
                        .equipment(Set
                                .of(Equipments.SECOND_BATHROOM.name(), Equipments.PROJECTOR.name(), Equipments.XBOX_CONSOLE.name()))
                        .build(),
                Room.builder().roomNumber(8).roomCapacity(2).description("""
                Lorem ipsum dolor sit amet, consectetur adipisicing elit. Doloremque et nostrum quasi ratione,
                reprehenderit sequi soluta sunt. Accusantium, aspernatur at commodi ex iste laudantium libero nam
                necessitatibus omnis rerum sequi tenetur velit voluptatem! Assumenda, atque dolore est in maiores necessitatibus
                nisi rerum! Ad architecto ipsum maxime. At autem, ipsa laboriosam laudantium nostrum perferendis repellat voluptas
                Deleniti dolorum eaque eos hic omnis repudiandae rerum sint voluptas! Eum odit perferendis unde ut! Alias atque """)
                        .priceForNight(new BigDecimal("800"))
                        .isBusy(false)
                        .equipment(Set
                                .of(Equipments.SECOND_BATHROOM.name(), Equipments.MINI_FRIDGE.name(), Equipments.MINI_BAR.name(),
                                        Equipments.OCEAN_VIEW_ROOM.name(), Equipments.WATER_BED.name(), Equipments.HOT_TUB.name(),
                                        Equipments.PROJECTOR.name(), Equipments.XBOX_CONSOLE.name()))
                        .build(),
                Room.builder().roomNumber(9).roomCapacity(3).description("""
                Lorem ipsum dolor sit amet, consectetur adipisicing elit. Doloremque et nostrum quasi ratione,
                reprehenderit sequi soluta sunt. Accusantium, aspernatur at commodi ex iste laudantium libero nam
                necessitatibus omnis rerum sequi tenetur velit voluptatem! Assumenda, atque dolore est in maiores necessitatibus
                nisi rerum! Ad architecto ipsum maxime. At autem, ipsa laboriosam laudantium nostrum perferendis repellat voluptas
                Deleniti dolorum eaque eos hic omnis repudiandae rerum sint voluptas! Eum odit perferendis unde ut! Alias atque """)
                        .priceForNight(new BigDecimal("750"))
                        .isBusy(false)
                        .equipment(Set
                                .of(Equipments.SECOND_BATHROOM.name(), Equipments.MINI_FRIDGE.name(), Equipments.MINI_BAR.name(),
                                        Equipments.OCEAN_VIEW_ROOM.name(), Equipments.WATER_BED.name(), Equipments.HOT_TUB.name(),
                                        Equipments.PROJECTOR.name(), Equipments.XBOX_CONSOLE.name()))
                        .build(),
                Room.builder().roomNumber(10).roomCapacity(3).description("""
                Lorem ipsum dolor sit amet, consectetur adipisicing elit. Doloremque et nostrum quasi ratione,
                reprehenderit sequi soluta sunt. Accusantium, aspernatur at commodi ex iste laudantium libero nam
                necessitatibus omnis rerum sequi tenetur velit voluptatem! Assumenda, atque dolore est in maiores necessitatibus
                nisi rerum! Ad architecto ipsum maxime. At autem, ipsa laboriosam laudantium nostrum perferendis repellat voluptas
                Deleniti dolorum eaque eos hic omnis repudiandae rerum sint voluptas! Eum odit perferendis unde ut! Alias atque """)
                        .priceForNight(new BigDecimal("700"))
                        .isBusy(false)
                        .equipment(Set
                                .of(Equipments.SECOND_BATHROOM.name(), Equipments.MINI_FRIDGE.name(), Equipments.MINI_BAR.name(),
                                        Equipments.OCEAN_VIEW_ROOM.name(), Equipments.WATER_BED.name(), Equipments.PROJECTOR.name()))
                        .build(),
                Room.builder().roomNumber(11).roomCapacity(3).description("""
                Lorem ipsum dolor sit amet, consectetur adipisicing elit. Doloremque et nostrum quasi ratione,
                reprehenderit sequi soluta sunt. Accusantium, aspernatur at commodi ex iste laudantium libero nam
                necessitatibus omnis rerum sequi tenetur velit voluptatem! Assumenda, atque dolore est in maiores necessitatibus
                nisi rerum! Ad architecto ipsum maxime. At autem, ipsa laboriosam laudantium nostrum perferendis repellat voluptas
                Deleniti dolorum eaque eos hic omnis repudiandae rerum sint voluptas! Eum odit perferendis unde ut! Alias atque """)
                        .priceForNight(new BigDecimal("650"))
                        .isBusy(false)
                        .equipment(Set
                                .of(Equipments.SECOND_BATHROOM.name(), Equipments.MINI_FRIDGE.name(), Equipments.MINI_BAR.name(),
                                        Equipments.OCEAN_VIEW_ROOM.name(), Equipments.WATER_BED.name(), Equipments.HOT_TUB.name(),
                                        Equipments.PROJECTOR.name(), Equipments.XBOX_CONSOLE.name()))
                        .build(),
                Room.builder().roomNumber(12).roomCapacity(4).description("""
                Lorem ipsum dolor sit amet, consectetur adipisicing elit. Doloremque et nostrum quasi ratione,
                reprehenderit sequi soluta sunt. Accusantium, aspernatur at commodi ex iste laudantium libero nam
                necessitatibus omnis rerum sequi tenetur velit voluptatem! Assumenda, atque dolore est in maiores necessitatibus
                nisi rerum! Ad architecto ipsum maxime. At autem, ipsa laboriosam laudantium nostrum perferendis repellat voluptas
                Deleniti dolorum eaque eos hic omnis repudiandae rerum sint voluptas! Eum odit perferendis unde ut! Alias atque """)
                        .priceForNight(new BigDecimal("900"))
                        .isBusy(false)
                        .equipment(Set
                                .of(Equipments.SECOND_BATHROOM.name(), Equipments.MINI_FRIDGE.name(), Equipments.MINI_BAR.name(),
                                        Equipments.OCEAN_VIEW_ROOM.name(), Equipments.WATER_BED.name(), Equipments.PROJECTOR.name()))
                        .build(),
                Room.builder().roomNumber(13).roomCapacity(4).description("""
                Lorem ipsum dolor sit amet, consectetur adipisicing elit. Doloremque et nostrum quasi ratione,
                reprehenderit sequi soluta sunt. Accusantium, aspernatur at commodi ex iste laudantium libero nam
                necessitatibus omnis rerum sequi tenetur velit voluptatem! Assumenda, atque dolore est in maiores necessitatibus
                nisi rerum! Ad architecto ipsum maxime. At autem, ipsa laboriosam laudantium nostrum perferendis repellat voluptas
                Deleniti dolorum eaque eos hic omnis repudiandae rerum sint voluptas! Eum odit perferendis unde ut! Alias atque """)
                        .priceForNight(new BigDecimal("700"))
                        .isBusy(false)
                        .equipment(Set
                                .of(Equipments.SECOND_BATHROOM.name(), Equipments.MINI_FRIDGE.name(), Equipments.MINI_BAR.name(),
                                        Equipments.OCEAN_VIEW_ROOM.name(), Equipments.WATER_BED.name(), Equipments.PROJECTOR.name(),
                                        Equipments.XBOX_CONSOLE.name()))
                        .build(),
                Room.builder().roomNumber(14).roomCapacity(4).description("""
                Lorem ipsum dolor sit amet, consectetur adipisicing elit. Doloremque et nostrum quasi ratione,
                reprehenderit sequi soluta sunt. Accusantium, aspernatur at commodi ex iste laudantium libero nam
                necessitatibus omnis rerum sequi tenetur velit voluptatem! Assumenda, atque dolore est in maiores necessitatibus
                nisi rerum! Ad architecto ipsum maxime. At autem, ipsa laboriosam laudantium nostrum perferendis repellat voluptas
                Deleniti dolorum eaque eos hic omnis repudiandae rerum sint voluptas! Eum odit perferendis unde ut! Alias atque """)
                        .priceForNight(new BigDecimal("800"))
                        .isBusy(false)
                        .equipment(Set
                                .of(Equipments.SECOND_BATHROOM.name(), Equipments.MINI_FRIDGE.name(), Equipments.MINI_BAR.name(),
                                        Equipments.OCEAN_VIEW_ROOM.name(), Equipments.PROJECTOR.name(), Equipments.XBOX_CONSOLE.name()))
                        .build(),
                Room.builder().roomNumber(15).roomCapacity(5).description("""
                Lorem ipsum dolor sit amet, consectetur adipisicing elit. Doloremque et nostrum quasi ratione,
                reprehenderit sequi soluta sunt. Accusantium, aspernatur at commodi ex iste laudantium libero nam
                necessitatibus omnis rerum sequi tenetur velit voluptatem! Assumenda, atque dolore est in maiores necessitatibus
                nisi rerum! Ad architecto ipsum maxime. At autem, ipsa laboriosam laudantium nostrum perferendis repellat voluptas
                Deleniti dolorum eaque eos hic omnis repudiandae rerum sint voluptas! Eum odit perferendis unde ut! Alias atque """)
                        .priceForNight(new BigDecimal("1000"))
                        .isBusy(false)
                        .equipment(Set
                                .of(Equipments.SECOND_BATHROOM.name(), Equipments.MINI_FRIDGE.name(), Equipments.MINI_BAR.name(),
                                        Equipments.OCEAN_VIEW_ROOM.name(), Equipments.WATER_BED.name(), Equipments.HOT_TUB.name(),
                                        Equipments.PROJECTOR.name(), Equipments.XBOX_CONSOLE.name()))
                        .build(),
                Room.builder().roomNumber(1).roomCapacity(1).description("""
                Lorem ipsum dolor sit amet, consectetur adipisicing elit. Doloremque et nostrum quasi ratione,
                reprehenderit sequi soluta sunt. Accusantium, aspernatur at commodi ex iste laudantium libero nam
                necessitatibus omnis rerum sequi tenetur velit voluptatem! Assumenda, atque dolore est in maiores necessitatibus
                nisi rerum! Ad architecto ipsum maxime. At autem, ipsa laboriosam laudantium nostrum perferendis repellat voluptas
                Deleniti dolorum eaque eos hic omnis repudiandae rerum sint voluptas! Eum odit perferendis unde ut! Alias atque """)
                        .priceForNight(new BigDecimal("150"))
                        .isBusy(false)
                        .equipment(Set
                                .of(Equipments.MINI_FRIDGE.name(), Equipments.MINI_BAR.name(), Equipments.OCEAN_VIEW_ROOM.name(),
                                        Equipments.PROJECTOR.name()))
                        .build(),
                Room.builder().roomNumber(2).roomCapacity(1).description("""
                Lorem ipsum dolor sit amet, consectetur adipisicing elit. Doloremque et nostrum quasi ratione,
                reprehenderit sequi soluta sunt. Accusantium, aspernatur at commodi ex iste laudantium libero nam
                necessitatibus omnis rerum sequi tenetur velit voluptatem! Assumenda, atque dolore est in maiores necessitatibus
                nisi rerum! Ad architecto ipsum maxime. At autem, ipsa laboriosam laudantium nostrum perferendis repellat voluptas
                Deleniti dolorum eaque eos hic omnis repudiandae rerum sint voluptas! Eum odit perferendis unde ut! Alias atque """)
                        .priceForNight(new BigDecimal("200"))
                        .isBusy(false)
                        .equipment(Set
                                .of(Equipments.XBOX_CONSOLE.name(), Equipments.PROJECTOR.name(), Equipments.OCEAN_VIEW_ROOM.name(),
                                        Equipments.MINI_FRIDGE.name(), Equipments.HOT_TUB.name(), Equipments.WATER_BED.name()))
                        .build());

        rooms.forEach(room -> roomsDto.add(roomRepository.save(room).toRoomDto()));

        List<ReservationDto> result = new ArrayList<>();
        List<Reservation> reservations = List.of(
                Reservation.builder().startOfBooking(LocalDate.now().minusDays(5)).endOfBooking(LocalDate.now().plusDays(5)).room(roomsDto.get(0).toRoom()).user(usersDto.get(2).toUser()).build(),
                Reservation.builder().startOfBooking(LocalDate.now().minusDays(5)).endOfBooking(LocalDate.now().plusDays(10)).room(roomsDto.get(1).toRoom()).user(usersDto.get(2).toUser()).build(),
                Reservation.builder().startOfBooking(LocalDate.now().plusDays(10)).endOfBooking(LocalDate.now().plusDays(20)).room(roomsDto.get(2).toRoom()).user(usersDto.get(2).toUser()).build(),
                Reservation.builder().startOfBooking(LocalDate.now().minusDays(10)).endOfBooking(LocalDate.now().plusDays(5)).room(roomsDto.get(3).toRoom()).user(usersDto.get(2).toUser()).build(),
                Reservation.builder().startOfBooking(LocalDate.now().minusDays(15)).endOfBooking(LocalDate.now().plusDays(10)).room(roomsDto.get(4).toRoom()).user(usersDto.get(2).toUser()).build(),
                Reservation.builder().startOfBooking(LocalDate.now().plusDays(15)).endOfBooking(LocalDate.now().plusDays(30)).room(roomsDto.get(5).toRoom()).user(usersDto.get(2).toUser()).build(),
                Reservation.builder().startOfBooking(LocalDate.now().minusDays(10)).endOfBooking(LocalDate.now().plusDays(5)).room(roomsDto.get(6).toRoom()).user(usersDto.get(2).toUser()).build(),
                Reservation.builder().startOfBooking(LocalDate.now().minusDays(10)).endOfBooking(LocalDate.now().plusDays(10)).room(roomsDto.get(7).toRoom()).user(usersDto.get(3).toUser()).build(),
                Reservation.builder().startOfBooking(LocalDate.now().minusDays(15)).endOfBooking(LocalDate.now().plusDays(5)).room(roomsDto.get(8).toRoom()).user(usersDto.get(3).toUser()).build(),
                Reservation.builder().startOfBooking(LocalDate.now().minusDays(15)).endOfBooking(LocalDate.now().plusDays(15)).room(roomsDto.get(9).toRoom()).user(usersDto.get(3).toUser()).build(),
                Reservation.builder().startOfBooking(LocalDate.now().minusDays(2)).endOfBooking(LocalDate.now().plusDays(15)).room(roomsDto.get(10).toRoom()).user(usersDto.get(3).toUser()).build(),
                Reservation.builder().startOfBooking(LocalDate.now().minusDays(3)).endOfBooking(LocalDate.now().plusDays(15)).room(roomsDto.get(11).toRoom()).user(usersDto.get(3).toUser()).build(),
                Reservation.builder().startOfBooking(LocalDate.now().minusDays(4)).endOfBooking(LocalDate.now().plusDays(15)).room(roomsDto.get(12).toRoom()).user(usersDto.get(3).toUser()).build(),
                Reservation.builder().startOfBooking(LocalDate.now().minusDays(5)).endOfBooking(LocalDate.now().plusDays(15)).room(roomsDto.get(13).toRoom()).user(usersDto.get(3).toUser()).build(),
                Reservation.builder().startOfBooking(LocalDate.now().plusDays(20)).endOfBooking(LocalDate.now().plusDays(35)).room(roomsDto.get(14).toRoom()).user(usersDto.get(3).toUser()).build()
        );

        reservations.forEach(reservation -> result.add(reservationRepository.save(reservation).toReservationDto()));

        if (result.size() == reservations.size()) log.info("Initial data was inserted");
    }
}
