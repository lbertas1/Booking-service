package bartos.lukasz.bookingservice.application.listener;

import bartos.lukasz.bookingservice.application.service.LoginAttemptService;
import bartos.lukasz.bookingservice.infrastructure.security.dto.MyUserPrincipal;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AuthenticationSuccessListener {
    private final LoginAttemptService loginAttemptService;

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        Object principal = event.getAuthentication().getPrincipal();

        if (principal instanceof MyUserPrincipal) {
            MyUserPrincipal myUserPrincipal = (MyUserPrincipal) event.getAuthentication().getPrincipal();
            loginAttemptService.evictUserFromLoginAttemptCache(myUserPrincipal.getUsername());
        }

    }
}
