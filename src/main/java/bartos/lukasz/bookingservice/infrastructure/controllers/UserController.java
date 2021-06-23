package bartos.lukasz.bookingservice.infrastructure.controllers;

import bartos.lukasz.bookingservice.application.annotations.CoveredControllerAdvice;
import bartos.lukasz.bookingservice.application.dto.CityAdminData;
import bartos.lukasz.bookingservice.application.event.AdminLogOutEvent;
import bartos.lukasz.bookingservice.application.exception.EmailServiceException;
import bartos.lukasz.bookingservice.application.exception.UserServiceException;
import bartos.lukasz.bookingservice.application.event.AdminLoginEvent;
import bartos.lukasz.bookingservice.application.service.HotelReportGenerator;
import bartos.lukasz.bookingservice.application.service.dataServices.UserService;
import bartos.lukasz.bookingservice.domain.user.dto.*;
import bartos.lukasz.bookingservice.domain.user.enums.Role;
import bartos.lukasz.bookingservice.infrastructure.security.dto.AccessDto;
import bartos.lukasz.bookingservice.infrastructure.security.dto.AdminIdentity;
import bartos.lukasz.bookingservice.infrastructure.security.dto.UserIdentity;
import bartos.lukasz.bookingservice.infrastructure.security.dto.MyUserPrincipal;
import bartos.lukasz.bookingservice.infrastructure.security.tokens.JwtTokenProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CoveredControllerAdvice
@Api(value = "/users")
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider tokenProvider;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final HotelReportGenerator hotelReportGenerator;

    @ApiOperation(value = "Returns UserIdentity or AdminIdentity object after correct login.")
    @PostMapping("/login")
    public ResponseEntity<UserIdentity> login(@RequestBody AccessDto accessDto) throws UserServiceException, EmailServiceException {
        UserDto user = userService.getUserDtoByUsername(accessDto.getUsername());
        authenticate(accessDto.getUsername(), accessDto.getPassword());
        MyUserPrincipal myUserPrincipal = new MyUserPrincipal(user);
        String token = tokenProvider.generateJwtToken(myUserPrincipal);

        if (user.getRole().equals(Role.ROLE_USER)) {
            return new ResponseEntity<>(UserIdentity
                    .builder()
                    .id(user.getId())
                    .name(user.getName())
                    .surname(user.getSurname())
                    .username(user.getUsername())
                    .role(user.getRole())
                    .token(token)
                    .build(), OK);
        } else {
            this.applicationEventPublisher.publishEvent(new AdminLoginEvent(this, user.getUsername()));
            AdminIdentity adminIdentity = AdminIdentity
                    .builder()
                    .id(user.getId())
                    .name(user.getName())
                    .surname(user.getSurname())
                    .username(user.getUsername())
                    .role(user.getRole())
                    .token(token)
                    .build();

            return new ResponseEntity<>(hotelReportGenerator.generateReport(adminIdentity), OK);
        }
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    @ApiOperation(value = "Returns identity object after correct log out.")
    @PostMapping("/log-out")
    public ResponseEntity<UserIdentity> logout(@RequestBody UserIdentity userIdentity) {
        this.applicationEventPublisher.publishEvent(new AdminLogOutEvent(this, userIdentity.getUsername()));
        return ResponseEntity.accepted().body(userIdentity);
    }

    @ApiOperation(value = "Returns UserResponseDto object after correct register new account.")
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody RegisterUserDto registerUserDto) throws UserServiceException {
        //emailService.send(user, null, user.getEmail(), null, EmailContent.REGISTRATION);
        return new ResponseEntity<>(userService.save(registerUserDto), OK);
    }

    @ApiOperation(value = "Returns UserResponseDto object based on the obtained id.")
    @GetMapping("/get-user/{id}")
    ResponseEntity<UserResponseDto> getProfile(@PathVariable Long id) throws UserServiceException {
        return ResponseEntity.status(ACCEPTED).body(userService.findById(id));
    }

    @ApiOperation(value = """
            Returns UserProfileDto object based on the obtained id.
            Contains all information about the user, as well as about his bookings, added opinions and used promotions.""")
    @GetMapping("/profile/{userId}")
    ResponseEntity<UserProfileDto> getFullProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getProfile(userId));
    }

    @ApiOperation(value = """
            Returns UserResponseDto object after saving new user.
            Method available only for admin.""")
    @RolesAllowed("ROLE_ADMIN")
    @PostMapping("/save")
    ResponseEntity<UserResponseDto> saveNewUser(@RequestBody RegisterUserDto registerUserDto) throws UserServiceException {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(registerUserDto));
    }

    @ApiOperation(value = """
            Returns UpdateUserDto object after the user account has been updated correctly.""")
    @PutMapping("/update")
    ResponseEntity<UpdateUserDto> updateUser(@RequestBody UpdateUserDto updateUserDto) throws UserServiceException {
        return ResponseEntity.status(ACCEPTED).body(userService.update(updateUserDto));
    }

    @ApiOperation(value = """
            Returns UserResponseDto object after removing account. Only for admin.""")
    @RolesAllowed("ROLE_ADMIN")
    @DeleteMapping("/remove/{userId}")
    ResponseEntity<UserResponseDto> removeUser(@PathVariable Long userId) {
        return ResponseEntity.status(ACCEPTED).body(userService.remove(userId));
    }

    @ApiOperation(value = """
            Returns user id after correct password change.""")
    @PostMapping("/change-password")
    ResponseEntity<Long> changePassword(@RequestBody ChangePasswordDto changePasswordDto) throws UserServiceException {
        return new ResponseEntity<>(userService.changePassword(changePasswordDto), ACCEPTED);
    }

    @ApiOperation(value = """
            Returns UserResponseDto after changing the role. Only for admin.""")
    @RolesAllowed("ROLE_ADMIN")
    @PostMapping("/change-role")
    ResponseEntity<UserResponseDto> changeRoleOnAdmin(@RequestBody AccessDto accessDto) throws UserServiceException {
        return new ResponseEntity<>(userService.changeRoleOnAdmin(accessDto.getUsername()), ACCEPTED);
    }

    @ApiOperation(value = """
            Returns the list of available admins in the form of CityAdminData list when user wants to chat.""")
    @GetMapping("/take-available-admins")
    ResponseEntity<List<CityAdminData>> getAvailableAdmins() {
        return new ResponseEntity<>(userService.getAvailableAdmins(), ACCEPTED);
    }
}