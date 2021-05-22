package bartos.lukasz.bookingservice.application;

import bartos.lukasz.bookingservice.application.enums.Equipments;
import bartos.lukasz.bookingservice.application.service.dataServices.OpinionService;
import bartos.lukasz.bookingservice.application.service.dataServices.ReservationService;
import bartos.lukasz.bookingservice.application.service.dataServices.RoomService;
import bartos.lukasz.bookingservice.application.service.dataServices.UserService;
import bartos.lukasz.bookingservice.application.service.email.EmailPdfFileService;
import bartos.lukasz.bookingservice.application.service.email.EmailService;
import bartos.lukasz.bookingservice.domain.reservation.ReservationRepository;
import bartos.lukasz.bookingservice.domain.reservation.opinion.dto.OpinionRequestDto;
import bartos.lukasz.bookingservice.domain.reservation.reservationDto.ReservationRequestDto;
import bartos.lukasz.bookingservice.domain.reservation.reservationDto.ReservationResponseDto;
import bartos.lukasz.bookingservice.domain.room.dto.RoomDto;
import bartos.lukasz.bookingservice.domain.user.dto.RegisterUserDto;
import bartos.lukasz.bookingservice.domain.user.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommandLine implements CommandLineRunner {

    private final RoomService roomService;
    private final ReservationService reservationService;
    private final UserService userService;
    private final OpinionService opinionService;
    private final EmailService emailService;
    private final EmailPdfFileService emailPdfFileService;
    private final EntityManager em;
    private final ReservationRepository reservationRepository;

    @Value("${spring.profiles.active}")
    private String applicationProfile;

    @Override
    public void run(String... args) throws Exception {


//        if (applicationProfile.equals("prod")) {
//            prepareBootData();
//        }
    }

//    private void prepareBootData() {
//        RegisterUserDto userDto1 = RegisterUserDto.builder().username("admin1").password("password").email("b_lukasz.1994@wp.pl").country("Poland").name("Lukas").surname("Kowalski").address("1200 W Harrison St").birthDate(LocalDate.now().minusYears(24).toString()).city("Chicago").phone("XXXXXXXXX").zipCode("XX-XXX").build();
//        RegisterUserDto userDto4 = RegisterUserDto.builder().username("admin2").password("password").email("oficjalny.1994@wp.pl").country("Poland").name("Lukas").surname("Kowalski").address("1200 W Harrison St").birthDate(LocalDate.now().minusYears(24).toString()).city("Manchester").phone("XXXXXXXXX").zipCode("XX-XXX").build();
//        RegisterUserDto userDto2 = RegisterUserDto.builder().username("user2").password("password").email("b.lukasz.1994@wp.pl").country("Stany Zjednoczone").name("Lukas").surname("Kowalski").address("1200 W Harrison St").birthDate(LocalDate.now().minusYears(24).toString()).city("Chicago").phone("XXXXXXXXX").zipCode("XX-XXX").build();
//        RegisterUserDto userDto3 = RegisterUserDto.builder().username("user3").password("password").email("lbertas1@wp.pl").country("Kanada").name("Lukas").surname("Kowalski").address("1200 W Harrison St").birthDate(LocalDate.now().minusYears(24).toString()).city("Chicago").phone("XXXXXXXXX").zipCode("XX-XXX").build();
//
//        UserResponseDto save = userService.save(userDto1);
//        UserResponseDto save18 = userService.save(userDto4);
//        UserResponseDto save1 = userService.save(userDto2);
//        UserResponseDto save17 = userService.save(userDto3);
//
//        userService.changeRoleOnAdmin("admin1");
//        userService.changeRoleOnAdmin("admin2");
//
//        RoomDto roomDto1 = RoomDto.builder().roomNumber(1).roomCapacity(1).description("""
//                Lorem ipsum dolor sit amet, consectetur adipisicing elit. Doloremque et nostrum quasi ratione,
//                reprehenderit sequi soluta sunt. Accusantium, aspernatur at commodi ex iste laudantium libero nam
//                necessitatibus omnis rerum sequi tenetur velit voluptatem! Assumenda, atque dolore est in maiores necessitatibus
//                nisi rerum! Ad architecto ipsum maxime. At autem, ipsa laboriosam laudantium nostrum perferendis repellat voluptas
//                Deleniti dolorum eaque eos hic omnis repudiandae rerum sint voluptas! Eum odit perferendis unde ut! Alias atque """)
//                .priceForNight(new BigDecimal("100"))
//                .isBusy(false)
//                .equipments(Set
//                        .of(Equipments.MINI_FRIDGE, Equipments.MINI_BAR, Equipments.OCEAN_VIEW_ROOM))
//                .build();
//
//        RoomDto roomDto2 = RoomDto.builder().roomNumber(2).roomCapacity(1).description("""
//                Lorem ipsum dolor sit amet, consectetur adipisicing elit. Doloremque et nostrum quasi ratione,
//                reprehenderit sequi soluta sunt. Accusantium, aspernatur at commodi ex iste laudantium libero nam
//                necessitatibus omnis rerum sequi tenetur velit voluptatem! Assumenda, atque dolore est in maiores necessitatibus
//                nisi rerum! Ad architecto ipsum maxime. At autem, ipsa laboriosam laudantium nostrum perferendis repellat voluptas
//                Deleniti dolorum eaque eos hic omnis repudiandae rerum sint voluptas! Eum odit perferendis unde ut! Alias atque """)
//                .priceForNight(new BigDecimal("200"))
//                .isBusy(false)
//                .equipments(Set
//                        .of(Equipments.XBOX_CONSOLE, Equipments.PROJECTOR, Equipments.OCEAN_VIEW_ROOM, Equipments.MINI_FRIDGE, Equipments.HOT_TUB, Equipments.WATER_BED))
//                .build();
//
//        RoomDto roomDto3 = RoomDto.builder().roomNumber(3).roomCapacity(1).description("""
//                Lorem ipsum dolor sit amet, consectetur adipisicing elit. Doloremque et nostrum quasi ratione,
//                reprehenderit sequi soluta sunt. Accusantium, aspernatur at commodi ex iste laudantium libero nam
//                necessitatibus omnis rerum sequi tenetur velit voluptatem! Assumenda, atque dolore est in maiores necessitatibus
//                nisi rerum! Ad architecto ipsum maxime. At autem, ipsa laboriosam laudantium nostrum perferendis repellat voluptas
//                Deleniti dolorum eaque eos hic omnis repudiandae rerum sint voluptas! Eum odit perferendis unde ut! Alias atque """)
//                .priceForNight(new BigDecimal("350"))
//                .isBusy(true)
//                .equipments(Set
//                        .of(Equipments.WATER_BED, Equipments.SECOND_BATHROOM, Equipments.MINI_BAR, Equipments.PROJECTOR, Equipments.HOT_TUB, Equipments.XBOX_CONSOLE))
//                .build();
//
//        RoomDto roomDto4 = RoomDto.builder().roomNumber(4).roomCapacity(2).description("""
//                Lorem ipsum dolor sit amet, consectetur adipisicing elit. Doloremque et nostrum quasi ratione,
//                reprehenderit sequi soluta sunt. Accusantium, aspernatur at commodi ex iste laudantium libero nam
//                necessitatibus omnis rerum sequi tenetur velit voluptatem! Assumenda, atque dolore est in maiores necessitatibus
//                nisi rerum! Ad architecto ipsum maxime. At autem, ipsa laboriosam laudantium nostrum perferendis repellat voluptas
//                Deleniti dolorum eaque eos hic omnis repudiandae rerum sint voluptas! Eum odit perferendis unde ut! Alias atque """)
//                .priceForNight(new BigDecimal("400"))
//                .isBusy(true)
//                .equipments(Set
//                        .of(Equipments.MINI_BAR, Equipments.MINI_FRIDGE, Equipments.PROJECTOR, Equipments.WATER_BED, Equipments.OCEAN_VIEW_ROOM, Equipments.SECOND_BATHROOM))
//                .build();
//
//        RoomDto roomDto5 = RoomDto.builder().roomNumber(5).roomCapacity(2).description("""
//                Lorem ipsum dolor sit amet, consectetur adipisicing elit. Doloremque et nostrum quasi ratione,
//                reprehenderit sequi soluta sunt. Accusantium, aspernatur at commodi ex iste laudantium libero nam
//                necessitatibus omnis rerum sequi tenetur velit voluptatem! Assumenda, atque dolore est in maiores necessitatibus
//                nisi rerum! Ad architecto ipsum maxime. At autem, ipsa laboriosam laudantium nostrum perferendis repellat voluptas
//                Deleniti dolorum eaque eos hic omnis repudiandae rerum sint voluptas! Eum odit perferendis unde ut! Alias atque """)
//                .priceForNight(new BigDecimal("300"))
//                .isBusy(false)
//                .equipments(Set
//                        .of(Equipments.SECOND_BATHROOM, Equipments.MINI_FRIDGE, Equipments.MINI_BAR, Equipments.OCEAN_VIEW_ROOM, Equipments.WATER_BED, Equipments.HOT_TUB, Equipments.PROJECTOR, Equipments.XBOX_CONSOLE))
//                .build();
//
//        RoomDto roomDto6 = RoomDto.builder().roomNumber(6).roomCapacity(2).description("""
//                Lorem ipsum dolor sit amet, consectetur adipisicing elit. Doloremque et nostrum quasi ratione,
//                reprehenderit sequi soluta sunt. Accusantium, aspernatur at commodi ex iste laudantium libero nam
//                necessitatibus omnis rerum sequi tenetur velit voluptatem! Assumenda, atque dolore est in maiores necessitatibus
//                nisi rerum! Ad architecto ipsum maxime. At autem, ipsa laboriosam laudantium nostrum perferendis repellat voluptas
//                Deleniti dolorum eaque eos hic omnis repudiandae rerum sint voluptas! Eum odit perferendis unde ut! Alias atque """)
//                .priceForNight(new BigDecimal("200"))
//                .isBusy(false)
//                .equipments(Set
//                        .of(Equipments.SECOND_BATHROOM, Equipments.MINI_FRIDGE, Equipments.MINI_BAR, Equipments.OCEAN_VIEW_ROOM, Equipments.WATER_BED, Equipments.HOT_TUB, Equipments.PROJECTOR, Equipments.XBOX_CONSOLE))
//                .build();
//
//        RoomDto roomDto7 = RoomDto.builder().roomNumber(7).roomCapacity(2).description("""
//                Lorem ipsum dolor sit amet, consectetur adipisicing elit. Doloremque et nostrum quasi ratione,
//                reprehenderit sequi soluta sunt. Accusantium, aspernatur at commodi ex iste laudantium libero nam
//                necessitatibus omnis rerum sequi tenetur velit voluptatem! Assumenda, atque dolore est in maiores necessitatibus
//                nisi rerum! Ad architecto ipsum maxime. At autem, ipsa laboriosam laudantium nostrum perferendis repellat voluptas
//                Deleniti dolorum eaque eos hic omnis repudiandae rerum sint voluptas! Eum odit perferendis unde ut! Alias atque """)
//                .priceForNight(new BigDecimal("1000"))
//                .isBusy(false)
//                .equipments(Set
//                        .of(Equipments.SECOND_BATHROOM, Equipments.PROJECTOR, Equipments.XBOX_CONSOLE))
//                .build();
//
//        RoomDto roomDto8 = RoomDto.builder().roomNumber(8).roomCapacity(2).description("""
//                Lorem ipsum dolor sit amet, consectetur adipisicing elit. Doloremque et nostrum quasi ratione,
//                reprehenderit sequi soluta sunt. Accusantium, aspernatur at commodi ex iste laudantium libero nam
//                necessitatibus omnis rerum sequi tenetur velit voluptatem! Assumenda, atque dolore est in maiores necessitatibus
//                nisi rerum! Ad architecto ipsum maxime. At autem, ipsa laboriosam laudantium nostrum perferendis repellat voluptas
//                Deleniti dolorum eaque eos hic omnis repudiandae rerum sint voluptas! Eum odit perferendis unde ut! Alias atque """)
//                .priceForNight(new BigDecimal("800"))
//                .isBusy(false)
//                .equipments(Set
//                        .of(Equipments.SECOND_BATHROOM, Equipments.MINI_FRIDGE, Equipments.MINI_BAR, Equipments.OCEAN_VIEW_ROOM, Equipments.WATER_BED, Equipments.HOT_TUB, Equipments.PROJECTOR, Equipments.XBOX_CONSOLE))
//                .build();
//
//        RoomDto roomDto9 = RoomDto.builder().roomNumber(9).roomCapacity(3).description("""
//                Lorem ipsum dolor sit amet, consectetur adipisicing elit. Doloremque et nostrum quasi ratione,
//                reprehenderit sequi soluta sunt. Accusantium, aspernatur at commodi ex iste laudantium libero nam
//                necessitatibus omnis rerum sequi tenetur velit voluptatem! Assumenda, atque dolore est in maiores necessitatibus
//                nisi rerum! Ad architecto ipsum maxime. At autem, ipsa laboriosam laudantium nostrum perferendis repellat voluptas
//                Deleniti dolorum eaque eos hic omnis repudiandae rerum sint voluptas! Eum odit perferendis unde ut! Alias atque """)
//                .priceForNight(new BigDecimal("750"))
//                .isBusy(false)
//                .equipments(Set
//                        .of(Equipments.SECOND_BATHROOM, Equipments.MINI_FRIDGE, Equipments.MINI_BAR, Equipments.OCEAN_VIEW_ROOM, Equipments.WATER_BED, Equipments.HOT_TUB, Equipments.PROJECTOR, Equipments.XBOX_CONSOLE))
//                .build();
//
//        RoomDto roomDto10 = RoomDto.builder().roomNumber(10).roomCapacity(3).description("""
//                Lorem ipsum dolor sit amet, consectetur adipisicing elit. Doloremque et nostrum quasi ratione,
//                reprehenderit sequi soluta sunt. Accusantium, aspernatur at commodi ex iste laudantium libero nam
//                necessitatibus omnis rerum sequi tenetur velit voluptatem! Assumenda, atque dolore est in maiores necessitatibus
//                nisi rerum! Ad architecto ipsum maxime. At autem, ipsa laboriosam laudantium nostrum perferendis repellat voluptas
//                Deleniti dolorum eaque eos hic omnis repudiandae rerum sint voluptas! Eum odit perferendis unde ut! Alias atque """)
//                .priceForNight(new BigDecimal("700"))
//                .isBusy(false)
//                .equipments(Set
//                        .of(Equipments.SECOND_BATHROOM, Equipments.MINI_FRIDGE, Equipments.MINI_BAR, Equipments.OCEAN_VIEW_ROOM, Equipments.WATER_BED, Equipments.PROJECTOR))
//                .build();
//
//        RoomDto roomDto11 = RoomDto.builder().roomNumber(11).roomCapacity(3).description("""
//                Lorem ipsum dolor sit amet, consectetur adipisicing elit. Doloremque et nostrum quasi ratione,
//                reprehenderit sequi soluta sunt. Accusantium, aspernatur at commodi ex iste laudantium libero nam
//                necessitatibus omnis rerum sequi tenetur velit voluptatem! Assumenda, atque dolore est in maiores necessitatibus
//                nisi rerum! Ad architecto ipsum maxime. At autem, ipsa laboriosam laudantium nostrum perferendis repellat voluptas
//                Deleniti dolorum eaque eos hic omnis repudiandae rerum sint voluptas! Eum odit perferendis unde ut! Alias atque """)
//                .priceForNight(new BigDecimal("650"))
//                .isBusy(false)
//                .equipments(Set
//                        .of(Equipments.SECOND_BATHROOM, Equipments.MINI_FRIDGE, Equipments.MINI_BAR, Equipments.OCEAN_VIEW_ROOM, Equipments.WATER_BED, Equipments.HOT_TUB, Equipments.PROJECTOR, Equipments.XBOX_CONSOLE))
//                .build();
//
//        RoomDto roomDto12 = RoomDto.builder().roomNumber(12).roomCapacity(4).description("""
//                Lorem ipsum dolor sit amet, consectetur adipisicing elit. Doloremque et nostrum quasi ratione,
//                reprehenderit sequi soluta sunt. Accusantium, aspernatur at commodi ex iste laudantium libero nam
//                necessitatibus omnis rerum sequi tenetur velit voluptatem! Assumenda, atque dolore est in maiores necessitatibus
//                nisi rerum! Ad architecto ipsum maxime. At autem, ipsa laboriosam laudantium nostrum perferendis repellat voluptas
//                Deleniti dolorum eaque eos hic omnis repudiandae rerum sint voluptas! Eum odit perferendis unde ut! Alias atque """)
//                .priceForNight(new BigDecimal("900"))
//                .isBusy(false)
//                .equipments(Set
//                        .of(Equipments.SECOND_BATHROOM, Equipments.MINI_FRIDGE, Equipments.MINI_BAR, Equipments.OCEAN_VIEW_ROOM, Equipments.WATER_BED, Equipments.PROJECTOR))
//                .build();
//
//        RoomDto roomDto13 = RoomDto.builder().roomNumber(13).roomCapacity(4).description("""
//                Lorem ipsum dolor sit amet, consectetur adipisicing elit. Doloremque et nostrum quasi ratione,
//                reprehenderit sequi soluta sunt. Accusantium, aspernatur at commodi ex iste laudantium libero nam
//                necessitatibus omnis rerum sequi tenetur velit voluptatem! Assumenda, atque dolore est in maiores necessitatibus
//                nisi rerum! Ad architecto ipsum maxime. At autem, ipsa laboriosam laudantium nostrum perferendis repellat voluptas
//                Deleniti dolorum eaque eos hic omnis repudiandae rerum sint voluptas! Eum odit perferendis unde ut! Alias atque """)
//                .priceForNight(new BigDecimal("700"))
//                .isBusy(false)
//                .equipments(Set
//                        .of(Equipments.SECOND_BATHROOM, Equipments.MINI_FRIDGE, Equipments.MINI_BAR, Equipments.OCEAN_VIEW_ROOM, Equipments.WATER_BED, Equipments.PROJECTOR, Equipments.XBOX_CONSOLE))
//                .build();
//
//        RoomDto roomDto14 = RoomDto.builder().roomNumber(14).roomCapacity(4).description("""
//                Lorem ipsum dolor sit amet, consectetur adipisicing elit. Doloremque et nostrum quasi ratione,
//                reprehenderit sequi soluta sunt. Accusantium, aspernatur at commodi ex iste laudantium libero nam
//                necessitatibus omnis rerum sequi tenetur velit voluptatem! Assumenda, atque dolore est in maiores necessitatibus
//                nisi rerum! Ad architecto ipsum maxime. At autem, ipsa laboriosam laudantium nostrum perferendis repellat voluptas
//                Deleniti dolorum eaque eos hic omnis repudiandae rerum sint voluptas! Eum odit perferendis unde ut! Alias atque """)
//                .priceForNight(new BigDecimal("800"))
//                .isBusy(false)
//                .equipments(Set
//                        .of(Equipments.SECOND_BATHROOM, Equipments.MINI_FRIDGE, Equipments.MINI_BAR, Equipments.OCEAN_VIEW_ROOM, Equipments.PROJECTOR, Equipments.XBOX_CONSOLE))
//                .build();
//
//        RoomDto roomDto15 = RoomDto.builder().roomNumber(15).roomCapacity(5).description("""
//                Lorem ipsum dolor sit amet, consectetur adipisicing elit. Doloremque et nostrum quasi ratione,
//                reprehenderit sequi soluta sunt. Accusantium, aspernatur at commodi ex iste laudantium libero nam
//                necessitatibus omnis rerum sequi tenetur velit voluptatem! Assumenda, atque dolore est in maiores necessitatibus
//                nisi rerum! Ad architecto ipsum maxime. At autem, ipsa laboriosam laudantium nostrum perferendis repellat voluptas
//                Deleniti dolorum eaque eos hic omnis repudiandae rerum sint voluptas! Eum odit perferendis unde ut! Alias atque """)
//                .priceForNight(new BigDecimal("1000"))
//                .isBusy(false)
//                .equipments(Set
//                        .of(Equipments.SECOND_BATHROOM, Equipments.MINI_FRIDGE, Equipments.MINI_BAR, Equipments.OCEAN_VIEW_ROOM, Equipments.WATER_BED, Equipments.HOT_TUB, Equipments.PROJECTOR, Equipments.XBOX_CONSOLE))
//                .build();
//
//        OpinionRequestDto opinion = OpinionRequestDto.builder().date(LocalDate.now()).message("""
//                Very Long positive message Very Long positive message Very Long positive message Very Long positive message Very Long positive message
//                Very Long positive message Very Long positive message Very Long positive message Very Long positive message Very Long positive message
//                Very Long positive message Very Long positive message Very Long positive message Very Long positive message Very Long positive message
//                Very Long positive message Very Long positive message Very Long positive message Very Long positive message Very Long positive message
//                """).evaluation(10).build();
//
//        RoomDto save16 = roomService.save(roomDto1);
//        RoomDto save2 = roomService.save(roomDto2);
//        RoomDto save3 = roomService.save(roomDto3);
//        RoomDto save4 = roomService.save(roomDto4);
//        RoomDto save5 = roomService.save(roomDto5);
//        RoomDto save6 = roomService.save(roomDto6);
//        RoomDto save7 = roomService.save(roomDto7);
//        RoomDto save8 = roomService.save(roomDto8);
//        RoomDto save9 = roomService.save(roomDto9);
//        RoomDto save10 = roomService.save(roomDto10);
//        RoomDto save11 = roomService.save(roomDto11);
//        RoomDto save12 = roomService.save(roomDto12);
//        RoomDto save13 = roomService.save(roomDto13);
//        RoomDto save14 = roomService.save(roomDto14);
//        RoomDto save15 = roomService.save(roomDto15);
//
//        ReservationRequestDto reservationDto1 = ReservationRequestDto.builder().startOfBooking(LocalDate.now().minusDays(5).toString()).endOfBooking(LocalDate.now().plusDays(5).toString()).roomId(save2.getId()).userId(save1.getId()).build();
//        ReservationRequestDto reservationDto2 = ReservationRequestDto.builder().startOfBooking(LocalDate.now().minusDays(5).toString()).endOfBooking(LocalDate.now().plusDays(10).toString()).roomId(save3.getId()).userId(save17.getId()).build();
//        ReservationRequestDto reservationDto3 = ReservationRequestDto.builder().startOfBooking(LocalDate.now().plusDays(10).toString()).endOfBooking(LocalDate.now().plusDays(20).toString()).roomId(save4.getId()).userId(save1.getId()).build();
//        ReservationRequestDto reservationDto4 = ReservationRequestDto.builder().startOfBooking(LocalDate.now().minusDays(10).toString()).endOfBooking(LocalDate.now().plusDays(5).toString()).roomId(save5.getId()).userId(save17.getId()).build();
//        ReservationRequestDto reservationDto5 = ReservationRequestDto.builder().startOfBooking(LocalDate.now().minusDays(15).toString()).endOfBooking(LocalDate.now().plusDays(10).toString()).roomId(save6.getId()).userId(save1.getId()).build();
//        ReservationRequestDto reservationDto6 = ReservationRequestDto.builder().startOfBooking(LocalDate.now().plusDays(15).toString()).endOfBooking(LocalDate.now().plusDays(30).toString()).roomId(save7.getId()).userId(save1.getId()).build();
//        ReservationRequestDto reservationDto7 = ReservationRequestDto.builder().startOfBooking(LocalDate.now().minusDays(10).toString()).endOfBooking(LocalDate.now().plusDays(5).toString()).roomId(save8.getId()).userId(save17.getId()).build();
//        ReservationRequestDto reservationDto8 = ReservationRequestDto.builder().startOfBooking(LocalDate.now().minusDays(10).toString()).endOfBooking(LocalDate.now().plusDays(10).toString()).roomId(save9.getId()).userId(save1.getId()).build();
//        ReservationRequestDto reservationDto9 = ReservationRequestDto.builder().startOfBooking(LocalDate.now().minusDays(15).toString()).endOfBooking(LocalDate.now().plusDays(5).toString()).roomId(save10.getId()).userId(save1.getId()).build();
//        ReservationRequestDto reservationDto10 = ReservationRequestDto.builder().startOfBooking(LocalDate.now().minusDays(15).toString()).endOfBooking(LocalDate.now().plusDays(15).toString()).roomId(save11.getId()).userId(save17.getId()).build();
//        ReservationRequestDto reservationDto11 = ReservationRequestDto.builder().startOfBooking(LocalDate.now().minusDays(2).toString()).endOfBooking(LocalDate.now().plusDays(15).toString()).roomId(save12.getId()).userId(save1.getId()).build();
//        ReservationRequestDto reservationDto12 = ReservationRequestDto.builder().startOfBooking(LocalDate.now().minusDays(3).toString()).endOfBooking(LocalDate.now().plusDays(15).toString()).roomId(save13.getId()).userId(save1.getId()).build();
//        ReservationRequestDto reservationDto13 = ReservationRequestDto.builder().startOfBooking(LocalDate.now().minusDays(4).toString()).endOfBooking(LocalDate.now().plusDays(15).toString()).roomId(save14.getId()).userId(save17.getId()).build();
//        ReservationRequestDto reservationDto14 = ReservationRequestDto.builder().startOfBooking(LocalDate.now().minusDays(5).toString()).endOfBooking(LocalDate.now().plusDays(15).toString()).roomId(save15.getId()).userId(save1.getId()).build();
//        ReservationRequestDto reservationDto15 = ReservationRequestDto.builder().startOfBooking(LocalDate.now().plusDays(20).toString()).endOfBooking(LocalDate.now().plusDays(35).toString()).roomId(save16.getId()).userId(save1.getId()).build();
//
//        List<ReservationRequestDto> reservationsDto = List.of(reservationDto1,
//                reservationDto2,
//                reservationDto3,
//                reservationDto4,
//                reservationDto5,
//                reservationDto6,
//                reservationDto7,
//                reservationDto8,
//                reservationDto9,
//                reservationDto10,
//                reservationDto11,
//                reservationDto12,
//                reservationDto13,
//                reservationDto14,
//                reservationDto15);
//
//        reservationsDto.forEach(reservationRequestDto -> {
//            ReservationResponseDto returnedReservationResponse = reservationService.save(reservationRequestDto);
//            opinion.setReservationId(returnedReservationResponse.getId());
//            opinionService.saveOpinion(opinion);
//        });
//    }
}
