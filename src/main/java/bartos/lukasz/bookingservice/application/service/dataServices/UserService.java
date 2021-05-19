package bartos.lukasz.bookingservice.application.service.dataServices;

import bartos.lukasz.bookingservice.application.dto.CityAdminData;
import bartos.lukasz.bookingservice.application.exception.ReservationServiceException;
import bartos.lukasz.bookingservice.application.exception.UserServiceException;
import bartos.lukasz.bookingservice.application.service.LoggedAdminService;
import bartos.lukasz.bookingservice.application.service.LoginAttemptService;
import bartos.lukasz.bookingservice.domain.reservation.Reservation;
import bartos.lukasz.bookingservice.domain.reservation.ReservationRepository;
import bartos.lukasz.bookingservice.domain.reservation.reservationDto.ReservationDto;
import bartos.lukasz.bookingservice.domain.reservation.reservationDto.SimpleReservationDto;
import bartos.lukasz.bookingservice.domain.user.User;
import bartos.lukasz.bookingservice.domain.user.UserRepository;
import bartos.lukasz.bookingservice.domain.user.dto.*;
import bartos.lukasz.bookingservice.domain.user.enums.Role;
import bartos.lukasz.bookingservice.infrastructure.security.dto.MyUserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final LoginAttemptService loginAttemptService;
    private final LoggedAdminService loggedAdminService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UserServiceException {
        Optional<User> byUsername = userRepository.findByUsername(username);
        if (byUsername.isEmpty())
            throw new UserServiceException("No user found by given username " + username, 404, HttpStatus.NOT_FOUND);
        else {
            UserDto userDto = byUsername.get().toUserDto();
            validateLoginAttempt(userDto);
            return new MyUserPrincipal(userDto);
        }
    }

    @Transactional
    public UserResponseDto save(RegisterUserDto registerUserDto) throws UserServiceException {
        if (Objects.isNull(registerUserDto))
            throw new UserServiceException("Given user object is null", 400, HttpStatus.BAD_REQUEST);

        validateUsername(registerUserDto.getUsername());
        validateEmail(registerUserDto.getEmail());

        var userDto = registerUserDto.toUserDto();
        userDto.setPassword(encodePassword(registerUserDto.getPassword()));
        userDto.setIsNotLocked(true);
        userDto.setRole(Role.ROLE_USER);

        return userRepository.save(userDto.toUser()).toUserResponseDto();
    }

    @Transactional
    public UpdateUserDto update(UpdateUserDto updateUserDto) throws UserServiceException {
        if (Objects.isNull(updateUserDto))
            throw new UserServiceException("Given user object is null", 400, HttpStatus.BAD_REQUEST);

        UserDto userDto = get(updateUserDto.getId());

        if (!userDto.getName().equals(updateUserDto.getName())) userDto.setName(updateUserDto.getName());
        if (!userDto.getSurname().equals(updateUserDto.getSurname())) userDto.setSurname(updateUserDto.getSurname());
        if (!userDto.getBirthDate().equals(LocalDate.parse(updateUserDto.getBirthDate())))
            userDto.setBirthDate(LocalDate.parse(updateUserDto.getBirthDate()));
        if (!userDto.getEmail().equals(updateUserDto.getEmail())) userDto.setEmail(updateUserDto.getEmail());
        if (!userDto.getCity().equals(updateUserDto.getCity())) userDto.setCity(updateUserDto.getCity());
        if (!userDto.getCountry().equals(updateUserDto.getCountry())) userDto.setCountry(updateUserDto.getCountry());
        if (!userDto.getAddress().equals(updateUserDto.getAddress())) userDto.setAddress(updateUserDto.getAddress());
        if (!userDto.getPhone().equals(updateUserDto.getPhone())) userDto.setPhone(updateUserDto.getPhone());
        if (!userDto.getZipCode().equals(updateUserDto.getZipCode())) userDto.setZipCode(updateUserDto.getZipCode());

        return userRepository.save(userDto.toUser()).toUpdateUserDto();
    }

    public UserProfileDto getProfile(Long id) {
        return reservationRepository
                .findAllByUserId(id)
                .stream()
                .map(Reservation::toReservationDto)
                .collect(Collectors
                        .groupingBy(ReservationDto::getUserDto,
                                Collectors.toCollection(ArrayList::new)))
                .entrySet()
                .stream()
                .map(userDtoArrayListEntry -> {
                    var userResponseDto = userDtoArrayListEntry.getKey().toUserResponseDto();
                    List<SimpleReservationDto> simpleReservations = userDtoArrayListEntry
                            .getValue()
                            .stream()
                            .map(reservationDto -> {
                                var simpleReservationDto = SimpleReservationDto
                                        .builder()
                                        .roomNumber(reservationDto.getRoomDto().getRoomNumber())
                                        .startOfBooking(reservationDto.getStartOfBooking().toString())
                                        .endOfBooking(reservationDto.getEndOfBooking().toString())
                                        .priceForNight(reservationDto.getRoomDto().getPriceForNight().toString())
                                        .totalAmountForReservation(reservationDto.getBookingStatusDto().getTotalAmountForReservation().toString())
                                        .paymentStatus(reservationDto.getBookingStatusDto().getPaymentStatus())
                                        .build();

                                if (reservationDto.getOpinionDto() == null) {
                                    simpleReservationDto.setOpinionDate("");
                                    simpleReservationDto.setOpinionMessage("");
                                    simpleReservationDto.setOpinionEvaluation("");
                                } else {
                                    simpleReservationDto.setOpinionDate(reservationDto.getOpinionDto().getDate().toString());
                                    simpleReservationDto.setOpinionMessage(reservationDto.getOpinionDto().getMessage());
                                    simpleReservationDto.setOpinionEvaluation(reservationDto.getOpinionDto().getEvaluation().toString());
                                }

                                return simpleReservationDto;
                            })
                            .collect(Collectors.toList());

                    return UserProfileDto
                            .builder()
                            .user(userResponseDto)
                            .reservations(simpleReservations)
                            .build();
                })
                .findFirst()
                .orElseThrow(() -> new ReservationServiceException("User profile with reservations not found", 404, HttpStatus.NOT_FOUND));
    }

    private UserDto get(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new UserServiceException("User not found", 404, HttpStatus.NOT_FOUND))
                .toUserDto();
    }

    @Transactional
    public Long changePassword(ChangePasswordDto changePasswordDto) throws UserServiceException {
        if (Objects.isNull(changePasswordDto))
            throw new UserServiceException("ChangePasswordDto object is null", 400, HttpStatus.BAD_REQUEST);

        var userDto = get(changePasswordDto.getUserId());

        if (bCryptPasswordEncoder.matches(changePasswordDto.getOldPassword(), userDto.getPassword())) {
            userDto.setPassword(encodePassword(changePasswordDto.getNewPassword()));
            userRepository.saveAndFlush(userDto.toUser());
        } else {
            throw new UserServiceException("Incorrect old password.", 400, HttpStatus.BAD_REQUEST);
        }

        return changePasswordDto.getUserId();
    }

    @Transactional
    public UserResponseDto changeRoleOnAdmin(String username) throws UserServiceException {
        var userDTO = getUserDtoByUsername(username);

        userDTO.setRole(Role.ROLE_ADMIN);
        return userRepository.save(userDTO.toUser()).toUserResponseDto();
    }

    public UserResponseDto remove(Long id) {
        UserDto userDto = get(id);
        userRepository.deleteById(id);

        return userDto.toUserResponseDto();
    }

    public UserResponseDto findById(Long id) {
        return get(id).toUserResponseDto();
    }

    private void validateLoginAttempt(UserDto userDTO) {
        if (userDTO.getIsNotLocked()) {
            userDTO.setIsNotLocked(!loginAttemptService.hasExceededMaxAttempts(userDTO.getUsername()));
        } else loginAttemptService.evictUserFromLoginAttemptCache(userDTO.getUsername());
    }

    public UserDto getUserDtoByUsername(String username) throws UserServiceException {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserServiceException("User not found", 404, HttpStatus.NOT_FOUND))
                .toUserDto();
    }

    private void validateUsername(String username) throws UserServiceException {
        if (userRepository.findByUsername(username).isPresent())
            throw new UserServiceException("Given username already exists", 400, HttpStatus.BAD_REQUEST);
    }

    private void validateEmail(String email) throws UserServiceException {
        if (Objects.isNull(email) || email.isEmpty())
            throw new UserServiceException("Given email already exists", 400, HttpStatus.BAD_REQUEST);
        if (userRepository.findByEmail(email).isPresent())
            throw new UserServiceException("Given email already exists", 400, HttpStatus.BAD_REQUEST);
    }

    private String encodePassword(String password) throws UserServiceException {
        if (password.isBlank() || password.length() < 6)
            throw new UserServiceException("You should enter valid password to create account", 400, HttpStatus.BAD_REQUEST);

        return bCryptPasswordEncoder.encode(password);
    }

    public List<CityAdminData> getAvailableAdmins() {
        List<String> allFreeAdmins = loggedAdminService.getAllFreeAdmins();

        return new ArrayList<>(userRepository
                .findAllByUsernameIsIn(allFreeAdmins)
                .stream()
                .map(user -> CityAdminData.builder().city(user.getCity()).username(user.getUsername()).build())
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(CityAdminData::getCity)))));
    }
}