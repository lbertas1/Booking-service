package bartos.lukasz.bookingservice.infrastructure.controllers;

import bartos.lukasz.bookingservice.application.annotations.CoveredControllerAdvice;
import bartos.lukasz.bookingservice.application.dto.CityAdminData;
import bartos.lukasz.bookingservice.application.event.AdminLogOutEvent;
import bartos.lukasz.bookingservice.application.exception.EmailServiceException;
import bartos.lukasz.bookingservice.application.exception.UserServiceException;
import bartos.lukasz.bookingservice.application.event.AdminLoginEvent;
import bartos.lukasz.bookingservice.application.service.ChatService;
import bartos.lukasz.bookingservice.application.service.LoggedAdminService;
import bartos.lukasz.bookingservice.application.service.dataServices.UserService;
import bartos.lukasz.bookingservice.application.service.email.EmailService;
import bartos.lukasz.bookingservice.domain.user.User;
import bartos.lukasz.bookingservice.domain.user.UserRepository;
import bartos.lukasz.bookingservice.domain.user.dto.*;
import bartos.lukasz.bookingservice.domain.user.enums.Role;
import bartos.lukasz.bookingservice.infrastructure.security.dto.AccessDto;
import bartos.lukasz.bookingservice.infrastructure.security.dto.Identity;
import bartos.lukasz.bookingservice.infrastructure.security.dto.MyUserPrincipal;
import bartos.lukasz.bookingservice.infrastructure.security.tokens.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CoveredControllerAdvice
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider tokenProvider;
    private final ApplicationEventPublisher applicationEventPublisher;

    @PostMapping("/login")
    public ResponseEntity<Identity> login(@RequestBody AccessDto accessDto) throws UserServiceException, EmailServiceException {
        UserDto user = userService.getUserDtoByUsername(accessDto.getUsername());
        authenticate(accessDto.getUsername(), accessDto.getPassword());
        MyUserPrincipal myUserPrincipal = new MyUserPrincipal(user);
        String token = tokenProvider.generateJwtToken(myUserPrincipal);

        Identity identity = Identity
                .builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .username(user.getUsername())
                .role(user.getRole())
                .token(token)
                .build();

        if (identity.getRole().equals(Role.ROLE_ADMIN)) {
            this.applicationEventPublisher.publishEvent(new AdminLoginEvent(this, identity.getUsername()));
        }

        return new ResponseEntity<>(identity, OK);
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    @PostMapping("/log-out")
    public ResponseEntity<Identity> logout(@RequestBody Identity identity) {
        if (identity.getRole().equals(Role.ROLE_ADMIN)) {
            this.applicationEventPublisher.publishEvent(new AdminLogOutEvent(this, identity.getUsername()));
        }

        return ResponseEntity.accepted().body(identity);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody RegisterUserDto registerUserDto) throws UserServiceException {
        //emailService.send(user, null, user.getEmail(), null, EmailContent.REGISTRATION);
        return new ResponseEntity<>(userService.save(registerUserDto), OK);
    }

    @GetMapping("/get-user/{id}")
    ResponseEntity<UserResponseDto> getProfile(@PathVariable Long id) throws UserServiceException {
        return ResponseEntity.status(ACCEPTED).body(userService.findById(id));
    }

    @GetMapping("/profile/{userId}")
    ResponseEntity<UserProfileDto> getFullProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getProfile(userId));
    }

    @RolesAllowed("ROLE_ADMIN")
    @PostMapping("/save")
    ResponseEntity<UserResponseDto> saveNewUser(@RequestBody RegisterUserDto registerUserDto) throws UserServiceException {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(registerUserDto));
    }

    @PutMapping("/update")
    ResponseEntity<UpdateUserDto> updateUser(@RequestBody UpdateUserDto updateUserDto) throws UserServiceException {
        return ResponseEntity.status(ACCEPTED).body(userService.update(updateUserDto));
    }

    @RolesAllowed("ROLE_ADMIN")
    @DeleteMapping("/remove/{userId}")
    ResponseEntity<UserResponseDto> removeUser(@PathVariable Long userId) {
        return ResponseEntity.status(ACCEPTED).body(userService.remove(userId));
    }

    @PostMapping("/change-password")
    ResponseEntity<Long> changePassword(@RequestBody ChangePasswordDto changePasswordDto) throws UserServiceException {
        return new ResponseEntity<>(userService.changePassword(changePasswordDto), ACCEPTED);
    }

    @RolesAllowed("ROLE_ADMIN")
    @PostMapping("/change-role")
    ResponseEntity<UserResponseDto> changeRoleOnAdmin(@RequestBody AccessDto accessDto) throws UserServiceException {
        return new ResponseEntity<>(userService.changeRoleOnAdmin(accessDto.getUsername()), ACCEPTED);
    }

    @GetMapping("/take-available-admins")
    ResponseEntity<List<CityAdminData>> getAvailableAdmins() {
        return new ResponseEntity<>(userService.getAvailableAdmins(), ACCEPTED);
    }
}
