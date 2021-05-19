package bartos.lukasz.bookingservice.application.service;

import bartos.lukasz.bookingservice.application.exception.LoggedAdminServiceException;
import bartos.lukasz.bookingservice.domain.user.User;
import bartos.lukasz.bookingservice.domain.user.UserRepository;
import bartos.lukasz.bookingservice.domain.user.enums.AdminAvailability;
import bartos.lukasz.bookingservice.domain.user.enums.Role;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class LoggedAdminService {

    private final UserRepository userRepository;

    private LoadingCache<String, AdminAvailability> loginAttemptCache;

    public LoggedAdminService(UserRepository userRepository) {
        this.userRepository = userRepository;

        Map<String, AdminAvailability> adminAvailabilityMap = userRepository
                .findAllByRole(Role.ROLE_ADMIN)
                .stream()
                .map(User::getUsername)
                .collect(Collectors.toMap(
                        username -> username,
                        username -> AdminAvailability.UNAVAILABLE
                ));

        loginAttemptCache = CacheBuilder
                .newBuilder()
                .build(new CacheLoader<String, AdminAvailability>() {
                    @Override
                    public AdminAvailability load(String s) throws Exception {
                        return null;
                    }
                });

        loginAttemptCache.putAll(adminAvailabilityMap);
    }

    public boolean addUsernameToCache(String username) {
        if (!whetherUsernameIsIncluded(username)) {
            loginAttemptCache.put(username, AdminAvailability.UNAVAILABLE);
            return true;
        } else return false;
    }

    private boolean whetherUsernameIsIncluded(String username) {
        try {
            return loginAttemptCache.get(username) != null;
        } catch (ExecutionException e) {
            throw new LoggedAdminServiceException("whetherUsernameIsIncluded method error", 400, HttpStatus.BAD_REQUEST);
        }
    }

    public boolean markAdminAsUnavailable(String username) {
        if (whetherUsernameIsIncluded(username)) {
            loginAttemptCache.put(username, AdminAvailability.UNAVAILABLE);
            return true;
        } else return false;
    }

    public boolean markAdminAsAvailable(String username) {
        if (whetherUsernameIsIncluded(username)) {
            loginAttemptCache.put(username, AdminAvailability.AVAILABLE);
            return true;
        } else return false;
    }

    public String getFreeAdmin() {
        return loginAttemptCache
                .asMap()
                .entrySet()
                .stream()
                .filter(stringBooleanEntry -> stringBooleanEntry.getValue().equals(AdminAvailability.AVAILABLE))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new LoggedAdminServiceException("No free admin available", 404, HttpStatus.NOT_FOUND));
    }

    public List<String> getAllFreeAdmins() {
        return loginAttemptCache
                .asMap()
                .entrySet()
                .stream()
                .filter(stringBooleanEntry -> stringBooleanEntry.getValue().equals(AdminAvailability.AVAILABLE))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public boolean isAdminInCache(String identifier) {
        return loginAttemptCache.getIfPresent(identifier) != null;
    }
}
